package com.siiam.compiler.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ListExpr implements Expr{
    private Expr[] elems;

}
