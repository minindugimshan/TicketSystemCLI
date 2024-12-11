package org.example;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;

    public Customer(TicketPool ticketPool, int retrievalRate) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        try {
            // Keep attempting to purchase tickets until the system is complete or the thread is interrupted
            while (!Thread.currentThread().isInterrupted() && !ticketPool.isSystemComplete()) {
                ticketPool.purchaseTickets(retrievalRate);
                Thread.sleep(1000); // Attempting to purchase tickets at 1-second intervals
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
