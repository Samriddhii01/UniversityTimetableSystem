package service;
import java.time.*; import java.util.*; import java.nio.charset.StandardCharsets; import java.util.Base64; import model.*;
public class DataPersistenceService {
 private final FileService fs=new FileService();
 private String enc(String... fields){StringJoiner j=new StringJoiner(",");for(String f:fields)j.add(Base64.getEncoder().encodeToString(f.getBytes(StandardCharsets.UTF_8)));return j.toString();}
 private String[] dec(String s){String[] a=s.split(",",-1);for(int i=0;i<a.length;i++)a[i]=new String(Base64.getDecoder().decode(a[i]),StandardCharsets.UTF_8);return a;}
 public void saveAll(FacultyService f,SubjectService su,RoomService r,SectionService se,ScheduleService sc){List<String> a=new ArrayList<>();for(Faculty x:f.getAllFaculty())a.add(enc(x.getFacultyId(),x.getName(),x.getDepartment()));fs.saveRecords("faculty.csv",a);
 a.clear();for(Subject x:su.getAllSubjects())a.add(enc(x.getSubjectId(),x.getSubjectName(),x.getFacultyId(),""+x.getWeeklyClasses()));fs.saveRecords("subjects.csv",a);
 a.clear();for(Room x:r.getAllRooms())a.add(enc(x.getRoomId(),x.getRoomName(),""+x.getCapacity(),x.getRoomType()));fs.saveRecords("rooms.csv",a);
 a.clear();for(StudentSection x:se.getAllSections())a.add(enc(x.getSectionId(),x.getSectionName(),""+x.getStudentCount()));fs.saveRecords("sections.csv",a);
 a.clear();for(ClassSchedule x:sc.getAllSchedules())a.add(enc(x.getSubject().getSubjectId(),x.getSection().getSectionId(),x.getRoom().getRoomId(),x.getTimeSlot().getDay().name(),x.getTimeSlot().getStartTime().toString(),x.getTimeSlot().getEndTime().toString()));fs.saveRecords("timetable.csv",a);}
 public void loadAll(FacultyService f,SubjectService su,RoomService r,SectionService se,ScheduleService sc){
  for(String s:fs.loadRecords("faculty.csv"))try{String[] a=dec(s);f.addFaculty(new Faculty(a[0],a[1],a[2]));}catch(Exception e){System.err.println("Skipped invalid faculty record.");}
  for(String s:fs.loadRecords("subjects.csv"))try{String[] a=dec(s);if(f.findFaculty(a[2])!=null)su.addSubject(new Subject(a[0],a[1],a[2],Integer.parseInt(a[3])));}catch(Exception e){System.err.println("Skipped invalid subject record.");}
  for(String s:fs.loadRecords("rooms.csv"))try{String[] a=dec(s);r.addRoom(new Room(a[0],a[1],Integer.parseInt(a[2]),a[3]));}catch(Exception e){System.err.println("Skipped invalid room record.");}
  for(String s:fs.loadRecords("sections.csv"))try{String[] a=dec(s);se.addSection(new StudentSection(a[0],a[1],Integer.parseInt(a[2])));}catch(Exception e){System.err.println("Skipped invalid section record.");}
  for(String s:fs.loadRecords("timetable.csv"))try{String[] a=dec(s);Subject sub=su.findSubject(a[0]);StudentSection sec=se.findSectionById(a[1]);Room room=r.findRoomById(a[2]);if(sub==null||sec==null||room==null)continue;Faculty fac=f.findFaculty(sub.getFacultyId());if(fac==null||room.getCapacity()<sec.getStudentCount())continue;sc.addSchedule(new ClassSchedule(sub,fac,sec,room,new TimeSlot(DayOfWeek.valueOf(a[3]),LocalTime.parse(a[4]),LocalTime.parse(a[5]))));}catch(Exception e){System.err.println("Skipped invalid timetable record.");}
 }
}
