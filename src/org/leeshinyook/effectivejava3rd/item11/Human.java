package org.leeshinyook.effectivejava3rd.item11;

public class Human {
    private int age;

    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return "나이는 : " + age;
    }
}
