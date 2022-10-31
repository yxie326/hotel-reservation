package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.*;

public class ReservationService {
    private static final Map<String, IRoom> mapOfRooms = new HashMap<>();
    private static final Map<String, List<Reservation>> emailToReservation = new HashMap<>();
    private static final Map<String, List<Reservation>> roomNumberToReservation = new HashMap<>();
    private static ReservationService reservationService = null;

    private ReservationService() { }
    public static ReservationService getInstance() {
        if (reservationService == null) {
            reservationService = new ReservationService();
        }
        return reservationService;
    }

    public void addRoom(IRoom room) {
        String roomNumber = room.getRoomNumber();
        if (mapOfRooms.containsKey(roomNumber)) {
            mapOfRooms.replace(roomNumber, room);
            throw new KeyAlreadyExistsException("Warning: Room number " + roomNumber + "already exists; overwriting record.");
        } else {
            mapOfRooms.put(roomNumber, room);
        }
    }

    public IRoom getARoom(String roomId) {
        return mapOfRooms.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        String email = customer.getEmail();
        String roomNumber = room.getRoomNumber();
        emailToReservation.putIfAbsent(email, new ArrayList<>());
        emailToReservation.get(email).add(reservation);
        roomNumberToReservation.putIfAbsent(roomNumber, new ArrayList<>());
        roomNumberToReservation.get(roomNumber).add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> listOfRooms = new ArrayList<>();
        for (IRoom room : mapOfRooms.values()) {
            if (roomIsAvailable(room, checkInDate, checkOutDate)) {
                listOfRooms.add(room);
            }
        }
        return listOfRooms;
    }

    private static boolean roomIsAvailable(IRoom room, Date checkInDate, Date checkOutDate) {
        String roomNumber = room.getRoomNumber();
        if (!roomNumberToReservation.containsKey(roomNumber)) { return true; }
        List<Reservation> listOfReservations = roomNumberToReservation.get(roomNumber);
        for (Reservation reservation : listOfReservations) {
            if (checkInDate.compareTo(reservation.getCheckOutDate()) <= 0) {
                if (checkOutDate.compareTo(reservation.getCheckInDate()) >= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public Collection<Reservation> getCustomerReservation(Customer customer) {
        String email = customer.getEmail();
        return emailToReservation.get(email);
    }

    public Collection<IRoom> getAllRooms() {
        return mapOfRooms.values();
    }

}
