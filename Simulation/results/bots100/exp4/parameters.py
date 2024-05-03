import random
# 5% failure chance
TRIALS=5
STEPS=5000
WIDTH=801
HEIGHT=801
BOT_COUNT=100
SPEED=2
SENSOR_RANGE=2
DECISION_STEPS=10
CONSIDERED_POSITIONS=5
PHEROMONE_STRENGTH=200
FENCE_STRENGTH=1
EVAPORATION_RATE=0.02
PHEROMONE_DROP_STEPS=20
COMM_RANGE=50

def failure(i):
    if random.random() < 0.05:
        return 1
    return 0

