package org.cyon.core;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Key {
    private Object obj;

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Key)) return false;
        return obj == ((Key)other).obj;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(obj);
    }
}
