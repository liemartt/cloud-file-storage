package com.liemartt.cloud.service.minio;

import com.liemartt.cloud.dto.BreadcrumbLink;
import com.liemartt.cloud.exception.InternalServiceException;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MinioService extends MinioAbstractClass {
    private final MinioClient minioClient;
    
    public List<Item> getObjects(String path, boolean recursive) {
        List<Item> items = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(path)
                    .recursive(recursive)
                    .build());
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            throw new InternalServiceException("Internal error");
        }
        return items;
    }
    
    public boolean isPathExists(String path) {
        List<Item> userObjects = getObjects(path, false);
        return !userObjects.isEmpty();
    }
    
    public List<BreadcrumbLink> getBreadcrumbLinks(String path) {
        List<BreadcrumbLink> links = new ArrayList<>();
        links.add(new BreadcrumbLink("Home", ""));
        
        if (path.isBlank()) {
            return links;
        } else {
            StringBuilder sb = new StringBuilder();
            String[] folders = path.split("/");
            for (String folder : folders) {
                sb.append(folder);
                links.add(new BreadcrumbLink(folder, sb.toString()));
                sb.append("/");
            }
        }
        return links;
    }
    
}
