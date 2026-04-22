public class BookMyStayApp {

    /**
     * Displays available rooms along with
     * their details and pricing.
     * <p>
     * This method performs read-only access
     * to inventory and room data.
     */
        void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {
        public static void main (String[]args){
            System.out.println("\n===== Available Rooms =====");

            // Single Room (read-only check)
            int singleAvailable = inventory.getAvailableRooms(singleRoom.getType());
            if (singleAvailable > 0) {
                System.out.println("\nRoom Type : " + singleRoom.getType());
                System.out.println("Price     : " + singleRoom.getPrice());
                System.out.println("Amenities : " + singleRoom.getAmenities());
                System.out.println("Available : " + singleAvailable);
            }

            // Double Room (read-only check)
            int doubleAvailable = inventory.getAvailableRooms(doubleRoom.getType());
            if (doubleAvailable > 0) {
                System.out.println("\nRoom Type : " + doubleRoom.getType());
                System.out.println("Price     : " + doubleRoom.getPrice());
                System.out.println("Amenities : " + doubleRoom.getAmenities());
                System.out.println("Available : " + doubleAvailable);
            }

            // Suite Room (read-only check)
            int suiteAvailable = inventory.getAvailableRooms(suiteRoom.getType());
            if (suiteAvailable > 0) {
                System.out.println("\nRoom Type : " + suiteRoom.getType());
                System.out.println("Price     : " + suiteRoom.getPrice());
                System.out.println("Amenities : " + suiteRoom.getAmenities());
                System.out.println("Available : " + suiteAvailable);
            }

            System.out.println("\n===========================\n");
        }
    }
}

