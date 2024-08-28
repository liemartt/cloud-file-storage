package com.liemartt.cloud.dto;

import io.minio.messages.Item;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ObjectResponseDto {
    private String name;
    private String size;
    private String pathWithObjectName;
    private String pathWithoutObjectName;
    private boolean isDir;
    
    
    public ObjectResponseDto(Item item) {
        name = getObjectName(item);
        size = getItemSize(item.size());
        pathWithObjectName = item.objectName();
        pathWithoutObjectName = getPathWithoutObjectName(item);
        isDir = item.isDir();
    }
    
    private String getItemSize(long size) {
        if (size / 1024 / 1024 > 0) {
            return size / 1024 / 1024 + " Mb";
        } else if (size / 1024 > 0) {
            return size / 1024 + " Kb";
        } else return size + "b";
    }
    
    private String getObjectName(Item item) {
        String name = item.objectName();
        if (item.isDir()) {
            name = name.substring(0, name.length() - 1); //remove last /
            return name.substring(name.lastIndexOf("/") + 1) + "/"; //find name and add /
        }
        return name.substring(name.lastIndexOf("/") + 1);
    }
    
    private String getPathWithoutObjectName(Item item) {
        String name = item.objectName();
        if (item.isDir()) {
            name = name.substring(0, name.length() - 1); //remove last /
        }
        return name.substring(0, name.lastIndexOf("/") + 1);
    }
}
