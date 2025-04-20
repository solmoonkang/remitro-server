package com.remitroserver.global.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.remitroserver.global.error.exception.BadRequestException;
import com.remitroserver.global.error.exception.ConflictException;
import com.remitroserver.global.error.exception.NotFoundException;
import com.remitroserver.global.error.exception.RemitroServerException;
import com.remitroserver.global.error.model.ErrorMessage;
import com.remitroserver.global.error.model.ErrorResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	protected ErrorResponse handleException(Exception exception) {
		log.error("[✅ LOGGER] GLOBAL EXCEPTION HANDLER: 알 수 없는 에러", exception);
		return new ErrorResponse(ErrorMessage.UNKNOWN_SERVER_ERROR.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RemitroServerException.class)
	protected ErrorResponse handleRemitroServerException(RemitroServerException remitroServerException) {
		log.error("[✅ LOGGER] GLOBAL EXCEPTION HANDLER: 알 수 없는 에러", remitroServerException);
		return new ErrorResponse(remitroServerException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException notValidException) {
		log.error("[✅ LOGGER] GLOBAL EXCEPTION HANDLER: 유효성 검증 에러", notValidException);
		return new ErrorResponse(notValidException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	protected ErrorResponse handleException(BadRequestException badRequestException) {
		log.error("[✅ LOGGER] GLOBAL EXCEPTION HANDLER: 잘못된 요청 에러", badRequestException);
		return new ErrorResponse(badRequestException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	protected ErrorResponse handleNotFoundException(NotFoundException notFoundException) {
		log.error("[✅ LOGGER] GLOBAL EXCEPTION HANDLER: 리소스를 찾을 수 없는 에러", notFoundException);
		return new ErrorResponse(notFoundException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ConflictException.class)
	protected ErrorResponse handleConflictException(ConflictException conflictException) {
		log.error("[✅ LOGGER] GLOBAL EXCEPTION HANDLER: 충돌 에러", conflictException);
		return new ErrorResponse(conflictException.getMessage(), null);
	}
}
