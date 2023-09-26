package frontend.parser;

// import static frontend.parser.ParserUtils.GenerateNodeClasses;

import frontend.lexer.Lexer;
import frontend.lexer.token.*;
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
        tokenList = Lexer.getTokens();
        compUnitNode = new CompUnitNode();
        compUnitNode.parseNode();

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

    public static Token peekToken(int offset)
    {
        if (tokenIndex + offset >= tokenList.size())
        {
            return null;
        }
        return tokenList.get(tokenIndex + offset);
    }
}
