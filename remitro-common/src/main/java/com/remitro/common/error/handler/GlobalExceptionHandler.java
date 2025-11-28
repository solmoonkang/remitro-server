package com.remitro.common.error.handler;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;

import com.remitro.common.error.exception.InternalServerException;
import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.exception.ForbiddenException;
import com.remitro.common.error.exception.LockAcquireException;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.model.ErrorResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalExceptionHandler {

	@ExceptionHandler(InternalServerException.class)
	protected ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
		log.error("[✅ LOGGER] BAD REQUEST ERROR: 잘못된 요청 에러, {}", e.getMessage());
		return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), null));
	}

	@ExceptionHandler(UnauthorizedException.class)
	protected ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
		log.error("[✅ LOGGER] UNAUTHORIZED ERROR: 인증되지 않은 사용자 에러, {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage(), null));
	}

	@ExceptionHandler(ForbiddenException.class)
	protected ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
		log.error("[✅ LOGGER] FORBIDDEN ERROR: 허용되지 않은 권한 에러, {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage(), null));
	}

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
		log.error("[✅ LOGGER] NOT FOUND ERROR: 리소스를 찾을 수 없는 에러, {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage(), null));
	}

	@ExceptionHandler(ConflictException.class)
	protected ResponseEntity<ErrorResponse> handleConflictException(ConflictException e) {
		log.error("[✅ LOGGER] CONFLICT ERROR: 충돌 에러, {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage(), null));
	}

	@ExceptionHandler(InternalServerException.class)
	protected ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException e) {
		log.error("[✅ LOGGER] SERVER ERROR: 서버 에러, {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), null));
	}

	@ExceptionHandler(LockAcquireException.class)
	protected ResponseEntity<ErrorResponse> handleLockAcquireException(LockAcquireException e) {
		log.error("[✅ LOGGER] LOCK ACQURE ERROR: 락 획득 에러, {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(e.getMessage(), null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("[✅ LOGGER] METHOD ARGUMENT NOT VALID ERROR: 잘못된 요청 에러");

		Map<String, String> validationMap = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

		return ResponseEntity.badRequest().body(new ErrorResponse(
			"잘못된 요청입니다. 입력한 데이터가 유효하지 않습니다.", validationMap
		));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("[✅ LOGGER] UNKNOWN ERROR: 알 수 없는 에러", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse("서버에서 알 수 없는 에러가 발생했습니다.", null));
	}
}
