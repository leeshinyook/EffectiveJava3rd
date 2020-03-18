package org.leeshinyook.effectivejava3rd.item3;

public class Singleton1 {

    public static final Singleton1 instance = new Singleton1();

    int count;
    // 생성자를 private으로 선언하는 방법
    private Singleton1(){

    };

}
