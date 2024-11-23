package com.assessment.consumer_content.application.helper;

import org.modelmapper.ModelMapper;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseServiceHelper <E,D>{
    private static final ModelMapper modelMapper = new ModelMapper();

    protected E convertForCreate(D d) {
        return modelMapper.map(d, getEntityClass());
    }
    public Class<E> getEntityClass() {
        return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    protected List<List<E>> partition(List<E> list, int size) {
        return IntStream.range(0, (list.size() + size - 1) / size)
                .mapToObj(i -> list.subList(i * size, Math.min((i + 1) * size, list.size())))
                .collect(Collectors.toList());
    }
}
