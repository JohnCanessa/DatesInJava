import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Random;


/**
 * Parking lot customers.
 */
class Customer {

    // **** member(s) ****
    public int      customerID;
    public int      spaceID;
    public double   rate;

    // **** methods ****
    public Customer(int customerID, int spaceID, double rate) {
        this.customerID = customerID;
        this.spaceID    = spaceID;
        this.rate       = rate;
    }

    @Override
    public String toString() {
        String s = String.format("%.2f", rate);
        return "[customerID: " + customerID + " spaceID: " + spaceID + " rate: " + s + "]";
    }
}


/**
 * Parking space in the parking lot.
 */
class ParkingSpace {

    // **** member(s) ****
    public int              spaceID;
    public int              customerID;
    public LocalDateTime    arrival;
    public LocalDateTime    departure;

    // **** methods ****
    public ParkingSpace(int spaceID, int customerID) {
        this.spaceID    = spaceID;
        this.customerID = customerID;
    }

    public ParkingSpace(int spaceID, int customerID, LocalDateTime arrival, LocalDateTime departure) {
        this.spaceID    = spaceID;
        this.customerID = customerID;
        this.arrival    = arrival;
        this.departure  = departure;
    }   

    @Override
    public String toString() {
        return "[spaceID: " + spaceID + " customerID: " + customerID 
                + " arrival: " + arrival + " departure: " + departure + "]";
    }
}


/**
 * Exeprimenting with dates in Java.
 */
public class DatesInJava {


    // **** constants ****
    final static int MIN_CUSTOMERS = 3;
    final static int MAX_CUSTOMERS = 5;

    final static int MIN_SPOT_ID = 3;
    final static int MAX_SPOT_ID = 5;

    final static double MIN_RATE = 5.00;
    final static double MAX_RATE = 7.00;


    /**
     * Determine gross revenue for the parking ramp for the remainder of the current month.
     * The value should be returned with no more than two decimal values.
     */
    static double grossRevenue(Month month, ArrayList<ParkingSpace> spaces, ArrayList<Customer> customers) {
       
        // ???? ????
        System.out.println("grossRevenue <<<          month: " + month.toString());
        System.out.println("grossRevenue <<<    spaces.size: " + spaces.size());
        System.out.println("grossRevenue <<< customers.size: " + customers.size());

        // **** gross sales ****
        double gross = 0.0;

        // **** loop once per space space in spaces ****
        for (int i = 0; i < spaces.size(); i++) {

            // **** get the customer ID ****
            int customerID = spaces.get(i).customerID;

            // **** get the parking rate ****
            double rate = customers.get(customerID - 1).rate;

            // **** get arrival time ****
            LocalDateTime arrival = spaces.get(i).arrival;

            // **** get the departure time ****
            LocalDateTime departure = spaces.get(i).departure;

            // **** compute time difference in minutes ****
            int arrivalInMins   = arrival.getHour() * 60 + arrival.getMinute();
            int departureInMins = departure.getHour() * 60 + departure.getMinute();
            double diffMins     = departureInMins - arrivalInMins;

            // **** compute charge ****
            double charge = (rate / 60.0) * diffMins;

            // **** update gross revenue ****
            gross += charge;  
        }

        // **** ****
        String s = String.format("%.2f", gross);
        gross = Double.parseDouble(s);

        // **** return gross revenue ****
        return gross;
    }


    /**
     * Test scaffolding.
     */
    static public void main(String[] args) {

        // **** get the current date  ****
        LocalDate localDate = LocalDate.now();
        int year            = localDate.getYear();
        int month           = localDate.getMonthValue();
        Month m             = localDate.getMonth();

        // **** display local date ****
        System.out.println("main <<< localDate: " + localDate.toString());

        // **** generate a list of customers ****
        ArrayList<Customer> customers = new ArrayList<Customer>();

        // **** generate a number of customers ****
        Random rand = new Random();
        int numOfCust = rand.nextInt(MAX_CUSTOMERS - MIN_CUSTOMERS) + MIN_CUSTOMERS;

        // **** populate list of customers ****
        int spaceID = MIN_SPOT_ID - 1;
        for (int id = 1; id <= numOfCust; id++) {

            // **** select a spot ID ****
            spaceID += 1;
            if (spaceID > MAX_SPOT_ID)
                spaceID = MIN_SPOT_ID;

            // **** compute an hourly rate ****
            Double rate = rand.nextInt((int)(MAX_RATE - MIN_RATE)) + MIN_RATE;

            // **** create a customer ****
            Customer customer = new Customer(id, spaceID, rate);

            // **** add customer to the list ****
            customers.add(customer);
        }

        // **** display customer list ****
        // customers.forEach(c -> System.out.println("main <<< " + c));
        // System.out.println();

        // **** generate list of parking spaces ****
        ArrayList<ParkingSpace> parkingSpaces = new ArrayList<ParkingSpace>();

        // **** populate parking spaces for the specified month ****
        LocalDateTime arrival   = LocalDateTime.now();
        year                    = arrival.getYear();
        month                   = arrival.getMonthValue();
        int dayOfMonth          = arrival.getDayOfMonth();
        int hour                = arrival.getHour();
        int minute              = arrival.getMinute();

        LocalDateTime departure = LocalDateTime.now();

        // **** loop once per day in the remaining days of the month ****
        int currentMonth = arrival.getMonthValue();
        while (arrival.getMonthValue() == currentMonth) {

            // **** loop once per customer ****
            for (int custID = 1; custID <= numOfCust; custID++) {

                // **** generate a random arrival time ****
                hour    = 6 + rand.nextInt(6);
                minute  = rand.nextInt(60);

                // **** ****
                arrival = LocalDateTime.of(year, month, dayOfMonth, hour, minute);

                // **** generate a random departure time ****
                hour    = 12 + rand.nextInt(12);
                minute  = rand.nextInt(60);               

                // **** ****
                departure = LocalDateTime.of(year, month, dayOfMonth, hour, minute);

                // **** customers are assigned a parking spot ****
                spaceID = custID;

                // **** ****
                ParkingSpace parkingSpace = new ParkingSpace(spaceID, custID, arrival, departure);

                // **** ****
                parkingSpaces.add(parkingSpace);
            }

            // **** increment day ****
            arrival = arrival.plusDays(1);
            dayOfMonth = arrival.getDayOfMonth();
        }

        // **** display parking spaces list ****
        // parkingSpaces.forEach( ps -> System.out.println("main <<< " + ps));
        // System.out.println();

        // **** compute the gross income for the parking lot for the specified month ****
        double gross = grossRevenue(m, parkingSpaces, customers);

        // **** display the gross income ****
        System.out.println("main <<< gross: " + gross);
    }
}