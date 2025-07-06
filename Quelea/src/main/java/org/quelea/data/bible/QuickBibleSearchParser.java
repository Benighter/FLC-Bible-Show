package org.quelea.data.bible;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Parser for quick Bible search strings like "Ge 1 2" (Genesis 1:2), "Mt 5:3-7" (Matthew 5:3-7).
 * Supports various formats including book abbreviations, chapter numbers, and verse ranges.
 * 
 * @author Augment Agent
 */
public class QuickBibleSearchParser {
    
    // Regex patterns for different search formats
    private static final Pattern BOOK_CHAPTER_VERSE_PATTERN = 
        Pattern.compile("^\\s*([a-zA-Z0-9\\s]+?)\\s+(\\d+)\\s+(\\d+)(?:-(\\d+))?\\s*$");
    
    private static final Pattern BOOK_CHAPTER_VERSE_COLON_PATTERN = 
        Pattern.compile("^\\s*([a-zA-Z0-9\\s]+?)\\s+(\\d+):(\\d+)(?:-(\\d+))?\\s*$");
    
    private static final Pattern BOOK_CHAPTER_VERSE_RANGE_PATTERN = 
        Pattern.compile("^\\s*([a-zA-Z0-9\\s]+?)\\s+(\\d+):(\\d+)-(\\d+):(\\d+)\\s*$");
    
    private static final Pattern BOOK_CHAPTER_ONLY_PATTERN = 
        Pattern.compile("^\\s*([a-zA-Z0-9\\s]+?)\\s+(\\d+)\\s*$");
    
    private String bookName;
    private String fullBookName;
    private int bookIndex = -1;
    private int fromChapter = -1;
    private int fromVerse = -1;
    private int toChapter = -1;
    private int toVerse = -1;
    private boolean isValid = false;
    private String errorMessage = "";
    
    /**
     * Create a parser and parse the given quick search string.
     * 
     * Supported formats:
     * - "Ge 1 2" -> Genesis 1:2
     * - "Mt 5:3" -> Matthew 5:3
     * - "Jn 3:16-17" -> John 3:16-17
     * - "Ro 8:28-30" -> Romans 8:28-30
     * - "1Co 13:1-15:58" -> 1 Corinthians 13:1-15:58
     * - "Ps 23" -> Psalm 23 (entire chapter)
     * 
     * @param searchString the string to parse
     */
    public QuickBibleSearchParser(String searchString) {
        if (searchString == null || searchString.trim().isEmpty()) {
            errorMessage = "Search string cannot be empty";
            return;
        }
        
        parseSearchString(searchString.trim());
    }
    
    private void parseSearchString(String searchString) {
        // Try different patterns in order of specificity
        
        // Pattern: "Book Chapter:Verse-Chapter:Verse" (e.g., "1Co 13:1-15:58")
        Matcher matcher = BOOK_CHAPTER_VERSE_RANGE_PATTERN.matcher(searchString);
        if (matcher.matches()) {
            parseBookChapterVerseRange(matcher);
            return;
        }
        
        // Pattern: "Book Chapter:Verse-Verse" (e.g., "Jn 3:16-17")
        matcher = BOOK_CHAPTER_VERSE_COLON_PATTERN.matcher(searchString);
        if (matcher.matches()) {
            parseBookChapterVerseColon(matcher);
            return;
        }
        
        // Pattern: "Book Chapter Verse" (e.g., "Ge 1 2")
        matcher = BOOK_CHAPTER_VERSE_PATTERN.matcher(searchString);
        if (matcher.matches()) {
            parseBookChapterVerse(matcher);
            return;
        }
        
        // Pattern: "Book Chapter" (e.g., "Ps 23")
        matcher = BOOK_CHAPTER_ONLY_PATTERN.matcher(searchString);
        if (matcher.matches()) {
            parseBookChapterOnly(matcher);
            return;
        }
        
        errorMessage = "Invalid search format. Try formats like 'Ge 1 2', 'Mt 5:3', or 'Jn 3:16-17'";
    }
    
    private void parseBookChapterVerseRange(Matcher matcher) {
        String book = matcher.group(1);
        int chapter1 = Integer.parseInt(matcher.group(2));
        int verse1 = Integer.parseInt(matcher.group(3));
        int chapter2 = Integer.parseInt(matcher.group(4));
        int verse2 = Integer.parseInt(matcher.group(5));
        
        if (parseBook(book)) {
            fromChapter = chapter1;
            fromVerse = verse1;
            toChapter = chapter2;
            toVerse = verse2;
            isValid = true;
        }
    }
    
    private void parseBookChapterVerseColon(Matcher matcher) {
        String book = matcher.group(1);
        int chapter = Integer.parseInt(matcher.group(2));
        int verse1 = Integer.parseInt(matcher.group(3));
        String verse2Str = matcher.group(4);
        
        if (parseBook(book)) {
            fromChapter = chapter;
            fromVerse = verse1;
            toChapter = chapter;
            
            if (verse2Str != null) {
                toVerse = Integer.parseInt(verse2Str);
            } else {
                toVerse = verse1;
            }
            isValid = true;
        }
    }
    
    private void parseBookChapterVerse(Matcher matcher) {
        String book = matcher.group(1);
        int chapter = Integer.parseInt(matcher.group(2));
        int verse1 = Integer.parseInt(matcher.group(3));
        String verse2Str = matcher.group(4);
        
        if (parseBook(book)) {
            fromChapter = chapter;
            fromVerse = verse1;
            toChapter = chapter;
            
            if (verse2Str != null) {
                toVerse = Integer.parseInt(verse2Str);
            } else {
                toVerse = verse1;
            }
            isValid = true;
        }
    }
    
    private void parseBookChapterOnly(Matcher matcher) {
        String book = matcher.group(1);
        int chapter = Integer.parseInt(matcher.group(2));
        
        if (parseBook(book)) {
            fromChapter = chapter;
            fromVerse = 1;
            toChapter = chapter;
            toVerse = 999; // Will be limited by actual chapter length
            isValid = true;
        }
    }
    
    private boolean parseBook(String bookStr) {
        bookName = bookStr.trim();
        bookIndex = BibleBookAbbreviations.getBookIndex(bookName);
        
        if (bookIndex == -1) {
            errorMessage = "Unknown book: '" + bookName + "'. Try abbreviations like 'Ge', 'Mt', 'Jn', etc.";
            return false;
        }
        
        fullBookName = BibleBookAbbreviations.getFullBookName(bookName);
        return true;
    }
    
    /**
     * Check if the parsed search string is valid.
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * Get the error message if parsing failed.
     * @return the error message, or empty string if no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Get the original book name/abbreviation from the search string.
     * @return the book name as entered by user
     */
    public String getBookName() {
        return bookName;
    }
    
    /**
     * Get the full book name.
     * @return the full book name (e.g., "Genesis" for "Ge")
     */
    public String getFullBookName() {
        return fullBookName;
    }
    
    /**
     * Get the 0-based book index.
     * @return the book index, or -1 if invalid
     */
    public int getBookIndex() {
        return bookIndex;
    }
    
    /**
     * Get the starting chapter (1-based).
     * @return the starting chapter
     */
    public int getFromChapter() {
        return fromChapter;
    }
    
    /**
     * Get the starting verse (1-based).
     * @return the starting verse
     */
    public int getFromVerse() {
        return fromVerse;
    }
    
    /**
     * Get the ending chapter (1-based).
     * @return the ending chapter
     */
    public int getToChapter() {
        return toChapter;
    }
    
    /**
     * Get the ending verse (1-based).
     * @return the ending verse
     */
    public int getToVerse() {
        return toVerse;
    }
    
    /**
     * Check if this is a single verse reference.
     * @return true if from and to are the same verse
     */
    public boolean isSingleVerse() {
        return fromChapter == toChapter && fromVerse == toVerse;
    }
    
    /**
     * Check if this is a whole chapter reference.
     * @return true if it references an entire chapter
     */
    public boolean isWholeChapter() {
        return fromVerse == 1 && toVerse == 999;
    }
    
    /**
     * Get a formatted string representation of the parsed reference.
     * @return formatted reference string (e.g., "Genesis 1:2" or "Matthew 5:3-7")
     */
    public String getFormattedReference() {
        if (!isValid) return "";
        
        StringBuilder sb = new StringBuilder();
        sb.append(fullBookName).append(" ");
        
        if (isWholeChapter()) {
            sb.append(fromChapter);
        } else if (isSingleVerse()) {
            sb.append(fromChapter).append(":").append(fromVerse);
        } else if (fromChapter == toChapter) {
            sb.append(fromChapter).append(":").append(fromVerse).append("-").append(toVerse);
        } else {
            sb.append(fromChapter).append(":").append(fromVerse)
              .append("-").append(toChapter).append(":").append(toVerse);
        }
        
        return sb.toString();
    }
}
