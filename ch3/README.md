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

대칭성은 두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다는 뜻이다.

* **추이성이란?**

첫 번째 객체와 두 번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다면 첫 번째 객체도 세 번째

객체와 같아야 한다는 뜻이다.

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
        return super.equals(o) && ((ColorPoint) o).color == color;
    }


new Point(1,2);
new ColorPoint(1,2,RED);

p.equals(cp); // true
cp.equals(p); // false
```
이 경우는 동치성을 위배하게 된다. 포인트의 경우에는 타입이 같기 때문에 true 를 반환하지만

컬러 포인트는 매개변수의 클래스 종류가 다르기 때문에 false 를 반환해서 동치성 위반 !


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
추이성을 위배한 코드! p1 p2, p2 p3 비교에서는 색상을 무시하지만 p1 p3 는 색상까지 고려하기 떄문

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
```





