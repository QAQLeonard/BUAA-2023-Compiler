package frontend.parser;

// import static frontend.parser.ParserUtils.GenerateNodeClasses;

import error.CompilerError;
import backend.errorhandler.ErrorHandler;
import error.ErrorType;
import symbol.SymbolTable;
import frontend.lexer.Lexer;
import token.Token;
import token.TokenType;
import node.CompUnitNode;


import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static utils.FileOperate.CreateFileUsingJava7Files;

public class Parser
{
    public static int tokenIndex = 0;
    public static SymbolTable RootSymbolTable = new SymbolTable();
    static CompUnitNode compUnitNode;

    public void run()
    {
        // GenerateNodeClasses();
        compUnitNode = CompUnitNode.getInstance();
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
            ErrorHandler.addError(new CompilerError(errorType, "Expect " + tokenType + " but get " + token.getType(), Objects.requireNonNull(Parser.peekToken(-1)).getLineNumber()));
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
