package node;

import token.Token;
import token.TokenType;
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
        this.INTTKToken = Parser.getToken(TokenType.INTTK);
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.INTTKToken.toString() + "\n", true);
    }
}
