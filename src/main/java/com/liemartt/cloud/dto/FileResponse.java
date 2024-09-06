package com.liemartt.cloud.dto;

import com.liemartt.cloud.util.FileSizeUtil;
import com.liemartt.cloud.util.PathUtil;
import io.minio.messages.Item;
import lombok.Data;

@Data
public class FileResponse {
    private String name;
    private String size;
    
    
    public FileResponse(Item item) {
        name = PathUtil.extractObjectName(item.objectName());
        size = FileSizeUtil.getFileSizeWithMeasureUnit(item.size());
    }
}
