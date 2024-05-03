import math
import bots500.exp1.parameters as parameters

class Pheromone:
    def __init__(self, step, bot_id, x, y, s):
        self.step = step
        self.bot_id = bot_id
        self.x = x
        self.y = y 
        self.initial_strength = s
        self.strength = s * 0.98
    
    def decay(self, frame):
        self.strength = self.initial_strength * 0.98 * math.exp(-parameters.EVAPORATION_RATE * (frame - self.step))
        return self.strength

    def __eq__(self, o):
        if self is o:
            return True
        return self.step == o.step and self.bot_id == o.bot_id and math.isclose(self.x, o.x) and math.isclose(self.y, o.y) and math.isclose(self.strength, o.strength)
  
