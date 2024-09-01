package com.liemartt.cloud.util;


public class PathUtil {
    public static String addUserPrefix(Integer userId, String path) {
        if (path.isBlank()) {
            path = "user-" + userId.toString() + "-files/";
        } else {
            path = "user-" + userId.toString() + "-files/" + path + "/";
        }
        return path;
    }
    
    public static String removeUserPrefix(String path) {
        return path.substring(path.indexOf("/") + 1);
    }
    
    public static String addSlashToFolder(String name) {
        if (!name.endsWith("/")) {
            name = name + "/";
        }
        return name;
    }
    
    public static String addExtensionToFile(String name, String extension) {
        if (!name.endsWith(extension)) {
            name = name + "." + extension;
        }
        return name;
    }
    
    public static String extractObjectName(String objectName) {
        if (objectName.endsWith("/")) {
            objectName = objectName.substring(0, objectName.length() - 1);
        }
        return objectName.substring(objectName.lastIndexOf("/") + 1);
    }
    
    public static String extractPathToObject(String objectName) {
        objectName = removeUserPrefix(objectName);
        
        if (objectName.endsWith("/")) { //folder case
            return objectName.substring(0, objectName.length() - 1);
        }
        if (!objectName.contains("/")) { //home directory case
            return "";
        }
        return objectName.substring(0, objectName.lastIndexOf("/")); //subfolder file case
    }
}
