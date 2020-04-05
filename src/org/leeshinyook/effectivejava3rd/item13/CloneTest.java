package org.leeshinyook.effectivejava3rd.item13;

import com.sun.javaws.IconUtil;

public class CloneTest {
    public static void main(String[] args) {
        A a1 = new A(10, new int[]{10, 20, 30});
        A a2 = a1.clone();

        for(int e : a2.arr) {
            System.out.println(e);
        }
        System.out.println(a2.i);
        System.out.println(a1.hashCode());
        System.out.println(a2.hashCode());

        // 둘의 해쉬코드가 다르다. 즉, 얕은 복사를 뜻한다.

    }
}
