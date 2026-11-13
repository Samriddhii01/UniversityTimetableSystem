import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import service.*;

public class TimetableGUI extends JFrame {
    private final FacultyService faculty = new FacultyService();
    private final SubjectService subjects = new SubjectService();
    private final RoomService rooms = new RoomService();
    private final SectionService sections = new SectionService();
    private final ScheduleService schedules = new ScheduleService();
    private final DataPersistenceService persistence = new DataPersistenceService();
    private final TimetableGenerator generator = new TimetableGenerator();
    private final TimetableExportService exporter = new TimetableExportService();
    private final JTextArea status = new JTextArea(4, 30);
    private final String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private final DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public TimetableGUI() {
        super("University Timetable Scheduling & Conflict Resolution System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 760); setLocationRelativeTo(null); setLayout(new BorderLayout(8, 8));
        persistence.loadAll(faculty, subjects, rooms, sections, schedules);
        JPanel buttons = new JPanel(new GridLayout(2, 5, 6, 6));
        addButton(buttons, "Add Faculty", this::addFaculty);
        addButton(buttons, "Add Subject", this::addSubject);
        addButton(buttons, "Add Room", this::addRoom);
        addButton(buttons, "Add Section", this::addSection);
        addButton(buttons, "Schedule Class", this::scheduleClass);
        addButton(buttons, "Check Conflicts", this::checkConflicts);
        addButton(buttons, "Generate Timetable", this::generate);
        addButton(buttons, "Refresh Grid", this::refreshGrid);
        addButton(buttons, "Export CSV", this::export);
        addButton(buttons, "Save Data", this::save);
        JLabel heading = new JLabel("UNIVERSITY TIMETABLE SCHEDULING SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 20));
        JPanel top = new JPanel(new BorderLayout(5, 5)); top.add(heading, BorderLayout.NORTH); top.add(buttons, BorderLayout.CENTER);
        table.setRowHeight(85); table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        status.setEditable(false); status.setLineWrap(true); status.setWrapStyleWord(true);
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(new JScrollPane(status), BorderLayout.SOUTH);
        refreshGrid();
    }
    private void addButton(JPanel p, String label, Runnable action) { JButton b = new JButton(label); b.addActionListener(e -> action.run()); p.add(b); }
    private String ask(String prompt) { return JOptionPane.showInputDialog(this, prompt); }
    private void message(String text) { status.setText(text); }
    private void save() { persistence.saveAll(faculty, subjects, rooms, sections, schedules); message("Data saved to the data folder."); }
    private boolean required(String... values) { for (String s : values) if (s == null || s.isBlank()) { message("All fields are required."); return false; } return true; }

    private void addFaculty() {
        String id=ask("Faculty ID:"), name=ask("Faculty name:"), dept=ask("Department:");
        if (!required(id,name,dept)) return;
        if (faculty.addFaculty(new Faculty(id.trim(),name.trim(),dept.trim()))) { save(); message("Faculty added."); }
        else message("That faculty ID already exists.");
    }
    private void addSubject() {
        String id=ask("Subject ID:"), name=ask("Subject name:"), fid=ask("Assigned faculty ID:");
        if (!required(id,name,fid)) return;
        String count=ask("Classes per week (1-6):"); if(count==null)return;
        try { int n=Integer.parseInt(count.trim()); if(n<1||n>6)throw new NumberFormatException();
            if(faculty.findFaculty(fid.trim())==null){message("Register the faculty member first.");return;}
            if(subjects.addSubject(new Subject(id.trim(),name.trim(),fid.trim(),n))){save();message("Subject added.");}else message("That subject ID already exists.");
        } catch(NumberFormatException e){message("Classes per week must be a number from 1 to 6.");}
    }
    private void addRoom() {
        String id=ask("Room ID:"), name=ask("Room name:"), cap=ask("Room capacity:"), type=ask("Room type (CLASSROOM/LAB):");
        if(!required(id,name,cap,type))return;
        try {int c=Integer.parseInt(cap.trim());if(c<1)throw new NumberFormatException();
            if(rooms.addRoom(new Room(id.trim(),name.trim(),c,type.trim().toUpperCase()))){save();message("Room added.");}else message("That room ID already exists.");
        }catch(NumberFormatException e){message("Capacity must be a positive whole number.");}
    }
    private void addSection() {
        String id=ask("Section ID:"), name=ask("Section name:"), count=ask("Number of students:");
        if(!required(id,name,count))return;
        try {int n=Integer.parseInt(count.trim());if(n<1)throw new NumberFormatException();
            if(sections.addSection(new StudentSection(id.trim(),name.trim(),n))){save();message("Section added.");}else message("That section ID already exists.");
        }catch(NumberFormatException e){message("Student count must be a positive whole number.");}
    }
    private ClassSchedule collectSchedule() {
        if(subjects.getAllSubjects().isEmpty()||rooms.getAllRooms().isEmpty()||sections.getAllSections().isEmpty()) {message("Add subjects, rooms and sections first.");return null;}
        JComboBox<Subject> sub=new JComboBox<>(subjects.getAllSubjects().toArray(new Subject[0]));
        JComboBox<StudentSection> sec=new JComboBox<>(sections.getAllSections().toArray(new StudentSection[0]));
        JComboBox<Room> room=new JComboBox<>(rooms.getAllRooms().toArray(new Room[0]));
        JComboBox<String> day=new JComboBox<>(new String[]{"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"});
        JTextField start=new JTextField("09:00"), end=new JTextField("10:00");
        JPanel p=new JPanel(new GridLayout(0,2,7,7));
        p.add(new JLabel("Subject:"));p.add(sub);p.add(new JLabel("Section:"));p.add(sec);p.add(new JLabel("Room:"));p.add(room);
        p.add(new JLabel("Day:"));p.add(day);p.add(new JLabel("Start (HH:mm):"));p.add(start);p.add(new JLabel("End (HH:mm):"));p.add(end);
        if(JOptionPane.showConfirmDialog(this,p,"Schedule class",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return null;
        Subject s=(Subject)sub.getSelectedItem();StudentSection st=(StudentSection)sec.getSelectedItem();Room r=(Room)room.getSelectedItem();
        if(s==null||st==null||r==null)return null;
        if(r.getCapacity()<st.getStudentCount()){message("Room capacity is too small for this section.");return null;}
        Faculty f=faculty.findFaculty(s.getFacultyId());if(f==null){message("Assigned faculty not found.");return null;}
        try{return new ClassSchedule(s,f,st,r,new TimeSlot(DayOfWeek.valueOf((String)day.getSelectedItem()),LocalTime.parse(start.getText().trim()),LocalTime.parse(end.getText().trim())));}
        catch(Exception e){message("Invalid time. Use HH:mm, and make sure end is after start.");return null;}
    }
    private void scheduleClass() {
        ClassSchedule s=collectSchedule();if(s==null)return;List<String> conflicts=schedules.checkConflicts(s);
        if(!conflicts.isEmpty()){message("Cannot schedule class:\n- "+String.join("\n- ",conflicts));return;}
        if(schedules.addSchedule(s)){save();refreshGrid();message("Class scheduled successfully.");}
    }
    private void checkConflicts() {
        ClassSchedule s=collectSchedule();if(s==null)return;List<String> c=schedules.checkConflicts(s);
        message(c.isEmpty()?"No conflicts found. This slot is available.":"Conflicts found:\n- "+String.join("\n- ",c));
    }
    private void generate() {message(generator.generate(subjects.getAllSubjects(),faculty.getAllFaculty(),rooms.getAllRooms(),sections.getAllSections(),schedules));save();refreshGrid();}
    private void refreshGrid() {
        model.setRowCount(0);for(int h=9;h<17;h++)model.addRow(new Object[]{String.format("%02d:00 - %02d:00",h,h+1),"","","","","",""});
        List<ClassSchedule> all=schedules.getAllSchedules();all.sort(Comparator.comparing((ClassSchedule s)->s.getTimeSlot().getDay()).thenComparing(s->s.getTimeSlot().getStartTime()));
        for(ClassSchedule s:all){int col=s.getTimeSlot().getDay().getValue();int row=s.getTimeSlot().getStartTime().getHour()-9;if(col<1||col>6||row<0||row>=model.getRowCount())continue;
            String text=s.getSubject().getSubjectName()+"\nSection: "+s.getSection().getSectionName()+"\nRoom: "+s.getRoom().getRoomName()+"\n"+s.getTimeSlot().getStartTime()+"-"+s.getTimeSlot().getEndTime();
            Object old=model.getValueAt(row,col);if(old!=null&&!old.toString().isBlank())text=old+"\n\n"+text;model.setValueAt(text,row,col);
        } message("Timetable refreshed. Scheduled classes: "+all.size());
    }
    private void export() {try {java.nio.file.Path p=exporter.exportTimetable(schedules.getAllSchedules());message("Timetable exported to:\n"+p.toAbsolutePath());}catch(IOException e){message("Export failed: "+e.getMessage());}}
}
