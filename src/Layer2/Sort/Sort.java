package Layer2.Sort;

import Layer1.Entities.Item;
import Layer2.API.MapAPI.errors.ApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Sort {

    public abstract ArrayList<Item> sort(ArrayList<Item> items) throws InterruptedException, ApiException, IOException;

    public ArrayList<Item> reverseSort(ArrayList<Item> items) throws InterruptedException, ApiException, IOException {
        this.sort(items);
        Collections.reverse(items);
        return items;
    }

}
