package org.leeshinyook.effectivejava3rd.item10;

import java.util.Objects;

public final class CaseInsensitiveString {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

//    @Override
//    public boolean equals(Object o) {
//        if(o instanceof CaseInsensitiveString) {
//            return s.equalsIgnoreCase(((CaseInsensitiveString)o).s);
//        }
//
//        if(o instanceof String) {
//            return s.equalsIgnoreCase((String) o);
//        }
//        return false;
//    }

    // 이게 맞다.
    @Override
    public boolean equals(Object o) {
        return o instanceof CaseInsensitiveString && ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }

}
