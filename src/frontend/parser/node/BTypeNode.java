package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.parser.Parser;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

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

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.INTTKToken.toString() + "\n", true);
    }
}
