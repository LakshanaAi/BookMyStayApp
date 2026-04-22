import java.util.LinkedList;
import java.util.Queue;

/* Reservation class */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
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

/* Booking Request Queue (FIFO) */
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    // Add request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    // Get next request (FIFO)
    public Reservation getNextRequest() {
        return queue.poll();
    }

    // Check pending requests
    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}

/* Main Use Case 5 */
public class BookMyStayApp {

    public static void main(String[] args) {

        // Display application header
        System.out.println("Booking Request Queue");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // Add requests to the queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queued booking requests in FIFO order
        while (bookingQueue.hasPendingRequests()) {
            Reservation request = bookingQueue.getNextRequest();

            System.out.println(
                    "Processing Request -> Guest: "
                            + request.getGuestName()
                            + ", Room Type: "
                            + request.getRoomType()
            );
        }
    }
}
