package org.cyclon.mapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyclon.parser.ast.AssignExpr;
import org.cyclon.parser.ast.Expr;
import org.cyclon.parser.ast.IdentExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Unbinder {
    private List<AssignExpr> assigns = new ArrayList<>();
    public IdentExpr identify(Expr expr){
        var ident = new IdentExpr(RandomStringUtils.randomAlphabetic(10));
        assigns.add(new AssignExpr(ident, expr));
        return ident;
    }
}
