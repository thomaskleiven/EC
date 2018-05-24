# A Hybrid Genetic Algorithm with a Local Search Procedure
Original code by Geoff Nitschke, with hybrid modifications by Thomas Kleiven.

This a Evolutionary Algorithm (EA) for solving the dynamic Traveling Salesman Problem. 

This repository proposes a novel, simplified, and efficient Hybrid Genetic Algorithm with an individual learning procedure that performs a local search within a child only when the best offspring (solution) in the offspring population is also the best in the current parent population.

#### Cities
Make sure your list of cities to visit are on same format as CityList.txt

### Run the program
```java
./gradlew run -PappArgs="[number_of_runs]"
```

### Example
```java
./gradlew run -PappArgs="[100]"
```
