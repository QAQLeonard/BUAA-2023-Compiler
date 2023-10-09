package frontend.parser;

// import static frontend.parser.ParserUtils.GenerateNodeClasses;

import backend.errorhandler.CompilerException;
import backend.errorhandler.ExceptionType;
import frontend.lexer.Lexer;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.node.CompUnitNode;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static utils.FileOperate.CreateFileUsingJava7Files;

public class Parser
{
    public static int tokenIndex = 0;
    public static ArrayList<Token> tokenList;

    CompUnitNode compUnitNode;

    public void run()
    {
        // GenerateNodeClasses();
        try{
            tokenList = Lexer.getTokens();
            compUnitNode = new CompUnitNode();
            compUnitNode.parseNode();
        }
        catch (CompilerException e)
        {
            System.out.println(e.getMessage());
        }


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
        return tokenList.get(tokenIndex - 1);
    }

    public static Token getToken(TokenType tokenType) throws CompilerException
    {
        Token token = tokenList.get(tokenIndex);
        if (token.getType() == tokenType)
        {
            tokenIndex++;
            return token;
        }
        else {
            ExceptionType exceptionType;
            switch (tokenType)
            {
                case SEMICN -> exceptionType = ExceptionType.i;
                case RPARENT -> exceptionType = ExceptionType.j;
                case RBRACK -> exceptionType = ExceptionType.k;
                default -> exceptionType = ExceptionType.UNEXPECTED_TOKEN;
            }
            throw new CompilerException(exceptionType, "Expect " + tokenType + " but get " + token.getType(), token.getLineNumber());
        }
    }

    public static Token peekToken(int offset)
    {
        if (tokenIndex + offset >= tokenList.size())
        {
            return null;
        }
        return tokenList.get(tokenIndex + offset);
    }
}
