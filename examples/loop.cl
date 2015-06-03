(* Tests assignments and loops. Inspired by an example from CS164 *)
class Main {
	main() : Object {
		let io : IO <- new IO in
		let x : Int <- 0 in
		while x < 9000 loop { 
			io.out_int(x);
			io.out_string("\n");
			x <- x+1;
		} pool
	};
};
