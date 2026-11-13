package model;
import java.time.DayOfWeek; import java.time.LocalTime;
public class TimeSlot {
    private final DayOfWeek day; private final LocalTime startTime,endTime;
    public TimeSlot(DayOfWeek day,LocalTime startTime,LocalTime endTime){
        if(day==null||startTime==null||endTime==null||!startTime.isBefore(endTime)) throw new IllegalArgumentException("Invalid day or time range.");
        this.day=day;this.startTime=startTime;this.endTime=endTime;
    }
    public DayOfWeek getDay(){return day;} public LocalTime getStartTime(){return startTime;} public LocalTime getEndTime(){return endTime;}
    public boolean overlaps(TimeSlot other){return day==other.day&&startTime.isBefore(other.endTime)&&other.startTime.isBefore(endTime);}
    public String toString(){return day+" "+startTime+"-"+endTime;}
}
