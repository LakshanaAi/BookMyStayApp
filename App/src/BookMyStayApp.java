import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Room Inventory Class
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public void checkAvailability(String type, int count) throws InvalidBookingException {
        if (!rooms.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
        if (count <= 0) {
            throw new InvalidBookingException("Booking count must be greater than 0");
        }
        if (rooms.get(type) < count) {
            throw new InvalidBookingException("Not enough rooms available for type: " + type);
        }
    }

    public void reserveRoom(String type, int count) {
        rooms.put(type, rooms.get(type) - count);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + rooms);
    }
}

// Validator Class
class ReservationValidator {
    public void validate(String type, int count, RoomInventory inventory) throws InvalidBookingException {
        inventory.checkAvailability(type, count);
    }
}

// Queue Class (basic simulation)
class BookingRequestQueue {
    private Queue<String> queue = new LinkedList<>();

    public void addRequest(String request) {
        queue.add(request);
    }

    public void processRequests() {
        while (!queue.isEmpty()) {
            System.out.println("Processing: " + queue.poll());
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Input
            System.out.print("Enter room type (Single/Double/Suite): ");
            String type = scanner.nextLine();

            System.out.print("Enter number of rooms: ");
            int count = scanner.nextInt();

            // VALIDATION (Fail-Fast)
            validator.validate(type, count, inventory);

            // If valid → proceed
            inventory.reserveRoom(type, count);
            bookingQueue.addRequest(type + " x " + count);

            System.out.println("Booking successful!");
            inventory.displayInventory();

            bookingQueue.processRequests();

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking failed: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Booking failed: Invalid input format.");
        } finally {
            scanner.close();
        }
    }
}