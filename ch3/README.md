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

**반사성이란?**

객체는 자기 자신과 같아야 한다는 뜻. 이 요건을 어긴 클래스의 인스턴스를 컬렉션에 넣은 다음

contains 메서드를 호출하면 인스턴스가 없다고 답할 것이다. (이걸 어떻게 어김?)

**대칭성이란?**

대칭성은 두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다는 뜻이다.

**추이성이란?**

첫 번째 객체와 두 번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다몬 첫 번째 객체도 세 번째

객체와 같아야 한다는 뜻이다.

```
class Point
@Override
    public boolean equals(Object o) {
        if(!(o instanceof Point))
            return false;

        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
    
    
class ColorPoint extends Point

@Override
  public boolean equals(Object o) {
        if(!(o instanceof ColorPoint))
            return false;
        return super.equals(o) && ((ColorPoint) o).color == color;
    }

p.equals(cp); // true
cp.equals(p); // false
```
이 경우는 동치성을 위배하게 된다. 포인트의 경우에는 타입이 같기 때문에 true 를 반환하지만

컬러 포인트는 매개변수의 클래스 종류가 다르기 때문에 false 를 반환해서 동치성 위반 !

