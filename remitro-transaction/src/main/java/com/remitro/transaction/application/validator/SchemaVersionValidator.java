package com.remitro.transaction.application.validator;

import org.springframework.stereotype.Component;

import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.transaction.infrastructure.constant.EventSchema;

@Component
public class SchemaVersionValidator {

	public void validateAccountOpened(int schemaVersion) {
		if (schemaVersion != EventSchema.ACCOUNT_OPENED_V1) {
			throw new ConflictException(ErrorMessage.UNSUPPORTED_EVENT_SCHEMA);
		}
	}

	public void validateAccountStatusUpdated(int schemaVersion) {
		if (schemaVersion != EventSchema.ACCOUNT_STATUS_UPDATED_V1) {
			throw new ConflictException(ErrorMessage.UNSUPPORTED_EVENT_SCHEMA);
		}
	}
}
