package frontend.parser.node;

import backend.errorhandler.CompilerError;
import frontend.parser.symbol.SymbolTable;
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
 * 语句块 Block → '{' { BlockItem } '}'
 */
public class BlockNode extends Node
{

    Token LBRACEToken;
    ArrayList<BlockItemNode> blockItemNodeList;
    Token RBRACEToken;

    public BlockNode()
    {
        super(NodeType.Block);
        this.LBRACEToken = null;
        this.blockItemNodeList = new ArrayList<>();
        this.RBRACEToken = null;
    }

    @Override
    public void parseNode()
    {
        this.LBRACEToken = Parser.getToken(TokenType.LBRACE);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.RBRACE)
        {
            BlockItemNode blockItemNode = new BlockItemNode();
            blockItemNode.parseNode();
            this.blockItemNodeList.add(blockItemNode);
        }
        this.RBRACEToken = Parser.getToken(TokenType.RBRACE);
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {

        FileOperate.outputFileUsingUsingBuffer(destFile, this.LBRACEToken.toString() + "\n", true);
        for (BlockItemNode blockItemNode : this.blockItemNodeList)
        {
            blockItemNode.outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, this.RBRACEToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        SymbolTable childSt = new SymbolTable(st);
        for (BlockItemNode blockItemNode : this.blockItemNodeList)
        {
            blockItemNode.parseSymbol(childSt);
        }
    }
}
