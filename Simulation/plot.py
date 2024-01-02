import sys
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd

raw_data_random = np.load(f'{sys.argv[1]}/random/coverage.npy')
raw_data_biased = np.load(f'{sys.argv[1]}/biased/coverage.npy')
#  plt.xlim([0, parameters.STEPS])
plt.ylim([0, 100])
data_random = []
data_biased = []
for d in raw_data_random:
    data_random.append(pd.DataFrame(d))

for d in raw_data_biased:
    data_biased.append(pd.DataFrame(d))
print(data_random)
print(data_biased)
for d in data_random:
    sns.lineplot(data=d)
for d in data_biased:
    sns.lineplot(data=d, palette=['red'])
plt.show()
