package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

public class NodeUtil
{
    public static Set<TokenType> UnaryOpTokenTypes = EnumSet.of(TokenType.PLUS, TokenType.MINU, TokenType.NOT);
    public static void parseArrayDimension(ArrayList<Token> LBRACKTokenList,
                                            ArrayList<ConstExpNode> constExpNodeList,
                                            ArrayList<Token> RBRACKTokenList)
    {
        LBRACKTokenList.add(Parser.getToken());
        ConstExpNode constExpNode = new ConstExpNode();
        constExpNode.parseNode();
        constExpNodeList.add(constExpNode);
        RBRACKTokenList.add(Parser.getToken());
    }

}
