package com.example.serviceapp.admin.contact;

public enum CONTACT_STATUS {
    PENDING("Chờ liên hệ"),
    APPROVED("Đã liên hệ"),
    REJECTED("Hủy bỏ");

    private final String status;

    CONTACT_STATUS(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
