package service;
import java.util.*; import model.*;
public class ConflictDetector {
 public List<String> detectConflicts(ClassSchedule p,List<ClassSchedule> existing){
  List<String> out=new ArrayList<>();
  for(ClassSchedule e:existing){if(!p.getTimeSlot().overlaps(e.getTimeSlot()))continue;
   if(p.getFaculty().getFacultyId().equalsIgnoreCase(e.getFaculty().getFacultyId()))out.add("Faculty clash: "+p.getFaculty().getName()+" is teaching "+e.getSubject().getSubjectName()+" at "+e.getTimeSlot());
   if(p.getRoom().getRoomId().equalsIgnoreCase(e.getRoom().getRoomId()))out.add("Room clash: "+p.getRoom().getRoomName()+" is booked at "+e.getTimeSlot());
   if(p.getSection().getSectionId().equalsIgnoreCase(e.getSection().getSectionId()))out.add("Section clash: "+p.getSection().getSectionName()+" has "+e.getSubject().getSubjectName()+" at "+e.getTimeSlot());
  } return out;
 }
}
