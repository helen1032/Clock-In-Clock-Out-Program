import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClockInOutProgram {

    private static Map<String, Employee> employees = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadEmployeesFromCSV("employees.csv");

        System.out.print("Enter your employee ID: ");
        String employeeId = scanner.next();

        Employee employee = employees.get(employeeId);

        if (employee != null) {
            System.out.println("Welcome, " + employee.getFirstName() + " " + employee.getLastName() + "!");

            while (true) {
                System.out.println("Options:");
                System.out.println("1. Clock In");
                System.out.println("2. Clock Out");
                System.out.println("3. Lunch Clock Out");
                System.out.println("4. Break Clock Out");
                System.out.println("5. View Timecard");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        employee.clockIn();
                        break;

                    case 2:
                        employee.clockOut();
                        break;

                    case 3:
                        employee.lunchClockOut();
                        break;

                    case 4:
                        employee.breakClockOut();
                        break;

                    case 5:
                        viewTimecard(employee);
                        break;
                    case 6:
                        System.out.println("Exiting the program.");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } else {
            System.out.println("Invalid employee ID. Exiting the program.");
        }
    }

    private static void loadEmployeesFromCSV(String csvFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    String employeeId = values[0].trim();
                    String firstName = values[1].trim();
                    String lastName = values[2].trim();
                    employees.put(employeeId, new Employee(employeeId, firstName, lastName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void viewTimecard(Employee employee) {
        System.out.println("Timecard for " + employee.getFirstName() + " " + employee.getLastName());
        System.out.println("Clock-in time: " + employee.formatTime(employee.getClockInTime()));
        System.out.println("Lunch clock-out time: " + (employee.isClockedOutForLunch() ? employee.formatTime(employee.getLunchClockOutTime()) : "---"));
        System.out.println("Break clock-out time: " + (employee.isClockedOutForBreak() ? employee.formatTime(employee.getBreakClockOutTime()) : "---"));
        System.out.println("Clock-out time: " + employee.formatTime(employee.getClockOutTime()));
    
        double totalHoursWorked = calculateTotalHoursWorked(employee);
        System.out.println("Total hours worked: " + employee.formatTotalHours(totalHoursWorked));
    }
    

    private static double calculateTotalHoursWorked(Employee employee) {
        long clockInTime = employee.getClockInTime();
        long lunchClockOutTime = employee.isClockedOutForLunch() ? employee.getLunchClockOutTime() : clockInTime;
        long breakClockOutTime = employee.isClockedOutForBreak() ? employee.getBreakClockOutTime() : clockInTime;
        long clockOutTime = employee.getClockOutTime();
    
        double lunchBreakDuration = (lunchClockOutTime - clockInTime) / 3600000.0;
        double breakDuration = (breakClockOutTime - clockInTime) / 3600000.0;
    
        // Check if the employee has clocked out
        double workDuration = (clockOutTime > 0) ? ((clockOutTime - lunchClockOutTime - breakClockOutTime) / 3600000.0) : 0;
    
        return lunchBreakDuration + breakDuration + workDuration;
    }
    
    

    private static class Employee {
        private String employeeId;
        private String firstName;
        private String lastName;
        private long clockInTime;
        private long clockOutTime;
        private double totalHoursWorked;
        private boolean onBreak;
        private long lunchClockOutTime;
        private long breakClockOutTime;

        public Employee(String employeeId, String firstName, String lastName) {
            this.employeeId = employeeId;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public void clockIn() {
            clockInTime = System.currentTimeMillis();
            System.out.println("Clocked in at " + formatTime(clockInTime));
            // Save clock-in time to CSV
            saveClockInToCSV(employeeId, firstName, lastName, clockInTime);
        }

        public void clockOut() {
            clockOutTime = System.currentTimeMillis();
            double hoursWorked = (clockOutTime - clockInTime) / 3600000.0; // convert milliseconds to hours
            totalHoursWorked += hoursWorked;
            System.out.println("Clocked out at " + formatTime(clockOutTime));
            System.out.println("Total hours worked: " + formatTotalHours(totalHoursWorked));
            onBreak = false; // Reset the break status after clocking out
        }

        public void lunchClockOut() {
            if (!isClockedOutForLunch()) {
                lunchClockOutTime = System.currentTimeMillis();
                System.out.println("Lunch clock out at " + formatTime(lunchClockOutTime));
                onBreak = true; // Set the break status after clocking out for lunch
            } else {
                System.out.println("Already clocked out for lunch.");
            }
        }

        public void breakClockOut() {
            if (!isClockedOutForBreak()) {
                breakClockOutTime = System.currentTimeMillis();
                System.out.println("Break clock out at " + formatTime(breakClockOutTime));
                onBreak = true; // Set the break status after clocking out for a break
            } else {
                System.out.println("Already clocked out for a break.");
            }
        }

        public long getLunchClockOutTime() {
            return lunchClockOutTime;
        }
    
        public long getBreakClockOutTime() {
            return breakClockOutTime;
        }

        private boolean isClockedOutForLunch() {
            return onBreak && lunchClockOutTime > 0;
        }

        private boolean isClockedOutForBreak() {
            return onBreak && breakClockOutTime > 0;
        }

        private String formatTime(long timeInMillis) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(timeInMillis);
            return sdf.format(date);
        }

        private String formatTotalHours(double totalHours) {
            int hours = (int) totalHours;
            int minutes = (int) ((totalHours - hours) * 60);
            int seconds = (int) ((totalHours - hours - minutes / 60.0) * 3600);

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public long getClockInTime() {
            return clockInTime;
        }

        public long getClockOutTime() {
            return clockOutTime;
        }

        public double getTotalHoursWorked() {
            return totalHoursWorked;
        }
    }

    private static void saveClockInToCSV(String employeeId, String firstName, String lastName, long clockInTime) {
        String csvFile = "clock_in.csv"; // Update the filename

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) {
            // Write data to the CSV file
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(clockInTime);
            writer.println(employeeId + "," + firstName + "," + lastName + "," + sdf.format(date));
            System.out.println("Clock-in time saved to 'clock_in.csv' file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
