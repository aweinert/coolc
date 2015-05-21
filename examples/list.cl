(* Showcases typecase-mechanism and simple data structures in cool *)
(* Inspired by an example from CS164 *)

class Publication { };

class Book inherits Publication {
	title: String;
	author: String;

	init(title: String, author: String) : Book {
		{
			self.title <- title;
			self.author <- author;
			self;
		}
	};

	get_title(): String { self.title };
	get_author(): String { self.author };
};

class Article inherits Publication {
	title: String;
	author: String;
	magazine String;

	init(title: String, author: String, magazine: String) : Article {
		{
			self.title <- title;
			self.author <- author;
			self.magazine <- magazine;
			self;
		}
	};

	get_title(): String { self.title };
	get_author(): String { self.author };
	get_magazine(): String { self.magazine };
};

class PublicationList {
	publication: Publication;
	next : PublicationList;

	init(publication: Publication, next: PublicationList): PublicationList {
		{
			self.publication <- publication;
			self.publicationList <- publicationList;
			self;
		}
	};

	prepend(publication: Publication): PublicationList {
		new PublicationList.init(publication, self)
	};
};
