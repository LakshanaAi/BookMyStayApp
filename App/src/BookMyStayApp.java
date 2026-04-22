import java.util.*;

/* Reservation */
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/* Booking History */
class BookingHistory {

    // list storing confirmed reservations
    private List<Reservation> confirmedReservations;

    // constructor
    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    // add reservation
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    // get all reservations
    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

/* Reporting Service */
class BookingReportService {

    public void generateReport(BookingHistory history) {

        List<Reservation> reservations =
                history.getConfirmedReservations();

        System.out.println("\n===== Booking History Report =====");

        for (Reservation r : reservations) {
            System.out.println(
                    "Reservation ID: " + r.getReservationId()
                            + ", Guest: " + r.getGuestName()
                            + ", Room Type: " + r.getRoomType()
            );
        }

        System.out.println("----------------------------------");
        System.out.println("Total Confirmed Bookings: "
                + reservations.size());
    }
}

/* Main Class */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking History & Reporting");

        BookingHistory history = new BookingHistory();

        // confirmed bookings
        history.addReservation(
                new Reservation("R101", "Abhi", "Single"));

        history.addReservation(
                new Reservation("R102", "Subha", "Double"));

        history.addReservation(
                new Reservation("R103", "Vanmathi", "Suite"));

        // generate report
        BookingReportService reportService =
                new BookingReportService();

        reportService.generateReport(history);
    }
}
