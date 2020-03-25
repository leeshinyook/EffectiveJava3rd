# 불필요한 객체 생성을 피하라

> 기능적으로 동일한 객체를 매번 생성하기 보다는 객체 하나를 재사용하는 편이 나을 때가 많다.
>
> 재사용하면 빠르고, 세련되다.

### 문자열 객체를 생성할때

~~~java
String s = new String("shin!");
// 절대 이렇게 하지 마라.
~~~

자바의 문자열 String을 new로 생성하게 되면, 항상 새로운 객체를 만들게 되니 쓸데없는 행위가 된다.

실행될 때 마다, String 인스턴스를 새로 만들게 되는 것이다.

~~~java
String s = "shin";
~~~

위 코드는 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다.

문자열 리터럴을 재사용하기 때문에, JVM에 동일한 문자열 리터럴이 존재한다면, 그 리터럴을 재사용한다.



### 정적 팩터리 메서드를 사용해 불필요한 객체 생성을 피하라

`Boolean(String)` 생성자 대신,   `Boolean.valueOf(String)` 팩터리 메서드를 사용하는 것이 좋다.

> 그래서 이 생성자는 자바 9에서 DeprecatedAPI로 지정되었다.

생성자는 반드시 새로운 객체를 만들지만, 정적 팩터리 메서드는 그렇기 않다.



### 생성 비용이 비싼 객체

즉, 무거운 객체를 뜻하는데 이러한 비싼 객체가 반복해서 필요하다면 캐싱하여 재사용하길 권한다.

~~~java
static boolean isRomanNumeral(String s) {
  return s.matches("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$")
}
~~~

문자열이 로마 숫자를 표현하는지 확인하는 코드이다.

이 방식의 문제는 String.matches 메서드를 사용한다는 것에 있다.

~~~java
public boolean matches(String regex) {
        return Pattern.matches(regex, this);
}
// java.lang.String API
~~~

위 내부작동 방식을 확인해보면, 정규표현식용 Pattern 인스턴스를 만들게 되는데, 이 과정에서 매우 큰 비용이 차지하게 된다.

성능을 개선하려면 필요한 정규표현식을 표현하는 Pattern 인스턴스를 클래스 초기화 과정에서 직접 생성해 캐싱해둔다.

그리고, isRomanNumberal 메서드가 호출될 때마다 이 인스턴스를 재사용한다.

~~~java
public class RomanNumber {
  private static final Pattern ROMAN = patten.compile("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
  static boolean isRomanNumberal(String s) {
    return ROMAN.matcher(s).matches();
  }
}
~~~



### 어댑터

> 어탭터는 실제 작업은 뒷단 객체에 위임하고, 자신은 제2의 인터페이스 역할을 해주는 객체다.
>
> 어댑터는 뒷단 객체만 관리하면 된다. 어댑터는 인터페이스를 통해서 뒤에 있는 객체로 연결해주는 객체라 여러개를 만들
>
> 필요가 없다.



~~~java
public class UsingKeySet {
  public static void main(String[] args) {
    Map<String, Integer> menu = new HashMap<>();
    menu.put("Coffee", 5);
    menu.put("Apple", 12);
    
    Set<String> name1 = menu.keySet();
    Set<String> name2 = menu.keySet();
    
    name1.remove("Coffee");
    System.out.println(name1.size()); // 1
    System.out.println(name2.size()); // 1
  }
}
~~~

Map 인터페이스가 제공하는 keySet은 Map이 뒤에 있는 Set 인터페이스의 뷰를 제공한다. keySet을 호출할 때마다

새로운 객체를 생성할 것 같지만, 같은 객체를 리턴하고있다. 밑에서 remove를 통해 Set 객체를 변경하면, 뒤의

Map 객체를 변경하게 되는 것 이다.



### 오토박싱

> 오토박싱은 프로그래머가 기본 타입과 박싱된 기본 타입을 섞어 쓸 때 자동으로 상호 변환해주는 기술이다.

*오토박싱은 기본타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.*

~~~java
private static long sum() {
  Long sum = 0L;
  for(long i = 0; i <= Integer.MAX_VALUE; i++)
    sum += i;
  return sum;
}
~~~

sum 변수 타입을 Long으로 만들었기 때문에, 불필요한 Long인스턴스가 만들어 지고 있다. 

단순히 sum의 타입을 long으로 바꾸어주면 성능차이가 많이 나게 된다.



이번 아이템은 객체 생성은 비싸니 피해야한다는 말을 전하는 것은 아니다. 



## Defensive Copy

~~~java
public final class Period {
  private final Date start;
  private final Date end;
  
  public Period(Date start, Date end) {
    if(start.compareTo(end) > 0) {....}
    this.start = start;
    this.end = end;
  }
  public Date getStart(){
    return start;
  }
  public Date getEnd() {
    return end;
  }
}
~~~

위 방법처럼 코딩하면. 변경이 가능하다.

아래처럼 코드를 변경한다.

~~~java
public Period(Date start, Date end) {
	this.start = new Date(start.getTime()); // defensive copy
  this.end = new Date(end.getTime()); // defensive copy
}
~~~

생성자로 전달되는 변경 가능 객체를 반드시 방어적으로 복사해야한다.

















































