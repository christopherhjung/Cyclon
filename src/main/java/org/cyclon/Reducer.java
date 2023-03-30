package org.cyclon;

import org.cyclon.parser.ast.*;

public class Reducer implements Visitor{
    private Expr result;

    public Expr getResult() {
        return result;
    }

    @Override
    public void visitList(ListExpr list) {
        var idx = 0;
        var elems = list.getElems();
        for(var elem : elems){
            elem.visit(this);
            elems[idx++] = result;
        }
        result = list;
    }

    @Override
    public void visitObject(ObjectExpr obj) {
        for(var elem : obj.getPairs()){
            elem.visit(this);
        }

        result = obj;
    }

    @Override
    public void visitLiteral(LiteralExpr literal) {
        result = literal;
    }

    @Override
    public void visitIdent(IdentExpr ident) {
        result = ident.getExpr() == null ?
                ident :
                ident.getExpr();
    }

    @Override
    public void visitAssign(AssignExpr assign) {
        assign.getValue().visit(this);
    }

    @Override
    public void visitBlock(BlockExpr block) {
        Expr last = null;
        for(var elem : block.getExprs()){
            elem.visit(this);
            last = result;
        }
        result = last;
    }

    @Override
    public void visitPair(PairExpr block) {
        block.getKey().visit(this);
        block.setKey(result);
        block.getValue().visit(this);
        block.setValue(result);
        result = block;
    }
}
