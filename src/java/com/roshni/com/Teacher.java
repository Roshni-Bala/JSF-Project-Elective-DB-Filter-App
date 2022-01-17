package com.roshni.com;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Roshni
 */
@javax.faces.bean.ManagedBean
@RequestScoped
public class Teacher implements Serializable {

    //variables
    String u_sname;
    int u_cid, u_sem;
    ResultSet res, res1;
    Teacher obj, obj1;

    ArrayList teacherAL = new ArrayList();
    ArrayList studentAL = new ArrayList();

    String sname;
    String name;
    int tid;
    int courseid;

    String course;
    int semester;
    int vacancy;
    
    //gettter and setter methods for all declared variables
    public ArrayList getTeacherAL() {
        return teacherAL;
    }
    public void setTeacherAL(ArrayList teacherAL) {
        this.teacherAL = teacherAL;
    }
    public ArrayList getStudentAL() {
        return studentAL;
    }
    public void setStudentAL(ArrayList studentAL) {
        this.studentAL = studentAL;
    }
    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public int getCourseid() {
        return courseid;
    }
    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    public String getName() {
        return name;
    }

    public int getTid() {
        return tid;
    }

    public String getCourse() {
        return course;
    }

    public int getSemester() {
        return semester;
    }

    public int getVacancy() {
        return vacancy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setVacancy(int vacancy) {
        this.vacancy = vacancy;
    }

    //submit the provided details to query and send data to the data table in displaypage.xhtml
    public String submit() {
        u_sname = getSname();
        u_sem = getSemester();
        u_cid = getCourseid();
        System.out.println("Submit method invoked.");
        System.out.println(u_sname + " has filled out the form!");
        //all entries
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/electives", "root", "");
            System.out.println("\nConnection made with database.\n");
            Statement s = c.createStatement();
            res = s.executeQuery("select * from teacher order by semester;");
            System.out.println("Excecuting query");
            while (res.next()) {
                obj = new Teacher();
                obj.setName(res.getString("name"));
                obj.setTid(res.getInt("tid"));
                obj.setCourse(res.getString("course"));
                obj.setCourseid(res.getInt("courseid"));
                obj.setSemester(res.getInt("semester"));
                obj.setVacancy(res.getInt("vacancy"));
                teacherAL.add(obj);
            }
            System.out.println("Records fetched:" + teacherAL.size());
            setTeacherAL(teacherAL);
            //student based on semester
            res1 = s.executeQuery("select name,tid,course,courseid,vacancy from teacher where courseid = '" + u_cid + "' and semester = '" + u_sem + "';");
            System.out.println("Excecuting query");
            while (res1.next()) {
                obj1 = new Teacher();
                obj1.setName(res1.getString("name"));
                obj1.setTid(res1.getInt("tid"));
                obj1.setCourse(res1.getString("course"));
                obj1.setCourseid(res1.getInt("courseid"));
                obj1.setVacancy(res1.getInt("vacancy"));
                studentAL.add(obj1);
            }
            System.out.println("Records fetched:" + studentAL.size());
            setStudentAL(studentAL);
            c.close();
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        } catch (SQLException e) {
        }
        return "displaypage.xhtml";
    }

    //update function for decrementing vacancy number from database 
    public String update() {
        
        System.out.println("Update method invoked\n");
        int rcid = getCourseid();
        int rtid = getTid();
        int rsem = getSemester();
        System.out.println(rcid + " " + rtid);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/electives", "root", "");
            System.out.println("\nConnection made with database.\n");
            Statement s = c.createStatement();
            s.executeUpdate("update teacher set vacancy=vacancy-1 where (semester = '" + rsem + "' or courseid='" + rcid + "') and tid ='" + rtid + "' and vacancy>0;");
            System.out.println("Excecuting query");
            System.out.println("Update made");
            System.out.println(rcid + " " + rtid + " registered. Vacancy updated.");

            c.close();
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        } catch (SQLException e) {
        }

        return "index.xhtml";
    }
}
