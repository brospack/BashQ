package by.vshkl.mvp.model;

public class ComicsThumbnail {

    private String thumbLink;
    private String imageLink;
    private String comicsLink;

    public ComicsThumbnail() {
    }

    public String getThumbLink() {
        return thumbLink;
    }

    public void setThumbLink(String thumbLink) {
        this.thumbLink = thumbLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getComicsLink() {
        return comicsLink;
    }

    public void setComicsLink(String comicsLink) {
        this.comicsLink = comicsLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComicsThumbnail that = (ComicsThumbnail) o;

        return thumbLink.equals(that.thumbLink);

    }

    @Override
    public int hashCode() {
        return thumbLink.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComicsThumbnail{");
        sb.append("thumbLink='").append(thumbLink).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
