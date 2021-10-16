package com.javabom.definitiveguide.chap4.job;

import java.util.Objects;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class NameFormatValidator implements JobParametersValidator {

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		if (Objects.isNull(parameters)) {
			// void 타입이며 해당 Exception 유무로 유효성 판단
			throw new JobParametersInvalidException("parameter is empty");
		}
		JobParameter name = parameters.getParameters().get("name");
		if (Objects.isNull(name) || name.getType() != JobParameter.ParameterType.STRING) {
			throw new JobParametersInvalidException("type is not null and not string");
		}
	}
}
