package frost.test.com.frosttest.model;


import java.util.List;


public class Children {

    private String type;
    private String tagName;
    private List<String> videoList;


    public Children(String type,String tagName){
        this.type = type;
        this.tagName = tagName;
    }

    public String getType() {
        return type;
    }

    public String getTagName() {
        return tagName;
    }


    public List<String> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<String> videoList) {
        this.videoList = videoList;
    }
}
