public interface Query_Fdb {
    QF_Main take_input();
    void submit(QF_Main obj);
}

class QF_Main implements Date_Time{
    private String name;
    private String email_id;
    private String text;
    private final String time = getDate_Time();

    public String get_time() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
