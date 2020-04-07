# Comparable을 구현할지 고려하라

이번 Comparable 인터페이스의 유일무이한 메서드 compareTo를 알아본다.

[Comparable](https://docs.oracle.com/javase/8/docs/api/) 문서

Comparable의 compareTo는 다른 메서들과 달리, Object메서드가 아니다. 하지만, 성격 두가지만 제외하면

equals와 동일하다. 무엇이 다른가?

compareTo는 단순 동치성 비교에 더해 순서까지 비교할 수 있으며, 제네릭하다.

Comparable을 구현했다는 것은 그 클래스의 인스턴스들에는 자연적인 순서가 있음을 뜻한다.

~~~java
public class WordList {
    public static void main(String[] args) {
        Set<String> s = new TreeSet<>();
        Collections.addAll(s, args);
        System.out.println(s);
    }
}
~~~

String이 Comparable을 구현한 덕분에, 자동 정렬 검색등의 혜택을 누릴 수 있다.



## compareTo 메서드의 일반 규약

> compareTo의 반환값, 기준이 되는 객체를 x, 비교하는 객체를 y라고 할때 x가 y보다 작은 경우를 -1
>
> 같은 경우를 0, 클 경우 1을 반환한다.

- (x.compareTo(y) < 0) 이라면 (y.compareTo(x) > 0) 이다.

  따라서, 하나가 Exception을 발생시키면, 다른 하나도 예외를 발생시킨다.

- x.compare(y) < 0 이고, y.compareTo(z) < 0 이라면 x.compareTo(z) < 0 이다.

- 크기가 같은 객체끼리는 어떤 객체와 비교하더라도 항상 같아야한다.

- 별도로 equals 메서드와 일관되게 작성하는 것이 좋다.



## 구현

compareTo를 구현 할 때, 타입이 다른 객체가 오는것을 신경쓰지 않아도 된다. 단순히 다른 타입이 온다면 

ClassCastException을 발생시키면 되고, 대부분 그렇게 해결한다.



객체 참조 필드가 하나뿐인 비교자

~~~java
public final class CaseInsensitiveString implements Comparable<CaseInsensitiveString> {
  public int compareTo(CaseInsensitiveString cis) {
    return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
  }
}
~~~

compareTo 메서드는 각 필드가 동치인지를 비교하는 게 아니라 그 순서를 비교한다. 위 코드는 자바가 제공하는 비교자를 사용

하고 있다.

 **compareTo 메서드에서 관계연산자 < 와 > 를 사용하는 이전 방식은 거추장스럽고 오류를 유발하니 이제 추천하지 않는다.**

compareTo 메서드에서 정수 기본 타입 필드를 비교할 때는 정적메서드 Double.compare와 Float.compare를 사용하라

권했다. 그런데 자바7에서 부터는 박싱된 기본 타입 클래스들에 새로 추가된 정적 메서드 compare를 이용하면 되는 것 이다.



기본 타입 필드가 여럿일 때의 비교자

~~~java
public int CompareTo(PhoneNumber pn) {
  int result = Short.compare(areaCode, pn.areaCode); // 가장 중요한 필드
  if(result  == 0) {
    result = Short.compare(prefix, pn.prefix); // 두번째로 중요한 필드
    if(result == 0) {
      result = Short.compare(lineNum, pn.lineNum); // 세번째로 중요한 필드
    }
  }
  return result;
}
~~~



## 정리

순서를 고려해야 하는 값 클래스를 작성한다면, 꼭 Comparable 인터페이스를 구현하여, 그 인스턴스들을

쉽게 정렬하고, 검색하고, 비교 기능을 제공하는 컬렉션과 어우러지게 해야한다.

박싱된 기본 타입 클래스가 제공하는 정적 compare메서드나 Comparator 인터페이가 제공하는

비교자 생성 메서드를 사용하자.