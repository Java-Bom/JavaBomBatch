package com.javabom.definitiveguide.chap7.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.file.FlatFileParseException;

@Slf4j
public class CustomerItemReadErrorListener {
    @OnReadError
    public void onReadError(Exception e) {
        if (e instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) e;

            String errorMessage = ffpe.getLineNumber() + "번째 줄에서 에러 발생" +
                    System.lineSeparator() +
                    "input: " + ffpe.getInput();

            log.error(errorMessage);
        } else {
            log.error("에러 발생");
        }
    }
}
