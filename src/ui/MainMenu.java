package ui;

import api.HotelResource;
import model.IRoom;
import model.Reservation;
import service.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);

    private static Date getDate() {
        SimpleDateFormat SDFormat = new SimpleDateFormat("MM/dd/yyyy");
        do {
            try {
                String dateString = scanner.nextLine();
                return SDFormat.parse(dateString);
            } catch (ParseException ex) {
                System.out.println("Date format is invalid. Please re-enter the date (mm/dd/yyyy): ");
            }
        } while (true);
    }

    private static Date addSevenDays(Date originalDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        return calendar.getTime();
    }

    private static Collection<IRoom> findRooms(Date checkIn, Date checkOut) {
        Collection<IRoom> availableRooms = HotelResource.getInstance().findARoom(checkIn, checkOut);
        if (availableRooms.isEmpty()) {
            System.out.println("There are no rooms available for requested check-in and check-out dates.");
            System.out.println("Trying to accommodate to 7 days later...");
            availableRooms = HotelResource.getInstance().findARoom(addSevenDays(checkIn), addSevenDays(checkOut));
            if (availableRooms.isEmpty()) {
                System.out.println("Sorry, a reservation cannot be made for the requested dates now.");
            } else {
                System.out.println("Showing recommended rooms for the alternative dates:");
            }
        } else {
            System.out.println("Showing recommended rooms for the requested dates:");
        }
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }
        return availableRooms;
    }


    private static void reserveRoom(Map<String, IRoom> mapOfRooms, Date checkIn, Date checkOut) {
        System.out.println("Please enter the room number for reservation:");
        boolean foundRoom = false;
        do {
            String roomNumber = scanner.nextLine();
            if (mapOfRooms.containsKey(roomNumber)) {
                String email;
                IRoom room = mapOfRooms.get(roomNumber);
                boolean reservedRoom = false;
                System.out.println("Please enter the email address for reservation");
                do {
                    email = scanner.nextLine();
                    try {
                        HotelResource.getInstance().bookARoom(email, room, checkIn, checkOut);
                        reservedRoom = true;
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getLocalizedMessage());
                        System.out.println("Please re-enter the email address for reservation");
                    }
                } while (!reservedRoom);
                foundRoom = true;
            } else {
                System.out.println("Sorry, the room " + roomNumber + " is either unavailable or nonexistent.");
                System.out.println("Do you wish to continue making the reservation? (Y/N)");
                /* TODO Continue reserving Y/N */
                System.out.println("Please re-enter the room number for reservation:");
            }
        } while (!foundRoom);
    }

    private static void findAndReserveRoom() {
        // Get check-in and check-out dates
        boolean datesAreValid = false;
        Date checkIn;
        Date checkOut;
        do {
            System.out.println("Please enter your check-in date (mm/dd/yyyy):");
            checkIn = getDate();
            System.out.println("Please enter your check-out date (mm/dd/yyyy):");
            checkOut = getDate();
            if (checkOut.compareTo(checkIn) > 0) {
                datesAreValid = true;
            } else {
                System.out.println("Check-out date needs to be after check-in date; please re-enter the check-in and check-out dates.");
            }
        } while (!datesAreValid);

        // Find and print out the rooms available for reservation
        Collection<IRoom> availableRooms = findRooms(checkIn, checkOut);
        if (availableRooms.isEmpty()) { return; }

        // Reserve a room. Use a map for convenience
        Map<String, IRoom> mapOfRooms = new HashMap<>();
        for (IRoom room : availableRooms) {
            mapOfRooms.put(room.getRoomNumber(), room);
        }
        reserveRoom(mapOfRooms, checkIn, checkOut);
    }
    private static void displayReservation() {
        System.out.println("Please enter the email address for query:");
        String email = scanner.nextLine();
        try {
            Collection<Reservation> listOfReservations = HotelResource.getInstance().getCustomerReservations(email);
            if (listOfReservations == null) {
                throw new IllegalArgumentException("No reservations found for account " + email + ".");
            }
            for (Reservation reservation : listOfReservations) {
                System.out.println(reservation);
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }
    private static void createAccount() {
        System.out.println("Please enter your first name:");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your last name:");
        String lastName = scanner.nextLine();
        System.out.println("Please enter the email address for registration:");
        do {
            String email = scanner.nextLine();
            try {
                HotelResource.getInstance().createACustomer(email, firstName, lastName);
                System.out.println("Created account " + email + " successfully.");
                return;
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        } while (true);
    }
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Reservation Application.");
        boolean exitProgram = false;
        do {
            System.out.println("--------------------------------------------------");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            System.out.println("--------------------------------------------------");
            System.out.println("Please select a number from the menu option (1-5).");
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "1": {
                    findAndReserveRoom();
                    break;
                }
                case "2": {
                    displayReservation();
                    break;
                }
                case "3": {
                    createAccount();
                    break;
                }
                case "4": {
                    /* TODO */
                    System.out.println("Admin");
                    break;
                }
                case "5": {
                    exitProgram = true;
                    System.out.println("Thank you for using the Hotel Reservation Application.");
                    System.out.println("Have a nice day!");
                    break;
                }
                default: {
                    System.out.println("Illegal input; please select a number from the menu option (1-5).");
                }
            }
            System.out.println("Press ENTER to return to the main menu");
            scanner.nextLine();
        } while (!exitProgram);
        scanner.close();
    }
}
