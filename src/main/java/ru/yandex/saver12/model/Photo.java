package ru.yandex.saver12.model;

import java.util.Objects;

/**
 * Photo Entity.
 *
 * @author Sergey Ermushin
 */

public class Photo {

    /**
     * Photo's id.
     */
    private int id;

    /**
     * ID value of User which this Photo belongs to.
     */
    private int userId;

    /**
     * Photo's description.
     */
    private String name;

    /**
     * Creates a new Photo object.
     *
     * @param id     Photo's id
     * @param userId User's id
     * @param name   Photo's description
     */
    public Photo(int id, int userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }

    /**
     * @return Photo's id
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
     * @return Photo's description
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Photo{" +
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
        Photo photo = (Photo) o;
        return id == photo.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
