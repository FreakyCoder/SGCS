import numpy as np
import matplotlib.pyplot as plt
import bots50.exp1.parameters as parameters

fig, sp = plt.subplots(2, 2, layout='compressed')
cbar_ax = fig.add_axes([.91, .3, .03, .4])
hm = sp.flatten()

data = np.load('bots50/exp1/biased/area0.npy')
print(data)
for i in range(len(data)):
    hm[i].set_xlim([0, parameters.WIDTH])
    hm[i].set_ylim([0, parameters.HEIGHT])
    hm[i].set_adjustable('box')
    hm[i].set_aspect('equal')
    hm[i].set_label(f'Quater {i + 1}')
    hm[i].imshow(data[i])

plt.show()
