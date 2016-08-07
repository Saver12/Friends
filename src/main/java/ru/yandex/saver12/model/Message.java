package ru.yandex.saver12.model;

import java.util.Objects;

/**
 * Message Entity.
 *
 * @author Sergey Ermushin
 */

public class Message {

    /**
     * String representing message.
     */
    private String message;

    /**
     * Message's id.
     */
    private int id;

    /**
     * ID of first User.
     */
    private int firstUserId;

    /**
     * ID of second User.
     */
    private int secondUserId;

    /**
     * Creates a new Message object.
     *
     * @param message      String representing message
     * @param id           Message's id
     * @param firstUserId  ID of first User
     * @param secondUserId ID of second User
     */
    public Message(String message, int id, int firstUserId, int secondUserId) {
        this.message = message;
        this.id = id;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
    }

    /**
     * @return String representing message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return Message's id
     */
    public int getId() {
        return id;
    }

    /**
     * @return ID of first User
     */
    public int getFirstUserId() {
        return firstUserId;
    }

    /**
     * @return ID of second User
     */
    public int getSecondUserId() {
        return secondUserId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", id=" + id +
                ", firstUserId=" + firstUserId +
                ", secondUserId=" + secondUserId +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
