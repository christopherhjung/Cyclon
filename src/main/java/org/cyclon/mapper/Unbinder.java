package org.cyclon.mapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyclon.parser.ast.AssignExpr;
import org.cyclon.parser.ast.BlockExpr;
import org.cyclon.parser.ast.Expr;
import org.cyclon.parser.ast.IdentExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Unbinder {
    private int identCounter;
    private Map<Expr, AssignExpr> map = new HashMap<>();

    private static char[] CHARS = new char[2 * 26 + 10];

    static{
        for(var i = 0; i < CHARS.length ; i++ ){
            if(i < 26){
                CHARS[i] = (char)(i + 'a');
            }else if(i < 52){
                CHARS[i] = (char)(i - 26 + 'A');
            }else{
                CHARS[i] = (char)(i - 36 + '0');
            }
        }
    }

    public String nextIdentifier(){
        var current = identCounter++;
        var sb = new StringBuilder();
        int radix = 52;
        while(true){
            var remainder = current % radix;
            sb.append(CHARS[remainder]);
            current /= radix;
            radix = 62;
            if(current == 0){
                break;
            }
        }
        return sb.toString();
    }

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
        for(var assign : assigns){
            assign.getKey().toggle(assign.getValue());
        }

        for (var expr : assigns) {
            expr.reduce();
        }

        var exprs = new ArrayList<Expr>();
        for(var assign : assigns){
            var key = assign.getKey();
            if(key.getExpr() == null){
                key.setKey(nextIdentifier());
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
