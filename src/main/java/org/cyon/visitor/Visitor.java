package org.cyon.visitor;

import org.cyon.parser.ast.*;

public interface Visitor {
    void visitLiteral(LiteralExpr literal);
    void visitList(ListExpr list);
    void visitObject(ObjectExpr obj);
    void visitPair(PairExpr pair);
    void visitIdent(IdentExpr ident);
    void visitAssign(AssignExpr assign);
    void visitBlock(BlockExpr block);
}
