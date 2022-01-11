package in.aceventura.evolvuschool.teacherapp.pojo;

public class CalendarModel  {
    String title, event_desc;
    long start_date;
    long end_date;
    String color;
    String colorValues;

    public CalendarModel(String title, String event_desc, long start_date, String color, String colorValues) {
        this.title = title;
        this.event_desc = event_desc;
        this.start_date = start_date;
        this.color = color;
        this.colorValues = colorValues;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CalendarModel() {
        super();
    }

    public String getColorValues() {
        return colorValues;
    }

    public void setColorValues(String colorValues) {
        this.colorValues = colorValues;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public long getStart_date() {
        return start_date;
    }

    public void setStart_date(long start_date) {
        this.start_date = start_date;
    }

    public long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(long end_date) {
        this.end_date = end_date;
    }
}

