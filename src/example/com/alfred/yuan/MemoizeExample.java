package example.com.alfred.yuan;

import com.alfred.yuan.function.Function;
import com.alfred.yuan.utilities.Memoizer;

/**
 * Created by alfred_yuan on 2019-01-28
 */
public class MemoizeExample {
    public static void main(String[] args) {
        Function<Integer, Integer> longCalculation = x -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {

            }
            return x * 2;
        };

        Function<Integer, Integer> memoizedLongCalculation = Memoizer.memoize(longCalculation);

        long startTime = System.currentTimeMillis();
        Integer result = memoizedLongCalculation.apply(1);
        long time1 = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        Integer result2 = memoizedLongCalculation.apply(1);
        long time2 = System.currentTimeMillis() - startTime;
        System.out.println(result);
        System.out.println(result2);
        System.out.println(time1);
        System.out.println(time2);

        Function<Integer, Function<Integer, Function<Integer, Integer>>> f3m =
            Memoizer.memoize(x ->
                Memoizer.memoize(y ->
                    Memoizer.memoize(z ->
                        longCalculation.apply(x) + longCalculation.apply(y) + longCalculation.apply(z))));

        startTime = System.currentTimeMillis();
        result = f3m.apply(1).apply(2).apply(3);
        time1 = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        result2 = f3m.apply(1).apply(2).apply(3);
        time2 = System.currentTimeMillis() - startTime;
        System.out.println(result);
        System.out.println(result2);
        System.out.println(time1);
        System.out.println(time2);
    }
}
