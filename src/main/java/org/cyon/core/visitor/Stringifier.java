package org.cyon.core.visitor;

import org.cyon.core.parser.ast.Expr;

public interface Stringifier {
    String stringify(Expr expr);
}
