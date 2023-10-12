package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.parser.symbol.SymbolTable;
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
 * 函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
 */
public class FuncFParamsNode extends Node {

    ArrayList<FuncFParamNode> funcFParamNodeList;
    ArrayList<Token> COMMATokenList;
    public FuncFParamsNode()
    {
        super(NodeType.FuncFParams);
        this.funcFParamNodeList = new ArrayList<>();
        this.COMMATokenList = new ArrayList<>();
    }

    @Override
    public void parseNode() throws CompilerException
    {
        FuncFParamNode funcFParamNode = new FuncFParamNode();
        funcFParamNode.parseNode();
        this.funcFParamNodeList.add(funcFParamNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType().equals(TokenType.COMMA))
        {
            this.COMMATokenList.add(Parser.getToken());
            funcFParamNode = new FuncFParamNode();
            funcFParamNode.parseNode();
            this.funcFParamNodeList.add(funcFParamNode);
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        funcFParamNodeList.get(0).outputNode(destFile);
        for (int i = 0; i < COMMATokenList.size(); i++)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, COMMATokenList.get(i).toString()+ "\n", true);
            funcFParamNodeList.get(i + 1).outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType())+"\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st) throws CompilerException
    {
        for (FuncFParamNode funcFParamNode : funcFParamNodeList)
        {
            funcFParamNode.parseSymbol(st);
        }
    }
}
