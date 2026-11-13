
package model;
public class ClassSchedule {
    private final Subject subject; private final Faculty faculty; private final StudentSection section; private final Room room; private final TimeSlot timeSlot;
    public ClassSchedule(Subject subject,Faculty faculty,StudentSection section,Room room,TimeSlot timeSlot){
        if(subject==null||faculty==null||section==null||room==null||timeSlot==null) throw new IllegalArgumentException("All schedule details are required.");
        this.subject=subject;this.faculty=faculty;this.section=section;this.room=room;this.timeSlot=timeSlot;
    }
    public Subject getSubject(){return subject;} public Faculty getFaculty(){return faculty;} public StudentSection getSection(){return section;}
    public Room getRoom(){return room;} public TimeSlot getTimeSlot(){return timeSlot;}
    public String toString(){return timeSlot+" | "+subject.getSubjectName()+" | "+section.getSectionName()+" | "+faculty.getName()+" | "+room.getRoomName();}
}
