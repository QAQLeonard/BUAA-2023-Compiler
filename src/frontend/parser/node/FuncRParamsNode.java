package frontend.parser.node;

import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 函数实参表 FuncRParams → Exp { ',' Exp }
 */
public class FuncRParamsNode extends Node {
    ArrayList<ExpNode> expNodeList;
    ArrayList<Token> COMMATokenList;
    public FuncRParamsNode()
    {
        super(NodeType.FuncRParams);
        this.expNodeList = new ArrayList<>();
        this.COMMATokenList = new ArrayList<>();
    }

    @Override
    public void parseNode()
    {
        ExpNode expNode = new ExpNode();
        expNode.parseNode();
        this.expNodeList.add(expNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType()== TokenType.COMMA)
        {
            this.COMMATokenList.add(Parser.getToken(TokenType.COMMA));
            expNode = new ExpNode();
            expNode.parseNode();
            this.expNodeList.add(expNode);
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        expNodeList.get(0).outputNode(destFile);
        for (int i = 0; i < COMMATokenList.size(); i++)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, COMMATokenList.get(i).toString()+ "\n", true);
            expNodeList.get(i + 1).outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType())+"\n", true);
    }

}
