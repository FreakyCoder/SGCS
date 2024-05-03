import sys
import numpy as np
import matplotlib
import matplotlib.pyplot as plt
from matplotlib.ticker import LinearLocator
import seaborn as sns

matplotlib.rc('font', family='Times New Roman') 
fig, sp = plt.subplots(2, 2, layout='compressed')
hm = sp.flatten()
ticks = [-400, -300, -200, -100, 0, 100, 200, 300, 400]

data = np.load(f'{sys.argv[1]}/area0.npy')
for i in range(len(data)):
    sns.heatmap(data=data[i], ax=hm[i], robust=True, square=True, cbar=False)
    hm[i].set_title(f'Quater {i + 1}', fontdict={'fontsize': '11'})
    hm[i].set_xticks([x + 400 for x in ticks])
    hm[i].set_xticklabels(ticks)
    hm[i].set_yticks([y + 400 for y in ticks])
    hm[i].set_yticklabels(ticks)

plt.show()
