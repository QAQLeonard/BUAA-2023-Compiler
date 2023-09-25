package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.Objects;

/**
 * 单目运算符 UnaryOp → '+' | '−' | '!'
 */
public class UnaryOpNode extends Node {
    Token PLUSToken;
    Token MINUSToken;
    Token NOTToken;
    public UnaryOpNode()
    {
        super(NodeType.UnaryOp);
    }

    @Override
    public void parseNode()
    {
        Token token = Parser.peekToken(0);
        if (Objects.requireNonNull(token).getType()== TokenType.PLUS)
        {
            this.PLUSToken = Parser.getToken();
        }
        else if (Objects.requireNonNull(token).getType()==TokenType.MINU)
        {
            this.MINUSToken = Parser.getToken();
        }
        else
        {
            this.NOTToken = Parser.getToken();
        }
    }
}
