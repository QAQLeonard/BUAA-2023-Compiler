package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
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
    public void parseNode() throws CompilerException
    {
        if(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.VOIDTK)
            this.VOIDTKToken = Parser.getToken(TokenType.VOIDTK);
        else
            this.INTTKToken = Parser.getToken(TokenType.INTTK);
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
