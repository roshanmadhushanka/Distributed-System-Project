package model;

import java.util.HashMap;

public class MessageTable {
    private static int SIZE = 100;
    private static HashMap<Long, String> messages = new HashMap<Long, String>(SIZE);

    public static void put(long timestamp, String sentMessage) {
        messages.put(timestamp, sentMessage);
    }

    public static boolean validate(String responseMessage) {
        String[] responseChunks = responseMessage.split(" ");
        String responseType = responseChunks[1];
        long responseTimestamp = Long.parseLong(responseChunks[responseChunks.length - 1]);

        if(messages.containsKey(responseTimestamp)) {
            String sentMessage = messages.get(responseTimestamp);
            String[] sentChunks = sentMessage.split(" ");
            String sentType = sentChunks[1];

            if(responseType.equals("JOINOK") && sentType.equals("JOIN")) {
                messages.remove(responseTimestamp);
                return true;
            } else if(responseType.equals("LEAVEOK") && sentType.equals("LEAVE")) {
                messages.remove(responseTimestamp);
                return true;
            } else if(responseType.equals("SEROK") && sentType.equals("SER")) {
                messages.remove(responseTimestamp);
                return true;
            }
        }

        return false;
    }


}
