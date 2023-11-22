package node;

import error.CompilerError;
import backend.errorhandler.ErrorHandler;
import error.ErrorType;
import ir.type.Type;
import ir.value.BuildFactory;
import symbol.ARRAYSymbol;
import symbol.INTSymbol;
import symbol.SymbolTable;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static frontend.parser.ParserUtils.*;
import static ir.LLVMGenerator.*;

/**
 * 变量定义 VarDef → Ident { '[' ConstExp ']' }<br>
 * | Ident { '[' ConstExp ']' } '=' InitVal
 */
public class VarDefNode extends Node
{

    Token IDENFERToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<ConstExpNode> constExpNodeList;
    ArrayList<Token> RBRACKTokenList;
    Token ASSIGNToken;
    InitValNode initValNode;

    public VarDefNode()
    {
        super(NodeType.VarDef);
        this.IDENFERToken = null;
        this.LBRACKTokenList = new ArrayList<>();
        this.constExpNodeList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();
        this.ASSIGNToken = null;
        this.initValNode = null;
    }

    @Override
    public void parseNode()
    {
        this.IDENFERToken = Parser.getToken(TokenType.IDENFR);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            parseArrayDimension(this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        }
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.ASSIGN)
        {
            this.ASSIGNToken = Parser.getToken(TokenType.ASSIGN);
            this.initValNode = new InitValNode();
            this.initValNode.parseNode();
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFERToken + "\n", true);
        outputArrayDimension(destFile, this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        if (this.ASSIGNToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.ASSIGNToken + "\n", true);
            this.initValNode.outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);

    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        if (!st.isDefinitionUnique(IDENFERToken.getValue()))
        {
            ErrorHandler.addError(new CompilerError(ErrorType.b, "Duplicate declaration of variable " + this.IDENFERToken.getValue(), this.IDENFERToken.getLineNumber()));
            return;
        }

        if (this.LBRACKTokenList.isEmpty())
        {
            INTSymbol intSymbol = new INTSymbol(this.IDENFERToken.getValue(), false, this.ASSIGNToken != null);
            st.addSymbol(intSymbol);
        }
        else
        {
            int dimension = this.LBRACKTokenList.size();
            ARRAYSymbol arraySymbol = new ARRAYSymbol(this.IDENFERToken.getValue(), dimension, false, this.ASSIGNToken != null);
            st.addSymbol(arraySymbol);
            for (ConstExpNode constExpNode : this.constExpNodeList)
            {
                constExpNode.parseSymbol(st);
            }

        }
    }

    @Override
    public void parseIR()
    {
        // VarDef -> Ident { '[' ConstExp ']' } [ '=' InitVal ]
        String name = IDENFERToken.getValue();
        // is not an array
        if (constExpNodeList.isEmpty())
        {
            if (initValNode != null)
            {
                tmpValue = null;
                if (isGlobal)
                {
                    isConst = true;
                    saveValue = null;
                }
                initValNode.parseIR();
                isConst = false;// initVal must be a const
            }
            else
            {
                tmpValue = null;
                if (isGlobal)
                {
                    saveValue = null;
                }
            }
            if (isGlobal)
            {
                tmpValue = BuildFactory.getGlobalVar(name, tmpType, false, BuildFactory.getConstInt(saveValue == null ? 0 : saveValue));
                addSymbol(name, tmpValue);
            }
            else
            {
                tmpValue = BuildFactory.buildVar(blockStack.peek(), tmpValue, isConst, tmpType);
                addSymbol(name, tmpValue);
            }
        }
        else    // is an array
        {
            isConst = true;
            List<Integer> dims = new ArrayList<>();
            for (ConstExpNode constExpNode : constExpNodeList)
            {
                constExpNode.parseIR();
                dims.add(saveValue);
            }
            isConst = false;
            tmpDims = new ArrayList<>(dims);
            Type type = null;
            for (int i = dims.size() - 1; i >= 0; i--)
            {
                if (type == null)
                {
                    type = BuildFactory.getArrayType(tmpType, dims.get(i));
                }
                else
                {
                    type = BuildFactory.getArrayType(type, dims.get(i));
                }
            }
            if (isGlobal)
            {
                tmpValue = BuildFactory.getGlobalArray(name, type, false);
            }
            else
            {
                tmpValue = BuildFactory.buildArray(blockStack.peek(), false, type);
            }
            addSymbol(name, tmpValue);
            curArray = tmpValue;
            if (initValNode != null)
            {
                isArray = true;
                tmpName = name;
                tmpDepth = 0;
                tmpOffset = 0;
                initValNode.parseIR();
                isArray = false;
            }
            isConst = false;
        }
    }
}
