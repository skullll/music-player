package my.musicapp.Model;

public class Upload {

    public String name ;
    public String url;
    public String songcategory;

    public Upload(String name, String url, String songcategory) {
        this.name = name;
        this.url = url;
        this.songcategory = songcategory;
    }

    public Upload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongcategory() {
        return songcategory;
    }

    public void setSongcategory(String songcategory) {
        this.songcategory = songcategory;
    }
}
