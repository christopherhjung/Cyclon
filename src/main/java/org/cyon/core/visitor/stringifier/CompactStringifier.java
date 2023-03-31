package org.cyon.core.visitor.stringifier;

import org.cyon.core.parser.ast.*;
import org.cyon.core.visitor.Visitor;

public class CompactStringifier implements Visitor, Stringifier {
    private final StringBuilder sb = new StringBuilder();

    @Override
    public String stringify(Expr expr){
        sb.setLength(0);
        expr.visit(this);
        return sb.toString();
    }

    private void print(String str){
        sb.append(str);
    }

    @Override
    public void visitList(ListExpr list) {
        print("[");
        if(list.getElems().length > 0){
            boolean remaining = false;
            for(var elem : list.getElems()){
                if(remaining){
                    print(",");
                }
                remaining = true;
                elem.visit(this);
            }
        }
        print("]");
    }

    @Override
    public void visitObject(ObjectExpr obj) {
        print("{");
        if(obj.getPairs().length > 0) {
            boolean remaining = false;
            for (var pair : obj.getPairs()) {
                if (remaining) {
                    print(",");
                }
                remaining = true;
                pair.visit(this);
            }
        }
        print("}");
    }

    @Override
    public void visitLiteral(LiteralExpr literal) {
        var value = literal.getValue();
        if(value == null){
            print("null");
        }else if(value instanceof String){
            print("\"");
            print((String)value);
            print("\"");
        }else{
            print(value.toString());
        }
    }

    @Override
    public void visitIdent(IdentExpr ident) {
        print(ident.getKey());
    }

    @Override
    public void visitAssign(AssignExpr assign) {
        assign.getKey().visit(this);
        print("=");
        assign.getValue().visit(this);
    }

    @Override
    public void visitBlock(BlockExpr block) {
        var remaining = false;
        for(var expr : block.getExprs()){
            if(remaining){
                print(";");
            }
            expr.visit(this);
            remaining = true;
        }
    }

    @Override
    public void visitPair(PairExpr pair) {
        pair.getKey().visit(this);
        print(":");
        pair.getValue().visit(this);
    }
}
