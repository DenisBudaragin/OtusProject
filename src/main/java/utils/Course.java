package utils;

public class Course {
    private final String title;
    private final String startDate;

    public Course(String title, String startDate) {
        this.title = title;
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }
}
