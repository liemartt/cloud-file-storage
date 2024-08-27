package com.liemartt.cloud.util;


import java.util.Objects;

public class PathUtil {
    public static String getUserPath(Integer userId, String path) {
        if (path.isBlank()) {
            path = "user-" + userId.toString() + "-files/";
        } else {
            path = "user-" + userId.toString() + "-files/" + path + "/";
        }
        return path;
    }
}
