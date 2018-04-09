package com.nebula.connect;

/**
 * Created by siddhesh on 7/25/16.
 */
public enum Settings {

    TOKEN("TOKEN"),
    SESS_NAME_ID("SESS_NAME_ID"),
    USER_ID("LOGIN"),
    PASSWORD("PASSWORD"),
    FIRST_TIME("FIRST_TIME"),
    STATE("STATE"),
    GALLERY("GALLERY"),
    DEVICE_ID("DEVICE_ID"),
    PAST_DAYS("PAST_DAYS"),
    PRODUCT_AVAIL_QUANTITY("PRODUCT_AVAIL_QUANTITY"),
    FUTURE_DAYS("FUTURE_DAYS"),
    START_TIME("START_TIME"),
    PHOTOS_ID("PHOTOS_ID"),
    START_TIME_DISPLAY("START_TIME_DISPLAY"),
    MEETING_TYPES("MEETING_TYPES"),
    SUPPORT_CALL("SUPPORT_CALL"),
    MIN_MEETING_COUNT("MIN_MEETING_COUNT"),
    MAX_MEETING_COUNT("MAX_MEETING_COUNT"),
    MIN_PAINTER_COUNT("MIN_PAINTER_COUNT"),
    MAX_PAINTER_COUNT("MAX_PAINTER_COUNT"),
    FCM_TOKEN("FCM_TOKEN");

    private final String text;

    /**
     * @param text
     */
    private Settings(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
