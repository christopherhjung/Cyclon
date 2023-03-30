package org.cyclon.mapper;

import org.cyclon.parser.ast.AssignExpr;
import org.cyclon.parser.ast.BlockExpr;
import org.cyclon.parser.ast.Expr;
import org.cyclon.parser.ast.IdentExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Unbinder {
    private final IdentFactory factory = new IdentFactoryImpl();
    private final Map<Expr, AssignExpr> map = new HashMap<>();

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
        assignExpr.setValue(expr.expand(this));
        return ident;
    }

    public Expr toBlock(IdentExpr root){
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
}
