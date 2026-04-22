import java.util.*;

/* Reservation */
class BookMyStayApp {
    private String guestName;
    private String roomType;

    public BookMyStayApp(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/* Booking Queue (FIFO) */
class BookingRequestQueue {
    private Queue<BookMyStayApp> queue = new LinkedList<>();

    public void addRequest(BookMyStayApp r) {
        queue.offer(r);
    }

    public BookMyStayApp getNextRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}

/* Room Inventory */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRooms(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailableRooms(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) {
        int count = inventory.get(type);
        inventory.put(type, count - 1);
    }
}

/* Room Allocation Service */
class RoomAllocationService {

    /* Prevent duplicate IDs */
    private Set<String> allocatedRoomIds;

    /* Track by room type */
    private Map<String, Set<String>> assignedRoomsByType;

    /* Constructor */
    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /* Allocate room */
    public void allocateRoom(BookMyStayApp reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        int available = inventory.getAvailableRooms(roomType);

        if (available <= 0) {
            System.out.println("No rooms available for "
                    + reservation.getGuestName()
                    + " (" + roomType + ")");
            return;
        }

        String roomId = generateRoomId(roomType);

        allocatedRoomIds.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        inventory.decrementRoom(roomType);

        System.out.println("Reservation Confirmed -> Guest: "
                + reservation.getGuestName()
                + ", Room Type: " + roomType
                + ", Room ID: " + roomId);
    }

    /* Generate unique room id */
    private String generateRoomId(String roomType) {
        String prefix = roomType.substring(0, 1).toUpperCase();

        String roomId;

        do {
            int number = 100 + new Random().nextInt(900);
            roomId = prefix + number;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}

/* Main Class */
public class main {

    public static void main(String[] args) {

        System.out.println("Room Allocation Service");

        /* Inventory setup */
        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 1);
        inventory.addRooms("Double", 1);
        inventory.addRooms("Suite", 1);

        /* Booking queue */
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new BookMyStayApp("Abhi", "Single"));
        queue.addRequest(new BookMyStayApp("Subha", "Double"));
        queue.addRequest(new BookMyStayApp("Vanmathi", "Suite"));
        queue.addRequest(new BookMyStayApp("Kumar", "Single")); // no room

        /* Allocation service */
        RoomAllocationService allocator = new RoomAllocationService();

        while (queue.hasPendingRequests()) {
            BookMyStayApp r = queue.getNextRequest();
            allocator.allocateRoom(r, inventory);
        }
    }
}
