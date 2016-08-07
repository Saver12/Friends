package ru.yandex.saver12.model;

import java.util.Objects;

/**
 * Audio Entity.
 *
 * @author Sergey Ermushin
 */

public class Audio {

    /**
     * Audio's id.
     */
    private int id;

    /**
     * ID value of User which this Audio belongs to.
     */
    private int userId;

    /**
     * Audio's description.
     */
    private String name;

    /**
     * Creates a new Audio object.
     *
     * @param id     Audio's id
     * @param userId User's id
     * @param name   Audio's description
     */
    public Audio(int id, int userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }

    /**
     * @return Audio's id
     */
    public int getId() {
        return id;
    }

    /**
     * @return User's id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return Audio's description
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Audio{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audio audio = (Audio) o;
        return id == audio.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
