package org.quelea.data.bible;

/**
 * Simple test to verify our quick search functionality.
 */
public class TestQuickSearch {
    public static void main(String[] args) {
        System.out.println("Testing Quick Bible Search Parser...");
        
        // Test basic abbreviations
        testParser("Ge 1 2", "Genesis 1:2");
        testParser("Mt 5:3", "Matthew 5:3");
        testParser("Jn 3:16-17", "John 3:16-17");
        testParser("Ps 23", "Psalms 23");
        testParser("1Co 13:1-15:58", "1 Corinthians 13:1-15:58");
        
        // Test invalid cases
        testParser("XYZ 1 2", null);
        testParser("", null);
        
        System.out.println("Testing Bible Book Abbreviations...");
        
        // Test abbreviation lookup
        testAbbreviation("Ge", "Genesis", 0);
        testAbbreviation("Mt", "Matthew", 39);
        testAbbreviation("Jn", "John", 42);
        testAbbreviation("Rev", "Revelation", 65);
        testAbbreviation("XYZ", null, -1);
        
        System.out.println("All tests completed!");
    }
    
    private static void testParser(String input, String expectedOutput) {
        QuickBibleSearchParser parser = new QuickBibleSearchParser(input);
        if (expectedOutput == null) {
            if (!parser.isValid()) {
                System.out.println("✓ '" + input + "' correctly failed: " + parser.getErrorMessage());
            } else {
                System.out.println("✗ '" + input + "' should have failed but got: " + parser.getFormattedReference());
            }
        } else {
            if (parser.isValid()) {
                String result = parser.getFormattedReference();
                if (expectedOutput.equals(result)) {
                    System.out.println("✓ '" + input + "' -> '" + result + "'");
                } else {
                    System.out.println("✗ '" + input + "' expected '" + expectedOutput + "' but got '" + result + "'");
                }
            } else {
                System.out.println("✗ '" + input + "' failed: " + parser.getErrorMessage());
            }
        }
    }
    
    private static void testAbbreviation(String abbrev, String expectedName, int expectedIndex) {
        String actualName = BibleBookAbbreviations.getFullBookName(abbrev);
        int actualIndex = BibleBookAbbreviations.getBookIndex(abbrev);
        
        if (expectedName == null) {
            if (actualName == null && actualIndex == -1) {
                System.out.println("✓ '" + abbrev + "' correctly not found");
            } else {
                System.out.println("✗ '" + abbrev + "' should not be found but got: " + actualName + " at index " + actualIndex);
            }
        } else {
            if (expectedName.equals(actualName) && expectedIndex == actualIndex) {
                System.out.println("✓ '" + abbrev + "' -> '" + actualName + "' (index " + actualIndex + ")");
            } else {
                System.out.println("✗ '" + abbrev + "' expected '" + expectedName + "' (index " + expectedIndex + ") but got '" + actualName + "' (index " + actualIndex + ")");
            }
        }
    }
}
