package com.example.demo.endpoint.rest.exception;

public class ForbiddenOperationException extends RuntimeException {
  public ForbiddenOperationException(String message) {
    super(message);
  }
}
