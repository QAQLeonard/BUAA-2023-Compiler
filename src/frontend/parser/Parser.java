package frontend.parser;

// import static frontend.parser.ParserUtils.GenerateNodeClasses;

import frontend.lexer.token.Token;

import java.util.ArrayList;

public class Parser
{
    public static int tokenIndex = 0;
    public static ArrayList<Token> tokens;

    public void run()
    {
        // GenerateNodeClasses();
    }

    public static Token getToken()
    {
        tokenIndex++;
        return tokens.get(tokenIndex - 1);
    }

    public static Token peekToken(int offset)
    {
        if (tokenIndex + offset >= tokens.size())
        {
            return null;
        }
        return tokens.get(tokenIndex + offset);
    }
}
