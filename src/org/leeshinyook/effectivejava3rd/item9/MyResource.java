package org.leeshinyook.effectivejava3rd.item9;

public class MyResource implements AutoCloseable {

    public void doSomeThing() {
        System.out.println("Do Something!");
        throw new FirstError();
    }

    @Override
    public void close() throws Exception {
        System.out.println("Close My Resource");
        throw new SecondError();
    }
}
