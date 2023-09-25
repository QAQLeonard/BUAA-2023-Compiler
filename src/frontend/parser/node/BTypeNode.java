package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.parser.Parser;

/**
 * 基本类型 BType → 'int'
 */
public class BTypeNode extends Node
{
    Token INTTKToken;
    public BTypeNode()
    {
        super(NodeType.BType);
    }

    @Override
    public void parseNode()
    {
        this.INTTKToken = Parser.getToken();
    }
}
