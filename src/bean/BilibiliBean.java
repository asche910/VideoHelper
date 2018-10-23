package bean;


public class BilibiliBean {
    private int aid;
    private int cid;
    private int duration;
    private int epId;
    private String index;
    private String indexTitle;

    public BilibiliBean(int aid, int cid, int duration, int epid, String index, String indexTitle) {
        this.aid = aid;
        this.cid = cid;
        this.duration = duration;
        this.epId = epid;
        this.index = index;
        this.indexTitle = indexTitle;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getEpId() {
        return epId;
    }

    public void setEpId(int epId) {
        this.epId = epId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndexTitle() {
        return indexTitle;
    }

    public void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }

    @Override
    public String toString() {
        return "{\"aid\":" + aid + ", " +
                "\"cid\":" + cid + ", " +
                "\"duration\":" + duration + ", " +
                "\"ep_id\":" + epId + ", " +
                "\"index\":" + index + ", " +
                "\"index_title\":" + indexTitle + " " +
                "}";
    }
}
