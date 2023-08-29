package com.ysy.accountbook.global.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class Utility {
    private final int leftLimit = 97; // letter 'a'
    private final int rightLimit = 122; // letter 'z'

    /**
     * 정렬된 ToString 출력
     *
     * @param object
     * @return
     */
    public static String prettyToString(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.toJson(object);
    }

    /**
     * check empty string
     *
     * @param value
     * @return
     */
    public boolean isEmpty(String value) {
        return StringUtils.isEmpty(value);
    }

    public String getRandomString(int length) {
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String getRandomKoreanString(int length) {
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String toString(Object object) {
        String result = "";

        Class<?> objectClass = object.getClass();
        String name = objectClass.getName();
        log.debug("name : {}", name);
        String simpleName = objectClass.getSimpleName();
        log.debug("simpleName : {}", simpleName);

        return result;
    }

    static public String getEncodedPassword(String password) {
        return ("{bcrypt}" + password);
    }
}
