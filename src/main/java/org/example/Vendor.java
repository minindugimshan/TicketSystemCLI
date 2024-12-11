package org.example;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;

//    Constructor to initialize the Vendor with a ticket pool and ticket release rate
    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        try {
            // Keep adding tickets until the system is complete or the thread is interrupted
            while (!Thread.currentThread().isInterrupted() && !ticketPool.isSystemComplete()) {
                ticketPool.addTickets(ticketReleaseRate);
                Thread.sleep(1000); // Adding tickets at 1 second intervals
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
