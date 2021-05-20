package com.apigate.exceptions;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public interface ResponseCodes {
    enum Errors {

        CIS9999("CIS9999", "System error has occurred. Please contact application support"),
        CIS9998("CIS9998", "Input Validation Error"),
        CIS9991("CIS9991", "Endpoint doesn't exist. RTFS!"),
        CIS9992("CIS9992", "Wrong header"),
        CIS9996("CIS9996", "Business validation error"),
        CIS9993("CIS9993", "Exhausted Resource. Try again later.");

        private String code, message;

        Errors(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return code + " | " + message;
        }


    }

    enum Warns {

        CIS0003("CIS0003", "Duplicate record. Found existing record in the table"),
        CIS0004("CIS0004","Resource doesn't exist");
        private String code, message;

        Warns(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return code + " | " + message;
        }


    }
}
