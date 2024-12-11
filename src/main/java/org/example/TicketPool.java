package org.example;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a shared pool of tickets for a ticketing system.
 */
public class TicketPool {
    private final Queue<Integer> tickets = new LinkedList<>();
    private final int maxCapacity;
    private final int totalTickets;
    private int totalTicketsAdded = 0;
    private int totalTicketsRemoved = 0;

// Constructor to initialize the TicketPool with maximum capacity and total tickets.
    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
    }

/** Adds tickets to the pool while ensuring that the pool does not exceed its capacity
   or the total ticket limit.*/
    public synchronized void addTickets(int count) {
        // Check for space or the total ticket limit before adding tickets
        while (tickets.size() >= maxCapacity || totalTicketsAdded >= totalTickets) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Calculate how many tickets can be added, ensuring not to exceed limits
        int ticketsToAdd = Math.min(count, maxCapacity - tickets.size());
        ticketsToAdd = Math.min(ticketsToAdd, totalTickets - totalTicketsAdded);

        // Add tickets to the pool
        for (int i = 0; i < ticketsToAdd; i++) {
            tickets.add(1);
            totalTicketsAdded++;
        }

        // Notify waiting threads that tickets have been added
        System.out.println(Thread.currentThread().getName() + " added " + ticketsToAdd + " tickets. Total tickets added: " + totalTicketsAdded);
        notifyAll();
    }

/**
 * Purchases tickets from the pool while ensuring that tickets are available.*/
    public synchronized void purchaseTickets(int count) {
        while (tickets.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Calculate how many tickets can be purchased
        int ticketsToRemove = Math.min(count, tickets.size());

        // Remove tickets from the pool
        for (int i = 0; i < ticketsToRemove; i++) {
            tickets.poll();
            totalTicketsRemoved++;
        }

        // Notify waiting threads that tickets have been purchased
        System.out.println(Thread.currentThread().getName() + " purchased " + ticketsToRemove + " tickets. Total tickets removed: " + totalTicketsRemoved);
        notifyAll();
    }

    // Get the number of tickets currently in the pool
    public synchronized int getTicketCount() {
        return tickets.size();
    }

    // Check if the system has finished processing tickets
    public synchronized boolean isSystemComplete() {
        return totalTicketsAdded >= totalTickets && tickets.isEmpty();
    }

    // Get the total number of tickets added
    public synchronized int getTotalTicketsAdded() {
        return totalTicketsAdded;
    }

    // Get the total number of tickets removed
    public synchronized int getTotalTicketsRemoved() {
        return totalTicketsRemoved;
    }
}
