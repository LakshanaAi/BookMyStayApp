import java.util.*;

// Room Inventory Class
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public void addRoom(String type, int count) {
        rooms.put(type, rooms.getOrDefault(type, 0) + count);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + rooms);
    }
}

// Cancellation Service
class CancellationService {

    // Stack for rollback tracking (LIFO)
    private Stack<String> releasedRoomIds;

    // Map reservation → room type
    private Map<String, String> reservationRoomTypeMap;

    // Constructor
    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    // Register confirmed booking
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
        System.out.println("Booking registered: " + reservationId + " -> " + roomType);
    }

    // Cancel booking
    public void cancelBooking(String reservationId, RoomInventory inventory) {

        // VALIDATION
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Invalid reservation ID");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        // ROLLBACK TRACKING (push to stack)
        releasedRoomIds.push(reservationId);

        // INVENTORY RESTORE
        inventory.addRoom(roomType, 1);

        // REMOVE booking (prevent duplicate cancellation)
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Cancellation successful for ID: " + reservationId);
    }

    // Show rollback history (LIFO)
    public void showRollbackHistory() {
        if (releasedRoomIds.isEmpty()) {
            System.out.println("No cancellations yet.");
            return;
        }

        System.out.println("Rollback History (Most Recent First):");
        Stack<String> temp = new Stack<>();

        while (!releasedRoomIds.isEmpty()) {
            String id = releasedRoomIds.pop();
            System.out.println(id);
            temp.push(id);
        }

        // Restore stack
        while (!temp.isEmpty()) {
            releasedRoomIds.push(temp.pop());
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        // Simulate bookings
        service.registerBooking("R101", "Single");
        service.registerBooking("R102", "Double");

        try {
            System.out.print("Enter reservation ID to cancel: ");
            String id = scanner.nextLine();

            service.cancelBooking(id, inventory);

            inventory.displayInventory();

            service.showRollbackHistory();

        } finally {
            scanner.close();
        }
    }
}