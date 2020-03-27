# Finalizer와 Cleaner는 피하라

> 소멸자에 관해

Finalizer는 예측할 수 없고, 상황에 따라 위험할 수 있어, 일반적으로 불필요하다.

이상하게 동작하고, 성능도 안좋아지고, 유용하게 이를 쓰는 경우는 드물다. 자바9에서는 Finalizer를 Deprecated API로 지정

하였고, Cleaner를 그 대안으로 제시한다.



### C++의 Destructor와는 다른 것.

C++에서 파괴자는 특정 객체와 관련된 자원을 회수하는 보편적인 방법이다. 자바에서는 접근할 수 없는 객체를

회수하는 역할을 GC가 담당하며, 프로그래머는 어떠한 소멸도 할 필요가 없다. C++에서 파괴자는 자바에서는 

Try-with-resources, try-finally를 사용하여 해결한다.



## 단점 1

언제 실행될지 알 수 없다. 객체에 접근을 할 수 없게 된 후 finalizer나 cleaner는 언제 실행될지 프로그래머 입장에서

예측할 수 없다.  즉, 제때 실행되어야 하는 작업은 절대 할 수 없다. 



## 단점 2

인스턴스의 반납을 지연시킬 수 있다. Finalizer 쓰레드는 우선순위가 낮아, 언제 실행될 지 모른다.

따라서 Finalizer 안에 어떠한 작업이 있고 그 작업이 처리되지 못하고 있다면, 그 인스턴스는 GC가 되지 않고, 계속해서

쌓이다가 결국에는 OutOfMemoryException이 발생 할 수 있다.

Cleaner는 별도의 스레드를 사용한다. 위 부분에 대한 문제는 괜찮겠지만, 그렇다고 Cleaner를 사용하게 되면 언제

처리될지 수행시간에 대해서는 확신을 얻을 수 없다.



## 단점 3

상태를 영구적으로 수정하는 작업에서는 절대 finalizer나 cleaner에 의존해서는 안된다. 위에서 언급한 수행시간이

불분명할 뿐 아니라, 수행 여부조차 보장하지 않는다. 



## 단점 4

심각한 성능 문제도 동반한다. GC가 수행하는 것 보다 수행시간에서의 50배 가까운 시간이 걸리게 된다.



## Cleaner 사용하기 (자바9 이상)

~~~java
public class CleanerSample implements AutoCloseable {
  
  privte static final Cleaner CLEANER = Cleaner.create()
  
  private final CleanerRunner cleanerRunner;
  
  private final Cleaner.Cleanable cleanable; // 이걸로 청소를 함.
  
  public CleanerSample() {
    cleanerRunner = new CleanerRunner();
    cleanable = CLEANER.register(this, cleanerRunner);
  }
  
  @Override
  public void close() {
    cleanable.clean();
  }
  
  public void doSomething() {
    System.out.println("do it!");
  }
  
  private static class CleanerRunner implements Runnable {
    // TODO
    
    @Override
    public void run() {
      // 여기서 해결한다.
      System.out.println("close");
    }
  }
}
~~~

- Cleaner 쓰레드를 만들 클래스는 반드시 static 클래스여야 한다. 