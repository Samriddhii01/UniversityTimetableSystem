package model;
public class Subject {
    private final String subjectId, subjectName, facultyId; private final int weeklyClasses;
    public Subject(String subjectId,String subjectName,String facultyId,int weeklyClasses){
        if(weeklyClasses<1) throw new IllegalArgumentException("Classes per week must be positive.");
        this.subjectId=subjectId;this.subjectName=subjectName;this.facultyId=facultyId;this.weeklyClasses=weeklyClasses;
    }
    public String getSubjectId(){return subjectId;} public String getSubjectName(){return subjectName;}
    public String getFacultyId(){return facultyId;} public int getWeeklyClasses(){return weeklyClasses;}
    public String toString(){return subjectId+" - "+subjectName;}
}
