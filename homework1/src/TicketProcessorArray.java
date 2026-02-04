package homework1.src;

import java.util.List;
import java.util.ArrayList;

public class TicketProcessorArray {
    public static void main(String[] args) {
        processTicketsArrayList();
    }

    public static void processTicketsArrayList() {
        ArrayList<String> ticketQueue = new ArrayList<>();

        // test cases
        // createShortQueue(ticketQueue);
        // createLongQueue(ticketQueue);

        while(!ticketQueue.isEmpty()) {
            String currentTicket = ticketQueue.remove(0);

            System.out.println("Processing: " + currentTicket);

            System.out.println("Finished! Remaining in line: " + ticketQueue.size());
            System.out.println("---------------------------");
        }
    }

    public static void createShortQueue(List<String> queue) {
        // feel free to change the number of tickets here to test different queue sizes
        for (int i = 1; i <= 50; i++) {
            queue.add("Ticket #" + i);
        }
    }

    public static void createLongQueue(List<String> queue) {
        // feel free to change the number of tickets here to test different queue sizes
        for (int i = 1; i <= 20000; i++) {
            queue.add("Ticket #" + i);
        }
    }
}
