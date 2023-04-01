package org.cyon.core.visitor;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.cyon.core.parser.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Unbinder implements ResultVisitor<Expr>{
    private final IdentFactory factory = new IdentFactoryImpl();
    private final Map<Expr, AssignExpr> map = new HashMap<>();

    private Expander expander;

    public IdentExpr identify(Expr expr){
        var assignExpr = map.get(expr);
        if(assignExpr != null){
            var key = assignExpr.getKey();
            key.assign(expr, false);
            return key;
        }

        var ident = new IdentExpr(null);
        assignExpr = new AssignExpr(ident, null);
        map.put(expr, assignExpr);
        assignExpr.setValue(expr.visit(expander));
        return ident;
    }

    public Expr collect(IdentExpr root){
        var assigns = map.values();
        assigns.forEach(it -> it.getKey().toggle(it.getValue()));
        assigns.forEach(Expr::reduce);

        var exprs = new ArrayList<Expr>();
        for(var assign : assigns){
            var key = assign.getKey();
            if(key.getExpr() == null){
                key.setKey(factory.next());
                exprs.add(assign);
            }
        }

        var normalizedRoot = root.getExpr() == null ? root : root.getExpr();
        if(exprs.isEmpty()){
            return normalizedRoot;
        }

        exprs.add(normalizedRoot);
        return new BlockExpr(exprs.toArray(Expr[]::new));
    }

    @Override
    public Expr visitLiteral(LiteralExpr literal) {
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
