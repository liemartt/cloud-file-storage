package com.liemartt.cloud.service.minio;

import com.liemartt.cloud.controller.FileController;
import com.liemartt.cloud.exception.NotEnoughSpaceException;
import com.liemartt.cloud.util.FileSizeUtil;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserMemoryService {
    private static final BigDecimal MAX_MEMORY_DEFAULT_USER = BigDecimal.valueOf(500);
    
    private final MinioService minioService;
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    public BigDecimal getUserFilesSize(String userPrefix) {
        List<Item> items = minioService.getObjects(userPrefix, true);
        Optional<Long> reduce = items.stream().map(Item::size).reduce(Long::sum);
        return FileSizeUtil.mapByteToMb(reduce.orElse(0L));
    }
    
    public void checkUserMemory(String userFolder, MultipartFile uploadFile) {
        BigDecimal userSpace = getUserFilesSize(userFolder)
                .add(FileSizeUtil.mapByteToMb(uploadFile.getSize()));
        
        if (userSpace.compareTo(MAX_MEMORY_DEFAULT_USER) >= 0) {
            logger.warn("Not enough space to upload file " + uploadFile.getOriginalFilename());
            throw new NotEnoughSpaceException("Not enough space to upload file " + uploadFile.getOriginalFilename());
        }
    }
    
    public void checkUserMemory(String userFolder, MultipartFile[] uploadFolder) {
        BigDecimal userSpace = getUserFilesSize(userFolder)
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
