# clone 재정의는 주의해서 진행하라

cloneable은 복제해도 되는 클래스임을 명시하는 용도의 믹스인 인터페이스지만, 의도한 목적을 제대로 이루지 못했다.

가장 큰 문제는 clone메서드가 선언된 곳이 cloneable이 아닌 Object이고, 그마저도 protected라는 데 있다.

그래서 Cloneable을 구현하는 것 만으로는 외부 객체에서 clone메서드를 호출할 수 없다.

그럼에도 불구하고, Cloneable 방식이 널리 사용되고 있어, 잘 알아두는 것이 좋다.

그럼 메서드 하나 없는 Cloneable 인터페이스는 대체 무슨일을 하나? 

놀랍게도, 이 인터페이스는 Object의 protected 메서드인 clone의 동작 방식을 결정한다.

Cloneable을 구현한 클래스의 인스턴스에서 clone을 호출하면 그 객체의 필드들을 하나하나 복사한 객체를 반환하며,

그렇지 않은 클래스의 인스턴스에서 호출하면 CloneNotSupportedException을 던진다.

**이 상황은 상당히 이례적으로 사용한 예이니 따라 하지는 말자**

실무에서는 Cloneable을 구현한 클래스는 clone메서드를 public으로 제공하며, 사용자는 복제가 제대로 이뤄지리라 믿고

사용하게 된다. 이를 위해 해당 클래스와 모든 상위 클래스는 복잡하고, 강제할 수 없고, 허술하게 기술된 프로토콜을 지켜야만

하는데, 이 결과 **깨지기 쉽고, 위험하고, 모순적인 매커니즘**이 탄생한다.



## clone 메서드 일반 규약

```java
x.clone() != x;
x.clone().getClass() == x.getClass();
x.clone().equals(x);
```

이를 반드시 만족해야 하는 것은 아니지만, 관례상 clone메서드가 반환하는 객체는 super.clone을 호출해 얻어야한다.

이 관례를 따른다면 위의 식들은 모두 참이다.



## clone 구현

```java
@Override
public PhoneNumber clone() {
  try {
    return (PhoneNumber) super.clone(); // 공변변환
  } catch (CloneNotSupportedException e) {
    throw new AssertionError(); // 일어 난수 없는일.
  }
}
```

모든 필드가 기본 타입이거나 불변 객체를 참조하다면 이 객체는 완벽히 우리가 원하는 상태라 손볼 것 없다.

이 메서드가 동작하게 하려면 Cloneable을 implements해야한다. Object의 clone메서드는 Object를 반환하지만,

PhoneNumber의 clone메서드는 PhoneNumber를 반환하게 했다. 

#### 공변반환

~~~java
public Point clone() {
  Object obj = null;
  try {
    obj = super.clone();
  } catch (CloneNotSupportedException e) {}
  return (Point)obj; 
}
~~~

공변 반환타입을 사용하면 조상의 타입이 아닌, 실제로 반환되는 자손 객체의 타입으로 반환할 수 있어 번거로운 형변환이 줄었다.



### 하지만, 가변 상태를 참조하는 경우는?

```java
class Stack {
  private Object[] elements;
  private int size = 0;
  private static final int DEFAULT_INITIAL_CAPACITY = 16;
  
  public Stack() {
    this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
  }
  
  public void push(Object e) {
    ensureCapacity();
    elements[size++] = e;
  }
  
  public Object pop)() {
    if(size == 0) {
      throw new EmptyStackException();
    }
    Object result = elements[--size];
    elements[size] = null; // 다 쓴 참조 해제
    return result;
  }
  // 가변 상태를 참조하지 않는 클래스용 clone메서드
  @Override
  public Stack clone() {
    try {
      return (Stack) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
  // 가변 상태를 참조하는 클래스용 clone메서드
  @Override
  public Stack Clone() {
    try {
      Stack result = (Stack) super.clone();
      result.elements = elements.clone();
      return result;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
  
  private void ensureCapacity() {
    if(elements.length == size) {
      elements = Arrays.copyOf(elements, 2 * size + 1);
    }
  }
}
```

위 Stack 클래스에서 Object 배열인 elements는 가변 상태이다. 

이에 대해 가변상태를 참조하지 않는 클래스처럼 clone을 단순히 정의하게 되면, clone된 객체에서의 elements에는

null값만 있게 된다. 따라서, 가변상태인 elements에 대해 clone을 추가적으로 해주면 우리가 원하는 복제값들을 만들 수 있다.



### 조금 더 복잡한 가변상태를 참조하는 클래스의 clone

~~~java
public class HashTable implements Cloneable {
  private Entry[] buckets = ...;
  
  private static class Entry {
    final Object key;
    Object value;
    Entry next;
    
    Entry(Object key, Object value, Entry next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }
  }
  .....
}
~~~

현재 HashTable에서 Entry배열은 또다른 Entry를 참조하고 있는 형태이다. 

#### 잘못된 clone

~~~java
@Override
public HashTable clone() {
  try {
    HashTable result = (HashTable) super.clone();
    result.buckets = buckets.clone();
    return result;
  } catch (CloneNotSupportedException e) {
    throw new AssertionError();
  }
}
~~~

복제본은 자신만의 버킷 배열을 갖지만, 이 배열은 원본과 같은 연결 리스트를 참조하여 원본과 복제본 모두 예기치 않게

동작할 가능성이 생긴다. 이를 해결하려면 각 버킷을 구성하는 연결 리스트를 복사해야 한다.



### 연결리스트는 재귀를 통한 방식과 반복자를 통한 방식이 있다.

```java
class HashTable implements Cloneable{
	private Entry[] buckets = ...;

	private static class Entry{
		...
		Entry deepCopy(){
			// 재귀적 deepCopy
			return new Entry(key, value, next == null ? null : next.deepCopy());
			
			// 반복자 deepCopy
			Entry result = new Entry(key, value, next);
            for(Entry p = result; p.next != null; p = p.next){
				p.next = new Entry(p.next.key, p.next.value, p.next.next);
			}
            return result;
		}
	}

	@Override
	public HashTable clone(){
		try{
			HashTable result = (HashTable) super.clone();
			result.buckets = new Entry[buckets.length];
			for(int i = 0; i < buckets.length; i++){
				if(buckets[i] != null){
					result.buckets[i] = buckets[i].deepCopy();
				}
			}
			return result;
		} catch (CloneNotSupportedException e){
			throw new AssertionError();
		}
	}
}
```

위와 같이 연결 리스트를 deepCopy를 해주고 이를 바탕으로 clone을 정의하여 연결 리스트를 참조하는 클래스도 올바르게

복제할 수 있다.

요약하자면, Cloneable을 구현한 스레드 안전 클래스를 작성할 때는 clone 메서드 역시 적절히 동기화해줘야 한다.



### 위처럼 복잡한 경우는 드물다.

하지만, Cloneable을 이미 구현한 클래스를 확장한다면 어쩔 수 없이 clone을 잘 작동하도록 구현해야 한다.

그렇지 않은 상황에서는?

### 복사 생성자와 복사 팩터리

~~~java
// 복사 생성자
public Yum(Yum yum){};

// 복사 팩터리 -> 아이템1, 정적 팩터리를 모방한 것 이다. 참고하자.
public static Yum newInstance(Yum yue) {};
~~~

복사 생성자와 그 변형인 복사 팩터리는 Cloneable/clone 방식보다 나은 면이 많다.

이는 언어 모순적이고, 위험천만한 객체 생성 매커니즘(생성자를 쓰지 않는 방식)을 사용하지 않으며,

엉성하게 문서화된 규약에 기대지 않고, 정상적인 final필드 용법과도 충돌하지 않으며,

불필요한 검사 예외를 던지지 않고, 형변환도 필요치 않다.

게다가, 해당 클래스가 구현한 인터페이스타입의 인스턴스를 인수로 받을 수 있다는 장점도 가지고 있다.



## 마무리

새로운 인터페이스를 만들 때는 절대 Cloneable을 확장해서는 안 되며, 새로운 클래스도 이를 구현해서는 안된다.

final클래스라면 Cloneable을 구현해도 위험이 크지 않지만, 성능 최적화 관점에서 검토한 후 별다른 문제가 없을 때만, 드물게 

혀용해야 한다. 기본적 원칙은 `복제 기능은 생성자와 팩터리를 이용하는 것이 최고이다.`

하지만, 배열만은 Clone메서드 방식이 가장 깔끔하고, 이 규칙의 합당한 예외로 볼 수 있다.





























