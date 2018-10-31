package group1.tcss450.uw.edu.messageappgroup1.contacts;

public class Contact {
    private final String mFirstName;
    private final String mLastName;
    private final String mNickName;
    private final int mID;

    /**
     * Helper class to build the contact.
     * @author Kevin Nguyen
     */
    public static class Builder {
        private String mFirstName;
        private String mLastName;
        private String mNickName;
        private int    mID;

        public Builder() {
            // Do nothing
        }

        public Builder addFirstName(final String val) {
            mFirstName = val;
            return this;
        }

        public Builder addLastName(final String val) {
            mLastName = val;
            return this;
        }

        public Builder addNickName(final String val) {
            mNickName = val;
            return this;
        }

        public Builder addID(final int val) {
            mID = val;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }

    private Contact(final Builder builder) {
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mNickName = builder.mNickName;
        this.mID = builder.mID;
    }

    public String getFirstName() {
        return this.mFirstName;
    }

    public String getLastName() {
        return this.mLastName;
    }

    public String getNickName() {
        return this.mNickName;
    }

    public int getID() {
        return this.mID;
    }




}
