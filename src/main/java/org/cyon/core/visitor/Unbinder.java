package org.cyon.core.visitor;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.cyon.core.parser.ast.*;
import org.cyon.core.visitor.sequence.AlphaNumericIdentSequence;
import org.cyon.core.visitor.sequence.IdentSequence;

import java.util.*;

@AllArgsConstructor
public class Unbinder implements ResultVisitor<Expr>{
    private final IdentSequence factory = new AlphaNumericIdentSequence();

    private Expander expander;
    private final List<AssignExpr> assigns = new ArrayList<>();

    public IdentExpr identify(Expr expr){
        var alt = expr.getAlt();
        AssignExpr assignExpr;
        if(alt != null){
            assignExpr = (AssignExpr) alt;
            var key = assignExpr.getKey();
            key.setAlt(expr);
            return key;
        }

        var ident = new IdentExpr(null);
        assignExpr = new AssignExpr(ident, null);
        expr.setAlt(assignExpr);
        assignExpr.setValue(expr.visit(expander));
        assigns.add(assignExpr);
        return ident;
    }

    public Expr collect(IdentExpr root){
        assigns.forEach(it -> it.getKey().toggle(it.getValue()));
        assigns.forEach(Expr::reduce);

        var exprs = new ArrayList<Expr>();
        for(var assign : assigns){
            var key = assign.getKey();
            if(key.getAlt() == null){
                key.setKey(factory.next());
                exprs.add(assign);
            }
        }

        var normalizedRoot = root.getAlt() == null ? root : root.getAlt();
        if(exprs.isEmpty()){
            return normalizedRoot;
        }

        exprs.add(normalizedRoot);
        return new BlockExpr(exprs.toArray(Expr[]::new));
    }

    @Override
    public Expr visitLiteral(LiteralExpr literal) {
        if(literal.isString()){
            var str = (String)literal.getValue();
            if(str.length() > 3){
                return identify(literal);
            }
        }
        return literal;
    }

    @Override
    public Expr visitList(ListExpr list) {
        if(list.getElems().length == 0){
            return list;
        }

        return identify(list);
    }

    @Override
    public Expr visitObject(ObjectExpr obj) {
        if(obj.getPairs().length == 0){
            return obj;
        }

        return identify(obj);
    }

    @Override
    public Expr visitPair(PairExpr block) {
        throw new NotImplementedException("Not supported");
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
