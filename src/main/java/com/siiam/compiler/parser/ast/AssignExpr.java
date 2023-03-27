package com.siiam.compiler.parser.ast;

import com.siiam.compiler.exception.InterpreterException;
import com.siiam.compiler.parser.Op;
import com.siiam.compiler.scope.Range;
import com.siiam.compiler.scope.Scope;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.IntStream;

@AllArgsConstructor
@Getter
public class InfixExpr implements Expr{
    private Expr lhs;
    private Expr rhs;


    @Override
    public Object eval(Scope scope) {
        var rhsVal = rhs.eval(scope);
        return lhs.assign(scope, rhsVal, false);
    }

    @Override
    public Expr bind(Scope scope, boolean define) {
        var newLhs = lhs.bind(scope, false);
        var newRhs = rhs.bind(scope, false);
        var newExpr = new InfixExpr(newLhs, newRhs, op);

        if(newLhs instanceof LiteralExpr && rhs instanceof LiteralExpr){
            var newValue = newExpr.eval(scope);
            return new LiteralExpr(newValue);
        }

        return newExpr;
    }
}
