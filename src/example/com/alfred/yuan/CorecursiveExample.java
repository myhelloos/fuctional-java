package example.com.alfred.yuan;

import com.alfred.yuan.corecursive.TailCall;
import com.alfred.yuan.function.Function;

import java.math.BigInteger;

import static com.alfred.yuan.corecursive.TailCall.ret;
import static com.alfred.yuan.corecursive.TailCall.sus;

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

    private static BigInteger fib2_(BigInteger acc1, BigInteger acc2, BigInteger x) {
        if (x.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        } else if (x.equals(BigInteger.ONE)) {
            return acc1.add(acc2);
        } else {
            return fib2_(acc2, acc1.add(acc2), x.subtract(BigInteger.ONE));
        }
    }

    public static BigInteger fib2(int x) {
        return fib2_(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(x));
    }

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

    public static void main(String[] args) {
        System.out.println(add.apply(10000).apply(4));
        System.out.println(add.apply(4).apply(10000));

        System.out.println(System.currentTimeMillis());
        BigInteger r = fib2(5000);
        System.out.println(System.currentTimeMillis());
        System.out.println(r);
        System.out.println(System.currentTimeMillis());

        System.out.println(System.currentTimeMillis());
        BigInteger r1 = fib.apply(5000);
        System.out.println(System.currentTimeMillis());
        System.out.println(r1);
        System.out.println(System.currentTimeMillis());
    }
}
