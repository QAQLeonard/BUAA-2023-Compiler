package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

/**
 * 乘除模表达式 MulExp → UnaryExp <br>
 * | UnaryExp ('*' | '/' | '%') MulExp
 */
public class MulExpNode extends Node
{

    UnaryExpNode unaryExpNode;
    MulExpNode mulExpNode;

    Token MULTToken;
    Token DIVToken;
    Token MODToken;


    public MulExpNode(NodeType type)
    {
        super(type);
        unaryExpNode = null;
        mulExpNode = null;
        MULTToken = null;
        DIVToken = null;
        MODToken = null;
    }

    @Override
    public void parseNode()
    {
        unaryExpNode = new UnaryExpNode();
        unaryExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.MULT)
        {
            this.MULTToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.DIV)
        {
            this.DIVToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.MOD)
        {
            this.MODToken = Parser.getToken();
        }
        else
        {
            return ;
        }
        mulExpNode = new MulExpNode(NodeType.MulExp);
        mulExpNode.parseNode();

    }
}
