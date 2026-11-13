package service;
import java.io.*; import java.nio.file.*; import java.util.*;
public class FileService {
 private final Path dir=Paths.get("data");
 public FileService(){try{Files.createDirectories(dir);}catch(IOException e){throw new IllegalStateException("Cannot create data folder",e);}}
 public void saveRecords(String name,List<String> records){try(BufferedWriter w=Files.newBufferedWriter(path(name))){for(String r:records){w.write(r);w.newLine();}}catch(IOException e){System.err.println("Save failed: "+e.getMessage());}}
 public List<String> loadRecords(String name){List<String> out=new ArrayList<>();Path p=path(name);if(!Files.exists(p))return out;try{out=Files.readAllLines(p);}catch(IOException e){System.err.println("Load failed: "+e.getMessage());}return out;}
 private Path path(String n){if(n==null||!n.matches("[A-Za-z0-9_-]+\\.csv"))throw new IllegalArgumentException("Invalid file name");return dir.resolve(n);}
}
