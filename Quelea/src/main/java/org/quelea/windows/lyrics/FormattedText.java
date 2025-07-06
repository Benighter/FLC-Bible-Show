/* 
 * This file is part of Quelea, free projection software for churches.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.quelea.windows.lyrics;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Basic formatted text that takes a (very) restricted subset of formatting tags
 * and applies them.
 * <p>
 * @author Michael
 */
public class FormattedText extends HBox {

    /**
     * Create new formatted text from a string.
     * <p>
     * @param line the string to create formatted text from.
     */
    public FormattedText(String line) {
        super(0);
        parseFormattedText(line);
    }

    /**
     * Parse formatted text with support for <sup> and <highlight> tags.
     * @param line the string to parse
     */
    private void parseFormattedText(String line) {
        while(!line.isEmpty()) {
            int startSup = line.indexOf("<sup>");
            int startHighlight = line.indexOf("<highlight>");

            // Find the earliest tag
            int nextTag = -1;
            String tagType = "";

            if(startSup != -1 && (startHighlight == -1 || startSup < startHighlight)) {
                nextTag = startSup;
                tagType = "sup";
            } else if(startHighlight != -1) {
                nextTag = startHighlight;
                tagType = "highlight";
            }

            if(nextTag == -1) {
                // No more tags, add remaining text
                if(!line.isEmpty()) {
                    getChildren().add(new Text(line));
                }
                break;
            }

            // Add text before the tag
            String normalStr = line.substring(0, nextTag);
            if(!normalStr.isEmpty()) {
                getChildren().add(new Text(normalStr));
            }

            if(tagType.equals("sup")) {
                line = line.substring(nextTag + 5); // Skip "<sup>"
                int endSup = line.indexOf("</sup>");
                if(endSup == -1) {
                    endSup = line.length();
                }
                String supStr = line.substring(0, Math.min(endSup, line.length()));
                if(!supStr.isEmpty()) {
                    Text supText = new Text(supStr);
                    supText.setScaleX(0.5);
                    supText.setScaleY(0.5);
                    getChildren().add(supText);
                }
                line = endSup < line.length() ? line.substring(Math.min(endSup + 6, line.length())) : "";
            } else if(tagType.equals("highlight")) {
                line = line.substring(nextTag + 11); // Skip "<highlight>"
                int endHighlight = line.indexOf("</highlight>");
                if(endHighlight == -1) {
                    endHighlight = line.length();
                }
                String highlightStr = line.substring(0, Math.min(endHighlight, line.length()));
                if(!highlightStr.isEmpty()) {
                    // Parse the highlighted text recursively to handle nested tags like <sup>
                    FormattedText highlightedText = new FormattedText("");
                    highlightedText.parseFormattedText(highlightStr);

                    // Apply highlighting style to all children
                    for(Node child : highlightedText.getChildren()) {
                        if(child instanceof Text) {
                            Text textNode = (Text) child;
                            textNode.setStyle("-fx-background-color: #e6f3ff; -fx-background-radius: 3px;");
                        }
                    }
                    getChildren().addAll(highlightedText.getChildren());
                }
                line = endHighlight < line.length() ? line.substring(Math.min(endHighlight + 12, line.length())) : "";
            }
        }
    }

    /**
     * Set the font of this formatted text.
     * <p>
     * @param font the font.
     */
    public void setFont(Font font) {
        for(Node node : getChildren()) {
            if(node instanceof Text) {
                ((Text) node).setFont(font);
            }
        }
    }

    /**
     * Set the fill of this formatted text.
     * <p>
     * @param paint the fill.
     */
    public void setFill(Paint paint) {
        for(Node node : getChildren()) {
            if(node instanceof Text) {
                ((Text) node).setFill(paint);
            }
        }
    }

    /**
     * Strip a string of all its formatting tags used to format text. This is
     * required when performed font metrics calculations.
     * <p>
     * @param text the text to strip tags from.
     * @return the string without formatting tags.
     */
    public static String stripFormatTags(String text) {
        if(text == null) {
            return null;
        }
        text = text.replace("<sup>", "");
        text = text.replace("</sup>", "");
        text = text.replace("<highlight>", "");
        return text.replace("</highlight>", "");
    }

}
