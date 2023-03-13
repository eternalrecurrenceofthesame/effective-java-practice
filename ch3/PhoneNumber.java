package equals;

public final class PhoneNumber {

    /**
     * [Tip]
     * float, double 을 제외한 기본 타입 필드는 == 연산자를,
     *
     * 참조 타입 필드는 각각의 equals 메서드로,
     *
     * float, double 는 float.compare 로 비교(??부동소숫값을 다뤄야 하기 때문)
     *
     * 배열 필드는 원소 각각을 앞서의 지침대로 비교하고 배열의 모든 원소가 핵심 필드라면 Arrays.equals 메서드들 중 하나를 사용하자.
     *
     * null 값을 정상 취급하는 참조 타입 필드는 Objects.equals(Object, Object) 로 비교해서 NPE 발생을 예방하자.
     *
     * 비교하기 복잡한 필드를 가진 클래스는 그 ??필드의 표준형을 저장해둔 후 표준형끼리 비교하면 훨씬 경제적이다.
     * 불변 클래스에 제격, 가변 클래스의 경우 계속 바꿔줘야 해.
     *
     * 비용이 저렴한 필드에서 무거운 필드 순서로 검사하자! 63p 그외 참고.
     */

    private final short areaCode, prefix, lineNum;

    public PhoneNumber(int areaCode, int prefix, int lineNum){
        this.areaCode = rangeCheck(areaCode, 999, "지역코드");
        this.prefix = rangeCheck(prefix, 999, "프리픽스");
        this.lineNum = rangeCheck(lineNum, 9999, "가입자 번호");
    }

    private static short rangeCheck(int val, int max, String arg){
        if(val < 0 || val > max)
            throw new IllegalArgumentException(arg + ": " + val);
        return (short) val; // 큰 자료형에서 작은 자료형으로 이동시 오류가 발생하기 때문에 강제적 형변환
    }


    /**
     * equals 를 재정의할 땐 hashCode 도 반드시 재정의하자
     * 너무 복잡하게 해결하려고 하지 말자 필드들의 동치성만 검사해도 equals 규약을 어렵지않게 지킬 수 있다.
     * Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자.
     */
    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof PhoneNumber))
            return false;

        PhoneNumber pn = (PhoneNumber) o;

        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }
}
