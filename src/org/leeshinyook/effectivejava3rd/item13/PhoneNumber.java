package org.leeshinyook.effectivejava3rd.item13;

public class PhoneNumber {

    @Override
    public PhoneNumber clone() {
        try {
            return (PhoneNumber) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
