package by.vshkl.model;

public class Quote {

    enum VoteState {
        VOTED_NONE, VOTED_UP, VOTED_DOWN, VOTED_OLD
    }

    private String id;
    private String date;
    private String link;
    private String content;
    private int rating;
    private VoteState voteState = VoteState.VOTED_NONE;

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public VoteState getVoteState() {
        return voteState;
    }

    public void setVoteState(VoteState voteState) {
        this.voteState = voteState;
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
