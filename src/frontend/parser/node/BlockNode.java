package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

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
}
