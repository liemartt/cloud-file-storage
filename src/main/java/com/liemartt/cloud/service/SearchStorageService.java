package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.SearchResponse;
import com.liemartt.cloud.exception.SearchOperationException;
import com.liemartt.cloud.util.MinioUtil;
import com.liemartt.cloud.util.PathUtil;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchStorageService extends MinioAbstractClass {
    private final MinioUtil minioUtil;
    private final Logger logger = LoggerFactory.getLogger(SearchStorageService.class);
    
    public List<SearchResponse> findObjects(String userPrefix, String query) {
        List<SearchResponse> foundObjects = new ArrayList<>();
        try {
            List<Item> objects = minioUtil
                    .getObjects(userPrefix, true)
                    .stream()
                    .filter(item -> item.objectName().contains(query))
                    .toList();
            
            for (Item object : objects) {
                String objectName = object.objectName();
                if (objectName.substring(objectName.lastIndexOf("/") + 1).contains(query)) {
                    foundObjects.add(new SearchResponse(object));
                } else {
                    int queryIndex = objectName.indexOf(query);
                    if (queryIndex == -1) {
                        continue;
                    }
                    String folderPath = objectName.substring(0, objectName.indexOf("/", queryIndex) + 1);
                    foundObjects.add(new SearchResponse(folderPath));
                }
            }
            return foundObjects.stream()
                    .sorted(Comparator.comparing(SearchResponse::isDir))
                    .toList();
        } catch (Exception e) {
            logger.error("Error searching objects for query {}: {}", query, e.getMessage());
            throw new SearchOperationException("Error searching object with name " + query);
        }
    }
}
