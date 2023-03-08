package effective.effective.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 차 이름이 입력값으로 주어지면, 해당 차 이름에 따라 객체를 생산하는 로직
 */
public class CarFactory {

    private static Map<String, Supplier<Car>> cars;


    static {
        cars = new HashMap<>();
        cars.put("Honda", HondaCar::new);
        cars.put("BMW", BMWCar::new);


    }

    public static Car create(final String carName){
        try{
            return cars.get(carName).get(); // Supplier 에서 get 으로 자동차를 생성!
        }catch(NullPointerException e){
            throw new IllegalArgumentException("해당하는 차가 없습니다!");
        }
        
    public static String testCreate(Supplier<? extends Car> carFactory){
        Car car = carFactory.get();
        String carName = car.getCarName();

        return carName;
      }
    }

    public static void main(String[] args) {

        String s = CarFactory.testCreate(BMWCar::new);
        System.out.println(s);

    }
}
