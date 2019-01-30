package example.com.alfred.yuan;

import com.alfred.yuan.util.Tuple;
import com.alfred.yuan.corecursive.TailCall;
import com.alfred.yuan.function.Function;

import java.math.BigInteger;
import java.util.List;

import static com.alfred.yuan.corecursive.TailCall.ret;
import static com.alfred.yuan.corecursive.TailCall.sus;
import static com.alfred.yuan.utilities.CollectionUtilities.append;
import static com.alfred.yuan.utilities.CollectionUtilities.flodLeft;
import static com.alfred.yuan.utilities.CollectionUtilities.head;
import static com.alfred.yuan.utilities.CollectionUtilities.iterate;
import static com.alfred.yuan.utilities.CollectionUtilities.list;
import static com.alfred.yuan.utilities.CollectionUtilities.map;
import static com.alfred.yuan.utilities.CollectionUtilities.tail;

/**
 * Created by alfred_yuan on 2019-01-28
 */
public class CorecursiveExample {
    static Function<Integer, Function<Integer, Integer>> add = x -> y -> {
        class AddHelper {
            Function<Integer, Function<Integer, TailCall<Integer>>> addHelper =
                a -> b -> b == 0
                    ? ret(a)
                    : sus(() -> this.addHelper.apply(a + 1).apply(b - 1));
        }

        return new AddHelper().addHelper.apply(x).apply(y).eval();
    };

    static Function<Integer, BigInteger> fib = x -> {
        class FibHelper {
            Function<BigInteger, Function<BigInteger, Function<BigInteger, TailCall<BigInteger>>>> fib_
                = acc1 -> acc2 -> x_ -> x_.equals(BigInteger.ZERO)
                ? ret(BigInteger.ZERO)
                : x_.equals(BigInteger.ONE)
                    ? ret(acc1.add(acc2))
                    : sus(() -> this.fib_.apply(acc2).apply(acc1.add(acc2)).apply(x_.subtract(BigInteger.ONE)));
        }
        return new FibHelper().fib_.apply(BigInteger.ONE).apply(BigInteger.ZERO).apply(BigInteger.valueOf(x)).eval();
    };

    static Function<Integer, String> fibStr = limit -> {
        class FibStrHelper {
            TailCall<List<BigInteger>> fib_(List<BigInteger> acc, BigInteger acc1, BigInteger acc2, BigInteger x) {
                return x.equals(BigInteger.ZERO)
                    ? ret(acc)
                    : x.equals(BigInteger.ONE)
                        ? ret(append(acc, acc1.add(acc2)))
                        : sus(() -> fib_(append(acc, acc1.add(acc2)), acc2, acc1.add(acc2), x.subtract(BigInteger.ONE)));
            }
        }
        List<BigInteger> list = new FibStrHelper().fib_(
            list(BigInteger.ZERO)
            , BigInteger.ONE
            , BigInteger.ZERO
            , BigInteger.valueOf(limit)
        ).eval();
        return makeString(list, ", ");
    };

    static Function<Integer, String> fibTuple = limit -> {
        Tuple<BigInteger, BigInteger> seed = new Tuple<>(BigInteger.ZERO, BigInteger.ONE);
        List<BigInteger> list = map(
            iterate(seed, t -> new Tuple<>(t._2, t._1.add(t._2)), limit + 1)
            , t -> t._1
        );
        return makeString(list, ", ");
    };

    static <T> String makeString(List<T> list, String separator) {
        return list.isEmpty()
            ? ""
            : tail(list).isEmpty()
                ? head(list).toString()
                : head(list) + flodLeft(tail(list), "", x -> y -> x + separator + y);
    }

    public static void main(String[] args) {
        System.out.println(add.apply(10000).apply(4));
        System.out.println(add.apply(4).apply(10000));

        System.out.println(System.currentTimeMillis());
        BigInteger r1 = fib.apply(5000);
        System.out.println(System.currentTimeMillis());
        System.out.println(r1);
        System.out.println(System.currentTimeMillis());

        System.out.println(System.currentTimeMillis());
        String str = fibStr.apply(50);
        System.out.println(System.currentTimeMillis());
        System.out.println(str);
        System.out.println(System.currentTimeMillis());

        System.out.println(System.currentTimeMillis());
        String str2 = fibTuple.apply(50);
        System.out.println(System.currentTimeMillis());
        System.out.println(str);
        System.out.println(System.currentTimeMillis());
    }
}
