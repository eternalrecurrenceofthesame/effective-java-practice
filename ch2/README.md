# effective-java-practice

## item 1 - 생성자 대신 정적 팩터리 메서드를 고려하라

**정적 팩터리 메서드가 생성자보다 좋은 장점 다섯가지**

1. 이름을 가질 수 있다.

2. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다. item 6 참고

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

클라이언트가 제공한 car 로 원하는 Car 클래스를 만들고 이름을 출력 하는 예시

```
public static String create(Supplier<? extends Car> car){
Car car = car.get(); // BMW 생성
String carName = car.getCarName();  // 이름을 반환

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


## item 6 불필요한 객체 생성을 피하라

```
String s = new String("car1") // 따라하지 말 것!!
-> String s = "car1"; //개선판


/** 정적 팩터리 메서드 사용 */
public static String create(Supplier<? extends Car> car){ 
        Car car = car.get(); // car 하위 타입 생성
        String carName = car.getCarName(); 

        return carName;
    }

```
생성자는 호출할 때마다 새로운 객체를 만들지만, 정적 팩터리 메서드는 전혀 그렇지 않다 

생성자 대신 정적 팩터리 메서드를 제공하는 불변 클래스에서는 정적 팩터리 메서드를 사용해 불필요한 

객체 생성을 피할 수 있다. 31p

**??** 32 ~ 34

결론: 기존 객채를 재사용해야 한다면 새로운 객체를 만들지 마라! 

#### +반복적으로 사용되는 인스턴스 캐싱하기
캐시는 데이터나 값을 미리 복사해놓는 임시 저장소를 가리킨다. 캐시는 캐시의 접근 시간에 비해 원래 데이터를 접근

하는 시간이 오래 걸리는 경우나 값을 다시 계산하는 시간을 절약하고 싶은 경우에 사용한다! 즉 미리 복사해 놓으면 

계산이나 접근 시간 없이 더 빠른 속도로 데이터에 접근이 가능함  캐싱은 이런 캐시라는 작업을 하는 행위.


## item 7 다 쓴 객체 참조를 해제하라

#### +메모리 누수와 GC
메모리 누수란? 더이상 사용되지 않는 객체들이 가비지 컬렉터에 의해 회수되지 않고 계속 누적되는 현상을 말한다.

GC 되지 않는 루트 참조 객체 3가지
1. static은 gc 의 대상이 되지 않는다.

static 변수는 클래스가 생성될 때 메모리를 할당 받고 프로그램 종료 시점에 반환되므로 사용하지 않고 있어도 메모리가 할당됨!

2. ?? 지역 변수와 매개 변수에 의해 참조되는 객체와 그 객체로부터 직간접적으로 참조되는 모든 객체

3. ?? JNI 프로그램에 의해 동적으로 만들어지고 제거되는 JNI global 객체 참조

그 외 여러가지 메모리 누수가 발생하는 패턴들
* Integer, Long 같은 래퍼 클래스를 이용하여 무의미한 객체를 생성하는 경우

```
public class Adder {

    public long addIncremental(long l){
        long sum = 0L;
        sum = sum + l;
        return sum;
    }

    public static void main(String[] args) {

        Adder adder = new Adder();
        for(Long i = 0L; i<1000; i ++){
            adder.addIncremental(i);
        }
    }
```
long 대신 Long을 사용함으로써 오토 박싱으로 인해 sum=sum+l; 반복시 새 객체를 생성해서 불필요한 객체가 생성된다.

* 맵에 캐쉬 데이터를 선언하고 해제하지 않는 경우
```
public class Cache {
    private Map<String, String> map = new HashMap<>();

    public void initCache(){
        map.put("Honda", "SuperCurb");
        map.put("AUDI", "A8");
        map.put("Ferrari", "Purosangue");
   }

    public Map<String, String> getCache(){
        return map;
    }

    public void forEachDisplay(){
        for(String key : map.keySet()){
            String val = map.get(key);
            System.out.println(key + ":" + val);
        }
    }

    public static void main(String[] args) {
        Cache cache = new Cache();
        cache.initCache();

        cache.forEachDisplay();
        }
}
```
Map 에는 강력한 참조가 있어서, 내부 객체가 사용되지 않을 때도 GC 대상이 되지 않음  Map 을 더 사용하지 않을 경우

데이터의 메모리를 해제해주자 WeakHashMap 을 사용하면 내부 데이터 초기화 가능. 근데 재사용 못하는게 문제 이러면 캐시가 아닌데?

* 스트림 객체를 사용하고 닫지 않는 경우
```
try{
Connection con = DriverManager.getConnection();
}
catch(exception e){
}
```
커넥션 얻고 커넥션 닫아주지 않아도 메모리 누수 발생함.

근데 @Transactional 어노테이션 사용시 프록시 객체를 주입받아서 쓰기 때문에 문제 없을듯?

* 맵의 키를 사용자 객체로 정의할 때는 equals(),hashcode()를 만들어 주자.
```
public class CustomKey {

    private String name;

    public CustomKey(String name){
        this.name = name;
    }
  public static void main(String[] args) {
        HashMap<CustomKey, String> map = new HashMap<>();

        map.put(new CustomKey("key"), "value");
        String val = map.get(new CustomKey("key"));

        System.out.println(val);
      
    }
}
```
equals hashcode 구현 안하면 값을 찾아오지 못함, 그리고 계속 데이터가 쌓여 메모리 누수가 발생한다.

* 맵의 키를 사용자 객체로 정의 하면서 equals(), hashcode()를 재정의 하였지만 키 값이 불변 데이터가 아니라서 데이터 비교시 계속 변하게 되는 경우
```
public class MutableCustomKey {

    private String name;

    public MutableCustomKey(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableCustomKey that = (MutableCustomKey) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public static void main(String[] args) {
        MutableCustomKey key = new MutableCustomKey("ferrariKey");

        HashMap<MutableCustomKey, String> map = new HashMap<>();
        map.put(key, "ferrari");

        MutableCustomKey refKey = new MutableCustomKey("ferrariKey");
        String val = map.get(refKey);
        System.out.println("value found " + val);

        key.setName("key2");

        String val1 = map.get(refKey);

        System.out.println("Due to MutableKey value not found " + val1);
        
    }

```
키가 변하면 값을 찾을 수 없지만, Map 에서는 참조가 있으므로 메모리 누수가 발생한다.

https://junghyungil.tistory.com/133


#### ++ 다 쓴 객체 참조 해제 (자료 구조)

Stack 클래스는 메모리 누수에 취약하다. 스택은 elements 배열로 저장소 풀을 만들고 원소들을 관리

배열의 활성 영역에 속한 원소들이 사용되고 비활성 영역은 사용되지 않음 문제는 가비지 컬렉터는 이것을 알지 못하고

비활성 영역에서 참조하는 객체도 똑같이 유용한 객체가 된다. 즉 프로그래머 말고는 비활성 영역의 객체가 쓸모없다는 것을 모름.

```
public class Stack {

    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack(){
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e){
        ensureCapacity();
        elements[size++] = e;
    }
     
    public Object pop(){
        if(size == 0)
            throw new EmptyStackException();

        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조를 해제하자.

        return result;
    }

    /**
     * 원소를 위한 공간을 적어도 하나 이상 확보하고
     * 배열 크기를 늘려야 할 때마다 대략 두 배씩 늘린다.
     */
    private void ensureCapacity(){
        if(elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
```

pop 메서드를 사용 후 비활성 영역의 참조를 해제해주자!!
```
public Object pop() {
if(size == 0)
throw new EmptyStackException();

Object result = elements [--size];

elements[size] = null; //다 쓴 참조를 해제!!

return result;
}

```


## ?? item 8 finerlizer 와 cleaner 사용을 피하라

## item 9 try-finally 보다는 try-with-resources 를 사용하라

자바 라이브러리에는 close 메서드를 호출해 직접 닫아줘야 하는 자원이 많음

InputStream, OutputStream, Connection 얻고 닫기 등등 이런 자원 닫기 문제들은 놓치기 쉬워서 예측할 수 없는

성능 문제로 이어질 수 있다.

Java 7 이전에는 try-catch-finally 구문에서 자원을 close 하려면 코드가 많고 지저분했다.

```
FileInputStream is = null;
BufferedInputStream bis = null;
        try {
            is = new FileInputStream("file.txt");
            bis = new BufferedInputStream(is);

            int data = -1;
            while ((data = bis.read()) != -1) {
                System.out.println((char) data);
            }
        }catch(IOException e){
            e.printStackTrace();
        } finally{
            if(is != null) is.close();
            if(bis != null) bis.close();
        }
    }
```
try 에서 예외가 터지면 catch 에서 잡고 finally 에서 다 쓴 자원을 close 해줘야함.

catch 에서 예외를 잡지 않는 경우에는 try 에서 예외가 터지고 close 메서드도 실패할 수 있다. 48p

이렇게 되면 두 번째 예외가 첫 번째 예외를 완전히 삼켜버리게됨.

이러한 문제들은 자바 7 try-with-resourcese 로 해결할 수 있음.


```
try(FileInputStream is = new FileInputStream("file.txt");
     BufferedInputStream bis = new BufferedInputStream(is)){
           int data = -1;
           while((data = bis.read()) != -1){
               System.out.println((char) data);
           }
       }catch(IOException e){
           e.printStackTrace();
       }
```
try 안에서 InputStream 객체를 만듦 여기에 선언한 변수들은 try 안에서 사용 가능하고 코드의 실행 위치가

try 문을 벗어나면 try-with-resources 는 try 안에 선언된 객체의 close() 메서드들을 호출해준다. 그렇기 떄문에

finally 에서 close 를 직접 호출하지 않아도 됨, try-with-resources 를 사용하면 코드가 간결하고 유지보수가 쉬워짐



#### + try-with-resources 사용시 close() 가 호출되는 객체는?

AutoCloseable 을 구현한 객체만 close() 가 호출된다!! BufferedInputStream 의 경우 AutoCloseable 을 구현한 InputStream 을 

상속 받았기 때문에 적용됨.


직접 만든 클래스가 try-with-resources 로 자원을 해제하고 싶으면 AutoCloseable 을 구현하면 된다.

https://codechacha.com/ko/java-try-with-resources/



