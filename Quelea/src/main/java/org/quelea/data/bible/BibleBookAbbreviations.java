package org.quelea.data.bible;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for handling Bible book abbreviations and quick search functionality.
 * Supports common abbreviations like "Ge" for Genesis, "Mt" for Matthew, etc.
 * 
 * @author Augment Agent
 */
public class BibleBookAbbreviations {
    
    private static final Map<String, Integer> ABBREVIATION_TO_INDEX = new HashMap<>();
    private static final Map<String, String> ABBREVIATION_TO_FULL_NAME = new HashMap<>();
    
    // Standard 66-book Bible order
    private static final String[] BOOK_NAMES = {
        "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
        "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel",
        "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra",
        "Nehemiah", "Esther", "Job", "Psalms", "Proverbs",
        "Ecclesiastes", "Song of Songs", "Isaiah", "Jeremiah", "Lamentations",
        "Ezekiel", "Daniel", "Hosea", "Joel", "Amos",
        "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk",
        "Zephaniah", "Haggai", "Zechariah", "Malachi",
        "Matthew", "Mark", "Luke", "John", "Acts",
        "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians",
        "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy",
        "2 Timothy", "Titus", "Philemon", "Hebrews", "James",
        "1 Peter", "2 Peter", "1 John", "2 John", "3 John",
        "Jude", "Revelation"
    };
    
    static {
        initializeAbbreviations();
    }
    
    private static void initializeAbbreviations() {
        // Old Testament abbreviations
        addAbbreviations(0, "Genesis", "Ge", "Gen", "Gn");
        addAbbreviations(1, "Exodus", "Ex", "Exo", "Exod");
        addAbbreviations(2, "Leviticus", "Le", "Lev", "Lv");
        addAbbreviations(3, "Numbers", "Nu", "Num", "Nm", "Nb");
        addAbbreviations(4, "Deuteronomy", "De", "Deut", "Dt");
        addAbbreviations(5, "Joshua", "Jos", "Josh", "Jsh");
        addAbbreviations(6, "Judges", "Jdg", "Judg", "Jg");
        addAbbreviations(7, "Ruth", "Ru", "Rut", "Rth");
        addAbbreviations(8, "1 Samuel", "1Sa", "1Sam", "1S", "1 Sa", "1 Sam");
        addAbbreviations(9, "2 Samuel", "2Sa", "2Sam", "2S", "2 Sa", "2 Sam");
        addAbbreviations(10, "1 Kings", "1Ki", "1Kgs", "1K", "1 Ki", "1 Kgs");
        addAbbreviations(11, "2 Kings", "2Ki", "2Kgs", "2K", "2 Ki", "2 Kgs");
        addAbbreviations(12, "1 Chronicles", "1Ch", "1Chr", "1 Ch", "1 Chr");
        addAbbreviations(13, "2 Chronicles", "2Ch", "2Chr", "2 Ch", "2 Chr");
        addAbbreviations(14, "Ezra", "Ezr", "Ez");
        addAbbreviations(15, "Nehemiah", "Ne", "Neh", "Nh");
        addAbbreviations(16, "Esther", "Es", "Est", "Eth");
        addAbbreviations(17, "Job", "Job", "Jb");
        addAbbreviations(18, "Psalms", "Ps", "Psa", "Psalm");
        addAbbreviations(19, "Proverbs", "Pr", "Pro", "Prov");
        addAbbreviations(20, "Ecclesiastes", "Ec", "Ecc", "Eccl");
        addAbbreviations(21, "Song of Songs", "So", "SoS", "Song", "SS");
        addAbbreviations(22, "Isaiah", "Is", "Isa", "Ish");
        addAbbreviations(23, "Jeremiah", "Je", "Jer", "Jr");
        addAbbreviations(24, "Lamentations", "La", "Lam", "Lm");
        addAbbreviations(25, "Ezekiel", "Eze", "Ezk", "Ez");
        addAbbreviations(26, "Daniel", "Da", "Dan", "Dn");
        addAbbreviations(27, "Hosea", "Ho", "Hos", "Hs");
        addAbbreviations(28, "Joel", "Joe", "Jl");
        addAbbreviations(29, "Amos", "Am", "Amo");
        addAbbreviations(30, "Obadiah", "Ob", "Oba", "Abd");
        addAbbreviations(31, "Jonah", "Jon", "Jnh");
        addAbbreviations(32, "Micah", "Mic", "Mi");
        addAbbreviations(33, "Nahum", "Na", "Nah", "Nh");
        addAbbreviations(34, "Habakkuk", "Hab", "Hb");
        addAbbreviations(35, "Zephaniah", "Zep", "Zph");
        addAbbreviations(36, "Haggai", "Hag", "Hg");
        addAbbreviations(37, "Zechariah", "Zec", "Zch", "Zc");
        addAbbreviations(38, "Malachi", "Mal", "Ml");
        
        // New Testament abbreviations
        addAbbreviations(39, "Matthew", "Mt", "Mat", "Matt");
        addAbbreviations(40, "Mark", "Mk", "Mar", "Mrk");
        addAbbreviations(41, "Luke", "Lk", "Luk", "Lu");
        addAbbreviations(42, "John", "Jn", "Joh", "Jo");
        addAbbreviations(43, "Acts", "Ac", "Act", "Acts");
        addAbbreviations(44, "Romans", "Ro", "Rom", "Rm");
        addAbbreviations(45, "1 Corinthians", "1Co", "1Cor", "1C", "1 Co", "1 Cor");
        addAbbreviations(46, "2 Corinthians", "2Co", "2Cor", "2C", "2 Co", "2 Cor");
        addAbbreviations(47, "Galatians", "Ga", "Gal", "Gl");
        addAbbreviations(48, "Ephesians", "Ep", "Eph", "Ephes");
        addAbbreviations(49, "Philippians", "Php", "Phil", "Phi");
        addAbbreviations(50, "Colossians", "Col", "Cl");
        addAbbreviations(51, "1 Thessalonians", "1Th", "1Thes", "1T", "1 Th", "1 Thes");
        addAbbreviations(52, "2 Thessalonians", "2Th", "2Thes", "2T", "2 Th", "2 Thes");
        addAbbreviations(53, "1 Timothy", "1Ti", "1Tim", "1Tm", "1 Ti", "1 Tim");
        addAbbreviations(54, "2 Timothy", "2Ti", "2Tim", "2Tm", "2 Ti", "2 Tim");
        addAbbreviations(55, "Titus", "Tit", "Ti");
        addAbbreviations(56, "Philemon", "Phm", "Phlm", "Pm");
        addAbbreviations(57, "Hebrews", "He", "Heb", "Hb");
        addAbbreviations(58, "James", "Ja", "Jam", "Jas");
        addAbbreviations(59, "1 Peter", "1Pe", "1Pet", "1P", "1 Pe", "1 Pet");
        addAbbreviations(60, "2 Peter", "2Pe", "2Pet", "2P", "2 Pe", "2 Pet");
        addAbbreviations(61, "1 John", "1Jn", "1Jo", "1J", "1 Jn", "1 Jo");
        addAbbreviations(62, "2 John", "2Jn", "2Jo", "2J", "2 Jn", "2 Jo");
        addAbbreviations(63, "3 John", "3Jn", "3Jo", "3J", "3 Jn", "3 Jo");
        addAbbreviations(64, "Jude", "Jud", "Jd");
        addAbbreviations(65, "Revelation", "Re", "Rev", "Rv");
    }
    
    private static void addAbbreviations(int index, String fullName, String... abbreviations) {
        // Add the full name
        ABBREVIATION_TO_INDEX.put(fullName.toLowerCase(), index);
        ABBREVIATION_TO_FULL_NAME.put(fullName.toLowerCase(), fullName);
        
        // Add all abbreviations (case insensitive)
        for (String abbrev : abbreviations) {
            ABBREVIATION_TO_INDEX.put(abbrev.toLowerCase(), index);
            ABBREVIATION_TO_FULL_NAME.put(abbrev.toLowerCase(), fullName);
        }
    }
    
    /**
     * Get the book index (0-based) for a given abbreviation or full name.
     * @param abbreviation The book abbreviation or full name (case insensitive)
     * @return The 0-based book index, or -1 if not found
     */
    public static int getBookIndex(String abbreviation) {
        if (abbreviation == null) return -1;
        Integer index = ABBREVIATION_TO_INDEX.get(abbreviation.toLowerCase().trim());
        return index != null ? index : -1;
    }
    
    /**
     * Get the full book name for a given abbreviation.
     * @param abbreviation The book abbreviation or full name (case insensitive)
     * @return The full book name, or null if not found
     */
    public static String getFullBookName(String abbreviation) {
        if (abbreviation == null) return null;
        return ABBREVIATION_TO_FULL_NAME.get(abbreviation.toLowerCase().trim());
    }
    
    /**
     * Check if a string is a valid book abbreviation or name.
     * @param abbreviation The string to check
     * @return true if it's a valid book reference, false otherwise
     */
    public static boolean isValidBook(String abbreviation) {
        return getBookIndex(abbreviation) != -1;
    }
    
    /**
     * Get the book name at the given index.
     * @param index The 0-based book index
     * @return The book name, or null if index is out of range
     */
    public static String getBookNameAtIndex(int index) {
        if (index >= 0 && index < BOOK_NAMES.length) {
            return BOOK_NAMES[index];
        }
        return null;
    }
    
    /**
     * Get all supported abbreviations for a book.
     * @param bookName The full book name
     * @return A list of abbreviations for the book
     */
    public static List<String> getAbbreviationsForBook(String bookName) {
        // This is a simplified version - in a full implementation,
        // you might want to maintain a reverse mapping
        return Arrays.asList(); // Placeholder
    }
}
