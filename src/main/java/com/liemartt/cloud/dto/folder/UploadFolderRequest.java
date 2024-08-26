package com.liemartt.cloud.dto.folder;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFolderRequest {
    private String path;
    private MultipartFile[] files;
}
