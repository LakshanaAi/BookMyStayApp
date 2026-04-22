import java.util.*;

// Booking Request
class BookingRequest {
    String roomType;
    int count;

    public BookingRequest(String roomType, int count) {
        this.roomType = roomType;
        this.count = count;
    }
}

// Thread-safe Booking Queue
class BookingRequestQueue  {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Room Inventory (CRITICAL SECTION)
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 3);
    }

    // synchronized → prevents race condition
    public synchronized boolean allocateRoom(String type, int count) {
        int available = rooms.getOrDefault(type, 0);

        if (available >= count) {
            // simulate delay (forces race condition if not synchronized)
            try { Thread.sleep(50); } catch (Exception e) {}

            rooms.put(type, available - count);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("Inventory: " + rooms);
    }
}

// Allocation Service
class AllocationService {
    public void allocate(BookingRequest req, RoomInventory inventory) {
        boolean success = inventory.allocateRoom(req.roomType, req.count);

        if (success) {
            System.out.println(Thread.currentThread().getName() +
                    " → Booking SUCCESS for " + req.roomType);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " → Booking FAILED for " + req.roomType);
        }
    }
}

// Concurrent Processor (Runnable)
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue queue;
    private RoomInventory inventory;
    private AllocationService service;

    public ConcurrentBookingProcessor(BookingRequestQueue queue,
                                      RoomInventory inventory,
                                      AllocationService service) {
        this.queue = queue;
        this.inventory = inventory;
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest req;

            // synchronized fetch
            synchronized (queue) {
                req = queue.getRequest();
            }

            if (req == null) break;

            service.allocate(req, inventory);
        }
    }
}

// MAIN CLASS
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        AllocationService allocationService = new AllocationService();

        // Add requests (more than available → exposes race condition)
        bookingQueue.addRequest(new BookingRequest("Single", 1));
        bookingQueue.addRequest(new BookingRequest("Single", 1));
        bookingQueue.addRequest(new BookingRequest("Single", 1));
        bookingQueue.addRequest(new BookingRequest("Single", 1));
        bookingQueue.addRequest(new BookingRequest("Single", 1));
        bookingQueue.addRequest(new BookingRequest("Single", 1)); // extra

        // Create threads (your snapshot)
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService
                )
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService
                )
        );

        // Start threads
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Final state
        inventory.displayInventory();
    }
}