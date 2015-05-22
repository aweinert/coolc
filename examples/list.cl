(* Showcases typecase-mechanism and simple data structures in cool *)
(* Inspired by an example from CS164 *)

class Publication { };

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

	get_title(): String { title };
	get_author(): String { author };
	get_magazine(): String { magazine };
};

class PublicationList {
	publication: Publication;
	next : PublicationList;

	init(publication_param: Publication, next_param: PublicationList): PublicationList {
		{
			publication <- publication_param;
			publicationList <- publicationList_param;
			self;
		}
	};

	prepend(publication: Publication): PublicationList {
		new PublicationList.init(publication, self)
	};
};
