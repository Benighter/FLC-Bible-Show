package org.quelea.data.bible;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for QuickBibleSearchParser.
 * 
 * @author Augment Agent
 */
public class QuickBibleSearchParserTest {
    
    @Test
    public void testBasicBookChapterVerse() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("Ge 1 2");
        assertTrue(parser.isValid(), "Should parse 'Ge 1 2' successfully");
        assertEquals("Genesis", parser.getFullBookName());
        assertEquals(1, parser.getFromChapter());
        assertEquals(2, parser.getFromVerse());
        assertEquals(1, parser.getToChapter());
        assertEquals(2, parser.getToVerse());
        assertTrue(parser.isSingleVerse());
    }
    
    @Test
    public void testColonFormat() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("Mt 5:3");
        assertTrue(parser.isValid(), "Should parse 'Mt 5:3' successfully");
        assertEquals("Matthew", parser.getFullBookName());
        assertEquals(5, parser.getFromChapter());
        assertEquals(3, parser.getFromVerse());
        assertTrue(parser.isSingleVerse());
    }
    
    @Test
    public void testVerseRange() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("Jn 3:16-17");
        assertTrue(parser.isValid(), "Should parse 'Jn 3:16-17' successfully");
        assertEquals("John", parser.getFullBookName());
        assertEquals(3, parser.getFromChapter());
        assertEquals(16, parser.getFromVerse());
        assertEquals(3, parser.getToChapter());
        assertEquals(17, parser.getToVerse());
        assertFalse(parser.isSingleVerse());
    }
    
    @Test
    public void testChapterRange() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("1Co 13:1-15:58");
        assertTrue(parser.isValid(), "Should parse '1Co 13:1-15:58' successfully");
        assertEquals("1 Corinthians", parser.getFullBookName());
        assertEquals(13, parser.getFromChapter());
        assertEquals(1, parser.getFromVerse());
        assertEquals(15, parser.getToChapter());
        assertEquals(58, parser.getToVerse());
    }
    
    @Test
    public void testWholeChapter() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("Ps 23");
        assertTrue(parser.isValid(), "Should parse 'Ps 23' successfully");
        assertEquals("Psalms", parser.getFullBookName());
        assertEquals(23, parser.getFromChapter());
        assertEquals(1, parser.getFromVerse());
        assertEquals(23, parser.getToChapter());
        assertEquals(999, parser.getToVerse());
        assertTrue(parser.isWholeChapter());
    }
    
    @Test
    public void testSpacedFormat() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("Ro 8 28");
        assertTrue(parser.isValid(), "Should parse 'Ro 8 28' successfully");
        assertEquals("Romans", parser.getFullBookName());
        assertEquals(8, parser.getFromChapter());
        assertEquals(28, parser.getFromVerse());
    }
    
    @Test
    public void testInvalidBook() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("XYZ 1 2");
        assertFalse(parser.isValid(), "Should not parse invalid book 'XYZ'");
        assertTrue(parser.getErrorMessage().contains("Unknown book"));
    }
    
    @Test
    public void testEmptyString() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("");
        assertFalse(parser.isValid(), "Should not parse empty string");
        assertTrue(parser.getErrorMessage().contains("empty"));
    }
    
    @Test
    public void testInvalidFormat() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("Genesis chapter one verse two");
        assertFalse(parser.isValid(), "Should not parse invalid format");
        assertTrue(parser.getErrorMessage().contains("Invalid search format"));
    }
    
    @Test
    public void testFormattedReference() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("Ge 1 2");
        assertEquals("Genesis 1:2", parser.getFormattedReference());
        
        parser = new QuickBibleSearchParser("Mt 5:3-7");
        assertEquals("Matthew 5:3-7", parser.getFormattedReference());
        
        parser = new QuickBibleSearchParser("Ps 23");
        assertEquals("Psalms 23", parser.getFormattedReference());
    }
    
    @Test
    public void testVariousAbbreviations() {
        // Test different abbreviations for the same book
        String[] genesisAbbrevs = {"Ge", "Gen", "Gn", "Genesis"};
        for (String abbrev : genesisAbbrevs) {
            QuickBibleSearchParser parser = new QuickBibleSearchParser(abbrev + " 1 1");
            assertTrue(parser.isValid(), "Should parse " + abbrev + " 1 1");
            assertEquals("Genesis", parser.getFullBookName());
        }
        
        String[] matthewAbbrevs = {"Mt", "Mat", "Matt", "Matthew"};
        for (String abbrev : matthewAbbrevs) {
            QuickBibleSearchParser parser = new QuickBibleSearchParser(abbrev + " 1 1");
            assertTrue(parser.isValid(), "Should parse " + abbrev + " 1 1");
            assertEquals("Matthew", parser.getFullBookName());
        }
    }
    
    @Test
    public void testCaseInsensitive() {
        QuickBibleSearchParser parser = new QuickBibleSearchParser("ge 1 2");
        assertTrue(parser.isValid(), "Should parse lowercase 'ge 1 2'");
        assertEquals("Genesis", parser.getFullBookName());
        
        parser = new QuickBibleSearchParser("GE 1 2");
        assertTrue(parser.isValid(), "Should parse uppercase 'GE 1 2'");
        assertEquals("Genesis", parser.getFullBookName());
    }
}
