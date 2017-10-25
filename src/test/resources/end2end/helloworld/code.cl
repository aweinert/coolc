class Main {
	x : String <- "Hello World\n";
	my_init() : Object { x <- "Hallo World\n" };
	main() : Object { {
		my_init(); (new IO).out_string(x);
	} };
};
