package org.leeshinyook.effectivejava3rd.item10;

public class Main {
    public static void main(String[] args) {
        CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
        String str = "Polish";

        System.out.println(cis.equals(str)); // true
        System.out.println(str.equals(cis)); // false
    }
}
