package org.cyclon.visitor;

import org.cyclon.parser.ast.*;

public class Binder implements Visitor{
    public void bind(Expr expr){
        expr.visit(this);
    }

    @Override
    public void visitAssign(AssignExpr assign) {
        assign.getKey().assign(assign.getValue(), true);
    }

    @Override
    public void visitBlock(BlockExpr block) {
        for(var elem : block.getExprs()){
            elem.visit(this);
        }
    }

    @Override
    public void visitList(ListExpr list) {}

    @Override
    public void visitObject(ObjectExpr obj) {}

    @Override
    public void visitLiteral(LiteralExpr literal) {}

    @Override
    public void visitIdent(IdentExpr ident) {}

    @Override
    public void visitPair(PairExpr block) {}
}
