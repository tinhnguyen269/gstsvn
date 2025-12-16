//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.serviceapp.admin.authenticate.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDTO {
    private @NotBlank(
            message = "Mật khẩu cũ không được để trống"
    ) String oldPassword;
    private @NotBlank(
            message = "Mật khẩu mới không được để trống"
    ) @Size(
            min = 6,
            max = 20,
            message = "Mật khẩu phải từ 6 đến 20 ký tự"
    ) String newPassword;
    @Transient
    private @NotBlank(
            message = "Vui lòng nhập lại mật khẩu mới"
    ) String passwordConfirmation;

    public ChangePasswordDTO() {
    }

    public String getOldPassword() {
        return this.oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordConfirmation() {
        return this.passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
