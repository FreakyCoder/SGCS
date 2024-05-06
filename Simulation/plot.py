import sys
import math
import numpy as np
import matplotlib
import matplotlib.pyplot as plt
from matplotlib.lines import Line2D
import seaborn as sns
import pandas as pd
import importlib
matplotlib.rc('font', family='Times New Roman') 

parameters = importlib.import_module(f"{sys.argv[1].replace('/', '.')}.parameters")

plt.xlim([0, parameters.STEPS])
plt.ylim([0, 100])

raw_data_random = np.load(f'{sys.argv[1]}/random/coverage.npy')
raw_random_transposed = np.transpose(raw_data_random)
raw_data_biased = np.load(f'{sys.argv[1]}/biased/coverage.npy')
raw_biased_transposed = np.transpose(raw_data_biased)

random_consistency = 0
random_done = np.empty(len(raw_random_transposed[0]))
prev = raw_random_transposed[0]
for t, tp in enumerate(raw_random_transposed):
    mean = np.mean(tp)
    s = 0
    for i, p in enumerate(tp):
        s += (p - mean) * (p - mean)
        if p >= 99 and prev[i] < 99:
            random_done[i] = t
    random_consistency += math.sqrt(s / len(tp))
    prev = tp

random_consistency /= len(raw_random_transposed)

biased_consistency = 0
biased_done = np.empty(len(raw_biased_transposed[0]))
prev = raw_biased_transposed[0]
for t, tp in enumerate(raw_biased_transposed):
    mean = np.mean(tp)
    s = 0
    for i, p in enumerate(tp):
        s += (p - mean) * (p - mean)
        if p >= 99 and prev[i] < 99:
            biased_done[i] = t
    biased_consistency += math.sqrt(s / len(tp))
    prev = tp
biased_consistency /= len(raw_biased_transposed)
print(f'Random done: {random_done}')
print(f'Mean: {np.mean(random_done)}')
print(f'Biased done: {biased_done}')
print(f'Mean: {np.mean(biased_done)}')
print(f'Random consistency: {random_consistency}')
print(f'Biased consistency: {biased_consistency}')

legend_handles={
        'biased': Line2D([0], [0], color='orange', lw=2, label=f'Biased'), 
        'random': Line2D([0], [0], color='blue', lw=2, label=f'Random')}

print(raw_data_biased.shape)
if np.size(raw_data_biased, 1) < np.size(raw_data_random, 1):
    raw_data_biased = np.pad(raw_data_biased, ((0, 0), (0, np.size(raw_data_random, 1) - np.size(raw_data_biased, 1))), 'constant', constant_values=None)
    print(raw_data_biased.shape)
else:
    pass
data = pd.DataFrame(np.transpose(np.concatenate((raw_data_biased, raw_data_random))), columns=[f'Trial {i+1} biased' for i in range(len(raw_data_biased))] + [f'Trial {i+1} random'for i in range(len(raw_data_random))])

ax = sns.lineplot(data=data, palette=[legend_handles['biased'].get_color()] * len(raw_data_biased) + [legend_handles['random'].get_color()] * len(raw_data_random), dashes=False)

#  raw_failures_random = [np.load(f'{sys.argv[1]}/random/failures{i}.npy') for i in range(len(raw_data_random))]
#  raw_failures_biased = [np.load(f'{sys.argv[1]}/biased/failures{i}.npy') for i in range(len(raw_data_biased))]

# failures
#  ax.axvline(x = 100, color='gray', label='t=100', ls='--') 
#  ax.text(100.5, 2, 't=100, 50 robots', rotation=-90, color='gray')
#  ax.axvline(x = 200, color='gray', ls='--') 
#  ax.text(200.5, 2, 't=200, 50 robots', rotation=-90, color='gray')

#  raw_failure_data = []
#  for i, trial_data in enumerate(raw_failures_random):
    #  values, counts = np.unique(trial_data, return_counts=True)
    #  for j, v in enumerate(values):
        #  y = raw_data_random[i][v]
        #  raw_failure_data.append({'x': v, 'y': y, 'count': counts[j]})
#  
#  
#  for i, trial_data in enumerate(raw_failures_biased):
    #  values, counts = np.unique(trial_data, return_counts=True)
    #  for j, v in enumerate(values):
        #  y = raw_data_biased[i][v]
        #  raw_failure_data.append({'x': v, 'y': y, 'count': counts[j]})
#  
#  if raw_failure_data:
    #  sns.scatterplot(data=pd.DataFrame(raw_failure_data), markers=True, x='x', y='y', legend=False)

plt.legend(handles=list(legend_handles.values()))
plt.show()
