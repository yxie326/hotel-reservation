package ui;

import api.AdminResource;
import api.HotelResource;
import model.*;

import java.util.*;

public class AdminMenu {
    static Scanner scanner;

    private static void displayCustomers() {
        Collection<Customer> customers = AdminResource.getInstance().getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers registered in the system.");
        } else {
            System.out.println("Displaying all customers registered in the system ...");
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    private static void displayRooms() {
        Collection<IRoom> rooms = AdminResource.getInstance().getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms available currently.");
        } else {
            System.out.println("Displaying all rooms in the system ...");
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    private static void addRooms() {
        String prompt;
        List<IRoom> rooms = new ArrayList<>();
        Set<String> roomNumbers = new HashSet<>();
        do {
            // Get room number
            System.out.println("Enter the room number:");
            // Room number is always valid, no need for a do-while loop
            String roomNumber = scanner.nextLine();
            if (HotelResource.getInstance().getRoom(roomNumber) != null || roomNumbers.contains(roomNumber)) {
                prompt = "Room number already exists. Overwrite? (Y/N)";
                YNReader.scanner = scanner;
                boolean overwrite = YNReader.ynReader(prompt);
                // if (overwrite): Continue reading room price and type.
                if (!overwrite) {
                    System.out.println("Stopped overwriting room " + roomNumber + ".");
                    prompt = "Do you want to add another room? (Y/N)";
                    YNReader.scanner = scanner;
                    boolean addAnotherRoom = YNReader.ynReader(prompt);
                    if (addAnotherRoom) {
                        System.out.println("Preparing to add another room ...");
                        continue;
                    } else { break; }
                }
            }

            // Get room price
            System.out.println("Enter the room price per night:");
            double roomPrice;
            do {
                String priceString = scanner.nextLine();
                try {
                    roomPrice = Double.parseDouble(priceString);
                    break;
                } catch (Exception ex) {
                    // The exception message is not very self-explanatory,
                    // but modifying it is a bit too tedious.
                    // I'll just use the default messages here.
                    System.out.println(ex.getLocalizedMessage());
                }
            } while (true);

            // Set room type
            System.out.println("Enter the room type (1 for single, 2 for double):");
            RoomType roomType = RoomType.UNASSIGNED;
            do {
                String roomTypeStr = scanner.nextLine();
                switch (roomTypeStr) {
                    case "1" -> roomType = RoomType.SINGLE;
                    case "2" -> roomType = RoomType.DOUBLE;
                    default -> System.out.println("Input is invalid; please enter 1 (single) or 2 (double):");
                }
            } while (roomType.equals(RoomType.UNASSIGNED));

            // Create room and add into rooms
            IRoom room;
            if (roomPrice == 0.0) {
                room = new FreeRoom(roomNumber, roomType);
            } else {
                room = new Room(roomNumber, roomPrice, roomType);
            }
            // Can handle overwriting in this form
            rooms.add(room);
            roomNumbers.add(roomNumber);

            // Add other rooms?
            prompt = "Do you want to add another room? (Y/N)";
            YNReader.scanner = scanner;
            boolean addAnotherRoom = YNReader.ynReader(prompt);
            if (addAnotherRoom) {
                System.out.println("Preparing to add another room ...");
            } else { break; }
        } while (true);
        AdminResource.getInstance().addRoom(rooms);
    }

    static void adminMenu() {
        System.out.println("Welcome to the admin interface!");
        do {
            System.out.println("--------------------------------------------------");
            System.out.println("1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            // I think the API for AdminResource.addRoom is intended to add multiple rooms at a time,
            // so I've changed the prompt from "Add a Room" to "Add Room(s)".
            System.out.println("4. Add Room(s)");
            System.out.println("5. Back to Main Menu");
            System.out.println("--------------------------------------------------");
            System.out.println("Please select a number from the menu option (1-5).");
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "1" -> displayCustomers();
                case "2" -> displayRooms();
                case "3" -> AdminResource.getInstance().displayAllReservations();
                case "4" -> addRooms();
                case "5" -> {
                    System.out.println("Leaving admin interface ...");
                    return;
                }
                default -> System.out.println("Illegal input; please select a number from the menu option (1-5).");
            }
            System.out.println("Press ENTER to return to the admin menu");
            scanner.nextLine();
        } while (true);
    }
}
