import java.util.Scanner;

public class main {
    public static void main (String[] args) {
        Scanner login = new Scanner(System.in);
        
        System.out.println("Please enter your Employee ID#: ");
        
        String id = login.nextLine();

        // takes the string id and looks it up in a method to check if the ID # is in files

        //returns Welcome Message

        // The next options are:
        // Clock In
           // returns a new line that says "Clocked in at HH:MM:SS DAY/MONTH/YEAR"
           // returns back to menu page
           // if tries to clock in again returns "Already clocked in at HH:MM:SS DAY/MONTH/YEAR"
        // Clock Out
           // retuns: 
              //Lunch
                // returns "Clocked out for Lunch at HH:MM:SS DAY/MONTH/YEAR"
                // if tries any of the opens except 'Clock In', returns "Already clocked out for lunch at HH:MM:SS DAY/MONTH/YEAR" 
                // returns back to menu
              //Break
                // returns "Clocked out for Break at HH:MM:SS DAY/MONTH/YEAR"
                // if tries any of the opens except 'Clock In', returns "Already clocked out for break at HH:MM:SS DAY/MONTH/YEAR"
                // returns back to menu
              //Clock Out
                // returns "Clocked out at HH:MM:SS DAY/MONTH/YEAR"
                // clocks back in if employee attemps so
        // View Hours
           // returns a table starting from sunday to saturday rows with date in/lunch/break/back in
        // Exit 


    }
}