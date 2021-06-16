package my.musicapp.Model;

public class UploadSong {

    String songTitle, songCategory , artist, songLink ,mKey;

    public UploadSong(String songTitle, String songCategory, String artist, String songLink) {
        this.songTitle = songTitle;
        this.songCategory = songCategory;
        this.artist = artist;
        this.songLink = songLink;
        //this.mKey = mKey;
    }

    public UploadSong() {
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongCategory() {
        return songCategory;
    }

    public void setSongCategory(String songCategory) {
        this.songCategory = songCategory;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
