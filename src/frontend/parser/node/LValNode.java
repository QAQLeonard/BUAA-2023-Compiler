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
 * LVal → Ident {'[' Exp ']'}
 */
public class LValNode extends Node
{

    Token IDENFRToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<ExpNode> expNodeList;
    ArrayList<Token> RBRACKTokenList;

    public LValNode()
    {
        super(NodeType.LVal);
        this.LBRACKTokenList = new ArrayList<>();
        this.expNodeList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();

    }

    @Override
    public void parseNode()
    {
        this.IDENFRToken = Parser.getToken();
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            this.LBRACKTokenList.add(Parser.getToken());
            ExpNode expNode = new ExpNode();
            expNode.parseNode();
            this.expNodeList.add(expNode);
            this.RBRACKTokenList.add(Parser.getToken());
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken.toString() + "\n", true);
        for (int i = 0; i < LBRACKTokenList.size(); i++)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, LBRACKTokenList.get(i).toString() + "\n", true);
            expNodeList.get(i).outputNode(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, RBRACKTokenList.get(i).toString() + "\n", true);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }
}
