package effective.effective.ch2;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Pizza 에는 토핑이 필수다.
 */
public abstract class Pizza {

    public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE};
    final Set<Topping> toppings;


    /** Builder<T extends Builder<T>> 는 Builder 에 들어오는 자료형이
     *  Pizza 클래스의 Builder 를 상속받은 녀석만 가능하다는 의미임.
     */
    abstract static class Builder<T extends Builder<T>>{
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }


        /**
         * 하위 클래스는 이 메서드들을 오버라이딩 해서 this 를 반환하도록 한다.
         */
        abstract Pizza build();

        protected abstract T self();
    }

    Pizza(Builder<?> builder){
        toppings = builder.toppings.clone(); // 아이템 50 참고. 아직 배우지 않음.
    }

}