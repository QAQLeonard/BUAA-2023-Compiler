package node;

import frontend.parser.ParserUtils;
import symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * 条件表达式 Cond → LOrExp
 */
public class CondNode extends Node
{

    LOrExpNode lOrExpNode;

    public CondNode()
    {
        super(NodeType.Cond);
    }

    @Override
    public void parseNode()
    {
        this.lOrExpNode = new LOrExpNode();
        this.lOrExpNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.lOrExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable parent)
    {
        this.lOrExpNode.parseSymbol(parent);
    }

    @Override
    public void parseIR()
    {
        this.lOrExpNode.parseIR();
    }
}
