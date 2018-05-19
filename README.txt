EC - Assignment - THOMAS KLEIVEN - KLVTHO001

I have compared my results with Halvor Reiten - RTNHLV001. My algorithm runs
in approx. 16 seconds on one core on a 2010 Dell Latitude,
Intel Core i5 (4th Gen) 4300U / 1.9 GHz and 8GB RAM. That is,
one run requires about 0.16 seconds.

When running on four cores using Java, I can achieve a speed-up of
approx. 80%. When parallellising in bash the algorithm runs
in approx. 8secs. The speed-up in bash vs. Java's parallel
stream is most likely due to less communication between cores when using
bash.

My results:
Best  : 2380
Mean  : 2762
Worst : 2990

His results:
Best  : 2381
Mean  : 2769
Worst : 2955

The statistical tests applied to the resulting dataset can
be seen in the "ks.py" file. Numpy's normality test claims that
my dataset is most likely not normally distributed, thus
I chose the Kolmogorov-Smirnov statistic test in order
to determine if there is any statistically significant difference
betweeen the fitness results of the two EAs.

The test concludes that the samples are likely drawn
from the same distributions, i.e. there
is no significantly difference in the two samples.

I am not surprised that there is no statistically significant
difference between the to EAs, mostly because we
have been working together and we have basically implemented
the same algorithm, i.e. currently we use the
same cross-over-rate, mutation-rate, tournament selection,
inverse mutation, local search algorithm, etc.

I have done a lot of experimenting where we have tried
different procedures and I have found that
tournament selection and the inverse mutation works
quite well. I have also implemented the ranked-based-selection and
swap-mutation, but this works slightly worse compared to
the aforementioned settings.

I have been following the implementation in
"An improved hybrid genetic algorithm with a new local search procedure"
by Wan, Wen and Birch, Jeffrey B, which was published by Hindawi in
Journal of Applied Mathematics in 2013.

To run the algorithm, go to your terminal and execute the following command:
./gradlew run -PappArgs="[number_of_runs]"

Example:
./gradlew run -PappArgs="[100]"
