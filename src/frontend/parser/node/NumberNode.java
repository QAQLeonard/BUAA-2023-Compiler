package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
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
    public void parseNode() throws CompilerException
    {
        this.INTCONToken = Parser.getToken(TokenType.INTCON);
        // System.out.println("NumberNode: " + this.INTCONToken.getLineNumber()+ " " + this.INTCONToken);
        this.value = Integer.parseInt(this.INTCONToken.getValue());
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.INTCONToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public String toString()
    {
        return this.INTCONToken.getValue();
    }


}
