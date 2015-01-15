/*
 * CS164: Spring 2004
 * Programming Assignment 2
 *
 * The scanner definition for Cool.
 *
 */

package net.alexweinert.coolc.lexer;

import java_cup.runtime.Symbol;

%%

/* Code enclosed in %{ %} is copied verbatim to the lexer class definition.
 * All extra variables/methods you want to use in the lexer actions go
 * here.  Don't remove anything that was here initially.  */
%{
    // Max size of string constants
    final static int MAX_STR_CONST = 1024;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    // For line numbers
    private int curr_lineno = 1;

    int get_curr_lineno() {
		return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
		filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
		return filename;
    }

	// The nesting depth of the current comment. 0 denotes the top-level comment.
	private int commentDepth = 0;

	private Symbol createStringToken(int type, String value) {
		AbstractSymbol symbol = AbstractTable.stringtable.addString(value);
		return new Symbol(type, symbol);
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
    switch(yy_lexical_state) {
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
		return createStringToken(TokenConstants.ERROR, "EOF in comment");
	case STRING:
		/* end of file terminates a comment, so it does not matter whether we 
		 * go to YYINITIAL or DISCARDSTRING. Both will eventually return EOF */
		yybegin(YYINITIAL);
		return createStringToken(TokenConstants.ERROR, "EOF in string constant");
	}

    return new Symbol(TokenConstants.EOF);
%eofval}

/* Do not modify the following two jlex directives */
%class Lexer
%cup

/* Define names for regular expressions here. */
/* Define newline seperately from whitespace, since it has to handled as a special
 * case quite often */
NEWLINE		= \n
WHITESPACE	= [ \f\r\v\t]

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
	this.curr_lineno += 1;
	yybegin(YYINITIAL); 
}

<YYINITIAL> \*\) {
	// Since we are in YYINITIAL, we know that we encountered an unmatched comment end
	AbstractSymbol errorMessage = AbstractTable.stringtable.addString("Unmatched *)");
	return new Symbol(TokenConstants.ERROR, errorMessage);
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
	this.curr_lineno += 1;
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
<STRING> "\t" {
	// Handle escaped tab
	this.string_buf.append('\t');
}
<STRING> "\n" {
	// Handle escaped newline
	this.string_buf.append('\n');
}
<STRING> "\f" {
	// Handle escaped formfeed
	this.string_buf.append('\f');
}
<STRING> \\\n { 
	// Also handle newlines inside string constants
	this.curr_lineno += 1;
	this.string_buf.append('\n');
}
<STRING> \000 {
	yybegin(DISCARDSTRING);
	return createStringToken(TokenConstants.ERROR, "String contains null character");
}
<STRING> \\. { 
	// matches any other escaped character, which is just lexed as the character
	// use substring(1) to remove the first character (the \)
	this.string_buf.append(yytext().substring(1)); 
}

<STRING> \n {
	// The actual newline character is disallowed, reject the string
	this.curr_lineno += 1;
	/* We switch to YYINITIAL instead of DISCARDSTRING since we already found the end of the line.
	 * DISCARDSTRING would look for the *next* newline */
	yybegin(YYINITIAL);
	return createStringToken(TokenConstants.ERROR, "Unterminated string constant");
}
<STRING> \" {
	// Found the end of the literal, tokenize the string if it not too long
	yybegin(YYINITIAL);
	String literal = this.string_buf.toString();
	final AbstractSymbol value;
	final int tokenCategory;
	if(literal.length() > MAX_STR_CONST) {
		return createStringToken(TokenConstants.ERROR, "String constant too long");
	} else {
		return createStringToken(TokenConstants.STR_CONST, literal);
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
	this.curr_lineno += 1;
	yybegin(YYINITIAL); 
}
<DISCARDSTRING> . { /* Eat up all remaining characters in the string, until we find either a " or a newline */ }


<YYINITIAL>{INTEGER}  { /* Integers */
                          return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }

<YYINITIAL>[Cc][Aa][Ss][Ee]	{ return new Symbol(TokenConstants.CASE); }
<YYINITIAL>[Cc][Ll][Aa][Ss][Ss] { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>[Ee][Ll][Ss][Ee]  	{ return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>[Ee][Ss][Aa][Cc]	{ return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>f[Aa][Ll][Ss][Ee]	{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }
<YYINITIAL>[Ff][Ii]             { return new Symbol(TokenConstants.FI); }
<YYINITIAL>[Ii][Ff]  		{ return new Symbol(TokenConstants.IF); }
<YYINITIAL>[Ii][Nn]             { return new Symbol(TokenConstants.IN); }
<YYINITIAL>[Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss] { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>[Ii][Ss][Vv][Oo][Ii][Dd] { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>[Ll][Ee][Tt]         { return new Symbol(TokenConstants.LET); }
<YYINITIAL>[Ll][Oo][Oo][Pp]  	{ return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>[Nn][Ee][Ww]		{ return new Symbol(TokenConstants.NEW); }
<YYINITIAL>[Nn][Oo][Tt] 	{ return new Symbol(TokenConstants.NOT); }
<YYINITIAL>[Oo][Ff]		{ return new Symbol(TokenConstants.OF); }
<YYINITIAL>[Pp][Oo][Oo][Ll]  	{ return new Symbol(TokenConstants.POOL); }
<YYINITIAL>[Tt][Hh][Ee][Nn]   	{ return new Symbol(TokenConstants.THEN); }
<YYINITIAL>t[Rr][Uu][Ee]	{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
<YYINITIAL>[Ww][Hh][Ii][Ll][Ee] { return new Symbol(TokenConstants.WHILE); }

<YYINITIAL>{OBJECT_ID} { return createStringToken(TokenConstants.OBJECTID, yytext()); }
<YYINITIAL>{TYPE_ID} { return createStringToken(TokenConstants.TYPEID, yytext()); }
<YYINITIAL> "*)" { return createStringToken(TokenConstants.ERROR, "Unmatched *)"); }


<YYINITIAL>"=>"			{ return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>"<="			{ return new Symbol(TokenConstants.LE); }
<YYINITIAL>"<-"			{ return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"+"			{ return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"/"			{ return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"-"			{ return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"*"			{ return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"="			{ return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"<"			{ return new Symbol(TokenConstants.LT); }
<YYINITIAL>"."			{ return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"~"			{ return new Symbol(TokenConstants.NEG); }
<YYINITIAL>","			{ return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>";"			{ return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":"			{ return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"("			{ return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")"			{ return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>"@"			{ return new Symbol(TokenConstants.AT); }
<YYINITIAL>"}"			{ return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"{"			{ return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>{NEWLINE} { this.curr_lineno += 1; }
.	{
	// If no other rule hit, we apparently encountered an error. Return it to the parser
	return createStringToken(TokenConstants.ERROR, yytext());
}
