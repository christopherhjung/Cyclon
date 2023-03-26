package com.siiam.compiler.parser.ast;

import com.siiam.compiler.exception.InterpreterException;
import com.siiam.compiler.parser.controlflow.ReturnException;
import com.siiam.compiler.scope.NestedScope;
import com.siiam.compiler.scope.Scope;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LambdaExpr implements Expr{
    private Expr[] params;
    private Expr body;

    @Override
    public Object call(Scope scope, Object[] args) {
        if(args.length != params.length){
            throw new InterpreterException("Mismatch arg length");
        }

        var idx = 0;
        scope = NestedScope.wrapMutual(scope);
        for(var param : params){
            param.assign(scope, args[idx++], false);
        }

        try {
            return body.eval(scope);
        }catch (ReturnException e){
            return e.getReturnValue();
        }
    }

    @Override
    public Object eval(Scope scope) {
        return new ScopedExpr(scope, this);
    }
}
