from scipy.stats import ks_2samp
import scipy
import numpy as np
from matplotlib import pyplot as plt
from scipy.stats import ttest_ind
import sys

args = [arg for arg in sys.argv]

data = np.loadtxt('evolution.out', delimiter="\n")
average = np.zeros(100)

print(len(data))

for i in range(0,len(data)):
    average[i%100] = average[i%100] + data[i]

print(data[100])
average /= 100
fig = plt.figure()
plt.plot(average)
plt.show()
