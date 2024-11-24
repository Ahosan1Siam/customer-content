package com.assessment.consumer_content.application.helper;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListPartitioner { ;
    public  <T> List<List<T>> partitionList(List<T> list, int divisor) {
        int listSize = list.size();

        int numPartitions =listSize < divisor ? 3 : Math.max(1, Math.min(Runtime.getRuntime().availableProcessors(), listSize / divisor));

        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < numPartitions; i++) {
            int start = i * listSize / numPartitions;
            int end = (i + 1) * listSize / numPartitions;
            partitions.add(list.subList(start, end));
        }

        return partitions;
    }
}
