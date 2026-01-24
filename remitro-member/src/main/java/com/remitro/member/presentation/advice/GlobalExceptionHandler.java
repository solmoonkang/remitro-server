package com.remitro.member.presentation.advice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.BaseException;
import com.remitro.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
		log.error(
			"[✅ LOGGER] BUSSINESS EXCEPTION: CODE = {}, MESSAGE = {}",
			e.getErrorCode().getCode(), e.getMessage(), e
		);

		return ResponseEntity.status(e.getErrorCode().getStatus())
			.body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
		Map<String, String> validationErrors = e.getBindingResult()
			.getFieldErrors().stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				fieldError -> Objects.requireNonNullElse(
					fieldError.getDefaultMessage(),
					"유효하지 않은 값입니다."
				),
				(first, duplicate) -> first
			));

		log.warn(
			"[✅ LOGGER] VALIDATION EXCEPTION: CODE = {}, ERRORS = {}",
			ErrorCode.INVALID_INPUT_VALUE.getCode(), validationErrors
		);

		return ResponseEntity.badRequest().body(ErrorResponse.builder()
			.code(ErrorCode.INVALID_INPUT_VALUE.getCode())
			.message(ErrorCode.INVALID_INPUT_VALUE.getMessage())
			.validation(validationErrors)
			.build());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		log.error("[✅ LOGGER] DATA INTEGRITY VIOLATION: {}", e.getMessage());

		return ResponseEntity
			.status(HttpStatus.CONFLICT)
			.body(ErrorResponse.of(ErrorCode.DUPLICATE_RESOURCE));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("[✅ LOGGER] INTERNAL SERVER EXCEPTION", e);

		return ResponseEntity
			.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
			.body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
	}
}
