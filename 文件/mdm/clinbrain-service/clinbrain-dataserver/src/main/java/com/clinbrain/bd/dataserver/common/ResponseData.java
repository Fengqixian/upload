package com.clinbrain.bd.dataserver.common;
import org.springframework.util.StringUtils;

public class ResponseData<T> {
    public enum Status {
        SUCCESS(1), ERROR(0);
        private Integer code;

        Status(int i) {
            this.code = i;
        }

        public Integer getCode() {
            return code;
        }
    }

    private Integer status;
    private String message;
    private T data;

    private ResponseData(Builder<T> builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.data = builder.data;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static class Builder<T> {
        private Integer status = Status.SUCCESS.getCode();
        private String message = "操作成功 !";
        private T data = null;

        public Builder() {
            this.data = null;
        }

        public Builder(T data) {
            this.data = data;
        }

        public Builder status(Status status) {
            this.status = status.getCode();
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(T data) {
            this.data = data;
            return this;
        }

        public ResponseData build() {
            return new ResponseData<>(this);
        }

        public ResponseData error(String message) {
            this.status = Status.ERROR.getCode();
            this.message = StringUtils.isEmpty(message) ? "error !" : message;

            return new ResponseData<>(this);
        }

        public ResponseData success(String message) {
            if (!StringUtils.isEmpty(message)) {
                this.message = message;
            }
            return new ResponseData<>(this);
        }

        public ResponseData success() {
            this.status = Status.SUCCESS.getCode();
            return new ResponseData<>(this);
        }
    }
}
