package com.remitro.auth.presentation.handler;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.common.error.response.ErrorResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
		log.warn("[✅ LOGGER] BAD REQUEST: CODE={}, MESSAGE={}", e.getErrorCode().getCode(), e.getMessage());

		return ResponseEntity.badRequest().body(ErrorResponse.from(e));
	}

	@ExceptionHandler(UnauthorizedException.class)
	protected ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException e) {
		log.warn("[✅ LOGGER] UNAUTHORIZED: CODE={}, MESSAGE={}", e.getErrorCode().getCode(), e.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.from(e));
	}

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
		log.warn("[✅ LOGGER] NOT FOUND: CODE={}, MESSAGE={}", e.getErrorCode().getCode(), e.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
		Map<String, String> validationMap = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				DefaultMessageSourceResolvable::getDefaultMessage
			));

		return ResponseEntity.badRequest()
			.body(new ErrorResponse(
				"COMMON_400_VALIDATION",
				"입력값이 유효하지 않습니다.",
				validationMap
			));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("[✅ LOGGER] UNKNOWN ERROR", e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(
				ErrorCode.UNKNOWN_SERVER_ERROR.getCode(),
				ErrorMessage.UNKNOWN_SERVER_ERROR.getMessage(),
				null
			));
	}
}
