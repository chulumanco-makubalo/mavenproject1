/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.mavenproject1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONObject;

/**
 * Represents a chat message in the QuickChat application.
 * Handles message creation, validation, hashing, sending, storing,
 * and all Part 3 array-based search, delete, and report features.
 */
public final class Message {

    // -------------------------------------------------------
    // INSTANCE FIELDS
    // -------------------------------------------------------
    private String messageID;
    private int numMessagesSent;
    private String recipient;
    private String messageText;
    private String messageHash;

    // -------------------------------------------------------
    // STATIC COUNTER
    // -------------------------------------------------------
    private static int totalMessagesSent = 0;

    // -------------------------------------------------------
    // PART 3 - FIVE PARALLEL ARRAYS
    // -------------------------------------------------------
    private static List<String> sentMessages        = new ArrayList<>();
    private static List<String> disregardedMessages = new ArrayList<>();
    private static List<String> storedMessages      = new ArrayList<>();
    private static List<String> messageHashes       = new ArrayList<>();
    private static List<String> messageIDs          = new ArrayList<>();
    private static List<String> recipientList       = new ArrayList<>();

    // -------------------------------------------------------
    // CONSTRUCTORS
    // -------------------------------------------------------

    /**
     * Creates a Message with a recipient and message text.
     * @param recipient the recipient cell number
     * @param messageText the message content
     */
    public Message(String recipient, String messageText) {
        this.messageID       = generateMessageID();
        this.recipient       = recipient;
        this.messageText     = messageText;
        this.numMessagesSent = totalMessagesSent + 1;
        this.messageHash     = createMessageHash();
    }

    /**
     * Creates an empty Message with a generated ID.
     */
    public Message() {
        this.messageID       = generateMessageID();
        this.numMessagesSent = totalMessagesSent + 1;
    }

    // -------------------------------------------------------
    // SETTERS
    // -------------------------------------------------------

    /**
     * Sets the recipient cell number.
     * @param recipient the recipient cell number
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Sets the message text and recalculates the hash.
     * @param messageText the message content
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // -------------------------------------------------------
    // 1. generateMessageID - random 10-digit number as String
    // -------------------------------------------------------

    /**
     * Generates a random 10-digit message ID.
     * @return a 10-character numeric String ID
     */
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        id.append((char) ('1' + random.nextInt(9)));
        for (int i = 1; i < 10; i++) {
            id.append((char) ('0' + random.nextInt(10)));
        }
        return id.toString();
    }

    // -------------------------------------------------------
    // 2. checkMessageID - ID must not exceed 10 characters
    // -------------------------------------------------------

    /**
     * Checks that the message ID does not exceed 10 characters.
     * @return true if valid, false otherwise
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // -------------------------------------------------------
    // 3. checkRecipientCell - returns String
    // -------------------------------------------------------

    /**
     * Validates the recipient cell number format.
     * Must start with international code (+) and be max 10 characters.
     * @return success or failure message String
     */
    public String checkRecipientCell() {
        if (recipient == null) {
            return "Cell phone number is incorrectly formatted or does not "
                 + "contain an international code. "
                 + "Please correct the number and try again.";
        }
        boolean startsWithCode = recipient.startsWith("+");
        boolean correctLength  = recipient.length() <= 10;
        if (startsWithCode && correctLength) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not "
                 + "contain an international code. "
                 + "Please correct the number and try again.";
        }
    }

    // -------------------------------------------------------
    // 4. createMessageHash
    // -------------------------------------------------------

    /**
     * Creates a hash in the format: XX:N:FIRSTWORDLASTWORD (all caps).
     * @return the message hash String
     */
    public String createMessageHash() {
        if (messageID == null || messageText == null || messageText.trim().isEmpty()) {
            return "";
        }
        String idPart  = messageID.substring(0, 2);
        String numPart = String.valueOf(numMessagesSent);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        String hash = idPart + ":" + numPart + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // -------------------------------------------------------
    // 5. sentMessage - send, disregard, or store
    // -------------------------------------------------------

    /**
     * Processes the user's choice to send, disregard, or store the message.
     * Populates the appropriate parallel arrays based on the choice.
     * @param choice 1=Send, 2=Disregard, 3=Store
     * @return status message String
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1 -> {
                totalMessagesSent++;
                numMessagesSent = totalMessagesSent;
                messageHash = createMessageHash();
                sentMessages.add(this.messageText);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                recipientList.add(this.recipient);
                return "Message successfully sent.";
            }
            case 2 -> {
                disregardedMessages.add(this.messageText);
                return "Press 0 to delete the message.";
            }
            case 3 -> {
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                recipientList.add(this.recipient);
                writeMessageToJSON();
                return "Message successfully stored.";
            }
            default -> {
                return "Invalid choice. Please select 1, 2, or 3.";
            }
        }
    }

    // -------------------------------------------------------
    // 6. writeMessageToJSON - writes stored message to file
    // -------------------------------------------------------

    /**
     * Writes the current message to messages.json as a JSON object.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     */
    private void writeMessageToJSON() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("messageID", this.messageID);
            obj.put("recipient", this.recipient);
            obj.put("messageText", this.messageText);
            obj.put("messageHash", this.messageHash);
            try (java.io.FileWriter fw = new java.io.FileWriter("messages.json", true)) {
                fw.write(obj.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing message to file: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // 7. loadStoredMessages - reads JSON file into storedMessages array
    // -------------------------------------------------------

    /**
     * Reads messages.json and populates the storedMessages array.
     * Called once at startup after login.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     */
    public static void loadStoredMessages() {
        try (BufferedReader br = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject obj = new JSONObject(line);
                    storedMessages.add(obj.getString("messageText"));
                }
            }
        } catch (IOException e) {
            // No file yet - continue without crashing
        }
    }

    // -------------------------------------------------------
    // 8. displayLongestMessage - finds longest in storedMessages
    // -------------------------------------------------------

    /**
     * Finds and returns the longest message in the storedMessages array.
     * @return the longest message String, or a message if array is empty
     */
    public String displayLongestMessage() {
        String longest = "";
        for (String msg : storedMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        if (longest.isEmpty()) {
            return "No stored messages found.";
        }
        return longest;
    }

    // -------------------------------------------------------
    // 9. searchByMessageID - parallel array search
    // -------------------------------------------------------

    /**
     * Searches the messageIDs array for a match and returns the corresponding message.
     * @param id the message ID to search for
     * @return the matching message text, or "Message not found."
     */
    public static String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                if (i < sentMessages.size()) {
                    return sentMessages.get(i);
                } else {
                    return storedMessages.get(i - sentMessages.size());
                }
            }
        }
        return "Message not found.";
    }

    // -------------------------------------------------------
    // 10. searchByRecipient - returns all messages for recipient
    // -------------------------------------------------------

    /**
     * Searches for all messages sent to the given recipient.
     * @param recipient the recipient cell number to search for
     * @return all matching messages as a formatted String
     */
    public static String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i) != null && recipientList.get(i).equals(recipient)) {
                if (i < sentMessages.size()) {
                    results.append(sentMessages.get(i)).append("\n");
                } else {
                    results.append(storedMessages.get(i - sentMessages.size())).append("\n");
                }
            }
        }
        if (results.length() == 0) {
            return "No messages found for recipient: " + recipient;
        }
        return results.toString().trim();
    }

    // -------------------------------------------------------
    // 11. deleteByHash - removes message and parallel entries
    // -------------------------------------------------------

    /**
     * Finds the message matching the given hash and removes it from all arrays.
     * @param hash the message hash to delete
     * @return success message or "Hash not found."
     */
    public static String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String deletedText = "";
                if (i < sentMessages.size()) {
                    deletedText = sentMessages.get(i);
                    sentMessages.remove(i);
                } else {
                    int storedIndex = i - sentMessages.size();
                    deletedText = storedMessages.get(storedIndex);
                    storedMessages.remove(storedIndex);
                }
                messageHashes.remove(i);
                messageIDs.remove(i);
                recipientList.remove(i);
                return "Message: " + deletedText + " successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    // -------------------------------------------------------
    // 12. printMessages - full report of all sent messages
    // -------------------------------------------------------

    /**
     * Returns a formatted report of all sent messages including hash, recipient, and text.
     * @return the full report as a String
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder report = new StringBuilder();
        report.append("=== Message Report ===\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("Message Hash : ").append(messageHashes.get(i)).append("\n");
            report.append("Recipient    : ").append(recipientList.get(i)).append("\n");
            report.append("Message      : ").append(sentMessages.get(i)).append("\n");
            report.append("-----------------------------\n");
        }
        return report.toString();
    }

    // -------------------------------------------------------
    // 13. validateMessageLength
    // -------------------------------------------------------

    /**
     * Validates that the message does not exceed 250 characters.
     * @param shortMessage the message to validate
     * @return "Message ready to send." or an error String with character overage
     */
    public static String validateMessageLength(String shortMessage) {
        if (shortMessage != null && shortMessage.length() <= 250) {
            return "Message ready to send.";
        } else {
            int over = shortMessage.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
    }

    // -------------------------------------------------------
    // 14. returnTotalMessages
    // -------------------------------------------------------

    /**
     * Returns the total number of messages sent this session.
     * @return total messages sent as int
     */
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // -------------------------------------------------------
    // 15. resetAll - used by unit tests
    // -------------------------------------------------------

    /**
     * Resets all static fields and arrays. Used by unit tests for clean state.
     */
    public static void resetAll() {
        totalMessagesSent = 0;
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipientList.clear();
    }

    // -------------------------------------------------------
    // GETTERS for stored messages list (used by sub-menu)
    // -------------------------------------------------------

    /**
     * Returns the list of stored messages.
     * @return storedMessages list
     */
    public static List<String> getStoredMessages() {
        return storedMessages;
    }

    // -------------------------------------------------------
    // GETTERS
    // -------------------------------------------------------
    public String getMessageID()       { return messageID; }
    public String getRecipient()       { return recipient; }
    public String getMessageText()     { return messageText; }
    public String getMessageHash()     { return messageHash; }
    public int    getNumMessagesSent() { return numMessagesSent; }
}
