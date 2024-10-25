# Advanced Algorithms: ICSISCET 2024
## Problem Statement: 
Schedule 50,000 conference talks across multiple rooms such that no two talks assigned to the same room overlap in time. 
The goal is to minimize the number of rooms required while also demonstrating the efficiency of different scheduling approaches.

## Naive Scheduling Approach:

Uses a brute-force algorithm to try all possible room assignments.
Iterates through each room and checks all scheduled talks for conflicts before assigning a new talk.
This approach may become inefficient as the number of talks increases, due to its high time complexity.

## Constraint Propagation Approach:

Uses constraint propagation techniques to optimize the scheduling process.
Sorts the talks by their start times to reduce the complexity of checking for conflicts.
Assigns talks to rooms based on end times, which results in fewer comparisons and reduced execution time.
Demonstrates a significant improvement in execution efficiency, especially with a large number of talks.