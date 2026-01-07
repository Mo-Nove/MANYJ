package com.example.manyj;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

/**
 * M.A.N.Y. - J. Word Guess
 * Version: CSS Styled
 */
public class ManyWordGuessApp extends Application {

    private static final int WORD_LENGTH = 5;
    private static final int MAX_TRIES = 6;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;

    // Spielstatus
    private String secretWord;
    private int currentAttempt = 0;
    private int currentLetter = 0;
    private boolean isGameOver = false;
    private boolean isDarkMode = false;
    private boolean isMuted = false;

    // Daten
    private final List<String> wordList = Arrays.asList(
            "ABOUT", "ABOVE", "ACTOR", "ACUTE", "ADMIT", "ADOPT", "ADULT", "AFTER", "AGAIN", "AGENT",
            "AGREE", "AHEAD", "ALARM", "ALBUM", "ALERT", "ALIKE", "ALIVE", "ALLOW", "ALONE", "ALONG",
            "ALTER", "AMONG", "ANGER", "ANGLE", "ANGRY", "APART", "APPLE", "APPLY", "ARENA", "ARGUE",
            "ARISE", "ARRAY", "ASIDE", "ASSET", "AUDIO", "AUDIT", "AVOID", "AWARD", "AWARE", "BADLY",
            "BAKER", "BASIC", "BASIS", "BEACH", "BEGIN", "BEING", "BELOW", "BENCH", "BIRTH", "BLACK",
            "BLAME", "BLIND", "BLOCK", "BLOOD", "BOARD", "BOOST", "BOUND", "BRAIN", "BRAND", "BREAD",
            "BREAK", "BRICK", "BRIEF", "BRING", "BROAD", "BROWN", "BUILD", "BUILT", "BUYER", "CABLE",
            "CALIF", "CARRY", "CATCH", "CAUSE", "CHAIN", "CHAIR", "CHART", "CHASE", "CHEAP", "CHECK",
            "CHESS", "CHIEF", "CHILD", "CHINA", "CHOIR", "CLAIM", "CLASS", "CLEAN", "CLEAR", "CLICK",
            "CLOCK", "CLOSE", "COACH", "COAST", "COULD", "COUNT", "COURT", "COVER", "CRAFT", "CRASH",
            "CREAM", "CRIME", "CROSS", "CROWD", "CROWN", "CURVE", "CYCLE", "DAILY", "DANCE", "DATED",
            "DEALT", "DEATH", "DEBUT", "DELAY", "DEPTH", "DOING", "DOUBT", "DOZEN", "DRAFT", "DRAMA",
            "DREAM", "DRESS", "DRINK", "DRIVE", "DROVE", "DYING", "EARLY", "EARTH", "EIGHT", "ELDER",
            "ELECT", "ELITE", "EMPTY", "ENEMY", "ENJOY", "ENTER", "ENTRY", "EQUAL", "ERROR", "EVENT",
            "EVERY", "EXACT", "EXIST", "EXTRA", "FAITH", "FALSE", "FAULT", "FIBER", "FIELD", "FIFTH",
            "FIFTY", "FIGHT", "FINAL", "FIRST", "FIXED", "FLASH", "FLEET", "FLOOR", "FLUID", "FOCUS",
            "FORCE", "FORTH", "FORTY", "FOUND", "FRAME", "FRANK", "FRAUD", "FRESH", "FRONT", "FRUIT",
            "FULLY", "FUNNY", "GIANT", "GIVEN", "GLASS", "GLOBE", "GOING", "GRACE", "GRADE", "GRAND",
            "GRANT", "GRASS", "GREAT", "GREEN", "GROSS", "GROUP", "GROWN", "GUARD", "GUESS", "GUEST",
            "GUIDE", "HAPPY", "HARRY", "HEART", "HEAVY", "HENCE", "HORSE", "HOTEL", "HOUSE", "HUMAN",
            "IDEAL", "IMAGE", "IMPLY", "INDEX", "INNER", "INPUT", "ISSUE", "JOINT", "JUDGE", "KNOWN",
            "LABEL", "LARGE", "LASER", "LATER", "LAUGH", "LAYER", "LEARN", "LEAST", "LEAVE", "LEGAL",
            "LEVEL", "LIGHT", "LIMIT", "LOCAL", "LOGIC", "LOOSE", "LOWER", "LUCKY", "LUNCH", "MAJOR",
            "MAKER", "MARCH", "MATCH", "MAYBE", "MEANT", "MEDIA", "METAL", "MIGHT", "MINOR", "MODEL",
            "MONEY", "MONTH", "MORAL", "MOTOR", "MOUNT", "MOUSE", "MOUTH", "MOVIE", "MUSIC", "NAKED",
            "NEEDS", "NEVER", "NEWLY", "NIGHT", "NOISE", "NORTH", "NOVEL", "NURSE", "OCCUR", "OCEAN",
            "OFFER", "OFTEN", "OUIJA", "ORDER", "OTHER", "OUGHT", "PAINT", "PANEL", "PAPER", "PARTY", "PEACE",
            "PHASE", "PHONE", "PHOTO", "PIECE", "PILOT", "PITCH", "PLACE", "PLAIN", "PLANE", "PLANT",
            "PLATE", "POINT", "POUND", "POWER", "PRESS", "PRICE", "PRIDE", "PRIME", "PRINT", "PRIOR",
            "PRIZE", "PROOF", "PROUD", "PROVE", "QUEEN", "QUICK", "QUIET", "RADIO", "RAISE", "RANGE",
            "RAPID", "RATIO", "REACH", "READY", "REFER", "RIGHT", "RIVER", "ROBOT", "ROUGH", "ROUND",
            "ROUTE", "ROYAL", "RURAL", "SCALE", "SCENE", "SCOPE", "SCORE", "SENSE", "SERVE", "SEVEN",
            "SHALL", "SHAPE", "SHARE", "SHARP", "SHEET", "SHELF", "SHELL", "SHIFT", "SHIRT", "SHOCK",
            "SHOOT", "SHORT", "SHOWN", "SIGHT", "SINCE", "SKILL", "SLEEP", "SLIDE", "SMALL", "SMART",
            "SMILE", "SMOKE", "SOLID", "SOLVE", "SORRY", "SOUND", "SOUTH", "SPACE", "SPARE", "SPEAK",
            "SPEED", "SPEND", "SPENT", "SPLIT", "SPORT", "STAFF", "STAGE", "STAND", "START", "STATE",
            "STEEL", "STICK", "STILL", "STOCK", "STONE", "STORE", "STORM", "STORY", "STRIP", "STUCK",
            "STUDY", "STUFF", "STYLE", "SUGAR", "SUITE", "SUPER", "SWEET", "TABLE", "TAKEN", "TASTE",
            "TEACH", "TEETH", "TEXAS", "THANK", "THEIR", "THEME", "THERE", "THICK", "THING", "THINK",
            "THIRD", "THOSE", "THREE", "THROW", "TIGER", "TIMES", "TIRED", "TITLE", "TODAY", "TOPIC",
            "TOTAL", "TOUCH", "TOUGH", "TOWER", "TRACK", "TRADE", "TRAIN", "TREAT", "TREND", "TRIAL",
            "TRIED", "TRIES", "TRUCK", "TRULY", "TRUST", "TRUTH", "TWICE", "UNDER", "UNION", "UNITY",
            "UNTIL", "UPPER", "UPSET", "URBAN", "USAGE", "USUAL", "VALID", "VALUE", "VIDEO", "VIRUS",
            "VISIT", "VITAL", "VOICE", "WASTE", "WATCH", "WATER", "WEIGH", "WHEEL", "WHERE", "WHILE",
            "WHITE", "WHOLE", "WHOSE", "WOMAN", "WORLD", "WORRY", "WORTH", "WOULD", "WRITE", "WRONG",
            "YIELD", "YOUNG", "YOUTH"

    );

    // UI Komponenten
    private LetterTile[][] grid = new LetterTile[MAX_TRIES][WORD_LENGTH];
    private Map<String, Button> keyboardButtons = new HashMap<>();
    private Scene mainScene;
    private BorderPane rootLayout;
    private Stage primaryStage;
    private VBox mainContainer; // Der Hauptcontainer für Styles

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("M.A.N.Y. - J. Word Guess");

        showStartScreen();
        primaryStage.show();
    }

    // --- HILFSMETHODE: CSS LADEN ---
    private void applyStyles(Scene scene, Region root) {
        // CSS Datei laden
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);

        // Theme anwenden
        updateTheme(root);
    }

    private void updateTheme(Region root) {
        // Klasse "dark-mode" hinzufügen oder entfernen
        if (isDarkMode) {
            if (!root.getStyleClass().contains("dark-mode")) {
                root.getStyleClass().add("dark-mode");
            }
        } else {
            root.getStyleClass().remove("dark-mode");
        }
    }

    // --- NAVIGATION ---

    private void showStartScreen() {
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);

        VBox logo = createLogo();

        Button btnStart = createStyledButton("Start Game");
        Button btnSettings = createStyledButton("Settings");
        Button btnQuit = createStyledButton("Quit Game");

        btnStart.setOnAction(e -> startNewGame());
        btnSettings.setOnAction(e -> showSettingsScreen());
        btnQuit.setOnAction(e -> Platform.exit());

        layout.getChildren().addAll(logo, btnStart, btnSettings, btnQuit); // logo statt title

        mainScene = new Scene(layout, WIDTH, HEIGHT);
        applyStyles(mainScene, layout);
        primaryStage.setScene(mainScene);
    }

    private void showSettingsScreen() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));

        Text title = new Text("Settings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        title.getStyleClass().add("text");

        CheckBox darkModCheck = new CheckBox("Dark Mode");
        darkModCheck.setSelected(isDarkMode);
        darkModCheck.setStyle("-fx-font-size: 18px;");
        darkModCheck.setOnAction(e -> {
            isDarkMode = darkModCheck.isSelected();
            updateTheme(layout); // Theme sofort ändern
        });

        CheckBox muteCheck = new CheckBox("Mute Sounds");
        muteCheck.setSelected(isMuted);
        muteCheck.setStyle("-fx-font-size: 18px;");
        muteCheck.setOnAction(e -> isMuted = muteCheck.isSelected());

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(e -> showStartScreen());

        layout.getChildren().addAll(title, darkModCheck, muteCheck, backButton);

        // Alte Scene behalten, nur Root tauschen, damit CSS geladen bleibt
        mainScene.setRoot(layout);
        updateTheme(layout);
    }

    private void startNewGame() {
        isGameOver = false;
        currentAttempt = 0;
        currentLetter = 0;
        secretWord = wordList.get(new Random().nextInt(wordList.size()));
        System.out.println("DEBUG: Secret Word is " + secretWord);

        rootLayout = new BorderPane();
        mainContainer = new VBox();
        mainContainer.getChildren().add(rootLayout);
        VBox.setVgrow(rootLayout, Priority.ALWAYS);

        StackPane topBar = new StackPane();
        topBar.setPadding(new Insets(10, 20, 10, 20));

        // 1. Exit Button (Links)
        Button btnBack = new Button("Exit");
        btnBack.getStyleClass().add("menu-button");
        btnBack.setPrefHeight(40);
        btnBack.setPrefWidth(80);

        // WICHTIG: Fokus deaktivieren, damit Enter nicht den Button drückt!
        btnBack.setFocusTraversable(false);

        btnBack.setOnAction(e -> showStartScreen());

        // 2. Titel (Mitte)
        Text gameTitle = new Text("Word Guess");
        gameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Etwas größer
        gameTitle.getStyleClass().add("text"); // Damit er im Dark Mode weiß wird

        // Elemente hinzufügen
        topBar.getChildren().addAll(gameTitle, btnBack);

        // Positionierung: Titel in die Mitte, Button nach Links
        StackPane.setAlignment(gameTitle, Pos.CENTER);
        StackPane.setAlignment(btnBack, Pos.CENTER_LEFT);

        rootLayout.setTop(topBar);
        // ---------------------------------------------------------

        // Grid (Mitte)
        VBox gridBox = new VBox(5);
        gridBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < MAX_TRIES; i++) {
            HBox row = new HBox(5);
            row.setAlignment(Pos.CENTER);
            for (int j = 0; j < WORD_LENGTH; j++) {
                LetterTile tile = new LetterTile();
                grid[i][j] = tile;
                row.getChildren().add(tile);
            }
            gridBox.getChildren().add(row);
        }
        rootLayout.setCenter(gridBox);

        // Keyboard (Unten)
        VBox keyboardBox = createOnScreenKeyboard();
        rootLayout.setBottom(keyboardBox);

        // Scene erstellen
        mainScene = new Scene(mainContainer, WIDTH, HEIGHT);
        applyStyles(mainScene, mainContainer);

        mainScene.setOnKeyPressed(event -> handleInput(event.getCode(), null));
        primaryStage.setScene(mainScene);
    }

    // --- GAME LOGIC (Unverändert, aber nutzt CSS Klassen) ---

    private VBox createOnScreenKeyboard() {
        VBox layout = new VBox(8);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        String[] rows = {"QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM"};
        keyboardButtons.clear();

        for (String rowChars : rows) {
            HBox rowBox = new HBox(5);
            rowBox.setAlignment(Pos.CENTER);
            for (char c : rowChars.toCharArray()) {
                String letter = String.valueOf(c);
                Button key = new Button(letter);
                key.setPrefSize(40, 50);
                key.getStyleClass().add("key-button");

                key.setFocusTraversable(false);

                key.setOnAction(e -> handleInput(null, letter));
                keyboardButtons.put(letter, key);
                rowBox.getChildren().add(key);
            }
            // Sonderbehandlung für die letzte Zeile (Z...)
            if (rowChars.startsWith("Z")) {
                // NEU: Breitere Enter-Taste (80 statt 60)
                Button enter = new Button("ENTER");

                enter.setFocusTraversable(false);

                enter.setPrefSize(90, 50); // Genug Platz für den Text
                enter.getStyleClass().add("key-button");
                enter.setStyle("-fx-font-size: 12px;"); // Schrift etwas kleiner damit es passt
                enter.setOnAction(e -> handleInput(KeyCode.ENTER, null));

                Button back = new Button("⌫");

                back.setFocusTraversable(false);

                back.setPrefSize(60, 50);
                back.getStyleClass().add("key-button");
                back.setOnAction(e -> handleInput(KeyCode.BACK_SPACE, null));

                // Enter links, Backspace rechts
                rowBox.getChildren().add(0, enter);
                rowBox.getChildren().add(back);
            }
            layout.getChildren().add(rowBox);
        }
        return layout;
    }

    private void handleInput(KeyCode code, String textInput) {
        if (isGameOver) return;
        if (code == KeyCode.BACK_SPACE) {
            if (currentLetter > 0) {
                currentLetter--;
                grid[currentAttempt][currentLetter].setLetter("");
            }
        } else if (code == KeyCode.ENTER) {
            if (currentLetter == WORD_LENGTH) checkGuess();
        } else {
            String letter = (textInput != null) ? textInput : (code != null ? code.getName() : "");
            if (letter.length() == 1 && letter.matches("[A-Z]") && currentLetter < WORD_LENGTH) {
                grid[currentAttempt][currentLetter].setLetter(letter);
                currentLetter++;
            }
        }
    }

    /**
     * Prüft das eingegebene Wort (Core Logic)
     */
    private void checkGuess() {
        StringBuilder guessBuilder = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            guessBuilder.append(grid[currentAttempt][i].getLetter());
        }
        String guess = guessBuilder.toString();

        // 1. VALIDIERUNG: Existiert das Wort?
        // ACHTUNG: Aktuell prüfen wir nur gegen unsere kleine Liste.
        // Für ein echtes Spiel bräuchte man eine sehr große Liste mit allen erlaubten Wörtern.
        if (!wordList.contains(guess)) {
            shakeRow(currentAttempt); // Visuelles Feedback
            return; // ABBRUCH: Versuch zählt nicht!
        }

        // 2. Farben berechnen
        int[] letterStatus = new int[WORD_LENGTH]; // 0: grau, 1: gelb, 2: grün
        boolean[] secretUsed = new boolean[WORD_LENGTH];

        // Erst Grüne prüfen (Richtige Position)
        for (int i = 0; i < WORD_LENGTH; i++) {
            char gChar = guess.charAt(i);
            if (gChar == secretWord.charAt(i)) {
                letterStatus[i] = 2;
                secretUsed[i] = true;
            }
        }

        // Dann Gelbe prüfen (Falsche Position)
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (letterStatus[i] == 2) continue; // Schon grün

            char gChar = guess.charAt(i);
            boolean found = false;
            for (int k = 0; k < WORD_LENGTH; k++) {
                if (!secretUsed[k] && secretWord.charAt(k) == gChar) {
                    letterStatus[i] = 1;
                    secretUsed[k] = true;
                    found = true;
                    break;
                }
            }
        }

        // 3. UI Update (Grid & Tastatur)
        for (int i = 0; i < WORD_LENGTH; i++) {
            LetterTile tile = grid[currentAttempt][i];
            String l = String.valueOf(guess.charAt(i));

            if (letterStatus[i] == 2) {
                tile.setTileStatus("tile-correct");
                updateKeyStatus(l, "key-correct");
            } else if (letterStatus[i] == 1) {
                tile.setTileStatus("tile-present");
                updateKeyStatus(l, "key-present");
            } else {
                tile.setTileStatus("tile-absent");
                updateKeyStatus(l, "key-absent");
            }
        }

        // 4. Sieg/Niederlage prüfen
        if (guess.equals(secretWord)) {
            showEndGameDialog(true);
        } else {
            currentAttempt++;
            currentLetter = 0;
            if (currentAttempt >= MAX_TRIES) {
                showEndGameDialog(false);
            }
        }
    }

    /**
     * Lässt die aktuelle Zeile wackeln (wenn Wort ungültig)
     */
    private void shakeRow(int rowData) {
        // Wir müssen die HBox finden, die die aktuelle Zeile darstellt.
        // Da 'grid' nur die Tiles speichert, holen wir die HBox über den Parent der Tiles.
        if (grid[rowData][0].getParent() instanceof HBox rowBox) {
            TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(50), rowBox);
            tt.setByX(10f);
            tt.setCycleCount(4);
            tt.setAutoReverse(true);
            tt.play();
        }
    }

    private void updateKeyStatus(String letter, String cssClass) {
        if (keyboardButtons.containsKey(letter)) {
            Button btn = keyboardButtons.get(letter);
            // Priorität: Correct > Present > Absent > Default
            if (btn.getStyleClass().contains("key-correct")) return;

            // Alte Klassen entfernen, um Konflikte zu vermeiden
            if (cssClass.equals("key-correct")) {
                btn.getStyleClass().removeAll("key-present", "key-absent");
                btn.getStyleClass().add(cssClass);
            } else if (cssClass.equals("key-present") && !btn.getStyleClass().contains("key-correct")) {
                btn.getStyleClass().removeAll("key-absent");
                btn.getStyleClass().add(cssClass);
            } else if (!btn.getStyleClass().contains("key-correct") && !btn.getStyleClass().contains("key-present")) {
                btn.getStyleClass().add(cssClass);
            }
        }
    }

    private void showEndGameDialog(boolean won) {
        isGameOver = true;

        VBox dialogLayout = new VBox(20);
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.setPadding(new Insets(30));
        // Wir verlassen uns jetzt voll auf CSS (Rahmen etc.), daher keine harten Styles hier
        dialogLayout.setStyle("-fx-border-color: gray; -fx-border-width: 2;");

        Text message = new Text(won ? "Gewonnen!" : "Verloren: " + secretWord);
        message.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        message.getStyleClass().add("text"); // Damit auch dieser Text im Darkmode weiß ist

        // NEU: Wir nutzen die Hilfsmethode für das einheitliche Design
        Button btnRetry = createStyledButton("Retry");
        btnRetry.setOnAction(e -> {
            ((Stage) btnRetry.getScene().getWindow()).close();
            startNewGame();
        });

        // NEU: Auch hier Styled Button
        Button btnQuit = createStyledButton("Quit");
        btnQuit.setOnAction(e -> {
            ((Stage) btnQuit.getScene().getWindow()).close();
            showStartScreen();
        });

        dialogLayout.getChildren().addAll(message, btnRetry, btnQuit);

        Scene dialogScene = new Scene(dialogLayout);

        // WICHTIG: Das CSS muss auch auf das Popup-Fenster angewendet werden!
        applyStyles(dialogScene, dialogLayout);

        Stage dialogStage = new Stage();
        dialogStage.setTitle(won ? "Victory" : "Game Over");
        dialogStage.setScene(dialogScene);
        dialogStage.initOwner(primaryStage);
        dialogStage.show();
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(200, 50);
        btn.getStyleClass().add("menu-button"); // CSS Styling
        return btn;
    }

    // --- INNER CLASS: TILE ---
    // Jetzt als StackPane gestylt via CSS
    private class LetterTile extends StackPane {
        private Text text;

        public LetterTile() {
            this.getStyleClass().add("letter-tile"); // Basis CSS Klasse

            text = new Text("");
            this.getChildren().add(text);
        }

        public void setLetter(String letter) {
            text.setText(letter);
        }

        public String getLetter() {
            return text.getText();
        }

        // Neue Methode: Setzt CSS Klasse statt harter Farbe
        public void setTileStatus(String cssClass) {
            // Alte Status entfernen
            this.getStyleClass().removeAll("tile-correct", "tile-present", "tile-absent");
            this.getStyleClass().add(cssClass);
        }
    }
    /**
     * Erstellt das Logo im "Montreal Team" Stil.
     * Nutzt CSS für Farben, damit Dark Mode funktioniert.
     */
    private VBox createLogo() {
        VBox logoBox = new VBox(5); // 5px Abstand vertikal
        logoBox.setAlignment(Pos.CENTER);
        logoBox.getStyleClass().add("logo-container");

        // 1. Haupttext (M.A.N.Y. - J.)
        Text mainText = new Text("M.A.N.Y. - J.");
        mainText.getStyleClass().add("logo-main");

        // 2. Die Linie
        // Wir nutzen eine "Line", deren Länge wir fixieren oder binden können.
        // Hier machen wir sie etwas breiter als den Text visuell wirkt.
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(0, 0, 250, 0);
        line.getStyleClass().add("logo-line");

        // 3. Untertext (WORD GUESS)
        // Wir packen ihn in eine HBox, um ihn rechtsbündig unter der Linie zu haben
        // (wie im Montreal Logo), oder zentriert. Hier zentriert für Symmetrie.
        Text subText = new Text("WORD GUESS");
        subText.getStyleClass().add("logo-sub");

        // Option A: Zentriert (Klassisch für Games)
        logoBox.getChildren().addAll(mainText, line, subText);

        /* // Option B: Wenn du es exakt wie im Bild willst (Subtext rechtsbündig):
        HBox subTextBox = new HBox(subText);
        subTextBox.setAlignment(Pos.CENTER_RIGHT); // Oder CENTER für Symmetrie
        subTextBox.setMaxWidth(250); // Breite der Linie
        logoBox.getChildren().addAll(mainText, line, subTextBox);
        */

        return logoBox;
    }
}