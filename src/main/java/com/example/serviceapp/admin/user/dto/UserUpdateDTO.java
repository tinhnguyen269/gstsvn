package com.example.serviceapp.admin.user.dto;

import com.example.serviceapp.common.entity.User;
import jakarta.validation.constraints.*;

public class UserUpdateDTO {

    @NotNull(message = "ID người dùng không được để trống", groups = User.OnUpdate.class)
    private Long userId;

    @NotBlank(message = "Họ và tên không được để trống", groups = User.OnUpdate.class)
    @Size(max = 100, message = "Họ và tên tối đa 100 ký tự", groups = User.OnUpdate.class)
    private String fullName;

    @NotBlank(message = "Tên đăng nhập không được để trống", groups = User.OnUpdate.class)
    @Size(max = 100, message = "Tên đăng nhập tối đa 100 ký tự", groups = User.OnUpdate.class)
    private String username;

    @NotBlank(message = "Email không được để trống", groups = User.OnUpdate.class)
    @Email(message = "Email không hợp lệ", groups = User.OnUpdate.class)
    @Size(max = 150, message = "Email tối đa 150 ký tự", groups = User.OnUpdate.class)
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống", groups = User.OnUpdate.class)
    @Pattern(regexp = "^(0[0-9]{9})$", message = "Số điện thoại không hợp lệ", groups = User.OnUpdate.class)
    @Size(max = 10, message = "Số điện thoại tối đa 10 ký tự", groups = User.OnUpdate.class)
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống", groups = User.OnUpdate.class)
    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự", groups = User.OnUpdate.class)
    private String address;

    @NotNull(message = "Quyền không được để trống", groups = User.OnUpdate.class)
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}

