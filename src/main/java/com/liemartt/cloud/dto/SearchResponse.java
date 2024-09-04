package com.liemartt.cloud.dto;

import com.liemartt.cloud.util.PathUtil;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class SearchResponse {
    private String name;
    private String path;
    private boolean isDir;
    
    public SearchResponse(Item item) {
        this.name = PathUtil.extractObjectName(item.objectName());
        this.path = PathUtil.extractPathToObject(item.objectName());
        this.isDir = item.isDir();
    }
    
    public SearchResponse(String fullName) {
        this.name = PathUtil.extractObjectName(fullName);
        this.path = PathUtil.extractPathToObject(fullName);
        this.isDir = true;
    }
}
