package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.parser.Parser;

import java.util.ArrayList;

public class NodeUtil
{
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
