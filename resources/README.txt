EC - Assignment - THOMAS KLEIVEN - KLVTHO001

I have compared my results with Halvor Reiten - RTNHLV001.

My algorithm runs
in approx. 16 seconds on one core on a 2010 Dell Latitude,
Intel Core i5 (4th Gen) 4300U / 1.9 GHz and 8GB RAM. That is,
one run requires about 0.16 seconds.

When running on four cores using Java, I can achieve a speed-up of
approx. 80%. When parallelizing in bash the algorithm runs
in approx. 8 secs. The speed-up in bash vs. Java's parallel
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

I have been inspired by the implementation in
"An improved hybrid genetic algorithm with a new local search procedure"
by Wan, Wen and Birch, Jeffrey B, which was published by Hindawi in
Journal of Applied Mathematics in 2013.

I have done some adjustment to the framework. In my opinion, the framework is
more object-oriented now and it is easier to parallelize the code.

################################################################################
A short summary of the code:

A new Main object is created for each run. The Main object creates a
an instance of Evolution and this object together with the Main object
is responsible for evolving the chromosomes for 100 generations.
This algorithm proposes a novel, simplified, and efficient
Hybrid Genetic Algorithm with an individual learning procedure that
performs a local search within a child only when the best offspring (solution)
in the offspring population is also the best in the current parent population.
################################################################################

The statistical tests applied to the final dataset can
be seen in the "ks.py" file. Numpy's normality test claims that
my dataset is most likely not normally distributed, thus
I chose the Kolmogorov-Smirnov test in order
to determine if there is any statistically significant difference
betweeen the fitness results of the two EAs.

The test concludes that our final datasets are likely drawn
from the same distribution, i.e. there
is no statistically significant difference in the two samples. The
test is performed with a significance level of alpha=0.001.

I believe that there is no significant difference because we
have been working together and we have implemented many of the same
solutions. Our solution is similar and
we use almost the
same cross-over-rate, mutation-rate, tournament selection,
inverse mutation, local search algorithm, adaptive mutation rate, etc.

I have done a lot of experimenting where I have tried
different procedures and I have found that
tournament selection and the inverse mutation works
quite well. I have also implemented the ranked-based-selection and
swap-mutation, but this works slightly worse compared to
the aforementioned settings.

To run the algorithm, go to your terminal and execute the following command:
./gradlew run -PappArgs="[number_of_runs]"

Example:
./gradlew run -PappArgs="[100]"

To run the Python3-script, run:
  - To plot the distribution with an overlay of the corresponding
    normal distribution curve, run:

      python3 ks.py [filename1] --normal

      Example:
      python3 ks.py results_thomas.out --normal

  - To run the KS-test for two datasets, run:

      python3 ks.py results_thomas.out results_halvor.out --ks-test

  - To run the 2-sampled Student-t test for two datasets, given
    that they both are normally distributed, run:

      python3 ks.py results_thomas.out results_halvor.out --normal

To parallelize the hybrid-genetic-algorithm in bash, see the file run.sh.

Alternatively, clone my github's master branch and follow the instructions:
https://github.com/thomaskleiven/EC
