package example.com.alfred.yuan;

import com.alfred.yuan.base.Supplier;
import com.alfred.yuan.function.Function;
import com.alfred.yuan.util.List;
import com.alfred.yuan.util.Map;
import com.alfred.yuan.util.Option;

import static com.alfred.yuan.util.List.list;

/**
 * Created by alfred_yuan on 2019-01-30
 */
public class OptionExample {
    public static void main(String[] args) {
        Function<List<Integer>, Option<Integer>> max =
            list -> list.isEmpty()
                ? Option.none()
                : Option.some(list.foldLeft(list.head(), x -> y -> x.compareTo(y) > 0 ? x : y));

        Supplier<Integer> defaultValue = () -> {throw new RuntimeException();};

        System.out.println(max.apply(list(1, 7, 3, 4)).getOrElse(defaultValue));
//        System.out.println(max.apply(list()).getOrElse(defaultValue));

        Map<String, Toon> toons = new Map<String, Toon>()
            .put("Mickey", new Toon("Mickey", "Mouse", "mickey@disney.com"))
            .put("Minnes", new Toon("Minnes", "Mouse"))
            .put("Donald", new Toon("Donald", "Duck", "donald@disney.com"));

        System.out.println(toons.get("Mickey").flatMap(Toon::getEmail).getOrElse(() -> "No Data"));
        System.out.println(toons.get("Minnes").flatMap(Toon::getEmail).getOrElse(() -> "No Data"));
        System.out.println(toons.get("Donald").flatMap(Toon::getEmail).getOrElse(() -> "No Data"));

        Function<List<Double>, Double> sum = list -> list.foldLeft(0.0, result -> e -> result + e);
        Function<List<Double>, Option<Double>> mean = list -> list.isEmpty()
            ? Option.none()
            : Option.some(sum.apply(list) / list.size());
        Function<List<Double>, Option<Double>> variance = list ->
            mean.apply(list).flatMap(m -> mean.apply(list.map(x -> Math.pow(x - m, 2))));

        System.out.println(variance.apply(list()));
        System.out.println(variance.apply(list(1.0, 2.0, 1.5)));
    }

    static class Toon {
        private final String firstName;
        private final String lastName;
        private final Option<String> email;

        Toon(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = Option.none();
        }

        public Toon(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = Option.some(email);
        }

        /**
         * Getter for property 'email'.
         *
         * @return Value for property 'email'.
         */
        public Option<String> getEmail() {
            return email;
        }
    }
}
