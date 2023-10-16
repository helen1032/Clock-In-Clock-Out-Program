import java.time.LocalTime;
import java.time.Duration;

class Employee{
    private int employeeID;
    private String firstName;
    private String lastName;
    private LocalTime clockInTime;
    private LocalTime clockOutTime;
    private LocalTime breakTime;
    private LocalTime lunchTime;
    private Duration totalWorkHours;
    

    public Employee(int employeeID, String firstName, String lastName){
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clockInTime = LocalTime.now();
        this.clockOutTime = LocalTime.now();
        this.breakTime = LocalTime.now();
    }
}