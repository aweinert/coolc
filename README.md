A compiler for the [The Classroom Object-Oriented Language](http://theory.stanford.edu/~aiken/software/cool/cool.html) (or Cool, for short), which was developed by Alex Aiken at Stanford University.
This compiler takes a Cool-program as input and emits Java-code.
At the moment, there are a couple of deviations from standardized Cool.
These will be fixed later down the line.

[![Code Climate](https://codeclimate.com/github/aweinert/coolc/badges/gpa.svg)](https://codeclimate.com/github/aweinert/coolc)

# Setup
Since the compiler itself is written in Java and uses maven for its builds, please make sure that you have maven installed.
On Ubuntu-based systems, this should be as simple as `sudo apt-get install maven`.

Once you have done that, clone this repository with `git clone https://github.com/aweinert/coolc.git`.
You can then compile the compiler with `mvn compile`.
It can be run using the `run`-script in the base directory.
This produces a file `out.jar` in the base directory.

# Differences to standard Cool
- Static function call is not implemented and works like a normal function call
