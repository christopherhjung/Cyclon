package org.cyclon;

import org.cyclon.parser.ast.*;

public class Expander implements Visitor{
    private Expr result;
    @Override
    public void visitList(ListExpr list) {

    }

    @Override
    public void visitObject(ObjectExpr obj) {

    }

    @Override
    public void visitLiteral(LiteralExpr literal) {

    }

    @Override
    public void visitIdent(IdentExpr ident) {

    }

    @Override
    public void visitAssign(AssignExpr assign) {

    }

    @Override
    public void visitBlock(BlockExpr block) {

    }

    @Override
    public void visitPair(PairExpr block) {
        block.getKey().visit(this);
        block.setKey(result);
        block.getValue().visit(this);
        block.setKey(result);
        result = block;
    }
}
