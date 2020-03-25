package org.leeshinyook.effectivejava3rd.item6;

public class StringTest {
    public static void main(String[] args) {
        Boolean true1 = Boolean.valueOf("true");
        Boolean true2 = Boolean.valueOf("true");
        System.out.println(true1 == true2);
        System.out.println(Boolean.TRUE);
//        String name = new String("hi");
//        String name2 = new String("hi");
//
//        System.out.println(name.equals(name2));
//        System.out.println(name == name2);
    }
}
