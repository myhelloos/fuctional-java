package com.alfred.yuan;

import com.alfred.yuan.function.Function;
import com.alfred.yuan.function.base.Effect;
import com.alfred.yuan.function.base.Executable;

import java.util.List;

import static com.alfred.yuan.function.Function.andThenAll;
import static com.alfred.yuan.function.Function.composeAll;
import static com.alfred.yuan.utilities.CollectionUtilities.flodLeft;
import static com.alfred.yuan.utilities.CollectionUtilities.flodRight;
import static com.alfred.yuan.utilities.CollectionUtilities.forEach;
import static com.alfred.yuan.utilities.CollectionUtilities.list;
import static com.alfred.yuan.utilities.CollectionUtilities.map;
import static com.alfred.yuan.utilities.CollectionUtilities.range;
import static com.alfred.yuan.utilities.CollectionUtilities.reverse;

public class Main {

    public static void main(String[] args) {
        List<Integer> list = list(1, 2, 3, 4, 5);
        System.out.println(flodLeft(list, "", x -> y -> y + x));
        System.out.println(flodRight(list, "", x -> y -> y + x));
        System.out.println(reverse(list));

        List<Double> prices = list(10.10, 23.45, 32.07, 9.23);
        System.out.println(map(
                prices
                , Function.andThen(x -> x * 1.09, x -> x + 3.50)
        ));

        List<Double> pricesIncludingShipping = map(
                prices
                , Function.compose(x -> x + 3.50, x -> x * 1.09)
        );

        forEach(pricesIncludingShipping, x -> System.out.printf("%.2f\n", x));

        Function<Executable, Function<Executable, Executable>> compose =
                x -> y -> () -> {
                    x.exec();
                    y.exec();
                };
        Effect<Double> printWith2decimals = d -> System.out.printf("%.2f\n", d);
        Executable program = flodLeft(
                pricesIncludingShipping
                , () -> {}
                , e -> d -> compose.apply(e).apply(() -> printWith2decimals.apply(d))
        );
        program.exec();


        Function<Integer, Integer> add = y -> y + 1;
        System.out.println(composeAll(map(range(0, 10000), x -> y -> x)).apply(0));
        System.out.println(andThenAll(map(range(0, 10000), x -> y -> x)).apply(0));
    }
}
