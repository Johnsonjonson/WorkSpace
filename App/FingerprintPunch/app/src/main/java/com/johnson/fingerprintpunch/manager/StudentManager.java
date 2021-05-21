package com.johnson.fingerprintpunch.manager;

import com.johnson.fingerprintpunch.bean.Student;

public class StudentManager {

    public static Student s_student;
    public static void setStudent(Student student){
        s_student = student;
    }

    public static Student getStudent(){
        return s_student;
    }
}
