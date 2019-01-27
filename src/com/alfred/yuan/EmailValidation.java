package com.alfred.yuan;

import com.alfred.yuan.function.Function;
import com.alfred.yuan.function.base.Case;
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

    static Function<String, Result<String>> emailChecker = email -> Case.match(
            Case.mcase(() -> Result.success(email))
            , Case.mcase(() -> email == null, () -> Result.failure("email must not be null"))
            , Case.mcase(() -> email.length() == 0, () -> Result.failure("email must not be empty"))
            , Case.mcase(() -> !emailPattern.matcher(email).matches(), () -> Result.failure("email " + email + " is invalid."))
    );

    public static void main(String[] args) {
        emailChecker.apply("this.is@my.email").bind(success, failure);
        emailChecker.apply(null).bind(success, failure);
        emailChecker.apply("").bind(success, failure);
        emailChecker.apply("john.doe@acme.com").bind(success, failure);
    }

    static Effect<String> success = s -> System.out.println("Mail sent to " + s);

    static Effect<String> failure = s -> System.out.println("Error message logged: " + s);
}
