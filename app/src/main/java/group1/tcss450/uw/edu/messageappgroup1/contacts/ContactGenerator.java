package group1.tcss450.uw.edu.messageappgroup1.contacts;

public class ContactGenerator {
    public static final Contact[] CONTACTS;
    public static final int COUNT = 20;

    static {
        CONTACTS = new Contact[COUNT];
        for (int i = 0; i < CONTACTS.length; i++) {
            CONTACTS[i] = new Contact
                    .Builder()
                    .addFirstName("Mariah")
                    .addLastName("Carey")
                    .addID(666)
                    .addNickName("MC")
                    .build();
        }
    }

    private ContactGenerator() { }
}
