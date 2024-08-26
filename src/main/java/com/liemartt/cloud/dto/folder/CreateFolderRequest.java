package com.liemartt.cloud.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateFolderRequest {
    private String path;
    private String folderName;
}
