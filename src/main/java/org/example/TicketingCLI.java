package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketingCLI {

    private static int totalTickets;
    private static int ticketReleaseRate;
    private static int customerRetrievalRate;
    private static int maxTicketCapacity;

    private static final List<Thread> vendorThreads = new ArrayList<>();
    private static final List<Thread> customerThreads = new ArrayList<>();
    private static TicketPool ticketPool;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==== Real-Time Event Ticketing System ====");
        System.out.println("Please enter system configuration:");

        totalTickets = getPositiveIntegerInput(scanner, "Total Tickets: ");
        ticketReleaseRate = getPositiveIntegerInput(scanner, "Ticket Release Rate (tickets/second): ");
        customerRetrievalRate = getPositiveIntegerInput(scanner, "Customer Retrieval Rate (tickets/second): ");
        maxTicketCapacity = getPositiveIntegerInput(scanner, "Maximum Ticket Capacity in Pool: ");

        System.out.println("\nConfiguration Completed!");
        displayMenu(scanner);
    }

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


    private static void displayMenu(Scanner scanner) {
        boolean systemStarted = false;

        while (true) {
            if (!systemStarted) {
                System.out.println("\n==== Main Menu ====");
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
                        if (systemStarted) {
                            System.out.println("System is already running!");
                        } else {
                            startSystem();
                            systemStarted = true; // Prevent showing the menu right after starting
                        }
                    }
                    case 2 -> {
                        viewTicketPoolStatus();
                        systemStarted = false; // Allow redisplay after showing the status
                    }
                    case 3 -> {
                        stopSystem();
                        systemStarted = false; // Allow redisplay after stopping the system
                    }
                    case 4 -> {
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
                systemStarted = false; // Re-display menu for invalid input
            }
        }
    }