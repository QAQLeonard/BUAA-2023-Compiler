package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
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
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACE)
        {
            LBRACEToken = Parser.getToken();
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.RBRACE)
            {
                RBRACEToken = Parser.getToken();
                return;
            }
            ConstInitValNode constInitValNode = new ConstInitValNode();
            constInitValNode.parseNode();
            this.constInitValNodeList.add(constInitValNode);
            while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.COMMA)
            {
                COMMATokenList.add(Parser.getToken());
                constInitValNode = new ConstInitValNode();
                constInitValNode.parseNode();
                this.constInitValNodeList.add(constInitValNode);
            }
            RBRACEToken = Parser.getToken();
        }
        else
        {
            this.constExpNode = new ConstExpNode();
            this.constExpNode.parseNode();
        }
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
            if (this.constInitValNodeList.size() != 0)
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
}
