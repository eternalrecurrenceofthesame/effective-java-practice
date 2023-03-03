# effective-java-practice

## item 1 - 생성자 대신 정적 팩터리 메서드를 고려하라

**정적 팩터리 메서드가 생성자보다 좋은 장점 다섯가지**
- - -
1. *이름을 가질 수 있다.*
>생성자 만으로는 반환될 객체의 특성을 제대로 설명하지 못한다.
>
>한 클래스에 시그니처가 같은 생성자가 여러 개 필요한 경우,
>정적 팩터리 메서드로 바꾸고 각각의 차이를 잘 드러내는 이름을 지어주자.

2. *호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.*
> ?

3. *반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.*

4. *입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.*

5. *정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.*


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

그리고 static 메서드로 같은 인스턴스를 호출할 수 있다.

**??** 싱글턴임을 보장해주는 readResolve 메서드를 이용해서 직렬화된 인스턴스를 역 직렬화 

할 때마다 새로운 인스턴스가 만들어지는 것을 막아둔다. item 89

**??** 열거 타입 싱글톤 25p

