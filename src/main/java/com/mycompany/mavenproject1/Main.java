// Main.java
package com.mycompany.mavenproject1;

import java.util.Scanner;

/**
 * Main entry point for the QuickChat application.
 * Handles registration, login, message sending, and Part 3 stored messages menu.
 */
public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Login login = new Login();

        // ================= REGISTRATION =================
        System.out.println("===== USER REGISTRATION =====");

        System.out.print("Enter First Name: ");
        String firstName = input.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = input.nextLine();

        System.out.print("Enter Username (must contain _ and be max 5 chars): ");
        String username = input.nextLine();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        System.out.print("Enter Cell Number (e.g. +27831234567): ");
        String cellNumber = input.nextLine();

        String registrationMessage = login.registerUser(firstName, lastName, username, password, cellNumber);
        System.out.println(registrationMessage);

        if (!registrationMessage.equals("User registered successfully.")) {
            input.close();
            return;
        }

        // ================= LOGIN =================
        System.out.println("\n===== USER LOGIN =====");

        System.out.print("Enter Username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter Password: ");
        String loginPassword = input.nextLine();

        String loginMessage = login.loginUser(loginUsername, loginPassword);
        System.out.println(loginMessage);

        if (!login.isLoggedIn()) {
            input.close();
            return;
        }

        // Load stored messages from previous sessions
        Message.loadStoredMessages();

        // ================= QUICKCHAT APP =================
        System.out.println("\nWelcome to QuickChat.");

        System.out.print("\nHow many messages would you like to send? ");
        int maxMessages = 0;
        try {
            maxMessages = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Exiting.");
            input.close();
            return;
        }

        int menuChoice;

        // -------- Main menu loop --------
        do {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            try {
                menuChoice = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                menuChoice = 0;
                continue;
            }

            switch (menuChoice) {
                case 1 -> {
                    int messagesSentThisSession = 0;

                    while (messagesSentThisSession < maxMessages) {
                        System.out.println("\n--- Message "
                                + (messagesSentThisSession + 1)
                                + " of " + maxMessages + " ---");

                        System.out.print("Enter recipient cell number: ");
                        String recipient = input.nextLine().trim();

                        String messageText = "";
                        while (true) {
                            System.out.print("Enter message (max 250 chars): ");
                            messageText = input.nextLine();
                            if (Message.validateMessageLength(messageText).equals("Message ready to send.")) {
                                System.out.println("Message ready to send.");
                                break;
                            } else {
                                System.out.println("Please enter a message of less than 250 characters.");
                            }
                        }

                        Message message = new Message();
                        message.setRecipient(recipient);
                        message.setMessageText(messageText);

                        if (message.checkRecipientCell().equals("Cell phone number successfully captured.")) {
                            System.out.println("Recipient cell number is valid.");
                        } else {
                            System.out.println("Invalid recipient number.");
                        }

                        System.out.println("Message ID: " + message.getMessageID());
                        System.out.println("Message Hash: " + message.getMessageHash());

                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose: ");

                        int sendChoice = 0;
                        try {
                            sendChoice = Integer.parseInt(input.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid choice.");
                        }

                        String sendResult = message.sentMessage(sendChoice);
                        System.out.println(sendResult);

                        System.out.println("\n--- Message Details ---");
                        System.out.println("Message ID   : " + message.getMessageID());
                        System.out.println("Message Hash : " + message.getMessageHash());
                        System.out.println("Recipient    : " + message.getRecipient());
                        System.out.println("Message      : " + message.getMessageText());

                        messagesSentThisSession++;
                    }

                    System.out.println("\n" + Message.returnTotalMessages());
                }
                case 2 -> System.out.println("Coming Soon.");
                case 3 -> System.out.println("Goodbye!");
                case 4 -> storedMessagesMenu(input);
                default -> System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }

        } while (menuChoice != 3);

        input.close();
    }

    /**
     * Displays the Stored Messages sub-menu and handles all six sub-options.
     * @param input the Scanner for user input
     */
    private static void storedMessagesMenu(Scanner input) {
        int subChoice;
        do {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display all stored messages");
            System.out.println("b) Display longest message");
            System.out.println("c) Search by message ID");
            System.out.println("d) Search by recipient");
            System.out.println("e) Delete by message hash");
            System.out.println("f) Display full report");
            System.out.println("0) Return to main menu");
            System.out.print("Choose: ");

            String subInput = input.nextLine().trim().toLowerCase();

            switch (subInput) {
                case "a" -> {
                    java.util.List<String> stored = Message.getStoredMessages();
                    if (stored.isEmpty()) {
                        System.out.println("No stored messages.");
                    } else {
                        for (String msg : stored) {
                            System.out.println(msg);
                        }
                    }
                }
                case "b" -> {
                    Message temp = new Message();
                    System.out.println(temp.displayLongestMessage());
                }
                case "c" -> {
                    System.out.print("Enter message ID: ");
                    String id = input.nextLine().trim();
                    System.out.println(Message.searchByMessageID(id));
                }
                case "d" -> {
                    System.out.print("Enter recipient number: ");
                    String rec = input.nextLine().trim();
                    System.out.println(Message.searchByRecipient(rec));
                }
                case "e" -> {
                    System.out.print("Enter message hash: ");
                    String hash = input.nextLine().trim();
                    System.out.println(Message.deleteByHash(hash));
                }
                case "f" -> System.out.println(Message.printMessages());
                case "0" -> System.out.println("Returning to main menu.");
                default  -> System.out.println("Invalid option.");
            }

            subChoice = subInput.equals("0") ? 0 : 1;

        } while (subChoice != 0);
    }
}