package by.vshkl.mvp.model;

public class Comic {

    private String thumbLink;
    private String imageLink;
    private String quoteLink;
    private String creator;
    private String content;

    public Comic() {
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

    public String getQuoteLink() {
        return quoteLink;
    }

    public void setQuoteLink(String quoteLink) {
        this.quoteLink = quoteLink;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comic comic = (Comic) o;

        return thumbLink.equals(comic.thumbLink);

    }

    @Override
    public int hashCode() {
        return thumbLink.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Comic{");
        sb.append("thumbLink='").append(thumbLink).append('\'');
        sb.append(", quoteLink='").append(quoteLink).append('\'');
        sb.append(", creator='").append(creator).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
