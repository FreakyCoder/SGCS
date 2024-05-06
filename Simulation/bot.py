import random
import numpy as np
import math
import copy
import sys
import importlib
parameters = importlib.import_module(f'{sys.argv[1].replace('/', '.')}.parameters')
import utils
from pheromone import Pheromone

class Bot:
    def __init__(self, id, x, y, v):
        self.id = id
        self.x = x
        self.y = y
        self.v = v
        self.angle = random.uniform(0, math.tau)
        self.pheromones = set([Pheromone(0, 0, 0, 0, parameters.PHEROMONE_STRENGTH * parameters.BOT_COUNT)])

    def change_direction_random(self):
        self.angle = ((math.tau + self.angle + (random.random() * 2 - 1) * math.pi / 2) % math.tau)

    def change_direction_biased(self):
        choices = []
        weights = []
        while len(choices) < parameters.CONSIDERED_POSITIONS:
            theta = ((math.tau + self.angle + (random.random() * 2 - 1) * math.pi / 2) % math.tau)
            choices.append(theta)
            weights.append(1)
        for p in self.pheromones.union([Pheromone(None, self.id, self.x, parameters.HEIGHT // 2, parameters.FENCE_STRENGTH * parameters.PHEROMONE_STRENGTH), Pheromone(None, self.id, self.x, -parameters.HEIGHT // 2, parameters.FENCE_STRENGTH * parameters.PHEROMONE_STRENGTH), Pheromone(None, self.id, parameters.WIDTH // 2, self.y, parameters.FENCE_STRENGTH * parameters.PHEROMONE_STRENGTH), Pheromone(None, self.id, -parameters.WIDTH // 2, self.y, parameters.FENCE_STRENGTH * parameters.PHEROMONE_STRENGTH)]):
            d = utils.dist(self.x, self.y, p.x, p.y)
            if not math.isclose(d, 0):
                ang = math.atan2(p.y - self.y, p.x - self.x)
                if ang < 0:
                    ang += math.tau
                for i, ch in enumerate(choices):
                    weights[i] += p.strength / (d * d) * (math.pi - abs(abs(ang - ch) - math.pi))
        if (math.pi - abs(abs(choices[np.argmax(weights)] - self.angle) - math.pi)) > math.pi / 2:
            print(math.degrees(math.pi - abs(abs(choices[np.argmax(weights)] - self.angle) - math.pi)), math.degrees(self.angle), np.degrees(choices))
        self.angle = choices[np.argmax(weights)]
    def change_direction_biased2(self):
        if self.pheromones:
            sum_x = 0
            sum_y = 0
            for p in self.pheromones:
                d = utils.dist(self.x, self.y, p.x, p.y)
                sum_x += -(p.x - self.x) * p.strength * d 
                sum_y += -(p.y - self.y) * p.strength * d 
            self.angle = math.atan2(sum_y, sum_x)
            fx = self.x + math.cos(self.angle) * self.v * parameters.DECISION_STEPS
            fy = self.y + math.sin(self.angle) * self.v * parameters.DECISION_STEPS
            if fx < -parameters.WIDTH // 2 or fx > parameters.WIDTH // 2 or fy < -parameters.HEIGHT // 2 or fy > parameters.HEIGHT // 2:
                self.angle = math.atan2(-self.y, -self.x)

    def update_pheromones(self, frame):
        self.pheromones = set(filter(lambda p: p.decay(frame) > 0.1, self.pheromones))
        if frame != 0 and frame % parameters.PHEROMONE_DROP_STEPS == 0:
            self.pheromones.add(Pheromone(frame, self.id, self.x, self.y, parameters.PHEROMONE_STRENGTH))
    def add_pheromones2(self, new_pheromones):
        self.pheromones.update(new_pheromones)

    def add_pheromones(self, new_pheromones):
        i, j, pheromones = 0, 0, []
        while i < len(self.pheromones) and j < len(new_pheromones):
            if self.pheromones[i].step < new_pheromones[j].step:
                pheromones.append(self.pheromones[i])
                i += 1
            elif self.pheromones[i].step > new_pheromones[j].step:
                pheromones.append(copy.deepcopy(new_pheromones[j]))
                j += 1
            else:
                if self.pheromones[i] == new_pheromones[j]:
                    pheromones.append(self.pheromones[i])
                    i += 1
                    j += 1
                else:
                    if self.pheromones[i].bot_id < new_pheromones[j].bot_id:
                        pheromones.append(self.pheromones[i])
                        i += 1
                    else:
                        pheromones.append(copy.deepcopy(new_pheromones[j]))
                        j += 1
        while i < len(self.pheromones):
                pheromones.append(self.pheromones[i])
                i += 1
        while j < len(new_pheromones):
                pheromones.append(copy.deepcopy(new_pheromones[j]))
                j += 1
        self.pheromones = pheromones

    def move(self, frame, random: bool):
        if frame != 0 and frame % parameters.DECISION_STEPS == 0:
            if random:
                self.change_direction_random()
            else:
                self.change_direction_biased()
        x = self.x + math.cos(self.angle) * self.v
        y = self.y + math.sin(self.angle) * self.v 
        if not(x < -parameters.WIDTH // 2 or x > parameters.WIDTH // 2 or y < -parameters.HEIGHT // 2 or y > parameters.HEIGHT // 2):
            self.x = x
            self.y = y

