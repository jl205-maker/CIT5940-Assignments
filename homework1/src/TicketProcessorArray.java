package homework1.src;

import java.util.List;
import java.util.ArrayList;

public class TicketProcessorArray {
    public static void main(String[] args) {
        long start;
        long end;
        long total = 0;
        // compute average runtime
        for (int i = 0; i < 10; i++) {
            start = System.nanoTime(); // start testing runtime
            processTicketsArrayList2();
            end = System.nanoTime(); // end testing runtime
            // System.out.println("Run time was " + (end - start) / 1000000.0 + " milliseconds." ); // print runtime
            total += (end - start);
        }
        System.out.println("Average run time was " + (total / 10.0) / 1000000.0 + " milliseconds." ); // print average runtime
    }

    public static void processTicketsArrayList() {
        ArrayList<String> ticketQueue = new ArrayList<>();

        // test cases
        // createShortQueue(ticketQueue);
        // createLongQueue(ticketQueue);

        while(!ticketQueue.isEmpty()) {
            String currentTicket = ticketQueue.remove(0);

            //System.out.println("Processing: " + currentTicket);

            //System.out.println("Finished! Remaining in line: " + ticketQueue.size());
            //System.out.println("---------------------------");
        }
    }

    // implementation that supports O(1) removal
    public static void processTicketsArrayList2() {
        ArrayList<String> ticketQueue = new ArrayList<>();

        // test cases
        // createShortQueue(ticketQueue);
        // createLongQueue(ticketQueue);

        while(!ticketQueue.isEmpty()) {
            // remove from end is O(1)
            // no longer a FIFO (queue) structure
            // now a LIFO (stack) structure
            String currentTicket = ticketQueue.remove(ticketQueue.size()-1);

            //System.out.println("Processing: " + currentTicket);

            //System.out.println("Finished! Remaining in line: " + ticketQueue.size());
            //System.out.println("---------------------------");
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
