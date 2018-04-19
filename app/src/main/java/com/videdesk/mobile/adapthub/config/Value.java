package com.videdesk.mobile.adapthub.config;

public class Value {

    // DATABASE DETAILS
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "videAdapthub";

    public static final String YOUTUBE_API_KEY = "AIzaSyAjPmkgiRYT1N8nO4n3JFxJwgIC6nhIT0E";

    // DIRECTORY NAMES
    public static final String KEY_DIR_IMAGES = "images";
    public static final String KEY_DIR_PHOTOS = "photos";

    // DATA VALUES
    public static final String KEY_READ_NO = "no";
    public static final String KEY_READ_YES = "yes";
    public static final String KEY_CHAT_IN = "in";
    public static final String KEY_CHAT_OUT = "out";
    public static final String KEY_GENDER_MALE = "male";
    public static final String KEY_GENDER_FEMALE = "female";
    public static final String KEY_USER_READ = "read";
    public static final String KEY_USER_WRITE = "write";
    public static final String KEY_USER_ADMIN = "admin";
    public static final String KEY_STATUS_PENDING = "pending";
    public static final String KEY_STATUS_ACTIVE = "active";
    public static final String KEY_STATUS_EXPIRED = "expired";
    public static final String KEY_STATUS_BLOCKED = "blocked";
    public static final String KEY_STATUS_DELETED = "deleted";

    public static final String KEY_NOTE_FAQ = "faq";
    public static final String KEY_NOTE_PRIVACY = "privacy";

    // TABLE COLUMNS
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_NODE = "node";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_ROLE= "role";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CAPTION = "caption";
    public static final String COLUMN_POSTAL = "postal";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_REGION = "region";
    public static final String COLUMN_NATION = "nation";
    public static final String COLUMN_ABOUT = "about";
    public static final String COLUMN_DETAILS = "detail";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DEADLINE = "deadline";
    public static final String COLUMN_WAGE = "wage";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_READ = "read";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_VENUE = "venue";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_VIDEO = "video";
    public static final String COLUMN_DOCUMENT = "document";

    // FOREIGN KEYS
    public static final String COLUMN_ALBUM_NODE = "album_node";
    public static final String COLUMN_CHAT_NODE = "chat_node";
    public static final String COLUMN_ENTITY_NODE = "entity_node";
    public static final String COLUMN_JOB_NODE = "job_node";
    public static final String COLUMN_LIKE_NODE = "like_node";
    public static final String COLUMN_NOTE_NODE = "note_node";
    public static final String COLUMN_PERSON_NODE = "person_node";
    public static final String COLUMN_PHOTO_NODE = "photo_node";
    public static final String COLUMN_SCHOLL_NODE = "scholl_node";
    public static final String COLUMN_STORY_NODE = "story_node";
    public static final String COLUMN_THEME_NODE = "theme_node";
    public static final String COLUMN_THEME_ID = "theme_id";
    public static final String COLUMN_THREAD_NODE = "thread_node";
    public static final String COLUMN_FILE_NODE = "file_node";
    public static final String COLUMN_EVENT_NODE = "event_node";
    public static final String COLUMN_BROWSE_URL = "browse_url";

    // TABLE NAMES
    public static final String TABLE_ALBUMS = "albums";
    public static final String TABLE_CHATS = "chats";
    public static final String TABLE_ENTITIES = "entities";
    public static final String TABLE_JOBS = "jobs";
    public static final String TABLE_LIKES = "likes";
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_PEOPLE = "people";
    public static final String TABLE_PHOTOS = "photos";
    public static final String TABLE_SCHOLLS = "scholls";
    public static final String TABLE_STORIES = "stories";
    public static final String TABLE_THEMES = "themes";
    public static final String TABLE_THREADS = "threads";
    public static final String TABLE_FILES = "files";
    public static final String TABLE_EVENTS = "events";

    // TABLE SQL
    public static final String SQL_ALBUM = "CREATE TABLE "  + TABLE_ALBUMS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NODE + " TEXT," +
            COLUMN_UID + " TEXT," +
            COLUMN_CAPTION + " TEXT," +
            COLUMN_IMAGE + " TEXT," +
            COLUMN_CREATED + " DATETIME," +
            COLUMN_STATUS + " TEXT," +
            COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_CHAT = "CREATE TABLE "  + TABLE_CHATS + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_THREAD_NODE + " TEXT," +
            Value.COLUMN_DETAILS + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_ENTITY = "CREATE TABLE "  + TABLE_ENTITIES + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_CAPTION + " TEXT," +
            Value.COLUMN_ABOUT + " TEXT," +
            Value.COLUMN_DETAILS + " TEXT," +
            Value.COLUMN_PHONE + " TEXT," +
            Value.COLUMN_EMAIL + " TEXT," +
            Value.COLUMN_URL + " TEXT," +
            Value.COLUMN_POSTAL + " TEXT," +
            Value.COLUMN_LOCATION + " TEXT," +
            Value.COLUMN_NATION + " TEXT," +
            Value.COLUMN_IMAGE + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_STATUS + " TEXT," +
            Value.COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_EVENT = "CREATE TABLE "  + TABLE_EVENTS + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_CAPTION + " TEXT," +
            Value.COLUMN_DATETIME + " TEXT," +
            Value.COLUMN_VENUE + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_STATUS + " TEXT," +
            Value.COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_FILE = "CREATE TABLE "  + TABLE_FILES + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_CONTENT + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_STATUS + " TEXT," +
            Value.COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_NOTE = "CREATE TABLE "  + TABLE_NOTES + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_CAPTION + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_STATUS + " TEXT," +
            Value.COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_JOB = "CREATE TABLE "  + TABLE_JOBS + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_DETAILS + " TEXT," +
            Value.COLUMN_LOCATION + " TEXT," +
            Value.COLUMN_IMAGE + " TEXT," +
            Value.COLUMN_DEADLINE + " DATETIME," +
            Value.COLUMN_WAGE + " TEXT," +
            Value.COLUMN_CURRENCY + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_STATUS + " TEXT," +
            Value.COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_LIKE = "CREATE TABLE "  + TABLE_LIKES + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_STORY_NODE + " TEXT" +
            ")";

    public static final String SQL_PERSON = "CREATE TABLE "  + TABLE_PEOPLE + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_ROLE + " TEXT," +
            Value.COLUMN_NAME + " TEXT," +
            Value.COLUMN_EMAIL + " TEXT," +
            Value.COLUMN_PHONE + " TEXT," +
            Value.COLUMN_IMAGE + " TEXT," +
            Value.COLUMN_NATION + " TEXT," +
            Value.COLUMN_REGION + " TEXT," +
            Value.COLUMN_LOCATION + " TEXT," +
            Value.COLUMN_GENDER + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_STATUS + " TEXT" +
            ")";

    public static final String SQL_PHOTO = "CREATE TABLE "  + TABLE_PHOTOS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NODE + " TEXT," +
            COLUMN_ALBUM_NODE + " TEXT," +
            COLUMN_CREATED + " DATETIME," +
            COLUMN_CAPTION + " TEXT," +
            COLUMN_IMAGE + " TEXT" +
            ")";

    public static final String SQL_SCHOLL = "CREATE TABLE "  + TABLE_SCHOLLS + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_CAPTION + " TEXT," +
            Value.COLUMN_IMAGE + " TEXT," +
            Value.COLUMN_DEADLINE + " DATETIME," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_STATUS + " TEXT," +
            Value.COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_STORY = "CREATE TABLE "  + TABLE_STORIES + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_THEME_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_CAPTION + " TEXT," +
            Value.COLUMN_DETAILS + " TEXT," +
            Value.COLUMN_IMAGE + " TEXT," +
            Value.COLUMN_VIDEO + " TEXT," +
            Value.COLUMN_DOCUMENT + " TEXT," +
            Value.COLUMN_ALBUM_NODE + " TEXT," +
            Value.COLUMN_URL + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME," +
            Value.COLUMN_STATUS + " TEXT," +
            Value.COLUMN_READ + " TEXT" +
            ")";

    public static final String SQL_THEME = "CREATE TABLE "  + TABLE_THEMES + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_CAPTION + " TEXT," +
            Value.COLUMN_IMAGE + " TEXT," +
            Value.COLUMN_COLOR + " TEXT" +
            ")";

    public static final String SQL_THREAD = "CREATE TABLE "  + TABLE_THREADS + "(" +
            Value.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Value.COLUMN_UID + " TEXT," +
            Value.COLUMN_NODE + " TEXT," +
            Value.COLUMN_TITLE + " TEXT," +
            Value.COLUMN_CAPTION + " TEXT," +
            Value.COLUMN_CREATED + " DATETIME" +
            ")";

    // Resources

    public static String[] themeNodes = {
            "2000",
            "1000",
            "3000",
            "4000",
            "5000",
            "6000",
            "7000",
            "8000",
            "9000",
    };

    public static String[] themeTitle = {
            "Water",
            "Food",
            "Market",
            "Ecosystem",
            "Gender",
            "Disaster",
            "RiU",
            "Weather",
            "News",
    };

    public static String[] themeCaption = {
            "Smart Water & Irrigation",
            "Sustainable Food & Livelihood",
            "Market System",
            "Ecosystem Management",
            "Gender & Social Differentiation",
            "Disaster & Risk Reduction",
            "Research into Use",
            "Weather & Irrigation System",
            "Current News",
    };

    public static String[] themeImages = {
            "img_water",
            "img_food",
            "img_market",
            "img_eco",
            "img_gender",
            "img_risk",
            "img_riu",
            "img_weather",
            "img_news",
    };

    public static String[] themeColors = {
            "colorWater",
            "colorFood",
            "colorMarket",
            "colorEco",
            "colorGender",
            "colorDanger",
            "colorRiu",
            "colorWeather",
            "colorNews"
    };
}
