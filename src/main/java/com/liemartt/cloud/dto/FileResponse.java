package com.liemartt.cloud.dto;

import io.minio.messages.Item;
import lombok.Data;

@Data
public class FileResponse {
    private String name;
    private String size;
    
    
    public FileResponse(Item item) {
        name = getObjectName(item);
        size = getItemSize(item.size());
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
        return name.substring(name.lastIndexOf("/") + 1);
    }
}
