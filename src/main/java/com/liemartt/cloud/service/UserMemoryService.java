package com.liemartt.cloud.service;

import com.liemartt.cloud.controller.FileController;
import com.liemartt.cloud.exception.NotEnoughSpaceException;
import com.liemartt.cloud.util.FileSizeUtil;
import com.liemartt.cloud.util.MinioUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserMemoryService {
    private static final BigDecimal MAX_MEMORY_DEFAULT_USER = BigDecimal.valueOf(500);
    private final MinioUtil minioUtil;
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    public void checkUserMemory(String userFolder, MultipartFile uploadFile) {
        BigDecimal userSpace = minioUtil.getUserFilesSize(userFolder)
                .add(FileSizeUtil.mapByteToMb(uploadFile.getSize()));
        
        if (userSpace.compareTo(MAX_MEMORY_DEFAULT_USER) >= 0) {
            logger.warn("Not enough space to upload file " + uploadFile.getOriginalFilename());
            throw new NotEnoughSpaceException("Not enough space to upload file " + uploadFile.getOriginalFilename());
        }
    }
    
    public void checkUserMemory(String userFolder, MultipartFile[] uploadFolder) {
        BigDecimal userSpace = minioUtil.getUserFilesSize(userFolder)
                .add(Arrays.stream(uploadFolder)
                        .map(MultipartFile::getSize)
                        .map(FileSizeUtil::mapByteToMb)
                        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        
        if (userSpace.compareTo(MAX_MEMORY_DEFAULT_USER) >= 0) {
            logger.warn("Not enough space to upload folder");
            throw new NotEnoughSpaceException("Not enough space to upload folder");
        }
    }
}
