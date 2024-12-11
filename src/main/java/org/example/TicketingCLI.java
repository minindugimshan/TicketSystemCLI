package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketingCLI {

    // Configuration variables
    private static int totalTickets;
    private static int ticketReleaseRate;
    private static int customerRetrievalRate;
    private static int maxTicketCapacity;

    // Lists to manage vendor and customer threads
    private static final List<Thread> vendorThreads = new ArrayList<>();
    private static final List<Thread> customerThreads = new ArrayList<>();
    private static TicketPool ticketPool;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("**** Real-Time Event Ticketing System ****");
        System.out.println("Please enter system configuration:");

        // Collect configuration inputs from the user
        totalTickets = getPositiveIntegerInput(scanner, "Total Tickets: ");
        ticketReleaseRate = getPositiveIntegerInput(scanner, "Ticket Release Rate (tickets/second): ");
        customerRetrievalRate = getPositiveIntegerInput(scanner, "Customer Retrieval Rate (tickets/second): ");
        maxTicketCapacity = getPositiveIntegerInput(scanner, "Maximum Ticket Capacity in Pool: ");

        System.out.println("\nConfiguration Completed!");
        // Start the main menu loop
        displayMenu(scanner);
    }


//      Prompt the user for a positive integer input.
//      Repeats until a valid value is entered.

    private static int getPositiveIntegerInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                if (value <= 0) {
                    throw new IllegalArgumentException("Value must be greater than 0.");
                }
                return value; // Valid input
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a positive integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }


    /**Displays the main menu and handles user selections.*/
    private static void displayMenu(Scanner scanner) {
        boolean systemStarted = false;

        while (true) {
            if (!systemStarted) {
                System.out.println("\n**** Main Menu ****");
                System.out.println("1. Start System");
                System.out.println("2. View Ticket Pool Status");
                System.out.println("3. Stop System");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
            }

            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        // Start the ticketing system
                        if (systemStarted) {
                            System.out.println("System is already running!");
                        } else {
                            startSystem();
                            systemStarted = true;
                        }
                    }
                    case 2 -> {
                        viewTicketPoolStatus();
                        systemStarted = false;
                    }
                    case 3 -> {
                        stopSystem();
                        systemStarted = false;
                    }
                    case 4 -> {
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();
                systemStarted = false; // Re-display menu for invalid input
            }
        }
    }

    /**
     * Starts the ticketing system by initializing the ticket pool
     * and creating vendor and customer threads.
     */
    private static void startSystem() {
        if (!vendorThreads.isEmpty() || !customerThreads.isEmpty()) {
            System.out.println("System is already running!");
            return;
        }

        // Initialize the ticket pool
        ticketPool = new TicketPool(maxTicketCapacity, totalTickets);

        // Create and start 3 vendor threads
        for (int i = 0; i < 3; i++) {
            Vendor vendor = new Vendor(ticketPool, ticketReleaseRate);
            Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create and start 5 customer threads
        for (int i = 0; i < 5; i++) {
            Customer customer = new Customer(ticketPool, customerRetrievalRate);
            Thread customerThread = new Thread(customer, "Customer-" + (i + 1));
            customerThreads.add(customerThread);
            customerThread.start();
        }

        System.out.println("System started with 3 Vendors and 5 Customers.");
    }

    private static void stopSystem() {
        // Interrupt all vendor threads
        for (Thread vendorThread : vendorThreads) {
            vendorThread.interrupt();
        }

        // Interrupt all customer threads
        for (Thread customerThread : customerThreads) {
            customerThread.interrupt();
        }

        // Clear the thread lists
        vendorThreads.clear();
        customerThreads.clear();

        System.out.println("System stopped.");
    }

    /**Displays the current status of the ticket pool**/
    private static void viewTicketPoolStatus() {
        if (ticketPool == null) {
            System.out.println("System not started yet.");
        } else {
            System.out.println("Current tickets in pool: " + ticketPool.getTicketCount());
            System.out.println("Total tickets added: " + ticketPool.getTotalTicketsAdded());
            System.out.println("Total tickets removed: " + ticketPool.getTotalTicketsRemoved());
        }
    }
}