package frontend.parser.node;

import frontend.error.CompilerError;
import frontend.error.errorhandler.ErrorHandler;
import frontend.error.ErrorType;
import frontend.ir.LLVMGenerator;
import frontend.ir.type.IntegerType;
import frontend.ir.value.Function;
import frontend.ir.value.BuildFactory;
import frontend.error.symbol.FUNCSymbol;
import frontend.error.symbol.SymbolTable;
import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static frontend.ir.utils.LLVMUtils.checkBlockEnd;

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
        LLVMGenerator.isGlobal = false;
        Function function = BuildFactory.getFunction("main", IntegerType.i32, new ArrayList<>());
        LLVMGenerator.functionStack.push(function);
        LLVMGenerator.addSymbol("main", function);
        LLVMGenerator.addSymbolAndConstTable();
        LLVMGenerator.addSymbol("main", function);
        LLVMGenerator.blockStack.push(BuildFactory.buildBasicBlock(LLVMGenerator.functionStack.peek()));
        LLVMGenerator.funcArgsList = BuildFactory.getFunctionArguments(LLVMGenerator.functionStack.peek());
        this.blockNode.parseIR();
        LLVMGenerator.isGlobal = true;
        LLVMGenerator.removeSymbolAndConstTable();
        checkBlockEnd(LLVMGenerator.blockStack.peek());
    }
}
