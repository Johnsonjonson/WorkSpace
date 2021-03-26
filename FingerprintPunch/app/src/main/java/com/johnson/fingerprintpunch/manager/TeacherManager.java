package com.johnson.fingerprintpunch.manager;

import com.johnson.fingerprintpunch.bean.Teacher;

public class TeacherManager {
    public static Teacher s_teacher;
    public static void setTeacher(Teacher teacher){
        s_teacher = teacher;
    }

    public static Teacher getTeacher(){
        return s_teacher;
    }
}
