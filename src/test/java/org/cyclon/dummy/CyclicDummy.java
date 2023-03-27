package org.cyclon.dummy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CyclicDummy {
    private CyclicDummy parent;
    private int number;
}
