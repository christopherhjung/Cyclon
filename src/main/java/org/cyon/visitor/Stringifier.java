package org.cyon.visitor;

import org.cyon.parser.ast.Expr;

public interface Stringifier {
    String stringify(Expr expr);
}
