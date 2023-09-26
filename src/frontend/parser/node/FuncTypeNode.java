package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
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

    @Override
    public void outputNode(File destFile) throws IOException
    {
        if(this.VOIDTKToken != null)
            FileOperate.outputFileUsingUsingBuffer(destFile, this.VOIDTKToken.toString() + "\n", true);
        else
            FileOperate.outputFileUsingUsingBuffer(destFile, this.INTTKToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType())+"\n", true);
    }
}
