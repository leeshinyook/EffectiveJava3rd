package org.leeshinyook.effectivejava3rd.item13;

public class A implements Cloneable {
    int i;
    int[] arr;
    public A(int i, int[] arr) {
        this.i = i;
        this.arr = arr;
    }

    @Override
    public A clone() {
        try {
            return (A) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
