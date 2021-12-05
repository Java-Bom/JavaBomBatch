package com.javabom.definitiveguide.config;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileLineMapperGenerator {

    public static <T> LineMapper<T> generateLineMapper(Class<T> value, String separator) {
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();

        List<String> fieldNames = extractFieldNames(value.getDeclaredFields());
        DelimitedLineTokenizer tokenizer = generateLineTokenizer(fieldNames, separator);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(generateFieldSetMapper(value));
        return lineMapper;
    }

    private static List<String> extractFieldNames(Field[] fields) {
        return Arrays.stream(fields)
                .filter(field -> !field.isSynthetic()) //런타임시 들어오는 리플렉션 데이터 제거 -> jacoco
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    private static <T> BeanWrapperFieldSetMapper<T> generateFieldSetMapper(Class<T> value) {
        BeanWrapperFieldSetMapper<T> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(value);
        return beanWrapperFieldSetMapper;
    }

    private static DelimitedLineTokenizer generateLineTokenizer(List<String> fieldNames, String separator) {
        String[] namesArr = new String[fieldNames.size()];
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(separator);
        delimitedLineTokenizer.setNames(fieldNames.toArray(namesArr));
        return delimitedLineTokenizer;
    }

}
