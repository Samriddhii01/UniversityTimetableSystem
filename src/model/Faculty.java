package model;
public class Faculty {
    private final String facultyId, name, department;
    public Faculty(String facultyId, String name, String department) {
        this.facultyId=facultyId; this.name=name; this.department=department;
    }
    public String getFacultyId(){return facultyId;} public String getName(){return name;}
    public String getDepartment(){return department;}
    public String toString(){return facultyId+" - "+name;}
}
