package node;

import error.CompilerError;
import backend.errorhandler.ErrorHandler;
import error.ErrorType;
import ir.IRGenerator;
import ir.type.IntegerType;
import ir.value.Function;
import ir.value.BuildFactory;
import symbol.FUNCSymbol;
import symbol.SymbolTable;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * MainFuncDef -> 'int' 'main' '(' ')' Block
 */

public class MainFuncDefNode extends Node
{

    Token INTTKToken;
    Token MAINTKToken;
    Token LPARENTToken;
    Token RPARENTToken;
    BlockNode blockNode;

    public MainFuncDefNode()
    {
        super(NodeType.MainFuncDef);
        this.INTTKToken = null;
        this.MAINTKToken = null;
        this.LPARENTToken = null;
        this.RPARENTToken = null;
        this.blockNode = null;
    }

    @Override
    public void parseNode()
    {
        this.INTTKToken = Parser.getToken(TokenType.INTTK);
        this.MAINTKToken = Parser.getToken(TokenType.MAINTK);
        this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
        this.RPARENTToken = Parser.getToken(TokenType.RPARENT);
        this.blockNode = new BlockNode();
        this.blockNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.INTTKToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.MAINTKToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
        this.blockNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        FUNCSymbol funcSymbol = new FUNCSymbol("main", ExpType.INT, null);
        st.addSymbol(funcSymbol);
        ParserUtils.funcSymbolStack.push(funcSymbol);
        this.blockNode.parseSymbol(st);
        ParserUtils.funcSymbolStack.pop();
        if (funcSymbol.ReturnStmtNodeList.isEmpty())
        {
            ErrorHandler.addError(new CompilerError(ErrorType.g, "main函数缺少return语句", this.blockNode.RBRACEToken.getLineNumber()));
        }
    }

    @Override
    public void parseIR()
    {
        IRGenerator.isGlobal = false;
        Function function = BuildFactory.getFunction("main", IntegerType.i32, new ArrayList<>());
        IRGenerator.functionStack.push(function);
        IRGenerator.addSymbol("main", function);
        IRGenerator.addSymbolAndConstTable();
        IRGenerator.addSymbol("main", function);
        IRGenerator.blockStack.push(BuildFactory.buildBasicBlock(IRGenerator.functionStack.peek()));
        IRGenerator.funcArgsList = BuildFactory.getFunctionArguments(IRGenerator.functionStack.peek());
        this.blockNode.parseIR();
        IRGenerator.isGlobal = true;
        IRGenerator.removeSymbolAndConstTable();
        BuildFactory.checkBlockEnd(IRGenerator.blockStack.peek());
    }
}
