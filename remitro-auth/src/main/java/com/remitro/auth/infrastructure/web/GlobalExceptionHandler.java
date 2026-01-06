package com.remitro.auth.infrastructure.web;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BaseException;
import com.remitro.common.error.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
		log.warn(
			"[✅ LOGGER] BUSINESS ERROR: CODE={}, MESSAGE={}",
			e.getErrorCode().getCode(),
			e.getMessage()
		);

		return ResponseEntity
			.status(ErrorStatusMapper.map(e.getErrorCode()))
			.body(ErrorResponse.from(e));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
		MethodArgumentNotValidException e
	) {
		Map<String, String> validation = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				FieldError::getDefaultMessage
			));

		return ResponseEntity
			.badRequest()
			.body(new ErrorResponse(
				ErrorCode.INVALID_REQUEST.getCode(),
				"입력값이 유효하지 않습니다.",
				validation
			));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
		log.error("[✅ LOGGER] UNEXPECTED ERROR", e);

		return ResponseEntity
			.internalServerError()
			.body(new ErrorResponse(
				ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
				"처리 중 오류가 발생했습니다.",
				null
			));
	}
}
