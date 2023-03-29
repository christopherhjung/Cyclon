package org.cyclon.mapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyclon.parser.ast.AssignExpr;
import org.cyclon.parser.ast.BlockExpr;
import org.cyclon.parser.ast.Expr;
import org.cyclon.parser.ast.IdentExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Unbinder {
    private List<AssignExpr> assigns = new ArrayList<>();
    private Map<Expr, IdentExpr> idents = new HashMap<>();
    public IdentExpr identify(Expr expr){
        var ident = idents.get(expr);
        if(ident != null){
            return ident;
        }

        ident = new IdentExpr(RandomStringUtils.randomAlphabetic(10));
        idents.put(expr, ident);
        assigns.add(new AssignExpr(ident, expr.expand(this)));
        return ident;
    }

    public BlockExpr toBlock(Expr root){
        var exprs = new Expr[assigns.size() + 1];
        assigns.toArray(exprs);
        exprs[assigns.size()] = root;
        return new BlockExpr(exprs);
    }
}
