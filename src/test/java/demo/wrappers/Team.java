package demo.wrappers;

public class Team {

    private String TeamName;
    private double WinPercentage;
    private int Year;
    private long epochTimeOfScrape;

    public Team(long epoch, String name, double winPct, int year){
        this.epochTimeOfScrape = epoch;
        this.TeamName = name;
        this.WinPercentage = winPct;
        this.Year = year;
    }

    
}
