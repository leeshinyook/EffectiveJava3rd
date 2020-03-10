# Item2 - 생정자에 매개변수가 많다면 빌더를 고려하라

> 정적 팩터리와 생성자에는 똑같은 제약이 하나 있다. 선택적 매개변수가 많을 때 적절히 대응하기 어렵다는 점이다.



## 생성자

~~~java
public NutritionFacts(int servingSize, int sodium, int carbohydrate, int servings) {
        this.servingSize = servingSize;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
        this.servings = servings;
    }
public static void main(String[] args) {
        // 생성자 - 보기 어렵다. 뭐가 들어있는지 모른다.
        NutritionFacts nutritionFacts = new NutritionFacts(1, 5, 2, 3);
    }
~~~

> 생성자를 이용하면, 위 같은 방식으로 인스턴스를 만들 수 있다.

- 필요없는 매개변수를 넘겨야하는 경우, 보통 0을 넘기게 된다. 물론 이 방법이 동작은 하나, 이 매개변수가 무엇을 가리키는지

  의도를 파악하기에 어려움이 있다.

  

## 자바빈

~~~java
 // 2. 자바빈 => 뭘 세팅하는 지 알 수 있다.
public void setCarbohydrate(int carbohydrate) {
  this.carbohydrate = carbohydrate;
}

public void setServings(int servings) {
  this.servings = servings;
}

public void setServingSize(int servingSize) {
  this.servingSize = servingSize;
}

public void setSodium(int sodium) {
  this.sodium = sodium;
}
public NutritionFacts() {};

public static void main(String[] args) {
        // 자바빈 - 세터메소드를 호출해야하는 것이 많아진다.
        NutritionFacts nutritionFacts1 = new NutritionFacts();
        nutritionFacts1.setCarbohydrate(1); // 가독성은 좋다.
        nutritionFacts1.setServings(2);
        nutritionFacts1.setSodium(10);
}
~~~

> 자바빈은 아무런 매개변수를 받지 않는 생성자를 만들고 이를 통해 비어 있는 인스턴스를 생성한다. 
>
> 그 이후, setter메소드를 통해 필요한 필드를 설정한다.

- 자바빈의 단점은 하나의 인스턴스를 만들기 까지, 많은 메소드 호출을 하게된다. 이는 스레드를 여러 개 사용할 경우

  안정적이지 않은 상태로 사용될 여지가 있다. 

- 위 단점은 객체에 `Locking` 을 걸어버리는 프리징을 하여 처리해야하는 문제가 있고, 프리징 또한 자바에서 이미 만들어진

  방법이 아니라, 커스텀으로 제작하여야한다.



## 빌더

> 생성자와 자바빈의 장점만을 모았다.

`신축적인 (필수적 매개변수와 부가적 매개변수 조합으로 여러 생성자를 만들 수 있다.)` 생성자의 안정성과 자바빈에서 얻을 수

있던, 가독성`인스턴스가 어떠한 필드를 어떠한 값으로 가졌는지 알 수 있다.` 모두 취하는 패턴이 빌더패턴이다.



각 계층의 클래스에 관련 빌더를 멤버로 정의하자. 추상 클래스는 추상 빌더를 , 구체 클래스는 구체 빌더를 갖게 한다.

예를 들어, 피자의 다양한 종류를 표현하는 계층구조, 루트에 놓인 추상 클래스를 보자.

~~~java
public abstract class Pizza {
    public enum Topping {
        HAM, MUSHROOM, ONION
    }

    final EnumSet<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> { // 재귀적인 타입 매개변수
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class); // 비어있는 enumSet

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }
        abstract Pizza build(); // Convariant 리턴 타입을 위한 준비 작업

        protected abstract T self(); // self-type 개념을 사용해서 메소드 체이닝이 가능케함.
    }
    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }
}

~~~

추상 빌더를 가지고 있는 추상 클래스를 만들고, 하위 클래스에서는 추상 클래스를 상속받으며 각 하위 클래스용 빌더도

추상 빌더를 상속받아 만들 수 있다.

~~~java
public class NyPizza extends Pizza {

    public enum Size {
        SMALL, MEDIUM, LARGE
    }
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }
}

~~~

~~~java
public class PizzaClient {

    public static void main(String[] args) {
        NyPizza nyPizza = new NyPizza.Builder(NyPizza.Size.MEDIUM)
                .addTopping(Pizza.Topping.HAM)
                .addTopping(Pizza.Topping.ONION)
                .build();
    }
}
~~~

추상 빌더는 재귀적인 매개변수를 사용하고 self라는 메소드를 통해 self-type개념을 모방한다. 하위 클래스에서

build메소드의 리턴타입으로 해당 하위 클래스 타입을 리턴하는 공변반환타이핑`Convariant return typing` 을 사용하면

타입 캐스팅(형변환)의 필요가 없어진다.

하지만, 빌더를 쓰고자 생성자패턴보다 코드가 장황하다. 따라서 매개변수가 4개이상은 되어야 또는 앞으로 늘어날 가능성이 

있다면 이 빌더로 시작하는 것이 좋다.























