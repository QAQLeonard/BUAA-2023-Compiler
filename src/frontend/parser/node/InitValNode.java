package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
    public void parseNode() throws CompilerException
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
            if (this.initValNodeList.size() > 0)
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
}
