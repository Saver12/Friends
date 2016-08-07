package ru.yandex.saver12.util;

/**
 * Constants representing HttpServletRequest object's names of attributes and parameters.
 *
 * @author Sergey Ermushin
 */

public enum Constants {

    ID("id"),
    REQUEST("request"),
    SENDER_ID("senderId"),
    NAME("name"),
    SURNAME("surname"),
    USER_ID("userId"),
    MESSAGE_LIST("messageList"),
    MESSAGE("message"),
    COUNTRY("country"),
    BIRTHDATE("birthdate"),
    JOB("job"),
    EDUCATION("education"),
    RELATIONSHIP("relationship"),
    EMAIL("email"),
    LANGUAGE("language"),
    PASSWORD("password"),
    PHOTO("photo"),
    AUDIO("audio"),
    DIALOGUE_LIST("dialogueList"),
    FRIENDS_LIST("friendsList"),
    REQUESTS_LIST("requestsList"),
    PHOTOS_LIST("photosList"),
    AUDIOS_LIST("audiosList"),
    USER("user"),
    UPLOAD_DIR("img"),
    MEDIA_DIR("media"),
    LIST("list"),
    USER_DAO("userDao"),
    MESSAGE_DAO("messageDAO"),
    FRIENDS_DAO("friendsDAO"),
    PHOTOS_DAO("photosDAO"),
    AUDIOS_DAO("audiosDAO"),
    STATS_RU("statsRU"),
    STATS_EN("statsEN");

    /**
     * String value of constant.
     */
    private final String text;

    /**
     * Creates a new Constants instance.
     *
     * @param text String value of constant
     */
    Constants(final String text) {
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return text;
    }
}
