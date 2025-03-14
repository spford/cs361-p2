# Project 2: Nondeterministic Finite Automata

* Author: Spencer Ford, Luis Acosta
* Class: CS361 Section #001
* Semester: Spring 2025

## Overview

This program implements NFA and FA interfaces making it possible to create nondeterministic finite automata.
After these machines are made the user can use this program to see if any string is in the language described by
the NFA.

## Reflection

Because this program is so similar to project 1 we started by implemented the similar methods of the 
DFA project and then focused on eClosure method. We knew eClosure would be important for determining
valid transitions from a specified state. The rest of the program had similar methods so the base
algorithm was copied and adjusted to properly implement the NFAInterface<br />
Spencer Ford

//ACOSTABSU

## Compiling and Using

javac -cp .:/usr/share/java/junit.jar ./test.nfa/NFATest.java
java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/core/jar
org.junit.runner.JUnitCore test.nfa.NFATest

## Sources used

We used Oracles website to find the best data structures to implement the appropriate interfaces. <br />
[Hash Map](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) <br />
[Linked Hash Set](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashSet.html) <br />
[Tree Set](https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html) <br />

----------
