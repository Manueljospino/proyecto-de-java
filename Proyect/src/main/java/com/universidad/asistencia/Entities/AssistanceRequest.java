package com.universidad.asistencia.Entities;

public class AssistanceRequest {
    private String numberId;
    private String name;
    private String mail;
    private String teacherNumberId;

    public String getNumberId() { return numberId; }
    public void setNumberId(String numberId) { this.numberId = numberId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public String getTeacherNumberId() { return teacherNumberId; }
    public void setTeacherNumberId(String teacherNumberId) { this.teacherNumberId = teacherNumberId; }
}