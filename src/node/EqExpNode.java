package node;

import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * 相等性表达式 EqExp → RelExp | RelExp ('==' | '!=') EqExp
 */
public class EqExpNode extends Node implements Expression
{

    RelExpNode relExpNode;
    EqExpNode eqExpNode;
    Token EQLToken;
    Token NEQToken;

    public EqExpNode()
    {
        super(NodeType.EqExp);
    }

    @Override
    public void parseNode()
    {
        relExpNode = new RelExpNode();
        relExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.EQL)
        {
            this.EQLToken = Parser.getToken(TokenType.EQL);
        }
        else if (token.getType() == TokenType.NEQ)
        {
            this.NEQToken = Parser.getToken(TokenType.NEQ);
        }
        else
        {
            return;
        }
        eqExpNode = new EqExpNode();
        eqExpNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.relExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);

        Token OPToken = this.getOPToken();
        if (OPToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, OPToken.toString() + "\n", true);
            this.eqExpNode.outputNode(destFile);
        }
    }

    @Override
    public Token getOPToken()
    {
        if (this.EQLToken != null)
        {
            return this.EQLToken;
        }
        else if (this.NEQToken != null)
        {
            return this.NEQToken;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        this.relExpNode.parseSymbol(st);
        if (this.eqExpNode != null)
        {
            this.eqExpNode.parseSymbol(st);
        }
    }

    @Override
    public ExpType getExpType()
    {
        if(this.eqExpNode == null)
        {
            return this.relExpNode.getExpType();
        }
        else
        {
            if(this.relExpNode.getExpType() == ExpType.INT && this.eqExpNode.getExpType() == ExpType.INT)
            {
                return ExpType.INT;
            }
            else
            {
                return ExpType.ERROR;
            }
        }
    }
}
