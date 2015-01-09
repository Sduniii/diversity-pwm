package de.diversity.androidpwm.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Item {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Integer, DummyItem> ITEM_MAP = new HashMap<Integer, DummyItem>();

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public int id;
        public String location;
        public String user;
        public String password;

        public DummyItem(int id, String location, String user, String password) {
            this.id = id;
            this.location = location;
            this.user = user;
            this.password = password;
        }

        @Override
        public String toString() {
            return location;
        }
    }
}
