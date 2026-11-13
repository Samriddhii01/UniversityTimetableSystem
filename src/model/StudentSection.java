package model;
public class StudentSection {
    private final String sectionId, sectionName; private final int studentCount;
    public StudentSection(String sectionId,String sectionName,int studentCount){
        if(studentCount<1) throw new IllegalArgumentException("Student count must be positive.");
        this.sectionId=sectionId;this.sectionName=sectionName;this.studentCount=studentCount;
    }
    public String getSectionId(){return sectionId;} public String getSectionName(){return sectionName;}
    public int getStudentCount(){return studentCount;}
    public String toString(){return sectionId+" - "+sectionName+" ("+studentCount+" students)";}
}
