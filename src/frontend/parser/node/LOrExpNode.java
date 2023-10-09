package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * 逻辑或表达式 LOrExp → LAndExp | LAndExp '||' LOrExp
 */
public class LOrExpNode extends Node implements Expression
{

    LAndExpNode lAndExpNode;
    LOrExpNode lOrExpNode;
    Token ORToken;

    public LOrExpNode()
    {
        super(NodeType.LOrExp);
    }

    @Override
    public void parseNode() throws CompilerException
    {
        lAndExpNode = new LAndExpNode();
        lAndExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.OR)
        {
            this.ORToken = Parser.getToken();
            lOrExpNode = new LOrExpNode();
            lOrExpNode.parseNode();
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.lAndExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);

        if (this.ORToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.ORToken.toString() + "\n", true);
            this.lOrExpNode.outputNode(destFile);
        }
    }

    @Override
    public Token getOPToken()
    {
        if (this.ORToken != null)
        {
            return this.ORToken;
        }
        return null;
    }
}
