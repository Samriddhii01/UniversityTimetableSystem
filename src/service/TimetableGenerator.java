package service;
import java.time.*;import java.util.*;import model.*;
public class TimetableGenerator {
 public String generate(List<Subject> subjects,List<Faculty> faculties,List<Room> rooms,List<StudentSection> sections,ScheduleService service){int added=0,failed=0;StringBuilder report=new StringBuilder("AUTOMATIC TIMETABLE GENERATION\n");
  if(subjects.isEmpty()||faculties.isEmpty()||rooms.isEmpty()||sections.isEmpty())return "Register faculty, subjects, rooms and sections first.";
  for(Subject subject:subjects){Faculty faculty=null;for(Faculty f:faculties)if(f.getFacultyId().equalsIgnoreCase(subject.getFacultyId()))faculty=f;if(faculty==null){report.append("No faculty for ").append(subject.getSubjectName()).append("\n");continue;}
   for(StudentSection section:sections){int existing=0;for(ClassSchedule s:service.getAllSchedules())if(s.getSubject().getSubjectId().equalsIgnoreCase(subject.getSubjectId())&&s.getSection().getSectionId().equalsIgnoreCase(section.getSectionId()))existing++;int needed=Math.max(0,subject.getWeeklyClasses()-existing);
    for(int n=0;n<needed;n++){ClassSchedule candidate=findSlot(subject,faculty,section,rooms,service);if(candidate!=null&&service.addSchedule(candidate))added++;else{failed++;report.append("Could not schedule ").append(subject.getSubjectName()).append(" for ").append(section.getSectionName()).append("\n");break;}}
   }
  }return report.append("\nClasses added: ").append(added).append("\nClasses left unscheduled: ").append(failed).toString();
 }
 private ClassSchedule findSlot(Subject subject,Faculty faculty,StudentSection section,List<Room> rooms,ScheduleService service){for(DayOfWeek day:DayOfWeek.values()){if(day==DayOfWeek.SUNDAY)continue;boolean subjectThatDay=false;for(ClassSchedule s:service.getAllSchedules())if(s.getSubject().getSubjectId().equalsIgnoreCase(subject.getSubjectId())&&s.getSection().getSectionId().equalsIgnoreCase(section.getSectionId())&&s.getTimeSlot().getDay()==day)subjectThatDay=true;if(subjectThatDay)continue;
  for(int h=9;h<17;h++){TimeSlot t=new TimeSlot(day,LocalTime.of(h,0),LocalTime.of(h+1,0));for(Room room:rooms){if(room.getCapacity()<section.getStudentCount())continue;ClassSchedule c=new ClassSchedule(subject,faculty,section,room,t);if(service.checkConflicts(c).isEmpty())return c;}}}return null;}
}
