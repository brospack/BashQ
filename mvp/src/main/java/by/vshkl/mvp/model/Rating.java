package by.vshkl.mvp.model;

public class Rating {
    private String rating;
    private int voteCount = 0;

    public Rating() {
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Rating(String rating, int voteCount) {
        this.rating = rating;
        this.voteCount = voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
