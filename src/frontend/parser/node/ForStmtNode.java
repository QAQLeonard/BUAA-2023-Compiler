package frontend.parser.node;

import backend.errorhandler.CompilerError;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.parser.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
/**
 * 语句 ForStmt → LVal '=' Exp
 */
public class ForStmtNode extends Node
{

    LValNode lValNode;
    Token ASSIGNToken;
    ExpNode expNode;
    public ForStmtNode()
    {
        super(NodeType.ForStmt);
        lValNode = null;
        ASSIGNToken = null;
        expNode = null;
    }

    @Override
    public void parseNode()
    {
        this.lValNode = new LValNode();
        this.lValNode.parseNode();
        this.ASSIGNToken = Parser.getToken(TokenType.ASSIGN);
        this.expNode = new ExpNode();
        this.expNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.lValNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.ASSIGNToken.toString() + "\n", true);
        this.expNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType())+"\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        this.lValNode.parseSymbol(st);
        this.expNode.parseSymbol(st);
    }

}
