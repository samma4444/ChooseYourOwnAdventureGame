public class Character {

    private int favour = 0;
    private int wound = 1;
    private String name = "";

    public Character(String name){
        this.name = name;
    }

    public int getFavour() {
        return favour;
    }

    public void setFavour(int favour) {
        this.favour = favour;
    }

    public int getWound() {
        return wound;
    }

    public void setWound(int wound) {
        this.wound = wound;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
