# effective-java-practice

## item 1 - 생성자 대신 정적 팩터리 메서드를 고려하라

**정적 팩터리 메서드가 생성자보다 좋은 장점 다섯가지**

1. 이름을 가질 수 있다.

2. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.
 ??

3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

4. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

5. 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.


## item 2 - 생성자에 매개변수가 많다면 빌더를 고려하라

생성자 매개 변수 개수가 많아지면 객체를 생성할 때 각 값의 의미가 헷갈리고

매개 변수가 몇 개인지도 주의해야한다.

`ex) public NutritionFacts(int servingSize, int sevings, int calories, int fat, int sodium ...) `

이런 경우 필수 매개변수만으로 생성자를 호출해 빌더 객체를 만들고 빌더 객체가 제공하는 세터 메서드들로 원하는 

선택 매개변수를 설정한 후 빌더 객체가 제공하는 build 메서드를 호출해 객체를 만들어서 반환하면 된다.

* **NutritionFacts.class Pizza.class, NyPizza.class**

```
Pizza 를 추상 클래스 빌더로 만들고 구체 클래스인 NyPizza 가 Pizza 를 상속받는다.

이렇게 함으로써 NyPizza 를 만들 때 필수 요소, Pizza 를 만들 때 필수 요소를 나눌 수 있고

정적 팩터리 메서드를 이용해서 의미가 명확해진다. 

또한 각 계층마다 필수 매개변수, 선택 매개변수를 나눠서 설계가 가능하다. 
```


## item 3 - private 생성자나 열거 타입으로 싱글턴임을 보증하라
```
public class Singleton{
private static final Singleton INSTANCE = new Singleton();
private Singlton(){}
public static Singleton getInstance() {return INSTANCE;}

private Object readResolve(){return INSTANCE;}

public void method() {}
```

private static final 필드를 이용해서 객체를 생성하지 않고 사용할 수 있으며

생성자를 private 로 막아서 다른 곳에서 이 객체를 생성할 수 없게 막아준다.

그리고 static 메서드로 싱글톤 인스턴스를 호출할 수 있다.

**??** 싱글톤 임을 보장해주는 readResolve 메서드를 이용해서 직렬화된 인스턴스를 역 직렬화 

할 때마다 새로운 인스턴스가 만들어지는 것을 막아둔다. item 89

**??** 열거 타입 싱글톤 25p

## item 4 인스턴스화를 막으려거든 private 생성자를 사용하라! 26p

생성자를 만들지 않으면 컴파일러는 자동으로 기본 생성자를 만듦

이때 사용자는 이 생성자가 자동 생성된 것인지 구분할 수없음.

private 생성자를 추가해서 인스턴스화를 막고 하위 클래스가 상위 클래스의 생성자에 접근 하는 것을 막을 수 있다.

private 으로 생성자를 만들면 생성자는 존재하는데 호출할 수없어서 직관적이지 않을 수 있다.

이런 경우 적절한 주석을 달아주도록 하자.

```
// 기본 생성자가 만들어 지는 것을 막는다(인스턴스화 방지)
private UtilityClass(){
 throw new AssertionError();
}
```

## item 5 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

```
public class SpellChecker {
private final Lexicon dictionary;

public SpellChecker(Lexicon dictionary){
this.dictionary = Objects.requireNonNull(dictionary);
}
public boolean isValid(String word) {...}
public List<String> suggestions(String typo) {...}
```

사용하는 자원에 따라 동작이 달라지는 클래스는 자원을 직접 명시하지 않고 의존 객체 주입을 사용하라

맞춤법 검사기에는 여러 가지 언어가 사용될 수 있고 여러 가지 사전이 사용된다.

이때 싱글톤이나 정적 유틸리티 클래스로 만들어버리면 사전 하나에 하나의 클래스밖에 대응하지 못한다. 28p

이 패턴의 변형으로 생성자에 자원 팩터리를 넘겨주는 방식이 있다.

#### + 팩터리란 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 의미한다.

Supplier<T> 인터페이스가 팩터리를 표현한 완벽한 예시이다.
 
팩터리의 타입 매개변수를 제한하고 클라이언트는 자신이 명시한 타입(Car)의
 
하위 타입이라면 무엇이든 생성할 수 있는 팩터리를 넘길 수 있다.

클라이언트가 제공한 carFactory 로 원하는 Car 클래스를 만들고 이름을 출력 하는 예시

```
?? public static String create(Supplier<? extends Car> carFactory){
Car car = carFactory.get();
String carName = car.getCarName();

return carName;
}

...

String carName = CarFactory.create(BMWCar::new);
System.out.println(carName);
```



#### ++ 팩토리 메서드 패턴
팩터리 메서드 패턴이란 메서드의 호출에 대한 반환값으로 객체를 생산하는 디자인 패턴이다. https://huisam.tistory.com/entry/FactoryMethod


사용 이유
* 매개변수로 객체를 생성할 때 어떤 객체인지 예상 불가능할 때
* 공통 분모를 가지는 부모클래스(car) 혹은 추상화된 인터페이스를 바탕으로 구성할 때
* 복잡한 객체를 인스턴스화 하는 논리적인 로직을 따로 분리할 때

Car.class, CarFactory.class 참고

