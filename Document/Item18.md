# 상속보다는 컴포지션을 사용하라



## 상속

상속은 코드를 재사용할 수 있는 좋은 수단이다. 하지만 메서드 호출과는 다르게 캡슐화를 깨드리는 단점이 있다.

상위 클래스의 구현을 바꾸어버리면, 상속한 하위 클래스에도 영향이 있기 때문이다.

~~~java
public class MyHashSet<E> extends HashSet<E> {
  private int addCount = 0; // 추가된 원소의 개수
  
  @Override
  public boolean add(E e) {
    addCount++;
    return super.add(e);
  }
  
  @Override
  public boolean addAll(Collection<? extends E> c) {
    addCount = addCount + c.size();
    return super.addAll(c);
  }
  public int getAddCount() {
    return addCount;
  }
}
MyHashSet<String> mySet = new MyHashSet<>();
mySet.addAll(List.of("이", "신", "육"));
// 출력 : 6
System.out.println(mySet.getAddCount());
~~~

위 코드를 실행하면 3이 나와야하는 것을 기대했다. 하지만, 실제로는 6이 나오는 것을 알 수 있다. 그 이유는 addAll() 메소드가

add메서드를 사용해서 구현되었기 때문에, 값이 중복으로 더해져 6이라는 결과가 나온다.



## 문제를 해결하는 방법

기존의 클래스를 확장하는 대신에 새로운 클래스를 만들고, private 필드로 기존 클래스의 인스턴스를 참조하게 하면 된다.

기존 클래스가 새로운 클래스의 구성요소로 쓰인다는 뜻에서 이러한 설계를 `Composition` 이라 한다.

새로운 클래스의 인스턴스 메서드들은 기존클래스에 대응하는 메서드를 호출해 그 결과를 반환합니다. 이를 전달`Forwarding`

이라고 하며, 새 클래스의 메서드들은 전달 메서드라고 한다.

> 위의 예제를 컴포지션과 전달방식으로 변경해보자.

~~~java
public class MySet<E> extends ForwardingSet<E> {
 	 private int addCount = 0;
  
    public MySet(Set<E> set) {
        super(set);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        addCount = addCount + collection.size();
        return super.addAll(collection);
    }

    public int getAddCount() {
        return addCount;
    }
}
~~~

> 재사용할 수 있는 전달 클래스

~~~java
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> set;
    public ForwardingSet(Set<E> set) { this.set = set; }
    public void clear() { set.clear(); }
    public boolean isEmpty() { return set.isEmpbty(); }
    public boolean add(E e) { return set.add(e); }
    public boolean addAll(Collection<? extends E> c) { return set.addAll(c); }
    // ... 생략
}
~~~

다른 Set 인스턴스를 감싸고 있다는 뜻에서 MySet 같은 클래스를 래퍼 클래스라 하며, 다른 Set에 계측 기능을 덧씌운다는 뜻에

서 `데코레이터 패턴` 이라고 한다. 컴포지션과 전달의 조합은 넓은 의미로 위임`delegation`이라고 한다. 엄밀하게, 래퍼 객체가

내부 객체에 자기 자신의 참조를 넘기는 경우만 위임에 해당한다.



**상속은 반드시 하위 클래스가 상위 클래스의 진짜 하위 타입인 상황에서만 쓰여야 한다.**

예를 들어, 클래스 A를 상속하는 클래스 B를 작성하려 한다면 B가 정말 A인가? 라고 자문해보자. 그렇다라고

확실할 수 없다면 B는 A를 상속해서는 안된다. 대답이 아니다라면, A를 private 인스턴스로 두고, A와는

다른 API를 제공해야 하는 상황이 대다수다. 즉, A가 B의 필수 구성요소가 아니라 구현하는 방법 중 하나일 뿐이다.



## 정리

상속의 취약점을 피하려면 상속 대신 컴포지션과 전달을 사용하자. 특히 래퍼 클래스로 구현할 적당한 인터페이스가 있다면

더욱이 그렇다. 래퍼 클래스는 하위 클래스보다 견고하고 강력하다.

































