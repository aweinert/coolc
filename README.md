A compiler for the [The Classroom Object-Oriented Language](http://theory.stanford.edu/~aiken/software/cool/cool.html) (or Cool, for short), which was developed by Alex Aiken at Stanford University.
This compiler takes a Cool-program as input and emits Java-code.
At the moment, there are a couple of deviations from standardized Cool.
These will be fixed later down the line.

# Setup
Since the compiler itself is written in Java and uses maven for its builds, please make sure that you have maven installed.
On Ubuntu-based systems, this should be as simple as `sudo apt-get install maven`.

Once you have done that, clone this repository with `git clone https://github.com/aweinert/coolc.git`.
You can then compile the compiler with `mvn compile`.
The resulting compiler can be run using the `run`-script in the base directory.

# Differences to standard Cool
- Case-statements are evaluated differently.
  The generated code tests the condition of each case-statement.
  If the given expression is of the given type, then the branch is executed.
  *This means that multiple branches of the case-statement may be executed.*
  This does not happen if the types of the case-branches are disjoint.
- Static function call is not implemented and works like a normal function call
