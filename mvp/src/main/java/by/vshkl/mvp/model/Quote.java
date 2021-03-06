package by.vshkl.mvp.model;

public class Quote {

    public enum VoteState {
        VOTED_NONE, VOTED_UP, VOTED_DOWN, VOTED_OLD
    }

    private String id;
    private String date;
    private String link;
    private String content;
    private String rating;
    private String comicLink;
    private VoteState voteState = VoteState.VOTED_NONE;
    private int voteCount = 0;

    public Quote() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComicLink() {
        return comicLink;
    }

    public void setComicLink(String comicLink) {
        this.comicLink = comicLink;
    }

    public VoteState getVoteState() {
        return voteState;
    }

    public void setVoteState(VoteState voteState) {
        this.voteState = voteState;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quote quote = (Quote) o;

        return id.equals(quote.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Quote{");
        sb.append("id='").append(id).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", voteState=").append(voteState);
        sb.append('}');
        return sb.toString();
    }
}
