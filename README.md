A compiler for the [The Classroom Object-Oriented Language](http://theory.stanford.edu/~aiken/software/cool/cool.html) (or Cool, for short), which was developed by Alex Aiken at Stanford University.
This compiler takes a Cool-program as input and emits jar-files.
At the moment, there are a couple of deviations from standardized Cool.
These will be fixed later down the line.

# Quick start
Since the compiler itself is written in Java and uses maven for its builds, please make sure that you have maven installed.
On Ubuntu-based systems, this should be as simple as `sudo apt-get install maven`.

Afterwards, you can download and compile the compiler like this:

    $ https://github.com/aweinert/coolc.git
    [...]
    $ mvn compile
    [...]
    $ ./run examples/hello-world.cl
    $ java -jar out.jar
    Hello World

## Examples

There are a couple of examples in the `examples`-directory that you can use to start out.
After compiling the compiler, just run `./run examples/hello-world.cl` to compile the obligatory hello-world-example.

# Cool

Cool is a purely object oriented language.
Thus, a program is composed of classes, which in turn contain methods and fields (or attributes, as they are known in other languages).
A method consists of a single expression that is evaluated whenever the method is called.

## Limitations of Cool

Since Cool was only ever meant to be an educational tool, there are a couple of things that you may be used to from other languages:

- There is no way to pass parameters from the command line to the program.
- There is no way to access the file system
- Most other languages are somewhat lenient when it comes to the positions of semicolons and other purely syntactical constructs as long as the intended behavior is clear. Cool isn't.
- Cool does not know visibility modifiers. All attributes are private and all methods are public.

# Differences to standard Cool
- Static function call is not implemented and works like a normal function call
- When no branch of a typecase-statement matches, the behavior is undefined
  instead of exiting with an error message
