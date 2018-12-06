package group1.tcss450.uw.edu.messageappgroup1.dummy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class ConversationListContent {

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * An item representing a conversation item
     */
    public static class ConversationItem implements Serializable {
        public String members;
        public String preview;
        public String timeStamp;
        public final int chatID;
        public final String topicName;
        public boolean hasNewMessage;


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

        public void setHasNewMessage(boolean has) {
            this.hasNewMessage = has;
        }

        @Override
        public String toString() {
            return preview;
        }
    }
}
