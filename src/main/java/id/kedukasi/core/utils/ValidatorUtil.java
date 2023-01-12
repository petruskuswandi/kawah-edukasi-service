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

    public boolean isEmailValid(String s){
        Pattern p = Pattern.compile(
                "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher m = p.matcher(s);
        return (m.matches());
    }

    /*
        password Validation
        gunakan 1 atau lebih huruf kapital
        gunakan 1 atau lebih special characters
        tidak boleh mengandung spasi
     */
    public boolean isPasswordValid(String s){
        Pattern p = Pattern.compile(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%#*?&])[A-Za-z\\d@$!%#*?&]{8,}$");
        Matcher m = p.matcher(s);
        return (m.matches());
    }

    public boolean isNumeric(String n){
        Pattern p = Pattern.compile(
                "^[0-9]+$");
        Matcher m = p.matcher(n);
        return (m.matches());
    }
}
