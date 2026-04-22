import java.io.*;
import java.util.*;

// Serializable Inventory
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public void allocateRoom(String type, int count) {
        rooms.put(type, rooms.get(type) - count);
    }

    public void addRoom(String type, int count) {
        rooms.put(type, rooms.getOrDefault(type, 0) + count);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + rooms);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }
}

// Serializable Booking History
class BookingStore implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> bookings = new ArrayList<>();

    public void addBooking(String booking) {
        bookings.add(booking);
    }

    public void displayBookings() {
        System.out.println("Bookings: " + bookings);
    }
}

// Wrapper class (entire system state)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    BookingStore bookingStore;

    public SystemState(RoomInventory inventory, BookingStore bookingStore) {
        this.inventory = inventory;
        this.bookingStore = bookingStore;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // SAVE
    public void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state.");
        }
    }

    // LOAD
    public SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) in.readObject();
            System.out.println("System state restored successfully.");
            return state;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Corrupted data. Starting with clean state.");
            return null;
        }
    }
}

// MAIN CLASS
public class BookMyStayApp {

    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        // LOAD STATE
        SystemState state = persistence.load();

        RoomInventory inventory;
        BookingStore bookingStore;

        if (state != null) {
            inventory = state.inventory;
            bookingStore = state.bookingStore;
        } else {
            inventory = new RoomInventory();
            bookingStore = new BookingStore();
        }

        // Simulate operations
        inventory.allocateRoom("Single", 1);
        bookingStore.addBooking("R101 - Single");

        inventory.allocateRoom("Double", 1);
        bookingStore.addBooking("R102 - Double");

        // Display current state
        inventory.displayInventory();
        bookingStore.displayBookings();

        // SAVE STATE BEFORE EXIT
        SystemState newState = new SystemState(inventory, bookingStore);
        persistence.save(newState);
    }
}