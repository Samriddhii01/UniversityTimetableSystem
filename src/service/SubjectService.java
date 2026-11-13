package service;
import java.util.*; import model.Subject;
public class SubjectService {
 private final List<Subject> list=new ArrayList<>();
 public boolean addSubject(Subject x){if(findSubject(x.getSubjectId())!=null)return false;list.add(x);return true;}
 public Subject findSubject(String id){for(Subject x:list)if(x.getSubjectId().equalsIgnoreCase(id))return x;return null;}
 public List<Subject> getAllSubjects(){return new ArrayList<>(list);}
}
