# public클래스에서는 public필드가 아닌 접근자 메서드를 사용하라

퇴보한 클래스 작성법

~~~java
class Point {
  public double x;
  public double y;
}
~~~

이런 클래스는 데이터 필드에 직접 접근할 수 있으니 캡슐화의 이점을 제공하지 못한다.



- 접근자와 변경자 메서드를 활용해 데이터를 캡슐화한다.

~~~java
class Point {
  private double x;
  private double y;
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
  public double getX() {return x;}
  public double getY() {return y;}
  
  public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
}
~~~

public 클래스에서라면 이 방식이 훨씬 맞다. 패키지 바깥에서 접근할 수 있는 클래스라면 접근자를 제공함으로써

클래스 내부 표현 방식을 언제든 바꿀수 있는 유연성을 얻을 수 있다.



## 정리

public 클래스는 절대 가변 필드를 직접 노출해서는 안 된다. 불변 필드라면 노출해도 덜 위험하지만 완전히 안심할 수 없다.

하지만, package-private 클래스나 private 중첩 클래스에서는 종종 필드를 노출하는 편이 나을 때도 있다.

