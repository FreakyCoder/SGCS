import numpy as np
import threading
import matplotlib.pyplot as plt
import matplotlib.animation as animation
from bot import Bot
import utils 
import sys
import importlib
parameters = importlib.import_module(f'{sys.argv[1].replace('/', '.')}.parameters')
PREFIX = sys.argv[1]

plt.gca().set_xlim([-parameters.WIDTH / 2, parameters.WIDTH / 2])
plt.gca().set_ylim([-parameters.HEIGHT / 2, parameters.HEIGHT / 2])
plt.gca().set_adjustable('box')
plt.gca().set_aspect('equal')
plt.suptitle('SGCS')

class Simulation:
    def __init__(self):
        self.coverage = [[] for _ in range(2 * parameters.TRIALS)]
        self.trial = 0
        self.animation = None

    def initialize(self):
        self.bots = [Bot(i, 0, 0, parameters.SPEED) for i in range(parameters.BOT_COUNT)]
        if self.animation:
            self.bots_scat = plt.gca().scatter([bot.x for bot in self.bots], [bot.y for bot in self.bots], c='black', s=5)
            self.pheromones_scat = plt.gca().scatter([], [], c='green', s=5)
            self.bots_lines = [([0], [0], plt.gca().plot(0, 0)[0]) for _ in self.bots]
        self.visited = np.full((1, parameters.WIDTH, parameters.HEIGHT), 0, dtype=int)
        self.visited_count = np.full(1, 0, dtype=int)

    def tick(self, trial, frame, bots, random):
        for (i, bot) in enumerate(bots):
            if not random:
                bot.update_pheromones(frame)
            bot.move(frame, random)
            if not random:
                for j in range(i):
                    bot2 = bots[j]
                    if utils.dist(bot.x, bot.y, bot2.x, bot2.y) <= parameters.COMM_RANGE:
                        bot.add_pheromones2(bot2.pheromones)
                        bot2.add_pheromones2(bot.pheromones)
            indx = int(bot.x + parameters.WIDTH // 2)
            indy = int(bot.y + parameters.HEIGHT // 2)
            for x in range(indx - parameters.SENSOR_RANGE, indx + parameters.SENSOR_RANGE + 1):
                for y in range(indy - parameters.SENSOR_RANGE, indy + parameters.SENSOR_RANGE + 1):
                    if x >= 0 and x < parameters.WIDTH and y >= 0 and y < parameters.HEIGHT:
                        if self.visited[trial][x][y] == 0:
                            self.visited_count[trial] += 1 
                        self.visited[trial][x][y] += 1
        self.coverage[trial].append(self.visited_count[trial] / (parameters.WIDTH * parameters.HEIGHT) * 100)

    def update(self, frame):
        self.tick(self.trial, frame, self.bots, False)
        for j in range(1, parameters.BOT_COUNT + 1):
            if frame == j * (parameters.STEPS - 1) // parameters.BOT_COUNT:
                self.bots.pop()
        for (i, bot) in enumerate(self.bots):
            if self.animation:
                self.bots_lines[i][0].append(bot.x)
                self.bots_lines[i][1].append(bot.y)
                self.bots_lines[i][2].set_data(self.bots_lines[i][0], self.bots_lines[i][1])
                self.bots_scat.set_offsets([[bot.x, bot.y] for bot in self.bots])
        if self.animation and i == 0:
            self.pheromones_scat.set_offsets([[p.x, p.y] for p in self.bots[i].pheromones])

    def run_trial(self, t: int, random: bool):
        print('Trial', t)
        bots = [Bot(i, 0, 0, parameters.SPEED) for i in range(parameters.BOT_COUNT)]
        save_visited = []
        failures = []
        for i in range(parameters.STEPS):
            print(t, i)
            self.tick(t, i, bots, random)
            for j in range(1, 5):
                if i == j * (parameters.STEPS - 1) // 4:
                    save_visited.append(np.transpose(self.visited[t]).copy())
            
            for k in range(parameters.failure(i)):
                if len(bots) > 0:
                    failures.append(i)
                    bots.pop()
        alg = 'random' if random else 'biased'
        trial = t if random else t - parameters.TRIALS
        np.save(f'{PREFIX}/{alg}/failures{trial}', failures)
        np.save(f'{PREFIX}/{alg}/area{trial}', save_visited)


    def run(self):
        self.visited = np.full((2 * parameters.TRIALS, parameters.WIDTH, parameters.HEIGHT), 0, dtype=int)
        self.visited_count = np.full((2 * parameters.TRIALS), 0, dtype=int)
        threads = []
        for t in range(parameters.TRIALS): # 2 * parameters.TRIALS
            threads.append(threading.Thread(target=self.run_trial, args=(t,t<parameters.TRIALS))) # t<parameters.TRIALS 
            threads[t].start()
                     
        for t in threads:
            t.join()
        print('Saving results...')
        np.save(f'{PREFIX}/random/coverage', self.coverage[:parameters.TRIALS])
        #  np.save(f'{PREFIX}/biased/coverage', self.coverage[parameters.TRIALS:])
        print('Done.')

    def run_animation(self):
        self.animation = animation.FuncAnimation(
            plt.gcf(), self.update, init_func=self.initialize, frames=parameters.STEPS, interval=0.001)
        self.paused = False

        plt.gcf().canvas.mpl_connect('button_press_event', self.toggle_pause)

    def toggle_pause(self, *args, **kwargs):
        if self.paused:
            self.animation.resume()
        else:
            self.animation.pause()
        self.paused = not self.paused
sim = Simulation()
#  sim.run_animation()
sim.run()
plt.show()
