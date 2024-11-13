package EventManagmentApplication;

import java.sql.*;
import java.util.Scanner;

public class EventManager {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Event Management System");

        while (true) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Register Attendee");
            System.out.println("2. Check-in Attendee");
            System.out.println("3. Show All Attendees");
            System.out.println("4. Add Event");
            System.out.println("5. Show All Events");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    registerAttendee();
                    break;
                case 2:
                    checkInAttendee();
                    break;
                case 3:
                    showAllAttendees();
                    break;
                case 4:
                    addEvent();
                    break;
                case 5:
                    showAllEvents(); // Display all events before registration
                    break;
                case 6:
                    System.out.println("Thank you for using the Event Management System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    // Register Attendee
    private static void registerAttendee() {
        System.out.println("\n--- Attendee Registration ---");

        // Display available events and their event_id
        System.out.println("Available Events:");
        showAllEvents();

        System.out.print("Enter Attendee Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Attendee Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Event ID for Registration: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();  // Consume newline character

        // Validate event ID (Check if the event exists in the database)
        if (!isValidEvent(eventId)) {
            System.out.println("Invalid Event ID. Please try again.");
            return;
        }

        // Register Attendee in Database
        String query = "INSERT INTO Attendee (name, email, event_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, eventId);
            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Attendee registered successfully: " + name);
            } else {
                System.out.println("Failed to register attendee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add New Event
    private static void addEvent() {
        System.out.println("\n--- Add New Event ---");

        System.out.print("Enter Event Name: ");
        String eventName = scanner.nextLine();

        System.out.print("Enter Event Date (yyyy-mm-dd): ");
        String eventDate = scanner.nextLine();

        // Insert new event into the Event table
        String query = "INSERT INTO Event (event_name, event_date) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            stmt.setString(2, eventDate);
            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Event added successfully: " + eventName);
            } else {
                System.out.println("Failed to add event.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show all events
    private static void showAllEvents() {
        String query = "SELECT event_id, event_name, event_date FROM Event";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                System.out.println("No events available.");
            } else {
                System.out.println("Event ID | Event Name | Event Date");
                do {
                    int eventId = rs.getInt("event_id");
                    String eventName = rs.getString("event_name");
                    String eventDate = rs.getString("event_date");
                    System.out.println(eventId + " | " + eventName + " | " + eventDate);
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Validate if the Event ID exists
    private static boolean isValidEvent(int eventId) {
        String query = "SELECT COUNT(*) FROM Event WHERE event_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;  // Event exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Event does not exist
    }

    // Check-in Attendee
    private static void checkInAttendee() {
        System.out.println("\n--- Attendee Check-in ---");

        System.out.print("Enter Attendee Email for Check-in: ");
        String email = scanner.nextLine();

        // Update the check-in status in the database
        String query = "UPDATE Attendee SET checked_in = TRUE WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Attendee checked in successfully!");
            } else {
                System.out.println("No attendee found with this email or already checked in.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show All Attendees
    private static void showAllAttendees() {
        System.out.println("\n--- All Attendees ---");

        String query = "SELECT * FROM Attendee";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                boolean checkedIn = rs.getBoolean("checked_in");
                System.out.println("Name: " + name + ", Email: " + email + ", Checked In: " + (checkedIn ? "Yes" : "No"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
