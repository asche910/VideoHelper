package bean;

public class VideoBean {
    private String title;
    // 解析后的timeLength, 行如2:30
    private String timeLength;
    // 单位为MB
    private int size;

    public VideoBean(String title, String timeLength, int size) {
        this.title = title;
        this.timeLength = timeLength;
        this.size = size;
    }

    public VideoBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
