package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

/**
 * AddExp → MulExp <br>
 * | MulExp ('+' | '−') AddExp
 */
public class AddExpNode extends Node {

    MulExpNode mulExpNode;
    AddExpNode addExpNode;
    Token PLUSToken;
    Token MINUSToken;
    public AddExpNode()
    {
        super(NodeType.AddExp);

    }

    @Override
    public void parseNode()
    {
        mulExpNode = new MulExpNode();
        mulExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.PLUS)
        {
            PLUSToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.MINU)
        {
            MINUSToken = Parser.getToken();
        }
        else
        {
            return ;
        }
        addExpNode = new AddExpNode();
        addExpNode.parseNode();
    }
}
