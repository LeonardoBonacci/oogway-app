package guru.bonacci.oogway.app;

public class Message {
    private String text; // message body
    private MemberData data;
    private boolean belongsToCurrentUser; // is this message sent by us?

    public Message(String text,  MemberData data, boolean belongsToCurrentUser) {
        this.text = text;
        this.data = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public MemberData getData() {
        return data;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}