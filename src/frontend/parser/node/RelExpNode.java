package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
/**
 * 关系表达式 RelExp → AddExp | AddExp ('<' | '>' | '<=' | '>=') RelExp
 */
public class RelExpNode extends Node implements Expression
{

    AddExpNode addExpNode;
    RelExpNode relExpNode;
    Token LSSToken;
    Token GREToken;
    Token LEQToken;
    Token GEQToken;

    public RelExpNode()
    {
        super(NodeType.RelExp);
    }

    @Override
    public void parseNode()
    {
        addExpNode = new AddExpNode();
        addExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.LSS)
        {
            LSSToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.GRE)
        {
            GREToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.LEQ)
        {
            LEQToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.GEQ)
        {
            GEQToken = Parser.getToken();
        }
        else
        {
            return;
        }
        relExpNode = new RelExpNode();
        relExpNode.parseNode();
    }

    @Override
    public Token getOPToken()
    {
        if (this.LSSToken != null)
        {
            return this.LSSToken;
        }
        else if (this.GREToken != null)
        {
            return this.GREToken;
        }
        else if (this.LEQToken != null)
        {
            return this.LEQToken;
        }
        else if (this.GEQToken != null)
        {
            return this.GEQToken;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.addExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
        Token OPToken = this.getOPToken();
        if (OPToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, OPToken.toString() + "\n", true);
            this.relExpNode.outputNode(destFile);
        }
    }
}
