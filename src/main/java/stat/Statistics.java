package stat;

import dnl.utils.text.table.TextTable;

public class Statistics {
    private static int sentMessages = 0;
    private static int receivedMessages = 0;

    public static void incrementSentMessages() {
        sentMessages++;
    }

    public static void incrementReceivedMessages() {
        receivedMessages++;
    }

    public static void displayStatistics() {

        System.out.println("Sent Messages");
        System.out.println("=============");
        TextTable sentStat = new TextTable(new String[] { "Sent Messages" }, new Object[][] {{ sentMessages }});
        sentStat.setAddRowNumbering(true);
        sentStat.setSort(0);
        sentStat.printTable();
        System.out.println();

        System.out.println("Received Message");
        System.out.println("================");
        TextTable receivedStat = new TextTable(new String[] { "Received Messages" },
                new Object[][] {{ receivedMessages }});
        receivedStat.setAddRowNumbering(true);
        receivedStat.setSort(0);
        receivedStat.printTable();
        System.out.println();

        JoinQueryStat.displayAsTable();

        LeaveQueryStat.displayAsTable();

        SearchQueryStat.displayAsTable();
    }

    public static void clear() {
        sentMessages = 0;
        receivedMessages = 0;
    }
}
