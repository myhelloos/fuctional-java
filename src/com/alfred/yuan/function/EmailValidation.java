package com.alfred.yuan.function;

import com.alfred.yuan.function.base.Effect;
import com.alfred.yuan.function.base.Result;

import java.util.regex.Pattern;

/**
 * Created by alfred_yuan on 2019-01-27
 */
public class EmailValidation {
    static Pattern emailPattern = Pattern.compile(
            "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
    );

    static Function<String, Result<String>> emailChecker = email -> {
        if (email == null) {
            return Result.failure("email must not be null");
        } else if (email.length() == 0) {
            return Result.failure("email must not be empty");
        } else if (emailPattern.matcher(email).matches()) {
            return Result.success(email);
        } else {
            return Result.failure("email " + email + " is invalid");
        }
    };

    public static void main(String[] args) {
        emailChecker.apply("this.is@my.email").bind(success, failure);
        emailChecker.apply(null).bind(success, failure);
        emailChecker.apply("").bind(success, failure);
        emailChecker.apply("john.doe@acme.com").bind(success, failure);
    }

    static Effect<String> success = s -> System.out.println("Mail sent to " + s);

    static Effect<String> failure = s -> System.out.println("Error message logged: " + s);
}
