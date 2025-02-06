package dev.lavan.git.difference.Config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {
    // Method to handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Return a custom error message with HTTP 400 status
        Map <String,Object>map=new HashMap<>() ;
        map.put("Error",ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    // Method to handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map <String,Object>map=new HashMap<>() ;
        map.put("Error",ex.getMessage());
        if(ex.getMessage().contains("404"))
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
       
    }
}
