package frontend.parser.node;

import frontend.errorhandler.symbol.SymbolTable;
import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static ir.LLVMGenerator.*;

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
    public void parseNode()
    {
        FuncFParamNode funcFParamNode = new FuncFParamNode();
        if(Objects.requireNonNull(Parser.peekToken(0)).getType()!= TokenType.INTTK)
        {
            return;
        }
        funcFParamNode.parseNode();
        this.funcFParamNodeList.add(funcFParamNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType().equals(TokenType.COMMA))
        {
            this.COMMATokenList.add(Parser.getToken(TokenType.COMMA));
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
    public void parseSymbol(SymbolTable st)
    {
        for (FuncFParamNode funcFParamNode : funcFParamNodeList)
        {
            funcFParamNode.parseSymbol(st);
        }
    }

    @Override
    public void parseIR()
    {
        // FuncFParams -> FuncFParam { ',' FuncFParam }
        if (isRegister)
        {
            tmpIndex = 0;
            for (FuncFParamNode funcFParamNode : funcFParamNodeList)
            {
                funcFParamNode.parseIR();
                tmpIndex++;
            }
        }
        else
        {
            tmpTypeList = new ArrayList<>();
            for (FuncFParamNode funcFParamNode : funcFParamNodeList)
            {
                funcFParamNode.parseIR();
                tmpTypeList.add(tmpType);
            }
        }
    }
}
