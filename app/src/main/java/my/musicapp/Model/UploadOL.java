package my.musicapp.Model;

public class UploadOL {

    String url;
    String name;
    String songcategory;

    public UploadOL(String url, String name, String songcategory) {
        this.url = url;
        this.name = name;
        this.songcategory = songcategory;
    }

    public UploadOL() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSongcategory() {
        return songcategory;
    }

    public void setSongcategory(String songcategory) {
        this.songcategory = songcategory;
    }
}
