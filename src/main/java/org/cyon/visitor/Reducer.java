package org.cyon.visitor;

import org.cyon.parser.ast.*;

public class Reducer implements ResultVisitor<Expr>{
    public Expr reduce(Expr expr) {
        return expr.visit(this);
    }

    @Override
    public Expr visitList(ListExpr list) {
        var idx = 0;
        var elems = list.getElems();
        for(var elem : elems){
            elems[idx++] = elem.visit(this);
        }
        return list;
    }

    @Override
    public Expr visitObject(ObjectExpr obj) {
        for(var elem : obj.getPairs()){
            elem.visit(this);
        }

        return obj;
    }

    @Override
    public Expr visitLiteral(LiteralExpr literal) {
        return literal;
    }

    @Override
    public Expr visitIdent(IdentExpr ident) {
        return ident.getExpr() == null ?
                ident :
                ident.getExpr();
    }

    @Override
    public Expr visitAssign(AssignExpr assign) {
        return assign.getValue().visit(this);
    }

    @Override
    public Expr visitBlock(BlockExpr block) {
        Expr last = null;
        for(var elem : block.getExprs()){
            last = elem.visit(this);
        }
        return last;
    }

    @Override
    public Expr visitPair(PairExpr block) {
        block.setKey(block.getKey().visit(this));
        block.setValue(block.getValue().visit(this));
        return block;
    }
}
