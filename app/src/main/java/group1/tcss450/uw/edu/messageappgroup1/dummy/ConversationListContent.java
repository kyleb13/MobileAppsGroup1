package group1.tcss450.uw.edu.messageappgroup1.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * This class is supposed to be CONVERSATIONS CLASS OK
 * Or we can totally make a new class
 * I just needed a class to populate Tab1
 *
 *
 */


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ConversationListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ConversationItem> ITEMS = new ArrayList<ConversationItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ConversationItem> ITEM_MAP = new HashMap<String, ConversationItem>();

    private static final int COUNT = 25;

//
//    private static ConversationItem createDummyItem(int position) {
//        return new ConversationItem(String.valueOf(position), "Item " + position, makeDetails(position));
//    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ConversationItem {
        public String members;
        public String preview;
        public String timeStamp;
        public final int chatID;
        public final String topicName;


        public ConversationItem(int chatID, String topicName) {
            this.chatID = chatID;
            this.topicName = topicName;
        }

        public void setPreview(String thePreview) {
            this.preview = thePreview;
        }

        public void setMembers(String theMembers) {
            this.members = theMembers;
        }

        public void setTimeStamp(String theTimestamp) {
            this.timeStamp = theTimestamp;
        }



        @Override
        public String toString() {
            return preview;
        }
    }
}
