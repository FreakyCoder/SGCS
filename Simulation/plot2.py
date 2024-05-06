import sys
import math
import numpy as np
import matplotlib
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
matplotlib.rc('font', family='Times New Roman') 

bot_count = []
data_agg = []
max_len = 0
for i in [10, 20, 50, 100, 500, 750, 1000]:
    raw_data = np.load(f'bots{i}/exp1/biased/coverage.npy')
    bot_count.extend([str(i) + " bots"] * np.size(raw_data, 0))
    for d in raw_data:
        max_len = 30000
        data_agg.append(d[:max_len])
data_agg = list(map(lambda d: np.pad(d, (0, max_len - len(d)), 'constant', constant_values=None), data_agg))
    
df = pd.DataFrame(data=data_agg, index=bot_count).transpose()
df = df.groupby(df.index//10).mean()
print(df)

ax = sns.lineplot(data=df, errorbar=None, dashes=False)
ax.set(ylim=(0, 100), xlim=(0, max_len//30))
ax.set_xlabel('Timestep')
ax.set_ylabel('% Area Covered')

plt.legend(loc='center right', title='Bot Count')
plt.show()

