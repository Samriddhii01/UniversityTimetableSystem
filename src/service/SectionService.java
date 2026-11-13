package service;
import java.util.*; import model.StudentSection;
public class SectionService {
 private final List<StudentSection> list=new ArrayList<>();
 public boolean addSection(StudentSection x){if(findSectionById(x.getSectionId())!=null)return false;list.add(x);return true;}
 public StudentSection findSectionById(String id){for(StudentSection x:list)if(x.getSectionId().equalsIgnoreCase(id))return x;return null;}
 public List<StudentSection> getAllSections(){return new ArrayList<>(list);}
}
