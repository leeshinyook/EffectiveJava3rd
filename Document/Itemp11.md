# equals를 재정의하려거든 hashCode도 재정의하라

> equals를 재정의한 클래스 모두에서 hashCode도 재정의해야한다.
>
> 그렇지 않으면, hashCode 일반 규약을 어기게 되어 해당 클래스의 인스턴스를 컬레션의 원소로 사용할 때
>
> 문제를 일으킬 것 이다.

## hashCode의 규약

- Equals 비교에 사용되는 정보가 변경되지 않았다면, 객채의 hashCode 메서드는 몇 번을 호출하여도

  항상 같은 값을 반환해야 한다.

- equals가 두 객체를 같다고 판단하였다면, 두 객체의 hashCode는 똑같은 값을 반환해야한다.

- equals가 두 객체를 다르다고 판단하였더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다.

  단, 다른 값을 반환해야 해시테이블의 성능이 좋아진다.

**HashTable에 대한 이해**

해쉬 테이블은 key에 value를 저장하는 자료구조로 값을 조회할 때, 매우 빠른 속도로 동작한다.

작동 구조를 살펴보면, 나의 전화번호를 저장할 때, `해쉬함수` 를 이용하여 해당 버킷의 index값을 구하여 거기에 저장하게 된다. 

이런 방식을 이용하게 되면, 나(key)에 대한 데이터를 찾을 때 해쉬함수를 한번만 수행하게 되면 buckets의 index를 빠르게 찾아

낼 수 있기 때문에, 저장과 삭제의 속도가 굉장히 빠르다.

**HashTable 충돌**

이 해쉬함수에 대한 근본적인 문제인데, 이 `hashFun(key) / sizeof(array)` 의 값이 중복이 될수 있다는 점이다. 

이를 `해쉬충돌` 이라 부르고 몇가지 해결 방법이 있다.

[충돌 처리 방식에 따른 알고리즘](https://bcho.tistory.com/1072)



## 두번째 조항을 어기게 된다.

> equals가 두 객체를 같다고 판단하였다면, 두 객체의 hashCode는 똑같은 값을 반환해야한다.



~~~java
Map<PhoneNumber, String> m = new HashMap<>();
m.put(new PhoneNumber(707, 876, 5309), "제니");
m.get(new PhoneNumber(707, 876, 5309)); // null을 반환한다.
~~~

HashMap은 해시코드가 다른 엔트리끼리는 동치성 비교를 시도조차 하지 않도록 최적화되어 있기 때문이다.



**최악의 hashcode 구현**

~~~java
@Override
public int hashcode() { return 42;}
~~~

이 코드는 동치인 모든 객체에서 똑같은 해시코드를 반환하니 적법하다. 하지만, 모든 객체에 똑같은 값만 내어주므로

모든 객체가 해시테이블의 버킷 하나에 담겨 마치 연결리스트처럼 동작한다. (동일한 객체를 찾기위해 O(1)이 아닌 연결된

리스트를 전부 탐색해야하므로 O(N)으로 느려지게된다.) 

즉, 좋은 해시 함수란 서로 다른 인스턴스에 대해 다른 해시코드를 반환한다.





## 좋은 Hashcode를 작성하는 방법

~~~java
@Override
public int hashCode() {
  int c = 31;
  // 1. int변수 result를 선언한 후 첫 번째 핵심필드에 대한 hashCode로 초기화한다.
  int result = Integer.hashCode(firstNumber);
  
  // 2. 기본타입 필드라면 Type.hashCode()를 실행한다.
  // Type은 기본타입의 Boxing 클래스이다.
  result = c * result + Integer.hashCode(secondNumber);
  
  // 3. 참조타입이라면 참조타입에 대한 hashcode 함수를 호출 한다.
  // 4. 값이 null이면 0을 더해 준다.
  result = c * result + address == null ? 0 : address.hashCode();
 
  // 5. 필드가 배열이라면 핵심원소를 각각의 필드처럼 다룬다.
  for(String elem : arr) {
    result = c * result + elem == null ? 0 : elem:hashCode();
  }
  // 6. 배열의 모든 원소가 핵심필드이면 Arrays.hashCode를 이용한다.
  result = c * result + Arrays.hashCode(arr);
  
  // 7. result = 31 * result + c 형태로 초기화 하여 result를 리턴한다.
  return result;
}
~~~



### 추가적 자료

[Naver D2 HashMap은 어떻게 동작하는가?](https://d2.naver.com/helloworld/831311)





















