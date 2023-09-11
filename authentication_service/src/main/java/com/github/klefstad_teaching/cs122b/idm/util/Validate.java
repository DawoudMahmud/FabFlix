package com.github.klefstad_teaching.cs122b.idm.util;

import com.gitcodings.stack.core.result.Result;
import com.gitcodings.stack.core.base.ResultResponse;
import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.IDMResults;
import org.springframework.stereotype.Component;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class Validate
{
    public static void checkPassword(char[] testPassword)
    {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{10,20}$";
        if(testPassword.length  < 10  || testPassword.length  > 20)
            throw new ResultError(IDMResults.PASSWORD_DOES_NOT_MEET_LENGTH_REQUIREMENTS);
        boolean matches = Pattern.matches(regex, CharBuffer.wrap(testPassword));
        //Pattern p = Pattern.compile(regex);
        //Matcher m = p.matcher(testPassword.toString());
        if(!matches)
            throw new ResultError(IDMResults.PASSWORD_DOES_NOT_MEET_CHARACTER_REQUIREMENT);

    }
    public static void checkEmail(String emailAddress) {
        String regexPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        if(emailAddress.length() < 6 || emailAddress.length() > 32)
            throw new ResultError(IDMResults.EMAIL_ADDRESS_HAS_INVALID_LENGTH);
        Pattern p = Pattern.compile(regexPattern,Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(emailAddress);

        if(!matcher.find())
            throw new ResultError(IDMResults.EMAIL_ADDRESS_HAS_INVALID_FORMAT);
    }
}
