package org.cyclon;

import org.cyclon.parser.ast.*;

public interface ResultVisitor<T> {
    T visitList(ListExpr list);
    T visitObject(ObjectExpr obj);
    T visitLiteral(LiteralExpr literal);
    T visitIdent(IdentExpr ident);
    T visitAssign(AssignExpr assign);
    T visitBlock(BlockExpr block);
    T visitPair(PairExpr pair);
}
