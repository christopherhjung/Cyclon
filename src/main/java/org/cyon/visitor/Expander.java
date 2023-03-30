package org.cyon.visitor;

import org.apache.commons.lang3.NotImplementedException;
import org.cyon.parser.ast.*;

public class Expander implements ResultVisitor<Expr>{
    private final Unbinder unbinder;

    public Expander(){
        unbinder = new Unbinder(this);
    }

    public Expr expand(Expr expr){
        var ident = unbinder.identify(expr);
        return unbinder.collect(ident);
    }

    @Override
    public Expr visitLiteral(LiteralExpr literal) {
        return literal;
    }

    @Override
    public Expr visitList(ListExpr list) {
        var idx = 0;
        var elems = list.getElems();
        for(var elem : elems){
            elems[idx++] = elem.visit(unbinder);
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
    public Expr visitPair(PairExpr block) {
        block.setKey(block.getKey().visit(unbinder));
        block.setValue(block.getValue().visit(unbinder));
        return block;
    }

    @Override
    public Expr visitIdent(IdentExpr ident) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public Expr visitAssign(AssignExpr assign) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public Expr visitBlock(BlockExpr block) {
        throw new NotImplementedException("Not supported");
    }
}
