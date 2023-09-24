package frontend.lexer;

import frontend.lexer.token.*;

import java.util.HashMap;
import java.util.Map;

public class LexerUtils
{


    private static final Map<String, TokenType> tokenMap = new HashMap<>();

    static
    {
        tokenMap.put("main", TokenType.MAINTK);
        tokenMap.put("const", TokenType.CONSTTK);
        tokenMap.put("int", TokenType.INTTK);
        tokenMap.put("break", TokenType.BREAKTK);
        tokenMap.put("continue", TokenType.CONTINUETK);
        tokenMap.put("if", TokenType.IFTK);
        tokenMap.put("else", TokenType.ELSETK);
        tokenMap.put("for", TokenType.FORTK);
        tokenMap.put("getint", TokenType.GETINTTK);
        tokenMap.put("printf", TokenType.PRINTFTK);
        tokenMap.put("return", TokenType.RETURNTK);
        tokenMap.put("void", TokenType.VOIDTK);
        tokenMap.put("!", TokenType.NOT);
        tokenMap.put("&&", TokenType.AND);
        tokenMap.put("||", TokenType.OR);
        tokenMap.put("+", TokenType.PLUS);
        tokenMap.put("-", TokenType.MINU);
        tokenMap.put("*", TokenType.MULT);
        tokenMap.put("/", TokenType.DIV);
        tokenMap.put("%", TokenType.MOD);
        tokenMap.put("<", TokenType.LSS);
        tokenMap.put("<=", TokenType.LEQ);
        tokenMap.put(">", TokenType.GRE);
        tokenMap.put(">=", TokenType.GEQ);
        tokenMap.put("==", TokenType.EQL);
        tokenMap.put("!=", TokenType.NEQ);
        tokenMap.put("=", TokenType.ASSIGN);
        tokenMap.put(";", TokenType.SEMICN);
        tokenMap.put(",", TokenType.COMMA);
        tokenMap.put("(", TokenType.LPARENT);
        tokenMap.put(")", TokenType.RPARENT);
        tokenMap.put("[", TokenType.LBRACK);
        tokenMap.put("]", TokenType.RBRACK);
        tokenMap.put("{", TokenType.LBRACE);
        tokenMap.put("}", TokenType.RBRACE);
    }

    public static final Map<Character, Character> twoCharSymbols = new HashMap<>();

    static {
        twoCharSymbols.put('!', '=');
        twoCharSymbols.put('&', '&');
        twoCharSymbols.put('|', '|');
        twoCharSymbols.put('<', '=');
        twoCharSymbols.put('>', '=');
        twoCharSymbols.put('=', '=');
    }

    public static boolean isSymbol(char c) {
        return "!&|+-*/%<>=;,()[]{}".indexOf(c) >= 0;
    }

    public static Token generateWordToken(String word, int lineNum)
    {
        TokenType tokenType = tokenMap.get(word);
        if (tokenType == null)
        {
            return new IdentifierToken(TokenType.IDENFR, word, lineNum);
        }
        return new KeywordToken(tokenType, word, lineNum);
    }

    public static Token generateNumToken(String number, int lineNum)
    {
        return new LiteralToken(TokenType.INTCON, number, lineNum);
    }

    public static Token generateStrToken(String str, int lineNum)
    {
        return new IdentifierToken(TokenType.STRCON, str, lineNum);
    }

    public static Token generateSymbolToken(String symbol, int lineNum)
    {
        TokenType tokenType;
        tokenType = tokenMap.get(symbol);
        if (tokenType == null)
        {
            tokenType = tokenMap.get(String.valueOf(symbol));
            if (tokenType == null)
            {
                return null;
            }
            return new DelimiterToken(tokenType, String.valueOf(symbol), lineNum);
        }
        return new OperatorToken(tokenType, symbol, lineNum);
    }
}
