package service;
import java.io.*;import java.nio.charset.StandardCharsets;import java.nio.file.*;import java.util.*;import model.ClassSchedule;
public class TimetableExportService {
 public Path exportTimetable(List<ClassSchedule> schedules)throws IOException{
  Path dir=Paths.get("exports");Files.createDirectories(dir);Path file=dir.resolve("university_timetable.csv");
  List<ClassSchedule> list=new ArrayList<>(schedules);list.sort(Comparator.comparing((ClassSchedule s)->s.getTimeSlot().getDay()).thenComparing(s->s.getTimeSlot().getStartTime()));
  try(BufferedWriter w=Files.newBufferedWriter(file,StandardCharsets.UTF_8)){
   w.write("Day,Start Time,End Time,Subject ID,Subject,Faculty ID,Faculty,Section ID,Section,Room ID,Room,Capacity");w.newLine();
   for(ClassSchedule s:list){String[] v={s.getTimeSlot().getDay().toString(),s.getTimeSlot().getStartTime().toString(),s.getTimeSlot().getEndTime().toString(),s.getSubject().getSubjectId(),s.getSubject().getSubjectName(),s.getFaculty().getFacultyId(),s.getFaculty().getName(),s.getSection().getSectionId(),s.getSection().getSectionName(),s.getRoom().getRoomId(),s.getRoom().getRoomName(),String.valueOf(s.getRoom().getCapacity())};
    for(int i=0;i<v.length;i++){if(i>0)w.write(",");w.write(csv(v[i]));}w.newLine();}
  }return file;
 }
 private String csv(String s){return "\""+s.replace("\"","\"\"")+"\"";}
}
