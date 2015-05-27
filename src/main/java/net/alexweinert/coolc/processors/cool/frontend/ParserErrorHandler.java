package net.alexweinert.coolc.processors.cool.frontend;

import java.util.LinkedList;
import java.util.List;

import net.alexweinert.coolc.processors.cool.frontend.parser.Tokens;

public class ParserErrorHandler {
    private List<String> errorMessages = new LinkedList<>();

    public void parseError(String filename, int lineNumber, int columnNumber, int received) {
        final String formatString = "Parsing error at %s:%d. Unexpected token: %s";
        final String errorMessage = String.format(formatString, filename, lineNumber, this.tokenToString(received));
        this.errorMessages.add(errorMessage);
    }

    public boolean hasErrors() {
        return this.errorMessages.isEmpty();
    }

    public List<String> getErrorMessages() {
        return this.errorMessages;
    }

    private String tokenToString(final int tokenId) {
        switch (tokenId) {
        case Tokens.MULT:
            return "*";
        case Tokens.INHERITS:
            return "\"inherits\"";
        case Tokens.POOL:
            return "\"pool\"";
        case Tokens.CASE:
            return "\"case\"";
        case Tokens.LPAREN:
            return "\"(\"";
        case Tokens.SEMI:
            return "\";\"";
        case Tokens.MINUS:
            return "\"-\"";
        case Tokens.STR_CONST:
            return "string constant";
        case Tokens.RPAREN:
            return "\")\"";
        case Tokens.NOT:
            return "\"not\"";
        case Tokens.TYPEID:
            return "type identifier";
        case Tokens.LT:
            return "\"<\"";
        case Tokens.IN:
            return "\"in\"";
        case Tokens.COMMA:
            return "\",\"";
        case Tokens.CLASS:
            return "\"class\"";
        case Tokens.FI:
            return "\"fi\"";
        case Tokens.DIV:
            return "\"/\"";
        case Tokens.LOOP:
            return "\"loop\"";
        case Tokens.PLUS:
            return "\"+\"";
        case Tokens.ASSIGN:
            return "\"<-\"";
        case Tokens.IF:
            return "\"if\"";
        case Tokens.DOT:
            return "\".\"";
        case Tokens.LE:
            return "\"<=\"";
        case Tokens.OF:
            return "\"of\"";
        case Tokens.EOF:
            return "end of file";
        case Tokens.INT_CONST:
            return "integer constant";
        case Tokens.NEW:
            return "\"new\"";
        case Tokens.ISVOID:
            return "\"isvoid\"";
        case Tokens.EQ:
            return "\"=\"";
        case Tokens.ERROR:
            return "\"error\"";
        case Tokens.COLON:
            return "\":\"";
        case Tokens.NEG:
            return "\"~\"";
        case Tokens.LBRACE:
            return "\"{\"";
        case Tokens.ELSE:
            return "\"else\"";
        case Tokens.DARROW:
            return "\"=>\"";
        case Tokens.WHILE:
            return "\"while\"";
        case Tokens.ESAC:
            return "\"esac\"";
        case Tokens.LET:
            return "\"let\"";
        case Tokens.RBRACE:
            return "\"}\"";
        case Tokens.THEN:
            return "\"then\"";
        case Tokens.BOOL_CONST:
            return "boolean constant";
        case Tokens.OBJECTID:
            return "object identifier";
        case Tokens.AT:
            return "\"@\"";
        default:
            return "Unknown Token";
        }
    }
}
