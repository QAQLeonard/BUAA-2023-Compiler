package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * LVal → Ident {'[' Exp ']'}
 */
public class LValNode extends Node
{

    Token IDENFRToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<ExpNode> expNodeList;
    ArrayList<Token> RBRACKTokenList;

    public LValNode()
    {
        super(NodeType.LVal);
        this.LBRACKTokenList = new ArrayList<>();
        this.expNodeList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();

    }

    @Override
    public void parseNode()
    {
        this.IDENFRToken = Parser.getToken();
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            this.LBRACKTokenList.add(Parser.getToken());
            ExpNode expNode = new ExpNode();
            expNode.parseNode();
            this.expNodeList.add(expNode);
            this.RBRACKTokenList.add(Parser.getToken());
        }
    }
}
