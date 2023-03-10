## item 10 equals 는 일반 규약을 지켜 재정의 하라

> equals 메서드는 동치 관계를 구현하며, 다음 5 가지를 만족해야 한다.
>
> 동치 관계란?
>
> 집합을 서로 같은 원소들로 이뤄진 부분집합으로 나누는 연산을 의미한다.
> 부분 집합을 동치류라 하고 equals 메서드가 의미 있으려면 모든 원소가 같은 동치류에 속한 어떤
> 원소와도 서로 교환할 수 있어야 한다.
>
>* 반사성
>* 대칭성
>* 추이성
>* 일관성
>* null-아님

* **반사성이란?**

객체는 자기 자신과 같아야 한다는 뜻. 이 요건을 어긴 클래스의 인스턴스를 컬렉션에 넣은 다음

contains 메서드를 호출하면 인스턴스가 없다고 답할 것이다. (이걸 어떻게 어김?)

* **대칭성이란?**

대칭성은 두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다는 뜻이다. 즉 두 객체를 비교했을 때 단순히 값이 같다고 해서

true 를 반환하는 것이 아닌 동치 여부에 따라서 ?? 교집합에 묶인 같은 값을 true 로 반환해야 한다는 의미

x,y 에 대해 x.equals(y) 가 ture 면 y.equals(x) 도 true 가 돼야 한다!

```
public final class CaseInsensitiveString {

    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = s;
    }

    // 대칭성 위배
    @Override
    public boolean equals(Object o) {
        if(o instanceof  CaseInsensitiveString)
            return s.equalsIgnoreCase(
                    ((CaseInsensitiveString) o).s);

        if(o instanceof String)
            return s.equalsIgnoreCase((String) o);

        return false;
    }
    public static void main(String[] args) {
        CaseInsensitiveString cis = new CaseInsensitiveString("Ferrari");
        String s = "Ferrari";

        cis.equals(s);
        s.equals(cis);
    }
```
cis.equals(s) 는 true 를 반환하지만 s.equals(cis)는 false 임 cis 의 equals 는 일반 String 을 알고 있지만, 문제는

String s 의 equals 는 Cis 의 존재를 모르는 데 있음 이 문제를 해결하려면 Cis 의 equals 를 String 과 연동하면 안 됨.

```
@Override public boolean equals(Object o){
return o instanceof CaseInsensitiveString && ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
```
수정 코드 둘 다 false 



* **추이성이란?**

첫 번째 객체와 두 번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다면 첫 번째 객체도 세 번째 객체와 같아야 한다는 뜻이다.

```
class Point

 private final int x;
 private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
@Override
    public boolean equals(Object o) {
        if(!(o instanceof Point))
            return false;

        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
    
    
class ColorPoint extends Point

 private final Color color;

 public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

@Override
  public boolean equals(Object o) {
        if(!(o instanceof ColorPoint))
            return false;
        return super.equals(o) && ((ColorPoint) o).color == color; // 대칭성 위배
    }


p = new Point(1,2);
cp = new ColorPoint(1,2,RED);

p.equals(cp); // true
cp.equals(p); // false
```
이 경우는 **대칭성**을 위배하게 된다. 포인트의 경우에는 타입이 같기 때문에 true 를 반환하지만

컬러 포인트는 매개변수의 클래스 종류가 다르기 때문에 false 를 반환해서 대칭성 위반 !


```
@Override
    public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;

        //o 가 일반 Point 면 색상을 무시하고 비교한다.
        if(!(o instanceof ColorPoint))
            return o.equals(this);

        //o 가 ColorPoint 면 색상까지 비교한다.
        return super.equals(o) && ((ColorPoint) o).color == color;
        }
        

        ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
        Point p2 = new Point(1, 2);
        ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);
        
        p1.equals(p2); // true
        p2.equals(p3); // true
        p3.equals(p1); // false
```
**추이성**을 위배한 코드! p1 p2, p2 p3 비교에서는 색상을 무시하지만 p1 p3 는 색상까지 고려하기 떄문

또한 이 방식은 무한 재귀에 빠질 위험도 있음. Point 의 또 다른 하위 클래스 SmellPoint 생성한다고 했을 때 

이 같은 방식으로 비교한다면 *if(!(o instanceof ColorPoint))* 이 로직에서 일반 포인트로 인식해서 

계속 상대 this 를 호출하게 된다. 58p

그렇다면 구체 클래스를 확장해 새로운 값(Color)을 추가하면서 equals 규약(**추이성**)을 만족시킬 방법은? - **존재하지 않는다!**

객체 지향적 추상화의 이점을 포기하지 않는한 존재하지 않음..

구체 클래스의 하위 클래스에서 값을 추가할 방법은 없지만 괜찮은? 우회 방법은 있음 "상속 대신 컴포지션을 사용하라"

아이템 18 의 조언을 따르면 됨.
```
조언
public class ColorPoint{

    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        point = new Point(x,y);
        this.color = Objects.requireNonNull(color);
    }

    /**
     * 이 ColorPoint 의 Point 뷰를 반환.
     * */
    public Point asPoint(){
        return this.point;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;


        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);

    }
```
상속하는 대신 포인트 객체를 만들어서 값을 받고 equals 비교할 때 point 따로 color 값 따로 비교하는 것

구체 클래스를 확장해 값을 추가한 클래스가 됨. 근데 이렇게 설계하지는 마라고함.. 60p


#### + 리스코프 치환 원칙과 위배 사례. 59p

리스코프 치환 원칙이란 단순한 컴파일 성공을 넘어서 다형성에서의 하위 클래스는 인터페이스의 규약을 지켜야 한다는 것

즉 인터페이스가 의도한 대로 객체를 생성하라는 의미.

상속관계의 경우에는 부모 객체를 호출하는 동작에서 자식 객체가 부모 객체의 동작을 완벽히 대체할 수 있다는 원칙을 말한다.


<위반 사례>
```
// 직사각형
@Getter
@Setter
public class Rectangle {  
    protected int width;
    protected int height;

    // 넓이 반환
    public int getArea(){
        return width * height;
    }
}


// 정사각형
@Getter
@Setter
public class Square extends Rectangle{

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(getWidth());
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        super.setWidth(getHeight());
    }
}

Rectangle rectangle = new Rectangle();
        rectangle.setWidth(10);
        rectangle.setHeight(5);
        
        50
        
Rectangle rectangle2 = new Square();

        rectangle2.setWidth(10);
        rectangle2.setHeight(5);
        
       25  
```
언뜻 보면 직사각형과 정사각형은 서로 비슷한 카테고리에 묶여 있는 것 처럼 보인다.

직사각형을 상속받은 정사각형을 구현하면 자식 객체인 정사각형도 직사각형의 역할을 할 수 있을 것 같지만 그렇지 않다. 

직사각형과 다르게 정사각형은 가로 세로의 길이가 모두 같아야 하기 때문에 서로 다른 값을 가질 수 없음.

즉 상위 클래스가 의도한 역할을 대체할 수 없어서 리스코프 치환 원칙에 위배된다.

<리스코프 치환 원칙 준수>
```
// 사각형
@Getter
@Setter
public class Shape {

    protected int width;
    protected int height;

    public int getArea(){
        return width * height;
    }
}


// 정사각형
@Getter
@Setter
public class Square extends Shape{

    public Square(int length){
        super.setWidth(length);
        super.setHeight(length);
    }
}

// 직 사각형
@Getter
@Setter
public class Rectangle extends Shape {

    public Rectangle(int width, int height) {

       super.setWidth(width);
       super.setHeight(height);
    }
}

```
이런 경우 부모 클래스로 직사각형 정사각형 역할을 모두 할 수 있는 사각형 클래스를 만들어서 기능을 수행하게 해야한다.

정 사각형의 경우 높이 너비가 모두 같기 때문에 같은 값을 하나만 넣어주면 된다.

https://blog.itcode.dev/posts/2021/08/15/liskov-subsitution-principle


* **일관성이란?**

두 객체가 같다면 앞으로도 영원히 같아야 한다! 가변 객체는 비교 시점에 따라 서로 다를 수도 혹은 같을 수도 있는 반면,

불변 객체는 한번 다르면 끝까지 달라야 한다. ?? equals 는 항시 메모리에 존재하는 객체만을 사용한 결정적 계산만 수행해야 한다.


* **null-아님**
```
// 묵시적 null 검사
@Override
public boolean equals(Object o){
if(!(o instanceof MyType))
return false;

MyType mt = (MyType) o;
...
}
```

equals 가 타입을 확인하지 않으면 잘못된 타입이 인수로 주어졌을 때 ClassCastException을 던져서 일반 규약을 위배하게 된다.

그런데 instanceof 를 사용하면 두번째 연산자와 무관한게 첫 번째 피연산자가 null 일때 false 를 반환하기 때문에 따로 null 체크를 하지 않아도 된다.


#### ++equals 메서드 구현 방법 단계별 정리 및 적용

1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.

2. instanceof 연산자로 입력이 올바른 타입인지 확인한다. (null 체킹까지 해줌)

3. 입력을 올바른 타입으로 형변환 한다.

4. 입력 객체와 자기 자신의 대응되는 핵심 필드들이 모두 일치하는지 하나씩 검사!

PohneNumber.class 참고.

참고로 대부분의 IDE 에서 equals hashcode 를 만들어주기 때문에 이거를 그대로 쓰면됨... 그외 롬복이라는 아주 좋은 녀석이 있다!


