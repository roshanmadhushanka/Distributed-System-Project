package stat;

import dnl.utils.text.table.TextTable;

import java.util.ArrayList;

public class LeaveQueryStat {
    private long sentTime;
    private long receivedTime;
    private static ArrayList<LeaveQueryStat> leaveQueryStats = new ArrayList<LeaveQueryStat>();

    public LeaveQueryStat() {
    }

    public LeaveQueryStat(long sentTime, long receivedTime) {
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

    public static void append(LeaveQueryStat leaveQueryStat) {
        leaveQueryStats.add(leaveQueryStat);
    }

    public static String[] getHeaders() {
        return new String[] { "Sent Time", "Received Time", "Elapsed Time" };
    }

    public static Object[][] getData() {
        Object[][] data = new Object[leaveQueryStats.size()][getHeaders().length];

        for(int row=0; row<leaveQueryStats.size(); row++) {
            LeaveQueryStat leaveQueryStat = leaveQueryStats.get(row);
            for(int col=0; col<getHeaders().length; col++) {
                data[row][0] = leaveQueryStat.sentTime;
                data[row][1] = leaveQueryStat.receivedTime;
                data[row][2] = leaveQueryStat.receivedTime - leaveQueryStat.sentTime;
            }
        }

        return data;
    }

    public static void displayAsTable() {
        System.out.println("Leave Statistics");
        System.out.println("================");
        TextTable leaveStat = new TextTable(LeaveQueryStat.getHeaders(), LeaveQueryStat.getData());
        leaveStat.setAddRowNumbering(true);
        leaveStat.setSort(0);
        leaveStat.printTable();
        System.out.println();
    }

    public static void clear() {
        leaveQueryStats = new ArrayList<LeaveQueryStat>();
    }
}
