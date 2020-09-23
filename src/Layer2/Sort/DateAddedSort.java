package Layer2.Sort;

import Layer1.Entities.Item;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The type Date added sort. Sorts an ArrayList of Item objects from oldest to newest.
 */
public class DateAddedSort extends Sort {

    @Override
    public ArrayList<Item> sort(ArrayList<Item> items) {
        ArrayList<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(Item::getDateAdded));
        return sorted;
    }
}
