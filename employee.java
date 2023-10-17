import java.time.LocalDateTime;
import java.time.Duration;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;

class Employee{
    private int employeeID;
    private String firstName;
    private String lastName;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
    private LocalDateTime breakTime;
    private LocalDateTime lunchTime;
    private Duration totalWorkHours;
    

    public Employee(int employeeID, String firstName, String lastName){
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clockInTime = LocalDateTime.now();
        this.clockOutTime = LocalDateTime.now();
        this.breakTime = LocalDateTime.now();
        this.lunchTime = LocalDateTime.now();
        this.totalWorkHours = Duration.ZERO;
    }

    public void clockIn(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Write to the clock_in_times.csv file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("hours/clock_in_times.csv", true))) {
            writer.write(employeeID + "," + currentDateTime.toLocalDate() + "," + currentDateTime.toLocalTime() + "\n");
            System.out.println("Clock-in time saved to clock_in_times.csv file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clockOut(){
        this.clockOutTime = LocalDateTime.now();
        this.totalWorkHours = this.totalWorkHours.plus(Duration.between(clockInTime, clockOutTime));
    }

    

    // Getter for total work hours
    public Duration getTotalWorkHours(){
        return totalWorkHours;
    }
}