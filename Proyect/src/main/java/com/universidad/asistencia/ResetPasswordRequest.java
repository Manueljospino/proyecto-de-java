package com.universidad.asistencia;

public class ResetPasswordRequest {
    private String numberId;
    private String newPassword;

    public String getNumberId() { return numberId; }
    public void setNumberId(String numberId) { this.numberId = numberId; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
