package de.commune.organizer.communeorganizer;

public class Item {

    String text;
    String text2;
    String lineNo;
    String userEmail;

    public Item(String text, String text2, String lineNo, String userEmail) {
        this.text = text;
        this.text2 = text2;
        this.lineNo = lineNo;
        this.userEmail = userEmail;
    }

    public String getText1() {
        return text;
    }

    public String getText2() {
        return text2;
    }

    public String getLineNo() {
        return lineNo;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
