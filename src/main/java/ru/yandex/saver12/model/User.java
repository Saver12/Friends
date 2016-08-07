package ru.yandex.saver12.model;

import java.util.Date;
import java.util.Objects;

/**
 * User Entity.
 *
 * @author Sergey Ermushin
 */

public class User {

    /**
     * User's name.
     */
    private String name;

    /**
     * User's surname.
     */
    private String surname;

    /**
     * User's email.
     */
    private String email;

    /**
     * User's country.
     */
    private String country;

    /**
     * User's birthdate.
     */
    private Date birthdate;

    /**
     * User's work place.
     */
    private String job;

    /**
     * User's education.
     */
    private String education;

    /**
     * User's relationship.
     */
    private String relationship;

    /**
     * User's id.
     */
    private int id;

    /**
     * ID of User's Profile Photo.
     */
    private int photoId;

    /**
     * Creates a new User object.
     *
     * @param name         User's name
     * @param surname      User's surname
     * @param email        User's email
     * @param country      User's country
     * @param birthdate    User's birthdate
     * @param job          User's work place
     * @param education    User's education
     * @param relationship User's relationship
     * @param id           User's id
     * @param photoId      ID of User's Profile Photo
     */
    public User(String name, String surname, String email, String country, Date birthdate, String job, String education, String relationship, int id, int photoId) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.country = country;
        this.birthdate = birthdate;
        this.job = job;
        this.education = education;
        this.relationship = relationship;
        this.id = id;
        this.photoId = photoId;
    }

    /**
     * @return User's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return User's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return User's id
     */
    public int getId() {
        return id;
    }

    /**
     * @return User's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return ID of User's Profile Photo
     */
    public int getPhotoId() {
        return photoId;
    }

    /**
     * @return User's surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return User's birthdate
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * @return User's work place
     */
    public String getJob() {
        return job;
    }

    /**
     * @return User's education
     */
    public String getEducation() {
        return education;
    }

    /**
     * @return User's relationship
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", birthdate=" + birthdate +
                ", job='" + job + '\'' +
                ", education='" + education + '\'' +
                ", relationship='" + relationship + '\'' +
                ", id=" + id +
                ", photoId=" + photoId +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(email, user.email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(email, id);
    }
}