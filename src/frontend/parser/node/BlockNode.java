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
        this.LBRACEToken = Parser.getToken();
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.RBRACE)
        {
            BlockItemNode blockItemNode = new BlockItemNode();
            blockItemNode.parseNode();
            this.blockItemNodeList.add(blockItemNode);
        }
        this.RBRACEToken = Parser.getToken();
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
}
