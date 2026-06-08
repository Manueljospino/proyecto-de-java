package com.universidad.asistencia.Entities;

public class InscriptionRequest {
    private String studentNumberId;
    private Long subjectId;

    public String getStudentNumberId() { return studentNumberId; }
    public void setStudentNumberId(String studentNumberId) { this.studentNumberId = studentNumberId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
}