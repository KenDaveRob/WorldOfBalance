package utility;

public class NewsContainer {

    private int news_id;
    private String text;
    private String create_time;

    public NewsContainer(int news_id) {
        this.news_id = news_id;
    }

    public int getID() {
        return news_id;
    }

    public String getText() {
        return text;
    }

    public String setText(String text) {
        return this.text = text;
    }

    public String getCreateTime() {
        return create_time;
    }

    public String setCreateTime(String create_time) {
        return this.create_time = create_time;
    }
}
