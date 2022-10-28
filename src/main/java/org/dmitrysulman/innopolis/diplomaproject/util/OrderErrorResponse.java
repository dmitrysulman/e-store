package org.dmitrysulman.innopolis.diplomaproject.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderErrorResponse {
    private String message;
    private Instant timestamp;
    private int status;
    private List<ProductError> productErrors;

    public OrderErrorResponse() {
    }

    public OrderErrorResponse(String message, Instant timestamp, int status) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }

    public void addProductError(int productId, String message) {
        if (productErrors == null) {
            productErrors = new ArrayList<>();
        }
        productErrors.add(new ProductError(productId, message));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ProductError> getProductErrors() {
        return productErrors;
    }

    public void setProductErrors(List<ProductError> productErrors) {
        this.productErrors = productErrors;
    }

    private static class ProductError {
        private int productId;
        private String message;

        ProductError() {
        }

        ProductError(int productId, String message) {
            this.productId = productId;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }
    }
}
