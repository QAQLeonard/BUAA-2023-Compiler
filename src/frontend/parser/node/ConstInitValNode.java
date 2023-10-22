package frontend.parser.node;

import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.parser.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
}
