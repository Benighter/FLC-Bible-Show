/*
 * This file is part of Quelea, free projection software for churches.
 *
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
package org.quelea.windows.main.toolbars;

import java.util.Date;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javax.swing.Timer;

import org.javafx.dialog.Dialog;
import org.quelea.services.languages.LabelGrabber;
import org.quelea.services.utils.QueleaProperties;
import org.quelea.services.utils.Utils;
import org.quelea.windows.main.QueleaApp;
import org.quelea.windows.main.actionhandlers.AddDVDActionHandler;
import org.quelea.windows.main.actionhandlers.AddImageActionHandler;
import org.quelea.windows.main.actionhandlers.AddPdfActionHandler;
import org.quelea.windows.main.actionhandlers.AddPowerpointActionHandler;
import org.quelea.windows.main.actionhandlers.AddVideoActionHandler;
import org.quelea.windows.main.actionhandlers.AddTimerActionHandler;
import org.quelea.windows.main.actionhandlers.AddWebActionHandler;
import org.quelea.windows.main.actionhandlers.RecordButtonHandler;
import org.quelea.windows.main.actionhandlers.NewScheduleActionHandler;
import org.quelea.windows.main.actionhandlers.NewSongActionHandler;
import org.quelea.windows.main.actionhandlers.OpenScheduleActionHandler;
import org.quelea.windows.main.actionhandlers.PrintScheduleActionHandler;
import org.quelea.windows.main.actionhandlers.QuickInsertActionHandler;
import org.quelea.windows.main.actionhandlers.SaveScheduleActionHandler;
import org.quelea.windows.main.actionhandlers.ShowNoticesActionHandler;
import org.quelea.data.bible.QuickBibleSearchParser;
import org.quelea.data.bible.BibleManager;
import org.quelea.data.bible.Bible;
import org.quelea.data.bible.BibleBook;
import org.quelea.data.bible.BibleChapter;
import org.quelea.data.bible.BibleVerse;
import org.quelea.data.displayable.BiblePassage;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Tooltip;

/**
 * Quelea's main toolbar.
 * <p/>
 *
 * @author Michael
 */
public class MainToolbar extends ToolBar {

    private final Button newScheduleButton;
    private final Button openScheduleButton;
    private final Button saveScheduleButton;
    private final Button printScheduleButton;
    private final Button newSongButton;
    private final Button quickInsertButton;
    private final Button manageNoticesButton;
    private final MenuButton add;
    private final ImageView loadingView;
    private final StackPane dvdImageStack;
    private final ToggleButton recordAudioButton;
    private final ProgressBar pb = new ProgressBar(0);
    private Dialog setRecordinPathWarning;
    private RecordButtonHandler recordingsHandler;
    private TextField recordingPathTextField;
    private boolean recording;
    private long openTime;
    private long recTime;

    // Quick Bible Search components
    private TextField quickBibleSearchField;
    private Label quickBibleSearchLabel;
    private ComboBox<Bible> quickBibleVersionSelector;
    private Timer recCount;

    /**
     * Create the toolbar and any associated shortcuts.
     */
    public MainToolbar() {
        if (Utils.isMac()) {
            newScheduleButton = getButtonFromImage("file:icons/filenewbig.png");
        } else {
            newScheduleButton = getButtonFromImage("file:icons/filenew.png");
        }
        Utils.setToolbarButtonStyle(newScheduleButton);
        newScheduleButton.setTooltip(new Tooltip(LabelGrabber.INSTANCE.getLabel("new.schedule.tooltip")));
        newScheduleButton.setOnAction(new NewScheduleActionHandler());
        getItems().add(newScheduleButton);

        if (Utils.isMac()) {
            openScheduleButton = getButtonFromImage("file:icons/fileopenbig.png");
        } else {
            openScheduleButton = getButtonFromImage("file:icons/fileopen.png");
        }
        Utils.setToolbarButtonStyle(openScheduleButton);
        openScheduleButton.setTooltip(new Tooltip(LabelGrabber.INSTANCE.getLabel("open.schedule.tooltip")));
        openScheduleButton.setOnAction(new OpenScheduleActionHandler());
        getItems().add(openScheduleButton);

        if (Utils.isMac()) {
            saveScheduleButton = getButtonFromImage("file:icons/filesavebig.png");
        } else {
            saveScheduleButton = getButtonFromImage("file:icons/filesave.png");
        }
        Utils.setToolbarButtonStyle(saveScheduleButton);
        saveScheduleButton.setTooltip(new Tooltip(LabelGrabber.INSTANCE.getLabel("save.schedule.tooltip")));
        saveScheduleButton.setOnAction(new SaveScheduleActionHandler(false));
        getItems().add(saveScheduleButton);

        if (Utils.isMac()) {
            printScheduleButton = getButtonFromImage("file:icons/fileprintbig.png");
        } else {
            printScheduleButton = getButtonFromImage("file:icons/fileprint.png");
        }
        Utils.setToolbarButtonStyle(printScheduleButton);
        printScheduleButton.setTooltip(new Tooltip(LabelGrabber.INSTANCE.getLabel("print.schedule.tooltip")));
        printScheduleButton.setOnAction(new PrintScheduleActionHandler());
        getItems().add(printScheduleButton);

        getItems().add(new Separator());

        if (Utils.isMac()) {
            newSongButton = getButtonFromImage("file:icons/newsongbig.png");
        } else {
            newSongButton = getButtonFromImage("file:icons/newsong.png");
        }
        Utils.setToolbarButtonStyle(newSongButton);
        newSongButton.setTooltip(new Tooltip(LabelGrabber.INSTANCE.getLabel("new.song.tooltip")));
        newSongButton.setOnAction(new NewSongActionHandler());
        getItems().add(newSongButton);

        getItems().add(new Separator());

        if (Utils.isMac()) {
            quickInsertButton = getButtonFromImage("file:icons/lightningbig.png");
        } else {
            quickInsertButton = getButtonFromImage("file:icons/lightning.png");
        }
        Utils.setToolbarButtonStyle(quickInsertButton);
        quickInsertButton.setTooltip(new Tooltip(LabelGrabber.INSTANCE.getLabel("quick.insert.text")));
        quickInsertButton.setOnAction(new QuickInsertActionHandler());
        getItems().add(quickInsertButton);

        add = new MenuButton("");

        ImageView iv = new ImageView(new Image(QueleaProperties.get().getUseDarkTheme() ? "file:icons/add_item-light.png" : "file:icons/add_item.png"));
        iv.setSmooth(true);
        iv.setFitWidth(20);
        iv.setFitHeight(20);
        add.setGraphic(iv);

        add.setStyle(Utils.TOOLBAR_BUTTON_STYLE);
        getItems().add(add);
        add.setOnMouseEntered(evt -> {
            QueleaApp.get().getMainWindow().requestFocus();
            add.show();
            openTime = new Date().getTime();
        });

        // Avoid menu being closed if users click to open it
        add.setOnMouseClicked(e -> {
            if (new Date().getTime() - openTime < 1000) {
                add.show();
            }
        });

        MenuItem addPresentationButton;
        if (Utils.isMac()) {
            addPresentationButton = getMenuItemFromImage("file:icons/powerpointbig.png");
        } else {
            addPresentationButton = getMenuItemFromImage("file:icons/powerpoint.png");
        }
        addPresentationButton.setText(LabelGrabber.INSTANCE.getLabel("add.presentation.tooltip"));
        addPresentationButton.setOnAction(new AddPowerpointActionHandler());
        add.getItems().add(addPresentationButton);

        MenuItem addMultimediaButton;
        if (Utils.isMac()) {
            addMultimediaButton = getMenuItemFromImage("file:icons/multimedia.png");
        } else {
            addMultimediaButton = getMenuItemFromImage("file:icons/multimedia.png");
        }
        addMultimediaButton.setText(LabelGrabber.INSTANCE.getLabel("add.multimedia.tooltip"));
        addMultimediaButton.setOnAction(new AddVideoActionHandler());
        add.getItems().add(addMultimediaButton);

        MenuItem addTimerButton;
        if (Utils.isMac()) {
            addTimerButton = getMenuItemFromImage("file:icons/timer-dark.png");
        } else {
            addTimerButton = getMenuItemFromImage("file:icons/timer-dark.png", 24, 24, false, true);
        }
        addTimerButton.setText(LabelGrabber.INSTANCE.getLabel("add.timer.tooltip"));
        addTimerButton.setOnAction(new AddTimerActionHandler());
        add.getItems().add(addTimerButton);

        if (Utils.isMac()) {
            loadingView = new ImageView(new Image("file:icons/loading.gif"));
        } else {
            loadingView = new ImageView(new Image("file:icons/loading.gif", 24, 24, false, true));
        }
        loadingView.setFitHeight(24);
        loadingView.setFitWidth(24);
        dvdImageStack = new StackPane();
        ImageView dvdIV;
        if (Utils.isMac()) {
            dvdIV = new ImageView(new Image("file:icons/dvd.png"));
        } else {
            dvdIV = new ImageView(new Image(QueleaProperties.get().getUseDarkTheme() ? "file:icons/dvd-light.png" : "file:icons/dvd.png", 24, 24, false, true));
        }
        dvdIV.setFitWidth(24);
        dvdIV.setFitHeight(24);
        dvdImageStack.getChildren().add(dvdIV);

        MenuItem addDVDButton = new MenuItem(LabelGrabber.INSTANCE.getLabel("add.dvd.button"), dvdImageStack);
        addDVDButton.setOnAction(new AddDVDActionHandler());
        if (!Utils.isMac()) {
            add.getItems().add(addDVDButton);
        }

        MenuItem addPdfButton;
        if (Utils.isMac()) {
            addPdfButton = getMenuItemFromImage("file:icons/add_pdfbig.png");
        } else {
            addPdfButton = getMenuItemFromImage("file:icons/add_pdf.png");
        }
        addPdfButton.setText(LabelGrabber.INSTANCE.getLabel("add.pdf.tooltip"));
        addPdfButton.setOnAction(new AddPdfActionHandler());
        add.getItems().add(addPdfButton);

        MenuItem addWebButton;
        if (Utils.isMac()) {
            addWebButton = getMenuItemFromImage("file:icons/web.png");
        } else {
            addWebButton = getMenuItemFromImage("file:icons/web-small.png");
        }
        addWebButton.setText(LabelGrabber.INSTANCE.getLabel("add.website"));
        addWebButton.setOnAction(new AddWebActionHandler());
        add.getItems().add(addWebButton);

        MenuItem addImageGroupButton;
        if (Utils.isMac()) {
            addImageGroupButton = getMenuItemFromImage("file:icons/image.png");
        } else {
            addImageGroupButton = getMenuItemFromImage("file:icons/image.png", 24, 24, false, true);
        }
        addImageGroupButton.setText(LabelGrabber.INSTANCE.getLabel("add.images.panel"));
        addImageGroupButton.setOnAction(new AddImageActionHandler());
        add.getItems().add(addImageGroupButton);

        getItems().add(new Separator());

        if (Utils.isMac()) {
            manageNoticesButton = getButtonFromImage("file:icons/infobig.png");
        } else {
            manageNoticesButton = getButtonFromImage("file:icons/info.png");
        }
        Utils.setToolbarButtonStyle(manageNoticesButton);
        manageNoticesButton.setTooltip(new Tooltip(LabelGrabber.INSTANCE.getLabel("manage.notices.tooltip")));
        manageNoticesButton.setOnAction(new ShowNoticesActionHandler());
        getItems().add(manageNoticesButton);

        // Add Quick Bible Search
        getItems().add(new Separator());

        quickBibleSearchLabel = new Label("Quick Bible:");
        quickBibleSearchLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666666;");
        getItems().add(quickBibleSearchLabel);

        // Bible version selector
        quickBibleVersionSelector = new ComboBox<>();
        quickBibleVersionSelector.setPrefWidth(120);
        quickBibleVersionSelector.setMaxWidth(120);
        quickBibleVersionSelector.setTooltip(new Tooltip("Select Bible version"));

        // Populate with available Bibles
        Bible[] availableBibles = BibleManager.get().getBibles();
        for (Bible bible : availableBibles) {
            quickBibleVersionSelector.getItems().add(bible);
        }

        // Set default selection
        String defaultBibleName = QueleaProperties.get().getDefaultBible();
        for (Bible bible : availableBibles) {
            if (bible.getBibleName().equals(defaultBibleName)) {
                quickBibleVersionSelector.getSelectionModel().select(bible);
                break;
            }
        }
        if (quickBibleVersionSelector.getSelectionModel().getSelectedItem() == null && availableBibles.length > 0) {
            quickBibleVersionSelector.getSelectionModel().selectFirst();
        }

        // Sync with main Bible panel when changed
        quickBibleVersionSelector.setOnAction(event -> {
            Bible selectedBible = quickBibleVersionSelector.getSelectionModel().getSelectedItem();
            if (selectedBible != null) {
                QueleaApp.get().getMainWindow().getMainPanel().getLibraryPanel().getBiblePanel().getBibleSelector().getSelectionModel().select(selectedBible);
            }
        });

        getItems().add(quickBibleVersionSelector);

        quickBibleSearchField = new TextField();
        quickBibleSearchField.setPromptText("e.g. Ge 1 2, Mt 5:3-7");
        quickBibleSearchField.setPrefWidth(150);
        quickBibleSearchField.setMaxWidth(150);
        quickBibleSearchField.setTooltip(new Tooltip("Quick Bible search: Type abbreviations like 'Ge 1 2' for Genesis 1:2, then press Enter to add to service, Shift+Enter to go live"));

        // Handle Enter key press for quick search
        quickBibleSearchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) {
                    // Shift+Enter: Add to service and go live
                    performQuickBibleSearch(true);
                } else {
                    // Enter: Add to service only
                    performQuickBibleSearch(false);
                }
            }
        });

        getItems().add(quickBibleSearchField);

        // Auto-hide add menu
        quickInsertButton.setOnMouseEntered(evt -> {
            add.hide();
        });
        manageNoticesButton.setOnMouseEntered(evt -> {
            add.hide();
        });
        QueleaApp.get().getMainWindow().getMainPanel().setOnMouseEntered(evt -> {
            add.hide();
        });
        QueleaApp.get().getMainWindow().getMainMenuBar().setOnMouseEntered(evt -> {
            add.hide();
        });

        recordingsHandler = new RecordButtonHandler();

        recordingPathTextField = new TextField();
        recordingPathTextField.setMinWidth(Region.USE_PREF_SIZE);
        recordingPathTextField.setMaxWidth(Region.USE_PREF_SIZE);

        // Set dynamic TextField width
        recordingPathTextField.textProperty().addListener((ov, prevText, currText) -> {
            Platform.runLater(() -> {
                Text text = new Text(currText);
                text.getStyleClass().add("text");
                text.setFont(recordingPathTextField.getFont());
                double width = text.getLayoutBounds().getWidth()
                        + recordingPathTextField.getPadding().getLeft() + recordingPathTextField.getPadding().getRight()
                        + 2d;
                recordingPathTextField.setPrefWidth(width);
                recordingPathTextField.positionCaret(recordingPathTextField.getCaretPosition());
            });
        });

        recordAudioButton = getToggleButtonFromImage("file:icons/record.png");
        Utils.setToolbarButtonStyle(recordAudioButton);
        recording = false;
        recordAudioButton.setOnMouseClicked(e -> {
            if (!QueleaProperties.get().getRecordingsPath().equals("")) {
                if (recording) {
                    stopRecording();
                } else {
                    startRecording();
                }
            } else {
                recordAudioButton.setSelected(false);
                Dialog.Builder setRecordingWarningBuilder = new Dialog.Builder()
                        .create()
                        .setTitle(LabelGrabber.INSTANCE.getLabel("recording.warning.title"))
                        .setMessage(LabelGrabber.INSTANCE.getLabel("recording.warning.message"))
                        .addLabelledButton(LabelGrabber.INSTANCE.getLabel("ok.button"), (ActionEvent t) -> {
                            setRecordinPathWarning.hide();
                            setRecordinPathWarning = null;
                        });
                setRecordinPathWarning = setRecordingWarningBuilder.setWarningIcon().build();
                setRecordinPathWarning.showAndWait();
            }
        });
        recordAudioButton.setTooltip(new Tooltip(LabelGrabber.INSTANCE.getLabel("record.audio.tooltip")));
        getItems().add(recordAudioButton);

    }

    public void setToggleButtonText(String text) {
        recordAudioButton.setText(text);
    }

    private Button getButtonFromImage(String uri) {
        return new Button("", getImageViewForButton(uri));
    }

    private ToggleButton getToggleButtonFromImage(String uri) {
        return new ToggleButton("", getImageViewForButton(uri));
    }

    private Button getButtonFromImage(String uri, int width, int height, boolean preserveRatio, boolean smooth) {
        ImageView iv = new ImageView(new Image(uri, width, height, preserveRatio, smooth));
        iv.setSmooth(true);
        iv.setFitWidth(24);
        iv.setFitHeight(24);
        return new Button("", iv);
    }

    private MenuItem getMenuItemFromImage(String uri) {
        return new MenuItem("", getImageViewForButton(uri));
    }

    private MenuItem getMenuItemFromImage(String uri, int width, int height, boolean preserveRatio, boolean smooth) {
        ImageView iv = new ImageView(new Image(QueleaProperties.get().getUseDarkTheme() ? uri.replace(".png", "-light.png") : uri, width, height, preserveRatio, smooth));
        iv.setSmooth(true);
        iv.setFitWidth(24);
        iv.setFitHeight(24);
        return new MenuItem("", iv);
    }

    private ImageView getImageViewForButton(String uri) {
        ImageView iv = new ImageView(new Image(QueleaProperties.get().getUseDarkTheme() ? uri.replace(".png", "-light.png") : uri));
        iv.setSmooth(true);
        iv.setFitWidth(24);
        iv.setFitHeight(24);
        return iv;
    }

    /**
     * Set if the DVD is loading.
     * <p>
     *
     * @param loading true if it's loading, false otherwise.
     */
    public void setDVDLoading(boolean loading) {
        if (loading && !dvdImageStack.getChildren().contains(loadingView)) {
            dvdImageStack.getChildren().add(loadingView);
        } else if (!loading) {
            dvdImageStack.getChildren().remove(loadingView);
        }
    }

    public void startRecording() {
        recordAudioButton.setSelected(true);
        recording = true;
//        getItems().add(pb);
        getItems().add(recordingPathTextField);
        recordAudioButton.setText("Recording...");
        recordAudioButton.setSelected(true);
        recordingsHandler = new RecordButtonHandler();
        recordingsHandler.passVariables("rec", pb, recordingPathTextField, recordAudioButton);
        final int interval = 1000; // 1000 ms
        recCount = new Timer(interval, (java.awt.event.ActionEvent e) -> {
            recTime += interval;
            setTime(recTime, recordAudioButton);
        });
        recCount.start();
    }

    public void stopRecording() {
        recordingsHandler.passVariables("stop", pb, recordingPathTextField, recordAudioButton);
        recordAudioButton.setSelected(false);
        recording = false;
        recCount.stop();
//        getItems().remove(pb);
        getItems().remove(recordingPathTextField);
        recordAudioButton.setText("");
        recordAudioButton.setSelected(false);
        recTime = 0;
    }

    public RecordButtonHandler getRecordButtonHandler() {
        return recordingsHandler;
    }

    /**
     * Method to set elapsed time on ToggleButton
     *
     * @param elapsedTimeMillis Time elapsed recording last was started
     * @param tb                ToggleButton to set time
     */
    private void setTime(long elapsedTimeMillis, ToggleButton tb) {
        float elapsedTimeSec = elapsedTimeMillis / 1000F;
        int hours = (int) elapsedTimeSec / 3600;
        int minutes = (int) (elapsedTimeSec % 3600) / 60;
        int seconds = (int) elapsedTimeSec % 60;
        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        Platform.runLater(() -> {
            tb.setText(time);
        });
    }

    /**
     * Perform quick Bible search based on the text in the quick search field.
     * Supports formats like "Ge 1 2" (Genesis 1:2), "Mt 5:3-7" (Matthew 5:3-7), etc.
     * @param goLive if true, add to service and go live; if false, just add to service
     */
    private void performQuickBibleSearch(boolean goLive) {
        String searchText = quickBibleSearchField.getText().trim();
        if (searchText.isEmpty()) {
            return;
        }

        QuickBibleSearchParser parser = new QuickBibleSearchParser(searchText);

        if (!parser.isValid()) {
            // Show error feedback
            quickBibleSearchField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            quickBibleSearchField.setTooltip(new Tooltip("Error: " + parser.getErrorMessage()));

            // Clear error styling after 3 seconds
            Platform.runLater(() -> {
                Timer timer = new Timer(3000, e -> {
                    quickBibleSearchField.setStyle("");
                    quickBibleSearchField.setTooltip(new Tooltip("Quick Bible search: Type abbreviations like 'Ge 1 2' for Genesis 1:2, then press Enter to add to service, Shift+Enter to go live"));
                });
                timer.setRepeats(false);
                timer.start();
            });
            return;
        }

        // Clear the search field and show success feedback
        quickBibleSearchField.clear();
        quickBibleSearchField.setStyle("-fx-border-color: green; -fx-border-width: 1px;");

        // Clear success styling after 1 second
        Platform.runLater(() -> {
            Timer timer = new Timer(1000, e -> {
                quickBibleSearchField.setStyle("");
            });
            timer.setRepeats(false);
            timer.start();
        });

        // Create and add Bible passage directly to the service
        Platform.runLater(() -> {
            try {
                createAndAddBiblePassage(parser, goLive);
            } catch (Exception e) {
                // If there's an error, show error feedback
                quickBibleSearchField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                quickBibleSearchField.setTooltip(new Tooltip("Error creating Bible passage: " + e.getMessage()));

                Platform.runLater(() -> {
                    Timer timer = new Timer(3000, evt -> {
                        quickBibleSearchField.setStyle("");
                        quickBibleSearchField.setTooltip(new Tooltip("Quick Bible search: Type abbreviations like 'Ge 1 2' for Genesis 1:2, then press Enter to add to service, Shift+Enter to go live"));
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            }
        });
    }

    /**
     * Get the quick Bible search field for external access (e.g., for keyboard shortcuts).
     * @return the quick Bible search text field
     */
    public TextField getQuickBibleSearchField() {
        return quickBibleSearchField;
    }

    /**
     * Create a Bible passage from the parsed search and add it to the service.
     * @param parser the parsed Bible search
     * @param goLive if true, also go live with the passage
     */
    private void createAndAddBiblePassage(QuickBibleSearchParser parser, boolean goLive) {
        // Get the current Bible from the toolbar selector
        Bible currentBible = quickBibleVersionSelector.getSelectionModel().getSelectedItem();
        if (currentBible == null) {
            throw new RuntimeException("No Bible version is selected");
        }

        // Get the book
        BibleBook[] books = currentBible.getBooks();
        if (parser.getBookIndex() >= books.length) {
            throw new RuntimeException("Book index out of range: " + parser.getBookIndex());
        }

        BibleBook book = books[parser.getBookIndex()];
        if (book == null) {
            throw new RuntimeException("Book not found: " + parser.getFullBookName());
        }

        // Get the chapter
        BibleChapter[] chapters = book.getChapters();
        int chapterIndex = parser.getFromChapter() - 1; // Convert to 0-based
        if (chapterIndex < 0 || chapterIndex >= chapters.length) {
            throw new RuntimeException("Chapter not found: " + parser.getFromChapter() + " in " + parser.getFullBookName());
        }

        BibleChapter chapter = chapters[chapterIndex];
        if (chapter == null) {
            throw new RuntimeException("Chapter is null: " + parser.getFromChapter() + " in " + parser.getFullBookName());
        }

        // Get the verses
        BibleVerse[] allVerses = chapter.getVerses();
        int fromVerse = parser.getFromVerse() - 1; // Convert to 0-based
        int toVerse = parser.getToVerse() - 1; // Convert to 0-based

        // Handle whole chapter case
        if (parser.isWholeChapter()) {
            toVerse = allVerses.length - 1;
        }

        // Validate verse range
        if (fromVerse < 0 || fromVerse >= allVerses.length) {
            throw new RuntimeException("Verse not found: " + parser.getFromVerse() + " in " + parser.getFullBookName() + " " + parser.getFromChapter());
        }
        if (toVerse < 0 || toVerse >= allVerses.length) {
            toVerse = allVerses.length - 1; // Clamp to last verse
        }
        if (toVerse < fromVerse) {
            toVerse = fromVerse; // Ensure valid range
        }

        // Create array of verses for the passage
        BibleVerse[] passageVerses = new BibleVerse[toVerse - fromVerse + 1];
        System.arraycopy(allVerses, fromVerse, passageVerses, 0, passageVerses.length);

        // Create the Bible passage
        String passageTitle = parser.getFormattedReference();
        BiblePassage passage = new BiblePassage(currentBible.getBibleName(), passageTitle, passageVerses, false);

        // Add to schedule
        QueleaApp.get().getMainWindow().getMainPanel().getSchedulePanel().getScheduleList().add(passage);

        // Navigate to the passage in the Bible panel
        navigateToBiblePassage(parser, currentBible);

        // Go live if requested
        if (goLive) {
            QueleaApp.get().getMainWindow().getMainPanel().getSchedulePanel().getScheduleList().getSelectionModel().select(passage);
            QueleaApp.get().getMainWindow().getMainPanel().getPreviewPanel().goLive();
        }
    }

    /**
     * Navigate to the specified Bible passage in the Bible panel.
     * @param parser the parsed Bible search
     * @param bible the Bible to use
     */
    private void navigateToBiblePassage(QuickBibleSearchParser parser, Bible bible) {
        Platform.runLater(() -> {
            try {
                var biblePanel = QueleaApp.get().getMainWindow().getMainPanel().getLibraryPanel().getBiblePanel();

                // Set the Bible selector
                biblePanel.getBibleSelector().getSelectionModel().select(bible);

                // Set the book selector
                BibleBook[] books = bible.getBooks();
                if (parser.getBookIndex() < books.length) {
                    BibleBook book = books[parser.getBookIndex()];
                    biblePanel.getBookSelector().getSelectionModel().select(book);

                    // Set the passage selector to show individual verses
                    String passageText;
                    if (parser.isWholeChapter()) {
                        // For whole chapter, just show the chapter number
                        passageText = String.valueOf(parser.getFromChapter());
                    } else if (parser.getFromVerse() == parser.getToVerse()) {
                        // Single verse
                        passageText = parser.getFromChapter() + ":" + parser.getFromVerse();
                    } else {
                        // Verse range - show the range to display all verses individually
                        passageText = parser.getFromChapter() + ":" + parser.getFromVerse() + "-" + parser.getToVerse();
                    }

                    biblePanel.getPassageSelector().setText(passageText);

                    // Switch to the Bible tab
                    QueleaApp.get().getMainWindow().getMainPanel().getLibraryPanel().getTabPane().getSelectionModel().select(1); // Bible tab is index 1
                }
            } catch (Exception e) {
                // If navigation fails, just continue - the passage was still added to the schedule
                System.err.println("Failed to navigate to Bible passage: " + e.getMessage());
            }
        });
    }
}
