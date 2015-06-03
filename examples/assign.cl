class Main {
	x:Int;
	io:IO <- new IO;
	main () : Object {
		while x<9000 loop io.out_int (x<-x+1) pool
	};
};
