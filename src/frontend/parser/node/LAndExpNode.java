package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

/**
 * 逻辑与表达式 LAndExp → EqExp | EqExp '&&' LAndExp
 */
public class LAndExpNode extends Node {

    EqExpNode eqExpNode;
    LAndExpNode lAndExpNode;
    Token ANDToken;
    Token ORToken;
    public LAndExpNode()
    {
        super(NodeType.LAndExp);
    }

    @Override
    public void parseNode()
    {
        eqExpNode = new EqExpNode();
        eqExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.AND)
        {
            this.ANDToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.OR)
        {
            this.ORToken = Parser.getToken();
        }
        else
        {
            return ;
        }
        lAndExpNode = new LAndExpNode();
        lAndExpNode.parseNode();
    }
}
