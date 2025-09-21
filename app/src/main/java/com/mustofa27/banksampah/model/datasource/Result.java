package com.mustofa27.banksampah.model.datasource;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private Result() {
    }

    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return success.getMessage();
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return error.getError();
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends Result {
        private T data;
        private String message;

        public Success(T data, String message) {
            this.data = data;
            this.message = message;
        }

        public T getData() {
            return this.data;
        }

        public String getMessage() {
            return message;
        }
    }

    // Error sub-class
    public final static class Error extends Result {
        private String error;

        public Error(String error) {
            this.error = error;
        }

        public String getError() {
            return this.error;
        }
    }
}