package id.kedukasi.core.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidatorUtil {

    public boolean isPhoneValid(String s) {
        Pattern p = Pattern.compile(
                "^(\\+62|62|0)8[1-9][0-9]{6,9}$");
        Matcher m = p.matcher(s);
        return (m.matches());
    }

//    So some of the email addresses that will be valid via this email validation technique are:
//
//    username@domain.com
//    user.name@domain.com
//    user-name@domain.com
//    username@domain.co.in
//    user_name@domain.com
    public boolean isEmailFormat(String str) {
        //String regex = "^[\\w-.]+@[\\w-.]+[\\w-]{2,4}$";
        String regexPattern = "^(?=.{1,64})[A-Za-z0-9_\\-]+(\\.[A-Za-z0-9_\\-]+)*+@[^-]{3,}[A-Za-z0-9-]+(\\.[A-Za-z]{2,})*+$";
        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(str).matches();
    }

    //standar input
    public boolean isStandardInput(String str, boolean skipNullValue) {
        if (str == null) return skipNullValue;
        String regex = "[a-zA-Z\\d\\s,./\\-+_]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
