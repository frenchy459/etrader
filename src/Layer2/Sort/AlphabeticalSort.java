package Layer2.Sort;

import Layer1.Entities.Item;

import java.util.ArrayList;

/**
 * The type Alphabetical sort. Sorts an ArrayList of Item objects in alphabetical order.
 */
public class AlphabeticalSort extends Sort {

    @Override
    public ArrayList<Item> sort(ArrayList<Item> items) {
        ArrayList<Item> sorted = new ArrayList<>(items);
        sorted.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return sorted;
    }
}
