package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 变量声明 VarDecl → BType VarDef { ',' VarDef } ';'
 */
public class VarDeclNode extends Node
{

    BTypeNode bTypeNode;
    ArrayList<VarDefNode> varDefNodeList;
    ArrayList<Token> COMMATokenList;
    Token SEMICNToken;

    public VarDeclNode()
    {
        super(NodeType.VarDecl);
        bTypeNode = null;
        varDefNodeList = new ArrayList<>();
        COMMATokenList = new ArrayList<>();
        SEMICNToken = null;
    }

    @Override
    public void parseNode()
    {
        this.bTypeNode = new BTypeNode();
        this.bTypeNode.parseNode();
        VarDefNode varDefNode = new VarDefNode();
        varDefNode.parseNode();
        this.varDefNodeList.add(varDefNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.COMMA)
        {
            this.COMMATokenList.add(Parser.getToken());
            varDefNode = new VarDefNode();
            varDefNode.parseNode();
            this.varDefNodeList.add(varDefNode);
        }
        this.SEMICNToken = Parser.getToken();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.bTypeNode.outputNode(destFile);
        this.varDefNodeList.get(0).outputNode(destFile);
        for (int i = 0; i < this.COMMATokenList.size(); i++)
        {

            FileOperate.outputFileUsingUsingBuffer(destFile, this.COMMATokenList.get(i).toString() + "\n", true);
            this.varDefNodeList.get(i + 1).outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }
}
