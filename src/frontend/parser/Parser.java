package frontend.parser;

// import static frontend.parser.ParserUtils.GenerateNodeClasses;

import backend.errorhandler.CompilerError;
import backend.errorhandler.ErrorHandler;
import backend.errorhandler.ErrorType;
import frontend.parser.symbol.SymbolTable;
import frontend.lexer.Lexer;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.node.CompUnitNode;


import java.io.File;
import java.io.IOException;

import static utils.FileOperate.CreateFileUsingJava7Files;

public class Parser
{
    public static int tokenIndex = 0;
    public static SymbolTable RootSymbolTable = new SymbolTable();
    static CompUnitNode compUnitNode;

    public void run()
    {
        // GenerateNodeClasses();
        compUnitNode = new CompUnitNode();
        compUnitNode.parseNode();
        // compUnitNode.parseSymbol(RootSymbolTable);
    }

    public void output() throws IOException
    {
        File destFile = new File("output.txt");
        CreateFileUsingJava7Files(destFile);
        compUnitNode.outputNode(destFile);
    }

    public static Token getToken()
    {
        tokenIndex++;
        return Lexer.tokenList.get(tokenIndex - 1);
    }

    public static Token getToken(TokenType tokenType)
    {
        Token token = Lexer.tokenList.get(tokenIndex);
        if (token.getType() == tokenType)
        {
            tokenIndex++;
            return token;
        }
        else
        {
            ErrorType errorType;
            switch (tokenType)
            {
                case SEMICN -> errorType = ErrorType.i;
                case RPARENT -> errorType = ErrorType.j;
                case RBRACK -> errorType = ErrorType.k;
                default -> errorType = ErrorType.UNEXPECTED_TOKEN;
            }
            ErrorHandler.addError(new CompilerError(errorType, "Expect " + tokenType + " but get " + token.getType(), token.getLineNumber()));
            return null;
        }
    }

    public static Token peekToken(int offset)
    {
        if (tokenIndex + offset >= Lexer.tokenList.size())
        {
            return null;
        }
        return Lexer.tokenList.get(tokenIndex + offset);
    }

}
