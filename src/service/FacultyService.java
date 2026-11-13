package service;
import java.util.*; import model.Faculty;
public class FacultyService {
 private final List<Faculty> list=new ArrayList<>();
 public boolean addFaculty(Faculty x){if(findFaculty(x.getFacultyId())!=null)return false;list.add(x);return true;}
 public Faculty findFaculty(String id){for(Faculty x:list)if(x.getFacultyId().equalsIgnoreCase(id))return x;return null;}
 public List<Faculty> getAllFaculty(){return new ArrayList<>(list);}
}
