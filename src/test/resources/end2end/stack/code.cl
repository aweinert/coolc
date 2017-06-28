(* Parent class for all commands *)
class Command {
	output : IO <- new IO;

	evaluate(stack : CommandStack) : CommandStack {
		{
		output.out_string("Command.evaluate() - This method should never be called");
		stack;
		}
	};

	display() : String {
		"This method should never be called directly"
	};
};

(* A simple integer on the stack *)
class IntegerCommand inherits Command {
	value : Int;
	atoi : A2I <- new A2I;

	evaluate(stack : CommandStack) : CommandStack {
		stack
	};

	init(valuePar : Int) : IntegerCommand {
		{
		value <- valuePar;
		self; 
		}
	};

	getValue() : Int {
		value
	};

	display() : String { atoi.i2a(value) };
};

(* The command to add the following two integers *)
class AddCommand inherits Command {
	evaluate(stack : CommandStack) : CommandStack {
		(* Remember that the command itself is still on the stack, thus start with stack.tail() *)
		let opOne : Command <- stack.tail().head(), opTwo : Command <- stack.tail().tail().head() in
		case opOne of
			intOpOne : IntegerCommand => case opTwo of
				intOpTwo : IntegerCommand => evaluateInternal(stack, intOpOne.getValue(), intOpTwo.getValue());
			esac;
		esac
	};

	(* Factor out the actual addition of the two integers together with the push of the result for readability *)
	evaluateInternal(stack : CommandStack, opOne : Int, opTwo : Int) : CommandStack {
		let result : Int <- (opOne + opTwo) in
		let resultCommand : IntegerCommand <- (new IntegerCommand).init(result) in
		let poppedStack : CommandStack <- stack.tail().tail().tail() in
		poppedStack.push(resultCommand)
	};

	display() : String { "+" };
};

(* The command to swap the following two integers *)
class SwapCommand inherits Command {
	evaluate(stack : CommandStack) : CommandStack {
		let opOne : Command <- stack.tail().head(), opTwo : Command <- stack.tail().tail().head() in
		stack.tail().tail().tail().push(opOne).push(opTwo)
	};

	display() : String { "s" };
};

(* The actual stack of commands *)
class CommandStack {
   head : Command;
   tail : CommandStack;

   isEmpty() : Bool { isvoid head };

   head() : Command { head };

   tail()  : CommandStack { tail };

   init(headPar : Command, tailPar : CommandStack) : CommandStack { 
	{ 
		head <- headPar; 
		tail <- tailPar; 
		self;
	} 
   };

   (* Returns a new commandstack whose head is the given command and whose tail is self *)
   push(com : Command) : CommandStack {
   	(new CommandStack).init(com, self)
   };
};

(* Exposes methods corresponding to the actual commands issued to the stack interpreter *)
class StackMachine {
	stack : CommandStack <- new CommandStack;
	io : IO <- new IO;

	pushInteger(value: Int) : StackMachine {
		{
		stack <- stack.push((new IntegerCommand).init(value));
		self;
		}
	};

	pushAddition() : StackMachine {
		{
		stack <- stack.push(new AddCommand);
		self;
		}
	};

	pushSwap() : StackMachine {
		{
		stack <- stack.push(new SwapCommand);
		self;
		}
	};

	evaluateTop() : StackMachine {
		{
		if (not stack.isEmpty()) then
			stack <- stack.head().evaluate(stack)
		else self fi;
		self;
		}
	};

	display() : Object {
		let currentStack : CommandStack <- stack in
		while (not currentStack.isEmpty()) loop { 
			io.out_string(currentStack.head().display());
			io.out_string("\n");
			currentStack <- currentStack.tail();
		} pool
	};
};

class Main {
   io : IO <- new IO;
	atoi : A2I <- new A2I;

   main() : Object { {
	show_usage();
	let machine : StackMachine <- new StackMachine in
	{
		io.out_string(">");
		let currentInput : String <- io.in_string() in
		while (not currentInput = "x") loop {
			if currentInput = "+" then { {
				machine <- machine.pushAddition();
			}; } else if currentInput = "s" then { {
				machine <- machine.pushSwap();
			}; } else if currentInput = "e" then { {
				machine <- machine.evaluateTop();
			}; } else if currentInput = "d" then { {
				machine.display();
			}; } else { {
				machine <- machine.pushInteger(atoi.a2i(currentInput));
			}; } fi fi fi fi;
			io.out_string(">");
			currentInput <- io.in_string();
		} pool;
	};
   } };

  show_usage() : Object { {
	io.out_string("This is a very simple stack machine that only supports the following commands:\n");
	io.out_string("<int> - Push the given integer on top of the stack. Cannot be evaluated.\n");
	io.out_string("+     - Push an addition command on the stack. When evaluated, it takes the next two elements from the stack,\n");
	io.out_string("        which must be ints, adds them and pushes the result back on the stack.\n");
	io.out_string("s     - Push a swap command on the stack. When evaluated, this takes the next two elements from the stack,\n");
	io.out_string("        and pushes them back on the stack in reverse order.\n");
	io.out_string("e     - Evaluate the command on top of the stack.\n");
	io.out_string("d     - Display the current stack.\n");
	io.out_string("x     - Quit the program.\n");
	} };
};

class A2I {

     c2i(char : String) : Int {
	if char = "0" then 0 else
	if char = "1" then 1 else
	if char = "2" then 2 else
        if char = "3" then 3 else
        if char = "4" then 4 else
        if char = "5" then 5 else
        if char = "6" then 6 else
        if char = "7" then 7 else
        if char = "8" then 8 else
        if char = "9" then 9 else
        { abort(); 0; }  (* the 0 is needed to satisfy the
				  typchecker *)
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   i2c is the inverse of c2i.
*)
     i2c(i : Int) : String {
	if i = 0 then "0" else
	if i = 1 then "1" else
	if i = 2 then "2" else
	if i = 3 then "3" else
	if i = 4 then "4" else
	if i = 5 then "5" else
	if i = 6 then "6" else
	if i = 7 then "7" else
	if i = 8 then "8" else
	if i = 9 then "9" else
	{ abort(); ""; }  -- the "" is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   a2i converts an ASCII string into an integer.  The empty string
is converted to 0.  Signed and unsigned strings are handled.  The
method aborts if the string does not represent an integer.  Very
long strings of digits produce strange answers because of arithmetic 
overflow.

*)
     a2i(s : String) : Int {
        if s.length() = 0 then 0 else
	if s.substr(0,1) = "-" then ~a2i_aux(s.substr(1,s.length()-1)) else
        if s.substr(0,1) = "+" then a2i_aux(s.substr(1,s.length()-1)) else
           a2i_aux(s)
        fi fi fi
     };

(* a2i_aux converts the usigned portion of the string.  As a
   programming example, this method is written iteratively.  *)


     a2i_aux(s : String) : Int {
	(let int : Int <- 0 in	
           {	
               (let j : Int <- s.length() in
	          (let i : Int <- 0 in
		    while i < j loop
			{
			    int <- int * 10 + c2i(s.substr(i,1));
			    i <- i + 1;
			}
		    pool
		  )
	       );
              int;
	    }
        )
     };

(* i2a converts an integer to a string.  Positive and negative 
   numbers are handled correctly.  *)

    i2a(i : Int) : String {
	if i = 0 then "0" else 
        if 0 < i then i2a_aux(i) else
          "-".concat(i2a_aux(i * ~1)) 
        fi fi
    };
	
(* i2a_aux is an example using recursion.  *)		

    i2a_aux(i : Int) : String {
        if i = 0 then "" else 
	    (let next : Int <- i / 10 in
		i2a_aux(next).concat(i2c(i - next * 10))
	    )
        fi
    };

};
