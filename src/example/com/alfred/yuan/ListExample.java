package example.com.alfred.yuan;

import com.alfred.yuan.base.List;
import com.alfred.yuan.function.Function;

import static com.alfred.yuan.base.List.foldRight;
import static com.alfred.yuan.base.List.list;

/**
 * Created by alfred_yuan on 2019-01-29
 */
public class ListExample {
    static Function<List<Integer>, Integer> sum = ints ->
        ints.isEmpty()
            ? 0
            : ints.head() + ListExample.sum.apply(ints.tail());

    static Function<List<Double>, Double> product = doubles ->
        doubles.isEmpty()
            ? 1.0
            : doubles.head() == 0.0
                ? 0.0
                : doubles.head() * ListExample.product.apply(doubles.tail());

    public static void main(String[] args) {
        System.out.println(sum.apply(list(1, 2, 3, 4)));

        System.out.println(product.apply(list(1.0, 2.0, 3.0, 4.0)));

        System.out.println(foldRight(list(1, 2, 3, 4), 0, x -> y -> x + y));

        System.out.println(foldRight(list(1.0, 2.0, 3.0, 4.0), 1.0, x -> y -> x * y));

        System.out.println(foldRight(list(1, 2, 3), list(), x -> y -> y.cons(x)));

        System.out.println(list(1, 2, 3, 4).map(i -> i * 3));

        System.out.println(list(1.0, 2.0, 3.0, 4.0).map(d -> Double.toString(d)));

        System.out.println(List.<Double>list().map(d -> Double.toString(d)));

        System.out.println(list(1, 2, 3, 4).filter(i -> i % 2 == 0));

        System.out.println(List.<Integer>list().filter(i -> i % 2 == 0));

        System.out.println(list(1, 2, 3, 4).flatMap(i -> list(-i, i)));

        System.out.println(List.<Integer>list().flatMap(i -> list(-i, i)));
    }
}
