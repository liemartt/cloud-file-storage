package com.liemartt.cloud.util;


public class PathUtil {
    public static String getPathWithUserPrefix(Integer userId, String path) {
        if (path.isBlank()) {
            path = "user-" + userId.toString() + "-files/";
        } else {
            path = "user-" + userId.toString() + "-files/" + path + "/";
        }
        return path;
    }
    
    public static String addSlashToFolderName(String name) {
        if(!name.endsWith("/")){
            name = name+"/";
        }
        return name;
    }
}
