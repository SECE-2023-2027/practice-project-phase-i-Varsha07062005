package EventManagmentApplication;

public class Attendee {
    private String name;
    private String email;
    private boolean checkedIn;

    public Attendee(String name, String email) {
        this.name = name;
        this.email = email;
        this.checkedIn = false;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void checkIn() {
        this.checkedIn = true;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Email: " + email + ", Checked In: " + (checkedIn ? "Yes" : "No");
    }
}
