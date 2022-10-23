package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    private static AdminResource adminResource = null;

    private AdminResource() { }
    public static AdminResource getInstance() {
        if (adminResource == null) {
            adminResource = new AdminResource();
        }
        return adminResource;
    }

    public Customer getCustomer(String email) {
        return CustomerService.getInstance().getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {
        // Adding multiple rooms
        for (IRoom room : rooms) {
            try {
                ReservationService.getInstance().addRoom(room);
                String roomNumber = room.getRoomNumber();
                System.out.println("Added room " + roomNumber + " successfully.");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    public Collection<IRoom> getAllRooms() {
        return ReservationService.getInstance().getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return CustomerService.getInstance().getAllCustomers();
    }

    public void displayAllReservations() {
        Collection<Customer> customers = getAllCustomers();
        for (Customer customer : customers) {
            System.out.println(customer);
            Collection<Reservation> reservations = ReservationService.getInstance().getCustomerReservation(customer);
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }


}
