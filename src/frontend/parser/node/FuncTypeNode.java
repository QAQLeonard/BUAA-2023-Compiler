package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.Objects;

/**
 * 函数类型 FuncType → 'void' | 'int'
 */
public class FuncTypeNode extends Node {

    Token VOIDTKToken;
    Token INTTKToken;
    public FuncTypeNode()
    {
        super(NodeType.FuncType);
        this.VOIDTKToken = null;
        this.INTTKToken = null;
    }

    @Override
    public void parseNode()
    {
        if(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.VOIDTK)
            this.VOIDTKToken = Parser.getToken();
        else
            this.INTTKToken = Parser.getToken();
    }
}
