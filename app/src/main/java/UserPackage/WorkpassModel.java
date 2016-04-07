package UserPackage;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

import UserPackage.Company;

/**
 * Created by oladahl on 16-03-28.
 */
public class WorkpassModel implements Serializable{
    private static final long serialVersionUID = 1L;
    private long id;
    private int userId;
    private Company company;
    private String title;
    private GregorianCalendar startDateTime;
    private GregorianCalendar endDateTime;
    private double breaktime;
    private double workingHours;
    private double salary;
    private String note;

    @Override
    public String toString() {
        return "WorkpassModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", company=" + company +
                ", title='" + title + '\'' +
                ", startDateTime=" + startDateTime.toString() +
                ", endDateTime=" + endDateTime.toString() +
                ", breaktime=" + breaktime +
                ", workingHours=" + workingHours +
                ", salary=" + salary +
                ", note='" + note + '\'' +
                '}';
    }

    public WorkpassModel(String title, double salary, Company company, GregorianCalendar startDateTime, GregorianCalendar endDateTime, double breaktime, double workingHours, String note) {
        this.title = title;
        this.company = company;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.breaktime = breaktime;
        this.workingHours = workingHours;
        this.salary = salary;
        this.note = note;
    }

    public WorkpassModel(){
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany(){
        return company;
    }

    public void setStartDateTime(GregorianCalendar startDateTime){
        this.startDateTime = startDateTime;
    }

    public GregorianCalendar getStartDateTime() {
        return startDateTime;
    }

    public void setEndDateTime(GregorianCalendar endDateTime) {
        this.endDateTime = endDateTime;
    }

    public GregorianCalendar getEndDateTime() {
        return endDateTime;
    }

    public void setSalary(double salary){
        this.salary = salary;
    }

    public double getSalary(){
        return salary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBreaktime() {
        return breaktime;
    }

    public void setBreaktime(double breaktime) {
        this.breaktime = breaktime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }
}
