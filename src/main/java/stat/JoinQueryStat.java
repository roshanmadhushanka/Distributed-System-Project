package stat;

import dnl.utils.text.table.TextTable;

import java.util.ArrayList;

public class JoinQueryStat {
    private long sentTime;
    private long receivedTime;
    private static ArrayList<JoinQueryStat> joinQueryStats = new ArrayList<JoinQueryStat>();

    public JoinQueryStat() {
    }

    public JoinQueryStat(long sentTime, long receivedTime) {
        this.sentTime = sentTime;
        this.receivedTime = receivedTime;
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

    public void display() {
        long elapsed = receivedTime - sentTime;
        System.out.println(sentTime + "\t" + receivedTime + "\t" + elapsed);
    }

    @Override
    public String toString() {
        long elapsed = receivedTime - sentTime;
        return sentTime + "\t" + receivedTime + "\t" + elapsed + "\n";
    }

    public static void append(JoinQueryStat joinQueryStat) {
        joinQueryStats.add(joinQueryStat);
    }

    public static String[] getHeaders() {
        return new String[]{ "Sent Time", "Received Time", "Elapsed Time" };
    }

    public static Object[][] getData() {
        Object[][] data = new Object[joinQueryStats.size()][getHeaders().length];

        for(int row=0; row<joinQueryStats.size(); row++) {
            JoinQueryStat joinQueryStat = joinQueryStats.get(row);
            for(int col=0; col<getHeaders().length; col++) {
                data[row][0] = joinQueryStat.sentTime;
                data[row][1] = joinQueryStat.receivedTime;
                data[row][2] = joinQueryStat.receivedTime - joinQueryStat.sentTime;
            }
        }

        return data;
    }

    public static void displayAsTable() {
        System.out.println("Join Statistics");
        System.out.println("===============");
        TextTable joinStat = new TextTable(JoinQueryStat.getHeaders(), JoinQueryStat.getData());
        joinStat.setAddRowNumbering(true);
        joinStat.setSort(0);
        joinStat.printTable();
        System.out.println();
    }

    public static void clear() {
        joinQueryStats = new ArrayList<JoinQueryStat>();
    }


}
