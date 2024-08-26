package com.liemartt.cloud.dto.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFileRequest {
    private String path;
    private MultipartFile file;
}
