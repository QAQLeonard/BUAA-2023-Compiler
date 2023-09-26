package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * 表达式 Exp → AddExp
 */
public class ExpNode extends Node implements Expression
{

    AddExpNode addExpNode;

    public ExpNode()
    {
        super(NodeType.Exp);
    }

    @Override
    public void parseNode()
    {
        this.addExpNode = new AddExpNode();
        this.addExpNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.addExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public String toString()
    {
        return addExpNode.toString();
    }

    @Override
    public Token getOPToken()
    {
        return null;
    }
}
