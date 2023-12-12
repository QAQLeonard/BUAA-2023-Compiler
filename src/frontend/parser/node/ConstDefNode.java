package frontend.parser.node;

import frontend.error.CompilerError;
import frontend.error.errorhandler.ErrorHandler;
import frontend.error.ErrorType;
import frontend.ir.type.IntegerType;
import frontend.ir.type.Type;
import frontend.ir.value.BuildFactory;
import frontend.ir.value.GlobalVar;
import frontend.ir.value.instructions.ConstArray;
import frontend.error.symbol.*;
import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static frontend.parser.ParserUtils.outputArrayDimension;
import static frontend.parser.ParserUtils.parseArrayDimension;
import static frontend.ir.LLVMGenerator.*;

/**
 * 常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
 */
public class ConstDefNode extends Node
{

    Token IDENFRToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<ConstExpNode> constExpNodeList;
    ArrayList<Token> RBRACKTokenList;
    Token ASSIGNToken;
    ConstInitValNode constInitValNode;

    public ConstDefNode()
    {
        super(NodeType.ConstDef);
        this.IDENFRToken = null;
        this.LBRACKTokenList = new ArrayList<>();
        this.constExpNodeList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();
        this.ASSIGNToken = null;
        this.constInitValNode = null;
    }

    @Override
    public void parseNode()
    {
        this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            parseArrayDimension(this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        }
        this.ASSIGNToken = Parser.getToken(TokenType.ASSIGN);
        this.constInitValNode = new ConstInitValNode();
        this.constInitValNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken.toString() + "\n", true);
        outputArrayDimension(destFile, this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.ASSIGNToken.toString() + "\n", true);
        this.constInitValNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        if (!st.isDefinitionUnique(IDENFRToken.getValue()))
        {
            ErrorHandler.addError(new CompilerError(ErrorType.b, "Duplicate declaration of variable " + IDENFRToken.getValue(), IDENFRToken.getLineNumber()));
            return;
        }

        if (LBRACKTokenList.isEmpty())
        {
            INTSymbol intSymbol = new INTSymbol(IDENFRToken.getValue(), true, true);
            st.addSymbol(intSymbol);
        }
        else
        {
            ARRAYSymbol arraySymbol = new ARRAYSymbol(IDENFRToken.getValue(), LBRACKTokenList.size(), true, true);
            st.addSymbol(arraySymbol);
            for (ConstExpNode constExpNode : constExpNodeList)
            {
                constExpNode.parseSymbol(st);
            }
        }
        if (constInitValNode != null)
        {
            constInitValNode.parseSymbol(st);
        }
    }

    @Override
    public void parseIR()
    {
        // ConstDef -> Ident { '[' ConstExp ']' } '=' ConstInitVal
        String name = IDENFRToken.getValue();
        if (constExpNodeList.isEmpty())  // is not an array
        {
            constInitValNode.parseIR();
            tmpValue = BuildFactory.getConstInt(saveVal == null ? 0 : saveVal);
            addConst(name, saveVal);
            if (isGlobal)
            {
                tmpValue = BuildFactory.getGlobalVar(name, tmpType, true, tmpValue);
                addSymbol(name, tmpValue);
            }
            else
            {
                tmpValue = BuildFactory.buildVar(blockStack.peek(), tmpValue, true, tmpType);
                addSymbol(name, tmpValue);
            }
        }
        else
        {
            // is an array
            List<Integer> dims = new ArrayList<>();
            for (ConstExpNode constExpNode : constExpNodeList)
            {
                constExpNode.parseIR();
                dims.add(saveVal);
                saveVal = null;
            }
            tmpDims = new ArrayList<>(dims);
            Type type = null;
            for (int i = dims.size() - 1; i >= 0; i--)
            {
                if (type == null)
                {
                    type = BuildFactory.getArrayType(IntegerType.i32, dims.get(i));
                }
                else
                {
                    type = BuildFactory.getArrayType(type, dims.get(i));
                }
            }
            if (isGlobal)
            {
                tmpValue = BuildFactory.getGlobalArray(name, type, true);
                ((ConstArray) ((GlobalVar) tmpValue).getValue()).init = true;
            }
            else
            {
                tmpValue = BuildFactory.buildArray(blockStack.peek(), true, type);
            }
            addSymbol(name, tmpValue);
            curArray = tmpValue;
            isArray = true;
            tmpName = name;
            tmpDepth = 0;
            tmpOffset = 0;
            constInitValNode.parseIR();
            isArray = false;
        }
    }
}
