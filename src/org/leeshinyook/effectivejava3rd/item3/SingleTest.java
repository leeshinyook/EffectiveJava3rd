package org.leeshinyook.effectivejava3rd.item3;

public class SingleTest {
    public static void main(String[] args) {
        Singleton1 singleton1 = Singleton1.instance;
        Singleton2 singleton2 = Singleton2.getInstance();
        String name = Singleton3.INSTANCE.getName();
        // 리플렉션이라고 한다.
//        try {
//            Singleton1.class.getConstructor(); // reflection으로 강제 생성
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }
}
