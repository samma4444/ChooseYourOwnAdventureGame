import java.util.ArrayList;
import java.util.List;

public class Page {

    private List<Page> nextPages; // list of nextPages
    private Character c; // the character
    private String msg; // the main text to display to the reader
    private String textForNextRoom; // choose to display for previous room
    private List<String> options; // chooses to display to the reader for this room
    private List<Page> filteredOptions = new ArrayList<>(); // chooses to display to the reader for this room
    private int favour;
    private int wound;
    private int woundChange;
    private int favourChange;

    // constructor for the page
    public Page(List<Page> a, Character c, String text, String msg,int favour, int wound, int woundChange, int favourChange){
        this.nextPages = a;
        this.c = c;
        this.textForNextRoom = text;
        this.msg = msg;
        this.favour = favour;
        this.wound = wound;
        this.options = new ArrayList<>();
        this.favourChange = favourChange;
        this.woundChange = woundChange;


    }
    // constructor for the page
    public Page(List<Page> a, Character c, String text, String msg , int woundChange, int favourChange){
        this.nextPages = a;
        this.c = c;
        this.textForNextRoom = text;
        this.msg = msg;
        this.favour = -1;
        this.wound = -1;
        this.options = new ArrayList<>();
        this.favourChange = favourChange;
        this.woundChange = woundChange;

    }
    // constructor for the page
    public Page(List<Page> a, Character c, String text, String msg, int favour , int woundChange, int favourChange){
        this.nextPages = a;
        this.c = c;
        this.textForNextRoom = text;
        this.msg = msg;
        this.favour = favour;
        this.wound = -1;
        this.options = new ArrayList<>();
        this.favourChange = favourChange;
        this.woundChange = woundChange;
    }

    // get the text to display to reader
    public String getMsg(){
        return msg;
    }

    // get the text for the previous room
    public String getText(){
        return textForNextRoom;
    }

    // get the current player object
    public Character getC(){
        return c;
    }

    public int getFavour(){
        return favour;
    }

    public int getWound() {
        return wound;
    }

    // get the msgs to display for the reader to make a choice
    public List<String> getOptions(){
        return options;
    }

    // get the next pages
    public List<Page> getNextPages(){
        return nextPages;
    }
    public void filterOptions () {

        if (nextPages.equals(new ArrayList<>())) {
            options.add("Enter: To Continue");
            return;
        }
        if (nextPages.get(0).nextPages.equals(new ArrayList<>())){
            if (wound == 0) {
                filteredOptions.add(nextPages.get(1));
            } else {
                filteredOptions.add(nextPages.get(0));
            }
        } else {

            for (Page p : nextPages) {
                if (c.getFavour() >= p.getFavour() && c.getWound() >= p.getWound()) {
                    filteredOptions.add(p);
                }
            }
        }
    }

    public List<Page> getFilteredOptions(){
        return filteredOptions;
    }
    // make the options text to display to the reader
    public void makeOptions(){
        filterOptions();
        for (int i = 0;i<filteredOptions.size();i++) {
            String p = filteredOptions.get(i).getText();
            options.add((i + 1) + ") " + p);
        }
    }

    // get the next page
    public Page nextPage(int i){
        if(i > filteredOptions.size()){
            return null;
        }
        c.setFavour(c.getFavour() + favourChange);
        c.setWound(c.getWound() + woundChange);
        return filteredOptions.get(i);
    }


}
