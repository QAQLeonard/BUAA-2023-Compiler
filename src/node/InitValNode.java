package node;

import ir.value.BuildFactory;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static ir.LLVMGenerator.*;

/**
 * InitVal → Exp <br>
 * | '{' [ InitVal { ',' InitVal } ] '}'
 */
public class InitValNode extends Node
{
    ExpNode expNode;
    Token LBRACEToken;
    ArrayList<InitValNode> initValNodeList;
    ArrayList<Token> COMMATokenList;
    Token RBRACEToken;

    public InitValNode()
    {
        super(NodeType.InitVal);
        expNode = null;
        LBRACEToken = null;
        initValNodeList = new ArrayList<>();
        COMMATokenList = new ArrayList<>();
        RBRACEToken = null;
    }

    @Override
    public void parseNode()
    {
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.LBRACE)
        {
            this.expNode = new ExpNode();

            this.expNode.parseNode();
            return;
        }
        LBRACEToken = Parser.getToken(TokenType.LBRACE);
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.RBRACE)
        {
            RBRACEToken = Parser.getToken(TokenType.RBRACE);
            return;
        }
        InitValNode initValNode = new InitValNode();

        initValNode.parseNode();
        this.initValNodeList.add(initValNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.COMMA)
        {
            COMMATokenList.add(Parser.getToken(TokenType.COMMA));
            initValNode = new InitValNode();
            initValNode.parseNode();
            this.initValNodeList.add(initValNode);
        }
        RBRACEToken = Parser.getToken(TokenType.RBRACE);
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        if (this.expNode != null) this.expNode.outputNode(destFile);
        else
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, LBRACEToken.toString() + "\n", true);
            if (!this.initValNodeList.isEmpty())
            {
                this.initValNodeList.get(0).outputNode(destFile);
                for (int i = 0; i < COMMATokenList.size(); i++)
                {
                    FileOperate.outputFileUsingUsingBuffer(destFile, COMMATokenList.get(i).toString() + "\n", true);
                    this.initValNodeList.get(i + 1).outputNode(destFile);
                }
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, RBRACEToken.toString() + "\n", true);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        if (this.expNode != null) this.expNode.parseSymbol(st);
        else
        {
            for (InitValNode initValNode : this.initValNodeList)
            {
                initValNode.parseSymbol(st);
            }
        }
    }
    // InitVal -> Exp | '{' [ InitVal { ',' InitVal } ] '}'
    @Override
    public void parseIR()
    {

        if (expNode != null && !isArray)
        {
            // Exp
            expNode.parseIR();
        }
        else
        {
            // '{' [ InitVal { ',' InitVal } ] '}' | ArrayEXP
            if (expNode != null)//ArrayEXP
            {
                if (isGlobal)
                {
                    isConst = true;
                }
                saveValue = null;
                tmpValue = null;
                expNode.parseIR();
                isConst = false;
                tmpDepth = 1;
                if (isGlobal)
                {
                    tmpValue = BuildFactory.getConstInt(saveValue);
                    BuildFactory.buildInitArray(curArray, tmpOffset, tmpValue);
                }
                else
                {
                    BuildFactory.getStoreInst(blockStack.peek(), BuildFactory.getGEPInst(blockStack.peek(), curArray, tmpOffset), tmpValue);
                }
                tmpOffset++;
            }
            else if (!initValNodeList.isEmpty())
            {
                int depth = 0, offset = tmpOffset;
                for (InitValNode initValNode1 : initValNodeList)
                {
                    initValNode1.parseIR();
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
