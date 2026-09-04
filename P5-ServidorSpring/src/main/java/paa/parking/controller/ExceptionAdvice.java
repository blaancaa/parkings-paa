package paa.parking.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ExceptionAdvice 
{
	 @ExceptionHandler(RuntimeException.class)
	 public ResponseEntity<String> handleRuntime (RuntimeException ex) 
	 {
		 return ResponseEntity
		 .status(HttpStatus.INTERNAL_SERVER_ERROR)
		 .body(ex.getMessage());
	 }

}
