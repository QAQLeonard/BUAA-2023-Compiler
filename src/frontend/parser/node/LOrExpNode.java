package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

/**
 * 逻辑或表达式 LOrExp → LAndExp | LAndExp '||' LOrExp
 */
public class LOrExpNode extends Node {

    LAndExpNode lAndExpNode;
    LOrExpNode lOrExpNode;
    Token ORToken;
    public LOrExpNode()
    {
        super(NodeType.LOrExp);
    }

    @Override
    public void parseNode()
    {
        lAndExpNode = new LAndExpNode();
        lAndExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.OR)
        {
            this.ORToken = Parser.getToken();
        }
        else
        {
            return ;
        }
        lOrExpNode = new LOrExpNode();
        lOrExpNode.parseNode();
    }
}
