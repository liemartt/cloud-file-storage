package com.liemartt.cloud.dto;

import com.liemartt.cloud.util.PathUtil;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderResponse {
    private String folderName;
    private String path;
    
    public FolderResponse(Item item) {
        path = PathUtil.extractPathToObject(item.objectName());
        folderName = PathUtil.extractObjectName(item.objectName());
    }
}
