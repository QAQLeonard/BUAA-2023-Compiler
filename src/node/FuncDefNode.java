package node;

import ir.value.BuildFactory;
import error.CompilerError;
import backend.errorhandler.ErrorHandler;
import error.ErrorType;
import ir.type.IntegerType;
import ir.type.Type;
import ir.type.VoidType;
import ir.value.Function;
import symbol.FUNCSymbol;
import symbol.Symbol;
import symbol.SymbolTable;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static frontend.parser.ParserUtils.funcSymbolStack;
import static ir.LLVMGenerator.*;
import static ir.utils.LLVMUtils.checkBlockEnd;

/**
 * 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
 */
public class FuncDefNode extends Node
{
    FuncTypeNode funcTypeNode;
    Token IDENFRToken;
    Token LPARENTToken;
    Token RPARENTToken;
    FuncFParamsNode funcFParamsNode;
    BlockNode blockNode;

    public FuncDefNode()
    {
        super(NodeType.FuncDef);
        this.funcTypeNode = null;
        this.IDENFRToken = null;
        this.LPARENTToken = null;
        this.RPARENTToken = null;
        this.funcFParamsNode = null;
        this.blockNode = null;
    }

    @Override
    public void parseNode()
    {
        this.funcTypeNode = new FuncTypeNode();
        this.funcTypeNode.parseNode();
        this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
        this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
        // not ()
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.INTTK)
        {
            this.funcFParamsNode = new FuncFParamsNode();
            this.funcFParamsNode.parseNode();
        }
        this.RPARENTToken = Parser.getToken(TokenType.RPARENT);
        this.blockNode = new BlockNode();
        this.blockNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.funcTypeNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
        if (this.funcFParamsNode != null)
        {
            this.funcFParamsNode.outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
        this.blockNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        SymbolTable funcTable = new SymbolTable(st);
        if (this.funcFParamsNode != null)
        {
            funcFParamsNode.parseSymbol(funcTable);
        }
        ArrayList<Symbol> paramList = new ArrayList<>(funcTable.getSymbolList());
        FUNCSymbol funcSymbol = new FUNCSymbol(this.IDENFRToken.getValue(), this.funcTypeNode.getExpType(), paramList);
        if (!st.isDefinitionUnique(this.IDENFRToken.getValue()))
        {
            ErrorHandler.addError(new CompilerError(ErrorType.b, "Duplicate declaration of function " + this.IDENFRToken.getValue(), this.IDENFRToken.getLineNumber()));
            return;
        }
        st.addSymbol(funcSymbol);
        funcSymbolStack.push(funcSymbol);
        for (BlockItemNode blockItemNode : blockNode.blockItemNodeList)
        {
            blockItemNode.parseSymbol(funcTable);
        }
        funcSymbolStack.pop();
        if (funcSymbol.ReturnStmtNodeList.isEmpty() && funcSymbol.getReturnType() != ExpType.VOID)
        {
            // System.out.println(this.blockNode.RBRACEToken.getValue()+"111"+this.blockNode.RBRACEToken.getLineNumber());
            ErrorHandler.addError(new CompilerError(ErrorType.g, "Function " + this.IDENFRToken.getValue() + " has no return statement", this.blockNode.RBRACEToken.getLineNumber()));
        }
    }

    @Override
    public void parseIR()
    {
        // FuncDef -> FuncType Ident '(' [FuncFParams] ')' Block
        isGlobal = false;
        String funcName = IDENFRToken.getValue();
        Type type = funcTypeNode.INTTKToken == null ? VoidType.voidType : IntegerType.i32;
        tmpTypeList = new ArrayList<>();
        if (funcFParamsNode != null)
        {
            funcFParamsNode.parseIR();
        }
        Function function = BuildFactory.getFunction(funcName, type, tmpTypeList);
        functionStack.push(function);
        addSymbol(funcName, function);
        addSymbolAndConstTable();
        addSymbol(funcName, function);
        blockStack.push(BuildFactory.buildBasicBlock(functionStack.peek()));
        funcArgsList = BuildFactory.getFunctionArguments(functionStack.peek());
        isRegister = true;
        if (funcFParamsNode != null)
        {
            funcFParamsNode.parseIR();
        }
        isRegister = false;
        blockNode.parseIR();
        isGlobal = true;
        removeSymbolAndConstTable();
        checkBlockEnd(blockStack.peek());
    }

}
