package frontend.parser.node;

import backend.errorhandler.CompilerError;
import backend.errorhandler.ErrorHandler;
import backend.errorhandler.ErrorType;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.parser.symbol.*;
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
    boolean isConstant;
    ExpType expType;

    public LValNode()
    {
        super(NodeType.LVal);
        this.LBRACKTokenList = new ArrayList<>();
        this.expNodeList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();
        expType = ExpType.INT;
        isConstant = false;
    }

    @Override
    public void parseNode()
    {
        this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            this.LBRACKTokenList.add(Parser.getToken(TokenType.LBRACK));
            ExpNode expNode = new ExpNode();
            expNode.parseNode();
            this.expNodeList.add(expNode);
            this.RBRACKTokenList.add(Parser.getToken(TokenType.RBRACK));
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

    @Override
    public void parseSymbol(SymbolTable st)
    {
        Symbol symbol = st.getSymbol(this.IDENFRToken.getValue());
        if (symbol == null)
        {
            ErrorHandler.addError(new CompilerError(ErrorType.c, this.IDENFRToken.getValue() + "undefined", this.IDENFRToken.getLineNumber()));
            expType = ExpType.ERROR;
            return;
        }
        switch (symbol.getType())
        {
            case FUNCTION ->
            {
                ErrorHandler.addError(new CompilerError(ErrorType.UNDEFINED, this.IDENFRToken.getValue() + "is a function", this.IDENFRToken.getLineNumber()));
                expType = ExpType.ERROR;
            }
            case INT ->
            {
                if (!LBRACKTokenList.isEmpty())
                {
                    ErrorHandler.addError(new CompilerError(ErrorType.UNDEFINED, this.IDENFRToken.getValue() + "is not an array", this.IDENFRToken.getLineNumber()));
                    expType = ExpType.ERROR;
                    return;
                }
                expType = ExpType.INT;
                isConstant = ((INTSymbol) symbol).isConstant();
            }
            case ARRAY ->
            {
                for (ExpNode expNode : expNodeList)
                {
                    expNode.parseSymbol(st);
                    if (expNode.getExpType() != ExpType.INT)
                    {
                        ErrorHandler.addError(new CompilerError(ErrorType.UNDEFINED, "array index must be int", this.IDENFRToken.getLineNumber()));
                        expType = ExpType.ERROR;
                        return;
                    }
                }
                ARRAYSymbol arraySymbol = (ARRAYSymbol) symbol;
                int temp = arraySymbol.getDim() - LBRACKTokenList.size();
                // System.out.println(temp);
                switch (temp)
                {
                    case 0 -> expType = ExpType.INT;
                    case 1 -> expType = ExpType.ARRAY1D;
                    case 2 -> expType = ExpType.ARRAY2D;
                    default -> expType = ExpType.ERROR;
                }
                isConstant = ((ARRAYSymbol) symbol).isConstant();
            }

        }
    }

    public ExpType getExpType()
    {
        return expType;
    }

}
