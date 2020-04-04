# toString을 항상 재정의하라

Object의 기본 toString 메서드가 우리가 작성한 클래스에 적합한 문자열을 반환하는 경우는 거의 없다. 이 메서드는

PhoneNumber@abdbd처럼 단순한 `클래스_이름@16진수로_표시한_해시코드` 를 반환할 뿐 이다.

위같은 반환 값 보다, 754-345-1231과 같은 형태가 훨씬 유익한 정보를 담고 있다.

`toString의 규약 - 모든 하위 클래스에서 이 메서드를 재정의하라!` 라고 한다.

즉, 사람이 읽기 쉬운 형태의 유익한 정보를 반환해야 하는게 이 메서드의 역할인 것 이다.



## toString은 자동으로 호출된다.

toString 메서드는 객체를 println, printf, 문자열 연결 연산자(+), assert 구문에 넘길 때, 혹은 디버거가

객체를 출력할 때 자동으로 호출된다. 즉, 우리가 사용하지 않아도 어디선가 자동으로 호출된다.

toString을 제대로 정의하지 않는다면 쓸모없는 메시지만 로그에 남을 것이다.



~~~java
// human.java
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
~~~

~~~java
// Example.java
package org.leeshinyook.effectivejava3rd.item11;

public class Example {
    public static void main(String[] args) {
        Human human = new Human();
        human.setAge(31);
        System.out.println(human);
    }
}
~~~

> 출력) 나이는 : 31
>
> 보다시피, println()이 실행될때 자동으로 toString메서드가 실행되는걸 알 수 있다.



## 실전에서 toString은 그 객체가 가진 주요 정보를 모두 반환하는게 좋다.

자세하게 보여주는것이 디버깅할때 매우 편하다. 하지만, 객체가 거대하거나 객체의 상태가 문자열로 표현하기에 적합하지 않다면

무리가 있다. 이러한 경우라면 `맨해튼 거주자 전화번호부(총 xxxx)`와 같이 요약정보를 담아주면 된다.



## toString을 구현할 때 반환값의 포맷을 문서화할지 정해야한다.

포맷을 명시하면 그 객체는 표준적이고, 명확하고, 사람이 읽을 수 있게 된다. 또한, 그 값 그대로 입출력에 사용하거나

CSV파일처럼 사람이 읽을 수 있는 데이터 객체로 저장할 수도 있다. 

포맷을 명시하기로 했다면, 명시한 포맷에 맞는 문자열과 객체를 상호 전환할 수 있는 정적팩터리나 생성자를 함께 제공하면 좋다.

단점이라면, 한번 포맷이 명시되면 평생 그 포맷에 엮이게 된다. 

**포맷을 명시하든 아니든 여러분의 의도는 명확히 밝혀야한다.**

```java
/**
* 이 약물에 관한 대략적인 설명을 반환한다.
* 다음은 이 설명의 일반적인 형태이나,
* 상세 형식은 정해지지 않았으며 향후 변경될 수 있다.
*
* "[약물 #9: 유형=사랑, 냄새=테레빈유, 겉모습=먹물]"
*/
@Override
public String toString() {....}
```

포맷 명시 여부와 상관없이 toString이 반환한 값에 포함된 정보를 얻어올수 있는 API를 제공하자.



## toString 재정의가 불필요한 경우

1. 정적 유틸리티 클래스(객체 상태를 가지는 클래스가 아니다.)
2. 대부분의 열거 타입 (기본적으로 제공하는 toString으로 충분하다.)
3. 상위 클래스의 toString으로도 충분한 경우
4. 애너테이션의 사용(Lombok, @ToString / @Autovalue)



## 마무리

모든 구체 클래스에서 Object의 toString을 재정의하자. 상위 클래스에서 이미 알맞게 재정의한 경우는 예외다.

toString을 재정의한 클래스는 사용하기도 즐겁고 그 클래스를 사용한 시스템을 디버깅하기 쉽게 해준다.

toString은 해당 객체에 관한 명확하고 유용한 정보를 읽기 좋은 형태로 반환해야 한다.



















































