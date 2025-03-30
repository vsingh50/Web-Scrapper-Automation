package demo.wrappers;

public class Oscar {

    private long EpochTimeOfScrape;
    private String Title;
    private int Year;
    private int Nomination;
    private int Awards;
    private boolean isWinner;

    public Oscar(long epoch, String title, int year, int nomination, int awards, boolean isWinner){
        this.EpochTimeOfScrape=epoch;
        this.Title=title;
        this.Year=year;
        this.Nomination=nomination;
        this.Awards=awards;
        this.isWinner=isWinner;
    }

    
}
