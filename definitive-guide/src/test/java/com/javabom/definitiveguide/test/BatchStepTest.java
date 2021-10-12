package com.javabom.definitiveguide.test;

import com.javabom.definitiveguide.main.DefiniteGuideBatchApplication;
import com.javabom.definitiveguide.test.config.BatchTestConfig;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BatchTestConfig.class)
@SpringBootTest(classes = DefiniteGuideBatchApplication.class)
@SpringBatchTest
public @interface BatchStepTest {
}
