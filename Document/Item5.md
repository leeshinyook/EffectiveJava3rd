# 자원을 직접 명시말고 의존 객체 주입을 사용하라

> 많은 클래스가 하나 이상의 자원에 의존한다.

이 책에서는 맞춤법 검사기라는 예를 들고 있다.

맞춤법 검사기는 사전(Dictionary)에 의존하는데, 이런 클래스를 정적 유틸리티 클래스로 구현한 모습을 볼 수 있다.

- 정적 유틸리티를 잘못 사용 - 유연하지 않고 테스트하기 어렵다.

~~~java
public class SpellChecker {
  private static final Lexicon dictionary = ....;
  
  private SpellChecker() {} // 객체 생성방지
  
  public static boolean isValid(String word) {}
  public static List<String> suggestions(String typo) {}
}
~~~

- 싱글턴을 잘못 사용 - 유연하지 않고 테스트하기 어렵다.

~~~java
public class SpellChecker {
  private final Lexicon dictionary = ....;
  
  private SpellChecker(){}
  public static SpellChecker INSTANCE = new SpellChecker();
  
  public boolean isValid(String word){}
  public List<String> suggestions(String typo) {}
}
~~~

실제 사전은 언어별로 따로 있고, 특수 어휘용 사전이 있을 수 도 있다.

심지어, 테스트하기 위한 사전도 필요하다.

즉, 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.

 대신, 클래스(SpellChecker)가 여러 자원 인스턴스를 지원해야 하며, 클라이언트가 원하는 자원(dictionary)를

사용해야한다. 

#### 인스턴스를 생성할 때, 생성자에 필요한 자원을 넘겨주는 방식

~~~java
public class SpellChecker {
  private final Lexicon dictionary;
  
  public SpellChecker(Lexcion dictionary) {
    this.dictionary = Objects.requireNonNull(dictionary);
  }
  
  public boolean isValid(String word) {}
  public List<String> suggestions(String typo){}
}
~~~



### 참고

의존 객체 주입이 유연성과 테스트 용이성을 개선해주지만, 의존성이 수천개나 되는 큰 프로젝트에서는 스프링, 대거

주스와 같은 DI프레임워크를 사용하면 해결할 수 있다.