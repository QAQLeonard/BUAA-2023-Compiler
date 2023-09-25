package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

/**
 * 相等性表达式 EqExp → RelExp | RelExp ('==' | '!=') EqExp
 */
public class EqExpNode extends Node {

    RelExpNode relExpNode;
    EqExpNode eqExpNode;
    Token EQLToken;
    Token NEQToken;
    public EqExpNode()
    {
        super(NodeType.EqExp);
    }

    @Override
    public void parseNode()
    {
        relExpNode = new RelExpNode();
        relExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.EQL)
        {
            this.EQLToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.NEQ)
        {
            this.NEQToken = Parser.getToken();
        }
        else
        {
            return ;
        }
        eqExpNode = new EqExpNode();
        eqExpNode.parseNode();
    }
}
