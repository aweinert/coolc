/*
 * CS164: Spring 2004
 * Programming Assignment 2
 *
 * The scanner definition for Cool.
 *
 */

package net.alexweinert.coolc.processors.cool.frontend.lexer;

import net.alexweinert.coolc.processors.cool.frontend.LineSymbol;
import net.alexweinert.coolc.processors.cool.frontend.parser.Tokens;
import net.alexweinert.coolc.representations.cool.symboltables.*;

%%

%public

/* Code enclosed in %{ %} is copied verbatim to the lexer class definition.
 * All extra variables/methods you want to use in the lexer actions go
 * here.  Don't remove anything that was here initially.  */
%{
	private IdTable idTable;
	private StringTable stringTable;
	private IntTable intTable;
	
	public void setIdTable(final IdTable idTable) {
		this.idTable = idTable;
	}
	
	public void setStringTable(final StringTable stringTable) {
		this.stringTable = stringTable;
	}
	
	public void setIntTable(final IntTable intTable) {
		this.intTable = intTable;
	}
	
    // Max size of string constants
    final static int MAX_STR_CONST = 1024;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    // For line numbers
    private int curr_lineno = 1;

    private void increment_lineno() { this.curr_lineno += 1; }

    public int get_curr_lineno() {
		return curr_lineno;
    }

    private String filename;

    public void set_filename(String fname) {
		filename = fname;
    }

    public String curr_filename() {
		return filename;
    }

	// The nesting depth of the current comment. 0 denotes the top-level comment.
	private int commentDepth = 0;

	private LineSymbol createStringToken(int type, int lineno, String value) {
		StringSymbol symbol = this.stringTable.addString(value);
		return new LineSymbol(type, lineno, symbol);
	}

	private LineSymbol createIdToken(int type, int lineno, String value) {
		IdSymbol symbol = this.idTable.addString(value);
		return new LineSymbol(type, lineno, symbol);
	}
%}


/*  Code enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here. */
%init{
    // All internal fields are initialized at declaration, so nothing to initialize here
%init}

/*  Code enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work. */
%eofval{
    switch(zzLexicalState) {
    case YYINITIAL:
		/* nothing special to do in the initial state */
		break;
	case LINE_COMMENT:
		/* A line comment can also be ended with an EOF */
		break;
	case COMMENT:
		// end of file terminates a comment, so reset the nesting depth
		yybegin(YYINITIAL);
		this.commentDepth = 0;

		// Hand the error over to the parser
		return createStringToken(Tokens.ERROR, this.get_curr_lineno(), "EOF in comment");
	case STRING:
		/* end of file terminates a comment, so it does not matter whether we 
		 * go to YYINITIAL or DISCARDSTRING. Both will eventually return EOF */
		yybegin(YYINITIAL);
		return createStringToken(Tokens.ERROR, this.get_curr_lineno(), "EOF in string constant");
	}

    return new LineSymbol(Tokens.EOF, this.get_curr_lineno());
%eofval}

/* Do not modify the following two jlex directives */
%class Lexer
%cup

/* Define names for regular expressions here. */
/* Define newline seperately from whitespace, since it has to handled as a special
 * case quite often */
NEWLINE		= \n
WHITESPACE	= [ \f\r\t]

/* Definitions according to COOL manual section 10.1 */
/* Regular expression that matches every integer */
INTEGER = [0-9]+
/* Regular expression that matches type id. Note that type ids have to start with an uppercase letter */
TYPE_ID = [A-Z][a-zA-Z0-9_]*
/* Regular expression that matches object id. Note that object ids have to start with a lowercase letter */
OBJECT_ID = [a-z][a-zA-Z0-9_]*


// Start condition when we are currently parsing a single-line comment
%state LINE_COMMENT
// Start condition when we are currently parsing a (possibly nested) comment
%state COMMENT
// Start condition when we are currently parsing a string constant
%state STRING
// Start condition when we encountered an error in a string and have to look for its end
%state DISCARDSTRING


/* Define lexical rules after the %% separator.  There is some code
 * provided for you that you may wish to use, but you may change it
 * if you like.
 * .
 * Some things you must fill-in (not necessarily a complete list):
 *   + Handle (* *) comments.  These comments should be properly nested.
 *   + Some additional multiple-character operators may be needed.  One
 *     (DARROW) is provided for you.
 *   + Handle strings.  String constants adhere to C syntax and may
 *     contain escape sequences: \c is accepted for all characters c
 *     (except for \n \t \b \f) in which case the result is c.
 * .
 * The complete Cool lexical specification is given in the Cool
 * Reference Manual (CoolAid).  Please be sure to look there. */
%%

<YYINITIAL>{WHITESPACE}+ { /* Eat up whitespace. Note that whitespace does not include newline */ }


<YYINITIAL>"--"         { 
	// Start of a single-line-comment
	yybegin(LINE_COMMENT); 
}
<LINE_COMMENT>.*        { /* Just munch up all characters inside a line comment */ }
<LINE_COMMENT>{NEWLINE} { 
	/* Since a line comment is ended by a newline, switch to initial mode again */
	this.increment_lineno();
	yybegin(YYINITIAL); 
}

<YYINITIAL> \*\) {
	// Since we are in YYINITIAL, we know that we encountered an unmatched comment end
	StringSymbol errorMessage = StringTable.getInstance().addString("Unmatched *)");
	return new LineSymbol(Tokens.ERROR, this.get_curr_lineno(), errorMessage);
}

<YYINITIAL> \(\* { 
	// Start a new comment, reset the comment depth just to be sure
	commentDepth = 0;
	yybegin(COMMENT); 
}

<COMMENT> \(\* { 
	// Start of a nested comment
	commentDepth += 1;
}
<COMMENT> \*\) {
	// If we are currently inside a nested comment, step out of it. Otherwise, resume normal lexing
	if(commentDepth == 0) {
		yybegin(YYINITIAL);
	} else {
		commentDepth -= 1;
	}
}
<COMMENT> . { /* Eat up everything that is neither "(*" nor "*)" */ }
<COMMENT> {NEWLINE} {
	// Also count newlines inside a comment
	this.increment_lineno();
}

<YYINITIAL> \" {
	// Start a new string constant
	this.string_buf = new StringBuffer();
	yybegin(STRING);
}

<STRING> "\b" {
	// Handle escaped backspace
	this.string_buf.append('\b');
}
<STRING> "\\t" {
	// Handle escaped tab
	this.string_buf.append('\t');
}
<STRING> "\\n" {
	// Handle escaped newline
	this.string_buf.append('\n');
}
<STRING> "\f" {
	// Handle escaped formfeed
	this.string_buf.append('\f');
}
<STRING> {NEWLINE} {
	// Also handle newlines inside string constants
	this.increment_lineno();
	this.string_buf.append('\n');
}
<STRING> \000 {
	yybegin(DISCARDSTRING);
	return createStringToken(Tokens.ERROR, this.get_curr_lineno(), "String contains null character");
}
<STRING> \\. { 
	// matches any other escaped character, which is just lexed as the character
	// use substring(1) to remove the first character (the \)
	this.string_buf.append(yytext().substring(1)); 
}

<STRING> \n {
	// The actual newline character is disallowed, reject the string
	this.increment_lineno();
	/* We switch to YYINITIAL instead of DISCARDSTRING since we already found the end of the line.
	 * DISCARDSTRING would look for the *next* newline */
	yybegin(YYINITIAL);
	return createStringToken(Tokens.ERROR, "Unterminated string constant");
}
<STRING> \" {
	// Found the end of the literal, tokenize the string if it not too long
	yybegin(YYINITIAL);
	String literal = this.string_buf.toString();
	final StringSymbol value;
	final int tokenCategory;
	if(literal.length() > MAX_STR_CONST) {
		return createStringToken(Tokens.ERROR, this.get_curr_lineno(), "String constant too long");
	} else {
		return createStringToken(Tokens.STR_CONST, this.get_curr_lineno(), literal);
	}
}
<STRING> . {
	// Every symbol not caught by the rules above is just included in the constant
	this.string_buf.append(yytext());
}



<DISCARDSTRING> "\"" { /* Include this lexeme explicitly to jump over it */ }
<DISCARDSTRING> \" { 
	/* Found the end of the string, return to normal-mode lexing */
	yybegin(YYINITIAL); 
}
<DISCARDSTRING> {NEWLINE} { 
	/* Found the end of the string, return to normal-mode lexing */
	this.increment_lineno();
	yybegin(YYINITIAL); 
}
<DISCARDSTRING> . { /* Eat up all remaining characters in the string, until we find either a " or a newline */ }


<YYINITIAL>{INTEGER}  { /* Integers */
                          return new LineSymbol(Tokens.INT_CONST, this.get_curr_lineno(), IntTable.getInstance().addInt(yytext())); }

<YYINITIAL>[Cc][Aa][Ss][Ee]	{ return new LineSymbol(Tokens.CASE, this.get_curr_lineno()); }
<YYINITIAL>[Cc][Ll][Aa][Ss][Ss] { return new LineSymbol(Tokens.CLASS, this.get_curr_lineno()); }
<YYINITIAL>[Ee][Ll][Ss][Ee]  	{ return new LineSymbol(Tokens.ELSE, this.get_curr_lineno()); }
<YYINITIAL>[Ee][Ss][Aa][Cc]	{ return new LineSymbol(Tokens.ESAC, this.get_curr_lineno()); }
<YYINITIAL>f[Aa][Ll][Ss][Ee]	{ return new LineSymbol(Tokens.BOOL_CONST, this.get_curr_lineno(), Boolean.FALSE); }
<YYINITIAL>[Ff][Ii]             { return new LineSymbol(Tokens.FI, this.get_curr_lineno()); }
<YYINITIAL>[Ii][Ff]  		{ return new LineSymbol(Tokens.IF, this.get_curr_lineno()); }
<YYINITIAL>[Ii][Nn]             { return new LineSymbol(Tokens.IN, this.get_curr_lineno()); }
<YYINITIAL>[Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss] { return new LineSymbol(Tokens.INHERITS, this.get_curr_lineno()); }
<YYINITIAL>[Ii][Ss][Vv][Oo][Ii][Dd] { return new LineSymbol(Tokens.ISVOID, this.get_curr_lineno()); }
<YYINITIAL>[Ll][Ee][Tt]         { return new LineSymbol(Tokens.LET, this.get_curr_lineno()); }
<YYINITIAL>[Ll][Oo][Oo][Pp]  	{ return new LineSymbol(Tokens.LOOP, this.get_curr_lineno()); }
<YYINITIAL>[Nn][Ee][Ww]		{ return new LineSymbol(Tokens.NEW, this.get_curr_lineno()); }
<YYINITIAL>[Nn][Oo][Tt] 	{ return new LineSymbol(Tokens.NOT, this.get_curr_lineno()); }
<YYINITIAL>[Oo][Ff]		{ return new LineSymbol(Tokens.OF, this.get_curr_lineno()); }
<YYINITIAL>[Pp][Oo][Oo][Ll]  	{ return new LineSymbol(Tokens.POOL, this.get_curr_lineno()); }
<YYINITIAL>[Tt][Hh][Ee][Nn]   	{ return new LineSymbol(Tokens.THEN, this.get_curr_lineno()); }
<YYINITIAL>t[Rr][Uu][Ee]	{ return new LineSymbol(Tokens.BOOL_CONST, this.get_curr_lineno(), Boolean.TRUE); }
<YYINITIAL>[Ww][Hh][Ii][Ll][Ee] { return new LineSymbol(Tokens.WHILE, this.get_curr_lineno()); }

<YYINITIAL>{OBJECT_ID} { return createIdToken(Tokens.OBJECTID, this.get_curr_lineno(), yytext()); }
<YYINITIAL>{TYPE_ID} { return createIdToken(Tokens.TYPEID, this.get_curr_lineno(), yytext()); }
<YYINITIAL> "*)" { return createStringToken(Tokens.ERROR, this.get_curr_lineno(), "Unmatched *)"); }


<YYINITIAL>"=>"			{ return new LineSymbol(Tokens.DARROW, this.get_curr_lineno()); }
<YYINITIAL>"<="			{ return new LineSymbol(Tokens.LE, this.get_curr_lineno()); }
<YYINITIAL>"<-"			{ return new LineSymbol(Tokens.ASSIGN, this.get_curr_lineno()); }
<YYINITIAL>"+"			{ return new LineSymbol(Tokens.PLUS, this.get_curr_lineno()); }
<YYINITIAL>"/"			{ return new LineSymbol(Tokens.DIV, this.get_curr_lineno()); }
<YYINITIAL>"-"			{ return new LineSymbol(Tokens.MINUS, this.get_curr_lineno()); }
<YYINITIAL>"*"			{ return new LineSymbol(Tokens.MULT, this.get_curr_lineno()); }
<YYINITIAL>"="			{ return new LineSymbol(Tokens.EQ, this.get_curr_lineno()); }
<YYINITIAL>"<"			{ return new LineSymbol(Tokens.LT, this.get_curr_lineno()); }
<YYINITIAL>"."			{ return new LineSymbol(Tokens.DOT, this.get_curr_lineno()); }
<YYINITIAL>"~"			{ return new LineSymbol(Tokens.NEG, this.get_curr_lineno()); }
<YYINITIAL>","			{ return new LineSymbol(Tokens.COMMA, this.get_curr_lineno()); }
<YYINITIAL>";"			{ return new LineSymbol(Tokens.SEMI, this.get_curr_lineno()); }
<YYINITIAL>":"			{ return new LineSymbol(Tokens.COLON, this.get_curr_lineno()); }
<YYINITIAL>"("			{ return new LineSymbol(Tokens.LPAREN, this.get_curr_lineno()); }
<YYINITIAL>")"			{ return new LineSymbol(Tokens.RPAREN, this.get_curr_lineno()); }
<YYINITIAL>"@"			{ return new LineSymbol(Tokens.AT, this.get_curr_lineno()); }
<YYINITIAL>"}"			{ return new LineSymbol(Tokens.RBRACE, this.get_curr_lineno()); }
<YYINITIAL>"{"			{ return new LineSymbol(Tokens.LBRACE, this.get_curr_lineno()); }
<YYINITIAL>{NEWLINE} { this.increment_lineno(); }
.	{
	// If no other rule hit, we apparently encountered an error. Return it to the parser
	return createStringToken(Tokens.ERROR, this.get_curr_lineno(), yytext());
}
