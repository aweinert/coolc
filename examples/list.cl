(* Showcases typecase-mechanism and simple data structures in cool *)
(* Inspired by an example from CS164 *)

class Publication {
	print_content() : Object { self };
};

class Book inherits Publication {
	title: String;
	author: String;

	init(title_param: String, author_param: String) : Book {
		{
			title <- title_param;
			author <- author_param;
			self;
		}
	};

	print_content() : Object {
		let io : IO <- new IO in {
			io.out_string(author);
			io.out_string(": ");
			io.out_string(title);
			io.out_string("\n");
		}
	};

	get_title(): String { title };
	get_author(): String { author };
};

class Article inherits Publication {
	title: String;
	author: String;
	magazine: String;

	init(title_param: String, author_param: String, magazine_param: String) : Article {
		{
			title <- title_param;
			author <- author_param;
			magazine <- magazine_param;
			self;
		}
	};

	print_content() : Object {
		let io : IO <- new IO in {
			io.out_string(author);
			io.out_string(": ");
			io.out_string(title);
			io.out_string(" (appeared in ");
			io.out_string(magazine);
			io.out_string(")\n");
		}
	};

	get_title(): String { title };
	get_author(): String { author };
	get_magazine(): String { magazine };
};

class PublicationList {
	publication: Publication;
	next : PublicationList;

	init(publication_param: Publication): PublicationList {
		{
			publication <- publication_param;
			self;
		}
	};

	print_types() : Object {
		let io : IO <- new IO in
		let list : PublicationList <- self in
		while not isvoid list loop {
			case list.get_elem() of
			 	b : Book => io.out_string("Book\n");
				a : Article => io.out_string("Article\n");
			esac;
			list <- list.getNext();
		} pool
	};

	print_contents() : Object {
		let io : IO <- new IO in
		let list : PublicationList <- self in
		while not isvoid list loop {
			list.get_elem().print_content();
			list <- list.getNext();
		} pool

	};

	append(publication: Publication): PublicationList {
		let lastElem : PublicationList <- self in {
			while lastElem.hasNext() loop lastElem <- lastElem.getNext() pool;
			lastElem.setNext(new PublicationList.init(publication));
			self;
		}
	};

	prepend(publication: Publication): PublicationList { new PublicationList.init(publication).setNext(self) };

	hasNext() : Bool { not isvoid next };
	setNext(next_param : PublicationList): PublicationList {
		{
			next <- next_param;
			self;
		}
	};
	getNext(): PublicationList { next };

	get_elem(): Publication { publication };
};

class Main {
	main() : Object {
		let io : IO <- new IO in
		let list : PublicationList <- new PublicationList in {
			list.init(new Book.init("The Art of Computer Programming", "Donald Knuth"));
			list.append(new Article.init("On Computable Numbers with an Application to the Entscheidungsproblem", "Alan Mathison Turing", "Journal of Math"));
			list.append(new Book.init("Introduction to Model Checking", "Christel Baier, Joost-Pieter Katoen"));
			list.append(new Article.init("Non-Cooperative Games", "John Forbes Nash", "The Annals of Mathematics"));
			list.print_contents();
		}
	};
};
