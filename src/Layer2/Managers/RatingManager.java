package Layer2.Managers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The use case class for the User rating system.
 */
public class RatingManager {

    /**
     * Creates the possible ratings users may rate each other for a given transaction.
     *
     * @param min The minimum rating a user may receive for a given transaction evaluation.
     * @param max The maximum rating a user may receive for a given transaction evaluation.
     * @param increment The difference between a rating in the set of possible ratings and the next or previous rating
     * in the set of ratings.
     * @return The possible ratings users may give one another for performing a transaction with each other.
     */
    public ArrayList<Double> getRatingChoices(Double min, Double max, Double increment) {
        ArrayList<Double> choices = new ArrayList<>();
        for (double i = min; i <= max; i += increment)
            choices.add(i);
        return choices;
    }

    /**
     * Updates the rating of a BasicUser based on a particular transaction.
     *
     * @param basicUser A BasicUser instance registered to the program.
     * @param transaction The Transaction instance the BasicUser instance will receive their rating from.
     */
    public void updateRating(BasicUser basicUser, Transaction transaction) {
        Double rating = transaction.getReview(basicUser.getUsername());

        basicUser.setTotalRatingScore(basicUser.getTotalRatingScore() + rating);
        basicUser.setNumOfReview(basicUser.getNumOfReview() + 1);

        if (basicUser.getRating() == -1) {
            basicUser.setRating(rating);
        } else {
            basicUser.setRating(basicUser.getTotalRatingScore() / basicUser.getNumOfReview());
        }
    }

    /**
     * Creates the visual formatting for a BasicUser instance's rating for transactions.
     *
     * @param basicUser The BasicUser instance registered to the program.
     * @return The String formatting representation of how a BasicUser instance will have their current rating
     * represented on their profile page.
     */
    public String formatRating(BasicUser basicUser) {
        if (basicUser.getRating() == -1)
            return "Unrated";
        DecimalFormat df = new DecimalFormat("#.#");
        String output = df.format(basicUser.getRating());
        output += "/5 (" + basicUser.getNumOfReview() + ")";
        return output;
    }
}
