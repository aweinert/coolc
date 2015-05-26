class Main {
	main() : Object {
		let io: IO <- new IO in
		let x: Int <- 0 in
		while x < 10 loop {
			io.out_string("Factorial ");
			io.out_int(x);
			io.out_string(": ");
			io.out_int(self.factorial(x));
			io.out_string("\n");
			x <- x+1;
		} pool
	};

	factorial(x: Int) : Int {
		if x < 1 then 1 else x * factorial(x-1) fi
	};
};
