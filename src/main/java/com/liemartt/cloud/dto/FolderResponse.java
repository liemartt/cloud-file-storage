package com.liemartt.cloud.dto;

import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderResponse {
    private String fullName;
    private String folderName;
    private String path;
    
    public FolderResponse(Item item) {
        fullName = item.objectName();
        
        path = parsePath(item);
        folderName = parseFolderName(path);
    }
    
    private String parseFolderName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
    
    private String parsePath(Item item) {
        String name = item.objectName();
        return name.substring(name.indexOf("/") + 1, name.length() - 1);
    }
}
