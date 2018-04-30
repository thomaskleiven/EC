import numpy as np
import matplotlib as mpl
import re
import sys
from os import listdir
from os.path import isfile, join
mpl.rcParams['svg.fonttype'] = "none"
mpl.rcParams['font.size'] = 16
from matplotlib import pyplot as plt

data = []

for line in reversed(open('results.out').readlines()):
    try:
        isdigit = float(line.strip("\n"))
    except:
        isdigit = False
    if (isdigit):
        data.append(isdigit)
    if ("Run Stats for experiment at:" in line):
        break

data = data[::-1]

fig = plt.figure()
ax = fig.add_subplot(1,1,1)

ax.plot(data)

ax.spines["right"].set_visible(False)
ax.spines["top"].set_visible(False)
ax.xaxis.set_ticks_position("bottom")
ax.yaxis.set_ticks_position("left")
plt.show()
