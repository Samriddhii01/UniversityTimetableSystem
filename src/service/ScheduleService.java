package service;
import java.util.*; import model.ClassSchedule;
public class ScheduleService {
 private final List<ClassSchedule> schedules=new ArrayList<>(); private final ConflictDetector detector=new ConflictDetector();
 public boolean addSchedule(ClassSchedule s){if(!checkConflicts(s).isEmpty())return false;schedules.add(s);return true;}
 public List<ClassSchedule> getAllSchedules(){return new ArrayList<>(schedules);}
 public List<String> checkConflicts(ClassSchedule s){return detector.detectConflicts(s,schedules);}
}
