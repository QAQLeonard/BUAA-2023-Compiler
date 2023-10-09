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
    public void parseNode() throws CompilerException
    {
        this.bTypeNode = new BTypeNode();
        this.bTypeNode.parseNode();
        this.IDENFRToken = Parser.getToken();
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            this.LBRACKTokenList.add(Parser.getToken());
            this.RBRACKTokenList.add(Parser.getToken());

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
        if (this.LBRACKTokenList.size() > 0)
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
}
