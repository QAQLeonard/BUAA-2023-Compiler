package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.Objects;

/**
 * 基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number
 */
public class PrimaryExpNode extends Node {

    Token LPARENTToken;
    ExpNode expNode;
    Token RPARENTToken;
    LValNode lValNode;
    NumberNode numberNode;
    public PrimaryExpNode(NodeType type)
    {
        super(type);
    }

    @Override
    public void parseNode()
    {
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LPARENT)
        {
            this.LPARENTToken = Parser.getToken();
            this.expNode = new ExpNode();
            this.expNode.parseNode();
            this.RPARENTToken = Parser.getToken();
        }
        else if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.IDENFR)
        {
            this.lValNode = new LValNode();
            this.lValNode.parseNode();
        }
        else
        {
            this.numberNode = new NumberNode();
            this.numberNode.parseNode();
        }
    }
}
