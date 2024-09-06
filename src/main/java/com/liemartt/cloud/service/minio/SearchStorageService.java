package com.liemartt.cloud.service.minio;

import com.liemartt.cloud.dto.SearchResponse;
import com.liemartt.cloud.exception.SearchOperationException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchStorageService extends MinioAbstractClass {
    private final MinioService minioService;
    private final Logger logger = LoggerFactory.getLogger(SearchStorageService.class);
    
    public List<SearchResponse> findObjects(String userPrefix, String query) {
        Set<SearchResponse> foundObjects = new HashSet<>();
        try {
            List<Item> objects = minioService
                    .getObjects(userPrefix, true)
                    .stream()
                    .filter(item -> item.objectName().contains(query))
                    .toList();
            
            for (Item object : objects) {
                String objectName = object.objectName();
                if (objectName.substring(objectName.lastIndexOf("/") + 1).contains(query)) {
                    foundObjects.add(new SearchResponse(object));
                } else {
                    List<Integer> indexes = findSubstringIndexes(objectName, query);
                    if (indexes.isEmpty()) {
                        continue;
                    }
                    List<SearchResponse> folders = indexes
                            .stream()
                            .map(index -> {
                                String objectPath = objectName.substring(0, objectName.indexOf("/", index) + 1);
                                return new SearchResponse(objectPath);
                            })
                            .toList();
                    foundObjects.addAll(folders);
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
    
    private List<Integer> findSubstringIndexes(String path, String substring) {
        List<Integer> indexes = new ArrayList<>();
        int index = path.indexOf(substring);
        while (index != -1) {
            indexes.add(index);
            index = path.indexOf(substring, index+1);
        }
        return indexes;
    }
    
    
}
