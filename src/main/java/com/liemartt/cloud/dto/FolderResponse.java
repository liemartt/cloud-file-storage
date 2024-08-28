package com.liemartt.cloud.dto;

import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderResponse {
    private String fullName; // format: user-1-files/f1/f2/f3
    private String folderName; // format: f3
    private String path; // format: f1/f2/f3
    
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
