package com.assessment.consumer_content.application.helper;

import org.springframework.stereotype.Component;

import java.util.List;

import static com.assessment.consumer_content.application.constant.GlobalConstant.BATCH_SIZE;
@Component
public class BatchSaveHelper {
    public  <T> void saveBatch(List<T> list, java.util.function.Consumer<List<T>> saveFunction, boolean isFinalBatch) {
        if (list.size() >= BATCH_SIZE || (isFinalBatch && !list.isEmpty())) {
            saveFunction.accept(list);
            list.clear();
        }
    }
}
