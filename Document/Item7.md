# 다 쓴 객체 참조를 해제하라

> c, c++ 처럼 메모리를 직접 관리해야 하는 언어를 넘어서 자바를 사용하게 되면 가비지 컬렉터가
>
> 메모리를 관리해준다. 하지만, 메모리 관리에 대해 신경쓰지 않아도 되는가? 이것도 아니다.

~~~java
public class Stack {
  private Object[] elements;
  private int size = 0;
  private static final int DEFAULT_INITIAL_CAPACITY = 16;
  
  public Stack() {
    elements = new Object[DEFAULT_INITIAL_CAPACITY];
  }
  
  public void push(Object e) {
    ensureCapacity();
    elements[size++] = e;
  }
  
  public Object pop() {
    if(size == 0)
      throw new EmptyStackException();
    return elements[--size]; // 이 코드에 주목하라
  }
  /*
  원소를 위한 공간을 적어도 하나 이상 확보한다.
  배열 크기를 늘려야 할 때마다 대략 두 배씩 늘린다.
  */
  private void ensureCapacity() {
    if(elements.length == size)
      elements = Arrays.copyOf(elements, 2 * size + 1);
  }
}
~~~

스택의 Pop을 계속하여 가지고 있는 모든 원소를 내보내어도, 스택이 차지하고 있는 메모리는 줄어들지 않는다. 왜?

이 스택의 구현체는 필요없는 객체에 대한 레퍼런스를 그대로 가지고 있기 때문이다. 

오래 사용하다보면, 메모리 사용량이 점점 늘어나 결국 성능저하로 이어진다.

~~~java
return elements[--size];
// 객체들의 다 쓴 참조(obsolete reference)를 여전히 가지고 있다.
~~~

~~~java
public Object pop() {
  if(size == 0)
    throw new EmptyStackException();
  Object result = elements[--size];
  element[size] = null; // 다 쓴 참조를 해제한다.
  return result;
}
~~~

Null 처리를 하여 이 참조를 사용하려고하면, NullPointerException을 던지며 종료한다.

그렇다고 필요없는 객체를 볼 때 마다 null로 설정하는 코드를 작성하지는 마라.

##### 객체 참조를 null 처리하는 일은 예외적인 경우여야 한다.

필요없는 객체 레퍼런스 참조를 해제하는 가장 좋은 방법은 유효 범위(Scope) 밖으로 밀어내는 것 이다.

로컬 변수는 그 영역이 넘어가면 정리되기 때문이다.

즉, 변수를 가능한 가장 최소의 스코프로 사용하면 자연스럽게 해결 가능하다. 하지만 위 같은 경우는 명시적인 방법이

필요했다.

##### 메모리를 직접 관리하는 클래스라면 프로그래머는 항상 메모리 누수에 주의해야 한다.



## 캐시

> 캐시를 사용할 때도 메모리 누수를 조심해야 한다.
>
> 객체의 레퍼런스를 캐시에 넣어 놓고 캐시를 비우는 일을 잊기 쉽기 때문이다.

캐시의 키에 대한 레퍼런스가 캐시 밖에서 필요 없어지면 해당 엔트리를 캐시에서 자동으로 비워주는 `WeakHashMap` 을 쓸 수 있다. 또한, 특정 시간이 지나면 캐시값이 의미가 없어지는 경우에 백그라운드 쓰레드를 사용하거나 (`Scheduled ThreadPoolExecutor` 와 같은..) 새로운 엔트리를 추가할 때 부가적인 작업으로 기존 캐시를 비우는 일을 할 것 이다.



## 콜백

클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는다면, 콜백은 계속 쌓이게 된다.

이것 역시 약한 참조 `Weak reference` 로 저장하면 가비지 컬렉터가 즉시 수거해간다.



> 참고.
>
> [WeakReference](https://docs.oracle.com/javase/7/docs/api/java/lang/ref/WeakReference.html)





