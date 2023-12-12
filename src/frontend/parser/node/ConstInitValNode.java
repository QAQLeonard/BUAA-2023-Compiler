package frontend.parser.node;

import frontend.ir.type.ArrayType;
import frontend.ir.type.PointerType;
import frontend.ir.value.BuildFactory;
import frontend.ir.value.ConstInt;
import frontend.ir.value.Value;
import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.error.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static frontend.ir.LLVMGenerator.*;
/**
 * 常量初值 ConstInitVal → ConstExp<br>
 * | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
 */
public class ConstInitValNode extends Node
{
    ConstExpNode constExpNode;
    Token LBRACEToken;
    ArrayList<ConstInitValNode> constInitValNodeList;
    ArrayList<Token> COMMATokenList;
    Token RBRACEToken;

    public ConstInitValNode()
    {
        super(NodeType.ConstInitVal);
        this.constInitValNodeList = new ArrayList<>();
        this.COMMATokenList = new ArrayList<>();
    }

    @Override
    public void parseNode()
    {
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.LBRACE)
        {
            this.constExpNode = new ConstExpNode();
            this.constExpNode.parseNode();
            return;
        }
        LBRACEToken = Parser.getToken(TokenType.LBRACE);
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.RBRACE)
        {
            RBRACEToken = Parser.getToken(TokenType.RBRACE);
            return;
        }
        ConstInitValNode constInitValNode = new ConstInitValNode();
        constInitValNode.parseNode();
        this.constInitValNodeList.add(constInitValNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.COMMA)
        {
            COMMATokenList.add(Parser.getToken(TokenType.COMMA));
            constInitValNode = new ConstInitValNode();
            constInitValNode.parseNode();
            this.constInitValNodeList.add(constInitValNode);
        }
        RBRACEToken = Parser.getToken(TokenType.RBRACE);

    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        if (this.constExpNode != null)
        {
            this.constExpNode.outputNode(destFile);
        }
        else
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LBRACEToken.toString() + "\n", true);
            if (!this.constInitValNodeList.isEmpty())
            {
                this.constInitValNodeList.get(0).outputNode(destFile);
                for (int i = 0; i < this.COMMATokenList.size(); i++)
                {
                    FileOperate.outputFileUsingUsingBuffer(destFile, this.COMMATokenList.get(i).toString() + "\n", true);
                    this.constInitValNodeList.get(i + 1).outputNode(destFile);
                }
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RBRACEToken.toString() + "\n", true);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        if (this.constExpNode != null)
        {
            this.constExpNode.parseSymbol(st);
        }
        else
        {
            for (ConstInitValNode constInitValNode : this.constInitValNodeList)
            {
                constInitValNode.parseSymbol(st);
            }
        }
    }

    @Override
    public void parseIR()
    {
        // ConstInitVal -> ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        if (constExpNode != null && !isArray)
        {
            // is not an array
            constExpNode.parseIR();
        }
        else
        {
            // '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
            if (constExpNode != null)
            {
                tmpValue = null;
                constExpNode.parseIR();
                tmpDepth = 1;
                tmpValue = BuildFactory.getConstInt(saveVal);
                if (isGlobal)
                {
                    BuildFactory.buildInitArray(curArray, tmpOffset, tmpValue);
                }
                else
                {
                    BuildFactory.getStoreInst(blockStack.peek(), BuildFactory.getGEPInst(blockStack.peek(), curArray, tmpOffset), tmpValue);
                }
                StringBuilder name = new StringBuilder(tmpName);
                List<Value> args = ((ArrayType) ((PointerType) curArray.getType()).getTargetType()).offset2Index(tmpOffset);
                for (Value v : args)
                {
                    name.append(((ConstInt) v).getValue()).append(";");
                }
                addConst(name.toString(), saveVal);
                tmpOffset++;
            }
            else if (!constInitValNodeList.isEmpty())
            {
                int depth = 0, offset = tmpOffset;
                for (ConstInitValNode constInitValNode1 : constInitValNodeList)
                {
                    constInitValNode1.parseIR();
                    depth = Math.max(depth, tmpDepth);
                }
                depth++;
                int size = 1;
                for (int i = 1; i < depth; i++)
                {
                    size *= tmpDims.get(tmpDims.size() - i);
                }
                tmpOffset = Math.max(tmpOffset, offset + size);
                tmpDepth = depth;
            }
        }
    }
}
