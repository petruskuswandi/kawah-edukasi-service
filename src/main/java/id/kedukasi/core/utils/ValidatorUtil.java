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
}
