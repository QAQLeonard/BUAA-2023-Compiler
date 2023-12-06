package node;

import error.CompilerError;
import backend.errorhandler.ErrorHandler;
import error.ErrorType;
import ir.type.PointerType;
import ir.type.Type;
import ir.value.BuildFactory;
import ir.value.ConstInt;
import ir.value.Value;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ir.IRGenerator.*;

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
