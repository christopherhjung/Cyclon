package org.cyclon;

import org.cyclon.parser.ast.*;

public interface Visitor {
    void visitList(ListExpr list);
    void visitObject(ObjectExpr obj);
    void visitLiteral(LiteralExpr literal);
    void visitIdent(IdentExpr ident);
    void visitAssign(AssignExpr assign);
    void visitBlock(BlockExpr block);
    void visitPair(PairExpr pair);
}
