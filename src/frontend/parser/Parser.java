package frontend.parser;

// import static frontend.parser.ParserUtils.GenerateNodeClasses;

import frontend.lexer.token.*;

import java.util.ArrayList;

public class Parser
{
    public static int tokenIndex = 0;
    public static ArrayList<Token> tokenList;

    public void run()
    {
        // GenerateNodeClasses();
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
