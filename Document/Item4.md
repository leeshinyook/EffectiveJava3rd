# 인스턴스화를 막으려거든 private 생성자를 사용하라

> java.lang.math 와 java.util.Arrays 처럼 단순히 정적 메서드와 정적 필드만을 담은 클래스를 만들어야 할 때가 있다.
>
> 무엇인가 유틸리티 클래스를 만드는 것 이다.

- 예를 들어 Java.util.Arrays의 코드를 한번 살펴보자.

```java
public class Arrays {

    /**
     * The minimum array length below which a parallel sorting
     * algorithm will not further partition the sorting task. Using
     * smaller sizes typically results in memory contention across
     * tasks that makes parallel speedups unlikely.
     */
    private static final int MIN_ARRAY_SORT_GRAN = 1 << 13;

    // Suppresses default constructor, ensuring non-instantiability.
    private Arrays() {}

    /**
```

> 찾아보고 나니, 이 책의 저자가 위 코드의 작성자였다. 책에서 설명하고자 하는 내용이 그대로 담겨 있다.

보통은 유틸클래스를 만들 때, 추상 클래스를 선언해서 static 메서드를 이용해 그 기능을 구현한다.

하지만, 추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다. 하위 클래스를 만들어 인스턴스화 할 수 있기 때문이다.

그리고 아무런 생성자를 작성하지 않으면, 컴파일러가 스스로 인자가 없는 public 생성자를 만들어 버리기 때문에, 

문제의 여지가 될 수 있다.

위의 해결책으로 `인스턴스를 만들 수 없는 유틸리티 클래스` 를  제시한다.

~~~java
public class UtilityClass {
  // 기본 생성자가 만들어지는 걸 막는다. => 즉, 인스턴스화를 방지한다.
  // Suppresses default constructor, ensuring non-instantiability.
  private Utility() {}
}
~~~

컴파일러가 기본 생성자를 만드는 경우는 오직 명시된 생성장가 없을 때 뿐이다.

private 생성자를 만들어 버린다면, 이 문제를 해결 할 수 있다.

명시적 생성자가 private이니 클래스 바깥에서 접근할 수 없다. 즉, 위 두가지 문제를 해결한 것 이다.





