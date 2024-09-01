package com.liemartt.cloud.service;

import com.liemartt.cloud.dto.SearchResponse;
import com.liemartt.cloud.exception.SearchOperationException;
import com.liemartt.cloud.util.MinioUtil;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchStorageService extends MinioAbstractClass {
    private final MinioUtil minioUtil;
    
    public List<SearchResponse> findObjects(String userPrefix, String query) {
        List<Item> objects = minioUtil
                .getObjects(userPrefix, true);
        objects.forEach(object-> System.out.println(object.objectName()));
        try {
            return objects
                    .stream()
                    .filter(item -> item.objectName().contains(query))
                    .map(SearchResponse::new)
                    .toList();
        } catch (Exception e) {
            throw new SearchOperationException("Error searching object with name " + query);
        }
    }
}
