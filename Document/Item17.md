# 변경 가능성을 최소화하라

불변 클래스란 그 인스턴스의 내부 값을 수정할 수 없는 클래스이다. 불변 인스턴스에 간직된 정보는 고정되어

객체가 파괴되는 순간까지 절대 달라지지 않는다.



### 불변 클래스를 만드는 규칙

- 객체의 상태를 변경하는 메서드를 제공하지 않는다.
- 클래스를 확장할 수 없도록 한다.
- 모든 필드를 final로 선언한다.
- 모든 필드를 private으로 선언한다.
- 자신 외에는 내부의 컴포넌트에 접근할 수 없도록 한다.



불변 객체는 근본적으로 스레드 안전하여, 따로 동기화할 필요 없다. 즉, 안심하고 사용할 수 있다.

불변 객체는 자유롭게 공유할 수 있음은 물론, 불변 객체끼리는 내부 데이터를 공유할 수 있다. 객체를 만들 때

다른 불변 객체들을 구성요소로 사용하면 이점이 많다. 

~~~java
public final class Complex {
  private final double realNumber;
  private final double imaginaryNumber;
  
  public Complex(double realNumber, double imaginaryNumber) {
    this.realNumber = realNumber;
    this.imaginaryNumber = imaginaryNumber;
  }
  // 덧셈 연산
  public Complex plus(Complex c) {
    return new Complex(realNumber + c.realNumber, imaginaryNumber + c.imaginaryNumber);
  }
}
~~~



### 단점

값이 다르면 반드시 독립된 객체로 만들어야 한다는 것이다.

그래서 값의 가짓수가 많다면 이들을 모두 만드는 데 큰 비용을 치러야 한다.



## 불변 클래스를 만드는 또 다른 방법

> 정적 팩터리를 이용하여 불변 클래스를 만들 수 있다.

~~~java
public class Complex {
  private final double rn;
  private final double in;
  
  // 생성자가 private이다.
  private Complex(double rn, double in) {
    this.rn = rn;
    this.in = in;
  }
  public static Complex valueOf(double rn, double in) {
    return new Complex(rn, in);
  }
}
~~~

 패키지 빠깥의 클라이언트에서 바라본 이 불변 객체는 사실상 final이다. public이나 protected 생성자가

없으니, 다른 패키지에서는 이 클래스를 확장하는 게 불가능하기 때문이다. 정적 팩터리 방식의

다수의 구현 클래스를 활용한 유연성을 제공하고, 이에 더해 다음 릴리스에서 객체 캐싱 기능을 추가해 성능을 끌어올릴

수도 있다.



## 정리

게터가 있다고 무조건 세터를 만들어야 하는 것은 아니다. 꼭 필요한 경우가 아니라면 클래스는 불변이여야 한다.

불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄이는 것이 좋다.

**다른 합당한 이유가 없다면 불변을 유지하기 위해서 모든 멤버 필드는 private final 이어야 한다.**

















