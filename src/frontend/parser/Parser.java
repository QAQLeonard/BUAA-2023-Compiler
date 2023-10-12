package frontend.parser;

// import static frontend.parser.ParserUtils.GenerateNodeClasses;

import backend.errorhandler.CompilerException;
import backend.errorhandler.ErrorHandler;
import backend.errorhandler.ExceptionType;
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
        try{
            compUnitNode = new CompUnitNode();
            compUnitNode.parseNode();
            compUnitNode.parseSymbol(RootSymbolTable);
        }
        catch (CompilerException e)
        {
            ErrorHandler.exceptionList.add(e);
            // System.out.println(e.getMessage());
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
        return Lexer.tokenList.get(tokenIndex - 1);
    }

    public static Token getToken(TokenType tokenType) throws CompilerException
    {
        Token token = Lexer.tokenList.get(tokenIndex);
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
        if (tokenIndex + offset >= Lexer.tokenList.size())
        {
            return null;
        }
        return Lexer.tokenList.get(tokenIndex + offset);
    }

    public static void parseSymbol() throws CompilerException
    {

    }
}
