package frontend.parser.node;

import frontend.error.CompilerError;
import frontend.error.errorhandler.ErrorHandler;
import frontend.error.ErrorType;
import frontend.ir.type.PointerType;
import frontend.ir.type.Type;
import frontend.ir.value.BuildFactory;
import frontend.ir.value.ConstInt;
import frontend.ir.value.Value;
import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.error.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static frontend.ir.LLVMGenerator.*;

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
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        this.lValNode.parseSymbol(st);
        this.expNode.parseSymbol(st);
        if (this.lValNode.isConstant)
        {
            ErrorHandler.addError(new CompilerError(ErrorType.h, "司马编译", this.lValNode.IDENFRToken.getLineNumber()));
        }
    }

    @Override
    public void parseIR()
    {
        if (lValNode.expNodeList.isEmpty()) {
            // is not an array
            Value input = getValue(lValNode.IDENFRToken.getValue());
            expNode.parseIR();
            tmpValue = BuildFactory.getStoreInst(blockStack.peek(), input, tmpValue);
        } else {
            // is an array
            List<Value> indexList = new ArrayList<>();
            for (ExpNode expNode : lValNode.expNodeList) {
                expNode.parseIR();
                indexList.add(tmpValue);
            }
            tmpValue = getValue(lValNode.IDENFRToken.getValue());
            Value addr;
            Type type = tmpValue.getType(), targetType = ((PointerType) type).getTargetType();
            if (targetType instanceof PointerType) {
                // arr[][3]
                tmpValue = BuildFactory.getLoadInst(blockStack.peek(), tmpValue);
            } else {
                // arr[3][2]
                indexList.add(0, ConstInt.ZERO);
            }
            addr = BuildFactory.getGEPInst(blockStack.peek(), tmpValue, indexList);
            expNode.parseIR();
            tmpValue = BuildFactory.getStoreInst(blockStack.peek(), addr, tmpValue);
        }
    }

}
