package stat;

import config.Configuration;
import dnl.utils.text.table.TextTable;
import javafx.beans.binding.ObjectExpression;

import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchQueryStat {
    private String fileName;
    private String query;
    private long sentTime;
    private long receivedTime;
    private int hopsCount;
    private static ArrayList<SearchQueryStat> searchQueryStats = new ArrayList<SearchQueryStat>();

    public SearchQueryStat() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public int getHopsCount() {
        return hopsCount;
    }

    public void setHopsCount(int hopsCount) {
        this.hopsCount = hopsCount;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void display() {
        long elapsed = receivedTime - sentTime;
        System.out.println(fileName + "\t\t\t" + sentTime + "\t" + receivedTime + "\t" + hopsCount + "\t" + elapsed);
    }

    @Override
    public String toString() {
        long elapsed = receivedTime - sentTime;
        return fileName + "\t\t\t" +sentTime + "\t" + receivedTime + "\t" + hopsCount + "\t" + elapsed + "\n";
    }

    public static void append(SearchQueryStat searchQueryStat) {
        searchQueryStats.add(searchQueryStat);
    }

    public static String[] getHeaders() {
        return new String[] { "Query", "File Name", "Sent Time", "Received Time", "Hop Count", "Elapsed Time" };
    }

    public static Object[][] getData() {
        Object[][] data = new Object[searchQueryStats.size()][getHeaders().length];
        for(int row=0; row<searchQueryStats.size(); row++) {
            SearchQueryStat searchQueryStat = searchQueryStats.get(row);
            for(int col=0; col<getHeaders().length; col++) {
                data[row][0] = searchQueryStat.query;
                data[row][1] = searchQueryStat.fileName;
                data[row][2] = searchQueryStat.sentTime;
                data[row][3] = searchQueryStat.receivedTime;
                data[row][4] = searchQueryStat.hopsCount;
                data[row][5] = searchQueryStat.receivedTime - searchQueryStat.sentTime;
            }
        }
        return data;
    }

    public static void displayAsTable() {
        System.out.println("Search Statistics");
        System.out.println("=================");
        TextTable searchStat = new TextTable(SearchQueryStat.getHeaders(), SearchQueryStat.getData());
        searchStat.setAddRowNumbering(true);
        searchStat.setSort(0);
        searchStat.printTable();
        System.out.println();

        double meanElapsedSqrd = 0;
        double meanElapsed = 0;
        double stdElapsed = 0;
        int count = searchQueryStats.size();
        long maxElapsed = Long.MIN_VALUE;
        long minElapsed = Long.MAX_VALUE;

        int maxHopCount = 0;
        int minHopCount = Integer.MAX_VALUE;
        double meanHopCount = 0;
        double meanHopCountSqrd = 0;
        double stdHopCount = 0;

        List<String> successQueries = new ArrayList<String>();
        List<String> allQueries = Arrays.asList(new String[]{ "Twilight",
                "Jack",
                "American_Idol",
                "Happy_Feet",
                "Twilight_saga",
                "Happy_Feet",
                "Happy_Feet",
                "Feet",
                "Happy_Feet",
                "Twilight",
                "Windows",
                "Happy_Feet",
                "Mission_Impossible",
                "Twilight",
                "Windows_8",
                "The",
                "Happy",
                "Windows_8",
                "Happy_Feet",
                "Super_Mario",
                "Jack_and_Jill",
                "Happy_Feet",
                "Impossible",
                "Happy_Feet",
                "Turn_Up_The_Music",
                "Adventures_of_Tintin",
                "Twilight_saga",
                "Happy_Feet",
                "Super_Mario",
                "American_Pickers",
                "Microsoft_Office_2010",
                "Twilight",
                "Modern_Family",
                "Jack_and_Jill",
                "Jill",
                "Glee",
                "The_Vampire_Diarie",
                "King_Arthur",
                "Jack_and_Jill",
                "King_Arthur",
                "Windows_XP",
                "Harry_Potter",
                "Feet",
                "Kung_Fu_Panda",
                "Lady_Gaga",
                "Gaga",
                "Happy_Feet",
                "Twilight",
                "Hacking",
                "King" });

        for(SearchQueryStat queryStat: searchQueryStats) {
            long elapsed = queryStat.receivedTime - queryStat.sentTime;
            if(elapsed > maxElapsed) {
                maxElapsed = elapsed;
            }

            if(elapsed < minElapsed) {
                minElapsed = elapsed;
            }

            int hopCount = Configuration.getMaxHopCount() - queryStat.hopsCount;
            if (hopCount < minHopCount && hopCount > 0) {
                minHopCount = hopCount;
            }

            if (hopCount > maxHopCount) {
                maxHopCount = hopCount;
            }

            meanElapsed += elapsed;
            meanElapsedSqrd += elapsed * elapsed;

            meanHopCount += hopCount;
            meanHopCountSqrd += hopCount * hopCount;

            if(!successQueries.contains(queryStat.query)) {
                successQueries.add(queryStat.query);
            }
        }

        meanElapsed = meanElapsed / count;
        meanElapsedSqrd = meanElapsedSqrd / count;
        stdElapsed = (meanElapsedSqrd - (meanElapsed * meanElapsed)) / count;

        meanHopCount = meanHopCount / count;
        meanHopCountSqrd = meanHopCountSqrd / count;
        stdHopCount = (meanHopCountSqrd - (meanHopCount*meanHopCount)) / count;

        double successCount = 0;
        for(String s: allQueries) {
            if(successQueries.contains(s)) {
                successCount++;
            } else {
                System.out.println("Failed Query : " + s);
            }
        }
        System.out.println();

        double successRatio = 100.0 * successCount / 50.0;


        System.out.println("Summary");
        System.out.println("=======");
        Object[][] data = new Object[9][2];
        data[0][0] = "Success Rate%";
        data[1][0] = "Min Elapsed";
        data[2][0] = "Max Elapsed";
        data[3][0] = "Mean Elapsed";
        data[4][0] = "Std. Elapsed";
        data[5][0] = "Min Hops";
        data[6][0] = "Max Hops";
        data[7][0] = "Mean Hops";
        data[8][0] = "Std. Hops";

        data[0][1] = successRatio;
        data[1][1] = minElapsed;
        data[2][1] = maxElapsed;
        data[3][1] = meanElapsed;
        data[4][1] = stdElapsed;
        data[5][1] = minHopCount;
        data[6][1] = maxHopCount;
        data[7][1] = meanHopCount;
        data[8][1] = stdHopCount;

        TextTable summary = new TextTable(new String[] { "Property", "Value"}, data);
        summary.setAddRowNumbering(true);
        summary.printTable();
        System.out.println();
    }

    public static void clear() {
        searchQueryStats = new ArrayList<SearchQueryStat>();
    }

}
