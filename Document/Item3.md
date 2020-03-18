## Item3 - private 생성자나 열거 타입으로 싱글턴임을 보증하라

> `싱글톤(Singleton)` 이란, 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다.
>
> 예) 
>
> 1. 무상태(Stateless) 객체
> 2. 설계상 유일해야 하는 시스템 컴포넌트를 들 수 있다.

- 문제 : 싱글톤을 사용하는 클라이언트를 테스트하기 어렵다.

- 이유 : 타입을 인터페이스로 정의한 다음 그 인터페이스를 구현해서 만든 싱글턴이 아니라면 싱글턴 인스턴스를

  가짜(Mock) 구현으로 대체할 수 없기 때문이다.

싱글턴을 만드는 방법은 두가지 방법이 있다.

## public static final 필드 구현 방식의 싱글턴

~~~java
public class Singleton1 {
    public static final Singleton1 instance = new Singleton1();
    // 생성자를 private으로 선언하는 방법
    private Singleton1(){};

}
~~~

private 생성자를 호출하여 사용하게 되면, 생성자는 오직 최조 한번만 호출되고 instance는 싱글톤이 된다.

*예외*  

> 리플렉션 : 구체적인 클래스의 타입은 알지 못해도, 그 클래스의 메소드, 타입, 변수들을 접근하게 하는 것.
>
> Constructors를 이용하여 그 클래스의 인스턴스를 생성해 낸다.
>
> AccessibleObject.setAccessible을 사용하여 private 생성자를 호출할 수 있다.

~~~java
// 리플렉션을 막는방법
int count = 0; 
    // 생성자를 private으로 선언하는 방법
private Singleton1(){
	count++;
  if(count > 1) { // 두번이상 호출되었을때 아래처럼 예외를 던져버린다.
    Throw new Error... 
  }
};
~~~

### 

## Static 팩토리 방식의 싱글턴

~~~java
public class Singleton2 {
    private static final Singleton2 instance = new Singleton2();

    private Singleton2() {};

    public static Singleton2 getInstance() {
        return instance;
    }
}
~~~

~~~java
Singleton2 singleton2 = Singleton2.getInstance();
~~~

> getInstance를 호출하여 인스턴스를 받는 방법이다.

- public static 필드가 final이니 절대로 다른 객체를 참조할 수 없다.
- 해당 클래스가 싱글턴임이 API에 명백히 드러난다.
- 메서드 참조를 공급자로 사용할 수 있다.[Supplier문서 참고](https://docs.oracle.com/javase/8/docs/api/)



## 열거 타입 방식의 싱글턴

~~~java
public enum Singleton3 {
    INSTANCE;
}
~~~

- 직렬화, 역직렬화의 문제를 해결할 필요 없고, 리플렉션으로 호출되는 문제도 고민할 필요없다.
- 하지만, 다른 상위 클래스를 상속해야 한다면, 사용할 수 없다.





