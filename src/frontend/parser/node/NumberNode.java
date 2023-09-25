package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.parser.Parser;

/**
 * 数值 Number → IntConst
 */
public class NumberNode extends Node {
    Token INTCONToken;
    int value;
    public NumberNode()
    {
        super(NodeType.Number);
    }

    @Override
    public void parseNode()
    {
        this.INTCONToken = Parser.getToken();
        this.value = Integer.parseInt(this.INTCONToken.getValue());
    }

}
