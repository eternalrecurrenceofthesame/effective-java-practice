package effective.effective.ch2;

import java.util.Objects;

/**
 * 뉴욕 피자는 크기 매개변수를 필수로 받는다
 */
public class NyPizza extends Pizza{

    public enum Size{SMALL, MEDIUM, LARGE};
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder>{
        private final Size size;

        public Builder(Size size){
            this.size = Objects.requireNonNull(size);
        }


        /**
         * build, self 를 재정의 해서 자신을 반환.
         */
        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder){
        super(builder);
        size = builder.size;
    }



    public static void main(String[] args) {

        /**
         * 피자 만들기
         */
        NyPizza pizza = new NyPizza.Builder(Size.SMALL)
                .addTopping(Topping.SAUSAGE).addTopping(Topping.ONION).build();

        System.out.println(pizza);

    }



}
