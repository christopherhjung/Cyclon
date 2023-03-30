package org.cyclon.visitor;

import org.cyclon.parser.ast.*;

public interface ResultVisitor<T> {
    T visitLiteral(LiteralExpr literal);
    T visitList(ListExpr list);
    T visitObject(ObjectExpr obj);
    T visitPair(PairExpr pair);
    T visitIdent(IdentExpr ident);
    T visitAssign(AssignExpr assign);
    T visitBlock(BlockExpr block);
}
