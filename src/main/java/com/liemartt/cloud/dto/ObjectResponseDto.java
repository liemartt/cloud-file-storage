package com.liemartt.cloud.dto;

import io.minio.messages.Item;
import lombok.Data;

@Data
public class ObjectResponseDto {
    private String name;
    private String pathWithObjectName;
    private String pathWithoutObjectName;
    private boolean isDir;
    
    
    public ObjectResponseDto(Item item) {
        name = getObjectName(item);
        pathWithObjectName = item.objectName();
        pathWithoutObjectName = getPathWithoutObjectName(item);
        isDir = item.isDir();
    }
    
    private String getObjectName(Item item) {
        String name = item.objectName();
        if (item.isDir()) {
            name = name.substring(0, name.length() - 1); //remove last /
            return name.substring(name.lastIndexOf("/") + 1) + "/"; //find name and add /
        }
        return name.substring(name.lastIndexOf("/") + 1);
    }
    private String getPathWithoutObjectName(Item item){
        String name = item.objectName();
        if (item.isDir()) {
            name = name.substring(0, name.length() - 1); //remove last /
        }
        return name.substring(0, name.lastIndexOf("/")+1);
    }
}
