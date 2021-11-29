package com.javabom.definitiveguide.test;

import com.javabom.definitiveguide.main.DefiniteGuideBatchApplication;
import com.javabom.definitiveguide.test.config.BatchTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BatchTestConfig.class)
@SpringBootTest(classes = DefiniteGuideBatchApplication.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
public @interface BatchSpringTest {
}
