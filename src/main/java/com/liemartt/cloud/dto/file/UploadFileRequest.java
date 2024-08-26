package com.liemartt.cloud.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UploadFileRequest {
    private String path;
    private MultipartFile file;
}
