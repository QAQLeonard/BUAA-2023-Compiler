package node;

import error.CompilerError;
import backend.errorhandler.ErrorHandler;
import error.ErrorType;
import ir.LLVMGenerator;
import ir.value.ConstInt;
import ir.value.Function;
import ir.value.instructions.Operator;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import symbol.FUNCSymbol;
import symbol.Symbol;
import symbol.SymbolTable;
import symbol.SymbolType;
import utils.FileOperate;
import ir.value.BuildFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import static frontend.parser.ParserUtils.TypeEqual;

/**
 * 一元表达式 UnaryExp → PrimaryExp <br>
 * | Ident '(' [FuncRParams] ')'<br>
 * | UnaryOp UnaryExp<br>
 */
public class UnaryExpNode extends Node implements Expression
{

    PrimaryExpNode primaryExpNode;
    Token IDENFRToken;
    Token LPARENTToken;
    FuncRParamsNode funcRParamsNode;
    Token RPARENTToken;
    UnaryOpNode unaryOpNode;
    UnaryExpNode unaryExpNode;

    ExpType expType;

    public UnaryExpNode()
    {
        super(NodeType.UnaryExp);
    }

    @Override
    public void parseNode()
    {
        // UnaryExp → UnaryOp UnaryExp
        if (ParserUtils.UnaryOpTokenTypes.contains(Objects.requireNonNull(Parser.peekToken(0)).getType()))
        {
            this.unaryOpNode = new UnaryOpNode();
            this.unaryOpNode.parseNode();
            this.unaryExpNode = new UnaryExpNode();
            this.unaryExpNode.parseNode();
        }
        // UnaryExp → Ident '(' [FuncRParams] ')'
        else if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.IDENFR && Objects.requireNonNull(Parser.peekToken(1)).getType() == TokenType.LPARENT)
        {
            this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
            this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
            Set<TokenType> FIRST_FuncRParams = Set.of(TokenType.INTCON, TokenType.IDENFR, TokenType.LPARENT, TokenType.PLUS, TokenType.MINU);
            if (FIRST_FuncRParams.contains(Objects.requireNonNull(Parser.peekToken(0)).getType()))
            {
                this.funcRParamsNode = new FuncRParamsNode();
                this.funcRParamsNode.parseNode();
            }
            this.RPARENTToken = Parser.getToken(TokenType.RPARENT);

        }
        // UnaryExp → PrimaryExp
        else
        {
            this.primaryExpNode = new PrimaryExpNode(NodeType.PrimaryExp);
            this.primaryExpNode.parseNode();
        }
    }

    @Override
    public Token getOPToken()
    {
        return null;
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        if (this.unaryOpNode != null)
        {
            this.unaryOpNode.outputNode(destFile);
            this.unaryExpNode.outputNode(destFile);
        }
        else if (this.IDENFRToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken + "\n", true);
            if (this.funcRParamsNode != null)
            {
                this.funcRParamsNode.outputNode(destFile);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken + "\n", true);
        }
        else
        {
            this.primaryExpNode.outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public String toString()
    {
        String temp = "";
        if (this.unaryOpNode != null)
        {
            temp += this.unaryOpNode.toString();
            temp += this.unaryExpNode.toString();
        }
        else if (this.IDENFRToken != null)
        {
            temp += this.IDENFRToken.getValue() + "\n";
            temp += this.LPARENTToken.getValue() + "\n";
            if (this.funcRParamsNode != null)
            {
                temp += this.funcRParamsNode.toString();
            }
            temp += this.RPARENTToken.getValue() + "\n";
        }
        else
        {
            temp += this.primaryExpNode.toString();
        }
        return temp;
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        if (this.primaryExpNode != null)
        {
            this.primaryExpNode.parseSymbol(st);
        }
        else if (this.IDENFRToken != null)
        {
            Symbol symbol = st.getSymbol(this.IDENFRToken.getValue());
            if (symbol == null)
            {
                ErrorHandler.addError(new CompilerError(ErrorType.c, "Not Defined Func", this.IDENFRToken.getLineNumber()));
                expType = ExpType.ERROR;
                return;
            }
            if (symbol.getType() != SymbolType.FUNCTION)
            {
                ErrorHandler.addError(new CompilerError(ErrorType.UNDEFINED, "Not a FUNC", this.IDENFRToken.getLineNumber()));
                expType = ExpType.ERROR;
                return;
            }
            FUNCSymbol funcSymbol = (FUNCSymbol) symbol;
            if (funcRParamsNode == null)
            {
                if (!funcSymbol.getParameters().isEmpty())
                {
                    ErrorHandler.addError(new CompilerError(ErrorType.d, "Error num of parameters", this.IDENFRToken.getLineNumber()));
                    expType = ExpType.ERROR;
                    return;
                }
                expType = funcSymbol.getReturnType();
                return;
            }
            if (funcRParamsNode.expNodeList.size() != funcSymbol.getParameters().size())
            {
                ErrorHandler.addError(new CompilerError(ErrorType.d, "Error num of parameters", this.IDENFRToken.getLineNumber()));
                expType = ExpType.ERROR;
                return;
            }
            for (int i = 0; i < funcRParamsNode.expNodeList.size(); i++)
            {
                funcRParamsNode.expNodeList.get(i).parseSymbol(st);
                if (!TypeEqual(funcRParamsNode.expNodeList.get(i).getExpType(), funcSymbol.getParameters().get(i)))
                {
                    // System.out.println(funcRParamsNode.expNodeList.get(i).getExpType()+"---"+funcSymbol.getParameters().get(i).getType());
                    ErrorHandler.addError(new CompilerError(ErrorType.e, "Error type of parameters", this.IDENFRToken.getLineNumber()));
                    expType = ExpType.ERROR;
                    return;
                }
            }
            expType = funcSymbol.getReturnType();
        }
        else if (this.unaryOpNode != null)
        {
            this.unaryExpNode.parseSymbol(st);
        }
    }

    @Override
    public ExpType getExpType()
    {
        if (this.primaryExpNode != null)
        {
            return this.primaryExpNode.getExpType();
        }
        else if (this.IDENFRToken != null)
        {
            return expType;
        }
        else if (this.unaryOpNode != null)
        {
            return this.unaryExpNode.getExpType();
        }
        return ExpType.ERROR;
    }

    @Override
    public void parseIR()
    {
        // UnaryExp -> PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        if (primaryExpNode != null)
        {
            primaryExpNode.parseIR();
        }
        else if (this.IDENFRToken != null)
        {
            // Ident '(' [FuncRParams] ')'
            LLVMGenerator.tmpList = new ArrayList<>();
            if (funcRParamsNode != null)
            {
                funcRParamsNode.parseIR();
            }

            LLVMGenerator.tmpValue = BuildFactory.getCallInst(LLVMGenerator.blockStack.peek(), (Function) LLVMGenerator.getValue(IDENFRToken.getValue()), LLVMGenerator.tmpList);
        }
        else
        {
            // UnaryOp UnaryExp
            // UnaryOp 直接在这里处理即可
            if (unaryOpNode.getOPToken().getType() == TokenType.PLUS)
            {
                unaryExpNode.parseIR();
            }
            else if (unaryOpNode.getOPToken().getType() == TokenType.MINU)
            {
                unaryExpNode.parseIR();
                if (LLVMGenerator.isConst)
                {
                    LLVMGenerator.saveValue = -LLVMGenerator.saveValue;
                }
                else
                {
                    LLVMGenerator.tmpValue = BuildFactory.getBinaryInst(LLVMGenerator.blockStack.peek(), Operator.Sub, ConstInt.ZERO, LLVMGenerator.tmpValue);
                }
            }
            else
            {
                unaryExpNode.parseIR();
                LLVMGenerator.tmpValue = BuildFactory.buildNot(LLVMGenerator.blockStack.peek(), LLVMGenerator.tmpValue);
            }
        }
    }

}
