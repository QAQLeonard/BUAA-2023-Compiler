package frontend.parser.node;

import frontend.errorhandler.CompilerError;
import frontend.errorhandler.ErrorHandler;
import frontend.errorhandler.ErrorType;
import ir.type.IntegerType;
import ir.value.BuildFactory;
import ir.value.Value;
import frontend.errorhandler.symbol.ARRAYSymbol;
import frontend.errorhandler.symbol.INTSymbol;
import frontend.errorhandler.symbol.SymbolTable;
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

import static ir.LLVMGenerator.*;
/**
 * 函数形参 FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
 */
public class FuncFParamNode extends Node
{
    BTypeNode bTypeNode;
    Token IDENFRToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<Token> RBRACKTokenList;
    ArrayList<ConstExpNode> constExpNodeList;

    public FuncFParamNode()
    {
        super(NodeType.FuncFParam);
        this.bTypeNode = null;
        this.IDENFRToken = null;
        this.LBRACKTokenList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();
        this.constExpNodeList = new ArrayList<>();
    }

    @Override
    public void parseNode()
    {
        this.bTypeNode = new BTypeNode();
        this.bTypeNode.parseNode();
        this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            this.LBRACKTokenList.add(Parser.getToken(TokenType.LBRACK));
            this.RBRACKTokenList.add(Parser.getToken(TokenType.RBRACK));

            while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
            {
                ParserUtils.parseArrayDimension(this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
            }
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.bTypeNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken.toString() + "\n", true);
        if (!this.LBRACKTokenList.isEmpty())
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LBRACKTokenList.get(0).toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RBRACKTokenList.get(0).toString() + "\n", true);
            for (int i = 0; i < this.constExpNodeList.size(); i++)
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, this.LBRACKTokenList.get(i+1).toString() + "\n", true);
                this.constExpNodeList.get(i).outputNode(destFile);
                FileOperate.outputFileUsingUsingBuffer(destFile, this.RBRACKTokenList.get(i+1).toString() + "\n", true);
            }
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType())+"\n", true);

    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        if (!st.isDefinitionUnique(IDENFRToken.getValue()))
        {
            ErrorHandler.addError(new CompilerError(ErrorType.b, "Duplicate declaration of variable " + this.IDENFRToken.getValue(), this.IDENFRToken.getLineNumber()));
            return;
        }

        if(this.LBRACKTokenList.isEmpty())
        {
            INTSymbol intSymbol = new INTSymbol(this.IDENFRToken.getValue(), false, false);
            st.addSymbol(intSymbol);
        }
        else
        {
            //st.addSymbol(new ARRAYSymbol(this.IDENFRToken.getValue(), this.LBRACKTokenList.size(), false, false));
            ARRAYSymbol arraySymbol = new ARRAYSymbol(this.IDENFRToken.getValue(), this.LBRACKTokenList.size(), false, false);
            st.addSymbol(arraySymbol);
        }
    }

    @Override
    public void parseIR()
    {
        // BType Ident [ '[' ']' { '[' ConstExp ']' }]
        if (isRegister)
        {
            int i = tmpIndex;
            Value value = BuildFactory.buildVar(blockStack.peek(), funcArgsList.get(i), false, tmpTypeList.get(i));
            addSymbol(IDENFRToken.getValue(), value);
        }
        else
        {
            if (LBRACKTokenList.isEmpty())
            {
                tmpType = IntegerType.i32;
            }
            else
            {
                List<Integer> dims = new ArrayList<>();
                dims.add(-1);
                if (!constExpNodeList.isEmpty())
                {
                    for (ConstExpNode constExpNode : constExpNodeList)
                    {
                        isConst = true;
                        constExpNode.parseIR();
                        dims.add(saveVal);
                        isConst = false;
                    }
                }
                tmpType = null;
                for (int i = dims.size() - 1; i >= 0; i--)
                {
                    if (tmpType == null)
                    {
                        tmpType = IntegerType.i32;
                    }
                    tmpType = BuildFactory.getArrayType(tmpType, dims.get(i));
                }
            }
        }
    }
}
