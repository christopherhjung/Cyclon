package org.cyclon.visitor;

import org.cyclon.parser.ast.Expr;

public interface Stringifier {
    String stringify(Expr expr);
}
