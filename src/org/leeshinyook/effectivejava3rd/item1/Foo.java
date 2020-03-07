package org.leeshinyook.effectivejava3rd.item1;

import java.util.EnumSet;

// 생성자 대신 정적 팩터리 메서드를 고려하라
public class Foo {

    String name;

    String address;

    public Foo() {}

    public Foo(String name) { // 기본적인 생성자
        this.name = name;
    }

    public static Foo withName(String name) { // static 팩토리 메서드
        return new Foo(name);
    }

    public static Foo withAddress(String address) { // 동일한 생성자의 개수를 갖더라도 만들 수 있다.
        Foo foo = new Foo();
        foo.address = address;
        return foo;
    }

    public static void main(String[] args) {
        Foo foo =  new Foo("shinyook");
        Foo foo1 = Foo.withName("shinyook");

        EnumSet<Color> colors = EnumSet.allOf(Color.class);


    }


    enum Color {
        RED, BLUE, WHITE;
    }
}
