package br.coop.integrada.auth.domain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import br.coop.integrada.auth.AuthApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{
	private static final Logger logger = LoggerFactory.getLogger(AuthApplication.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity handleException(NullPointerException e) {	
		String message = "A aplicação retornou um arquivo nulo!";
		if(e.getMessage() != null) {
			message = e.getMessage();
		}
		DefaultExceptions exception = new DefaultExceptions(HttpStatus.NOT_FOUND.value(), message);
		logger.info("NullPointerException: " + e.getMessage());
		return new ResponseEntity(exception, HttpStatus.NOT_FOUND);
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(Exception e) {
		DefaultExceptions exception = new DefaultExceptions(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
				"A aplicação retornou um arquivo invalido!");
		logger.info("Exception: " + e.getMessage());
		return new ResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handleException(RuntimeException e) {
		String message = "A aplicação não conseguiu retornar sua requisição!";
		if(e.getMessage() != null) {
			message = e.getMessage();
		}
		DefaultExceptions exception = new DefaultExceptions(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
		logger.info("Exception: " + e.getMessage());
		return new ResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);		
	}
}
