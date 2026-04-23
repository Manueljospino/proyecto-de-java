package com.universidad.asistencia;

public class RegisterRequest {
    private String name;
    private String mail;
    private String password;
    private String role;
    private String numberId;

    public RegisterRequest() {}

    public RegisterRequest(String name, String mail, String password, String role, String numberId){
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.role = role;
        this.numberId = numberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }
}
