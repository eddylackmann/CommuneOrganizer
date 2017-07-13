package de.commune.organizer.communeorganizer;

public class Item {

    String text;
    String text2;
    String lineNo;

    public Item(String text,String text2, String lineNo)
    {
        this.text = text;
        this.text2 = text2;
        this.lineNo = lineNo;
    }
    public String getText1()
    {
        return text;
    }
    public String getText2()
    {
        return text2;
    }
    public String getLineNo(){
        return lineNo;
    }
}
