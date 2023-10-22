package frontend.parser.node;

import backend.errorhandler.CompilerError;
import backend.errorhandler.ErrorHandler;
import backend.errorhandler.ErrorType;
import frontend.parser.symbol.*;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static frontend.parser.ParserUtils.outputArrayDimension;
import static frontend.parser.ParserUtils.parseArrayDimension;

/**
 * 常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
 */
public class ConstDefNode extends Node
{

    Token IDENFRToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<ConstExpNode> constExpNodeList;
    ArrayList<Token> RBRACKTokenList;
    Token ASSIGNToken;
    ConstInitValNode constInitValNode;

    public ConstDefNode()
    {
        super(NodeType.ConstDef);
        this.IDENFRToken = null;
        this.LBRACKTokenList = new ArrayList<>();
        this.constExpNodeList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();
        this.ASSIGNToken = null;
        this.constInitValNode = null;
    }

    @Override
    public void parseNode()
    {
        this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            parseArrayDimension(this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        }
        this.ASSIGNToken = Parser.getToken(TokenType.ASSIGN);
        this.constInitValNode = new ConstInitValNode();
        this.constInitValNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken.toString() + "\n", true);
        outputArrayDimension(destFile, this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.ASSIGNToken.toString() + "\n", true);
        this.constInitValNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }



    @Override
    public void parseSymbol(SymbolTable st)
    {
        if (!st.isDefinitionUnique(IDENFRToken.getValue()))
        {
            ErrorHandler.addError(new CompilerError(ErrorType.b, "Duplicate declaration of variable " + IDENFRToken.getValue(), IDENFRToken.getLineNumber()));
            return;
        }

        if (LBRACKTokenList.isEmpty())
        {
            INTSymbol intSymbol = new INTSymbol(IDENFRToken.getValue(), true, true);
            st.addSymbol(intSymbol);
        }
        else
        {
            ARRAYSymbol arraySymbol = new ARRAYSymbol(IDENFRToken.getValue(), LBRACKTokenList.size(), true, true);
            st.addSymbol(arraySymbol);
            for (ConstExpNode constExpNode : constExpNodeList)
            {
                constExpNode.parseSymbol(st);
            }
        }
        if (constInitValNode != null)
        {
            constInitValNode.parseSymbol(st);
        }
    }
}
