public class Player {
    //fields
    private String name;
    private int odds;

    //constructors
    public Player(String name, int odds) {
        this.name = name;
        this.odds = odds;
    }

    //methods
    public int compareTo(Player other) {
        if(this.getOdds() > other.getOdds()) {
            return 1;
        } else if(this.getOdds() < other.getOdds()) {
            return -1;
        } else {
            return 0;
        }
    }

    //getters
    public String getName() {
        return name;
    }

    public int getOdds() {
        return odds;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setOdds(int odds) {
        this.odds = odds;
    }

    //toString
    public String toString() {
        return getName() + "'s odds: +" + getOdds();
    }
}