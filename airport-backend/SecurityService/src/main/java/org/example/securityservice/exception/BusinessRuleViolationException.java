package org.example.securityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException() {
        super();
    }

    public BusinessRuleViolationException(String message) {
        super(message);
    }

    public BusinessRuleViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessRuleViolationException(Throwable cause) {
        super(cause);
    }

    // Możesz dodać dodatkowe pola dla specyficznych kodów błędów biznesowych
    private String errorCode;
    private String fieldName;

    public BusinessRuleViolationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessRuleViolationException(String message, String errorCode, String fieldName) {
        super(message);
        this.errorCode = errorCode;
        this.fieldName = fieldName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    // Predefiniowane kody błędów biznesowych
    public static class ErrorCodes {
        public static final String INVALID_STATUS_TRANSITION = "BR001";
        public static final String INSUFFICIENT_PERMISSIONS = "BR002";
        public static final String TEAM_NOT_AVAILABLE = "BR003";
        public static final String QUALIFICATION_MISMATCH = "BR004";
        public static final String CLOSURE_REPORT_REQUIRED = "BR005";
        public static final String INVALID_INCIDENT_DATA = "BR006";
        public static final String LOCATION_NOT_FOUND = "BR007";
        public static final String EMPLOYEE_NOT_FOUND = "BR008";
        public static final String TEAM_NOT_FOUND = "BR009";
        public static final String RESOURCE_NOT_FOUND = "BR010";
        public static final String DUPLICATE_REPORT_NUMBER = "BR011";
        public static final String SENSOR_ZONE_NOT_FOUND = "BR012";
        public static final String INVALID_SENSOR_DATA = "BR013";
    }
}