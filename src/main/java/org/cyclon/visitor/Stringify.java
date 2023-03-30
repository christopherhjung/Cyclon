package org.cyclon.visitor;

import org.cyclon.parser.ast.*;

public class Stringify implements Visitor{
    private static final int WIDTH = 4;
    private StringBuilder sb = new StringBuilder();
    private int depth = 0;
    private boolean init = true;

    private void increase(){
        depth+=WIDTH;
        nl();
    }

    private void decrease(){
        depth-=WIDTH;
        nl();
    }

    private void nl(){
        sb.append("\n");
        init = true;
    }

    private void space(){
        sb.append(" ");
    }

    private void print(String str){
        if(init){
            init = false;
            sb.append(" ".repeat(depth));
        }

        sb.append(str);
    }

    @Override
    public void visitList(ListExpr list) {
        print("[");
        if(list.getElems().length > 0){
            increase();
            boolean remaining = false;
            for(var elem : list.getElems()){
                if(remaining){
                    print(",");
                    nl();
                }
                remaining = true;
                elem.visit(this);
            }
            decrease();
        }
        print("]");
    }

    @Override
    public void visitObject(ObjectExpr obj) {
        print("{");
        if(obj.getPairs().length > 0) {
            increase();
            boolean remaining = false;
            for (var pair : obj.getPairs()) {
                if (remaining) {
                    print(",");
                    nl();
                }
                remaining = true;
                pair.visit(this);
            }
            decrease();
        }
        print("}");
    }

    @Override
    public void visitLiteral(LiteralExpr literal) {
        var value = literal.getValue();
        if(value instanceof String){
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
        print(" = ");
        assign.getValue().visit(this);
    }

    @Override
    public void visitBlock(BlockExpr block) {
        var remaining = false;
        for(var expr : block.getExprs()){
            if(remaining){
                print(";");
                nl();
            }
            expr.visit(this);
            remaining = true;
        }
    }

    @Override
    public void visitPair(PairExpr pair) {
        pair.getKey().visit(this);
        print(" : ");
        pair.getValue().visit(this);
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
