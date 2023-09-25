package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

/**
 * 关系表达式 RelExp → AddExp | AddExp ('<' | '>' | '<=' | '>=') RelExp
 */
public class RelExpNode extends Node {

    AddExpNode addExpNode;
    RelExpNode relExpNode;
    Token LSSToken;
    Token GREToken;
    Token LEQToken;
    Token GEQToken;
    public RelExpNode()
    {
        super(NodeType.RelExp);
    }

    @Override
    public void parseNode()
    {
        addExpNode = new AddExpNode();
        addExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.LSS)
        {
            LSSToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.GRE)
        {
            GREToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.LEQ)
        {
            LEQToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.GEQ)
        {
            GEQToken = Parser.getToken();
        }
        else
        {
            return ;
        }
        relExpNode = new RelExpNode();
        relExpNode.parseNode();
    }
}
