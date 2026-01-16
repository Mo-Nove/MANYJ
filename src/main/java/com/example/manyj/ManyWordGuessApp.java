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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;


import java.util.*;


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
    private boolean isFullScreen = false;
    private javafx.scene.media.AudioClip currentAudioClip;

    // Daten
    private List<String> wordList = new ArrayList<>();
    private List<String> allWordList = new ArrayList<>();
    private List<String> easyWordList = new ArrayList<>();
    private boolean isHardMode = false;

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
        loadWords();

        this.primaryStage = stage;
        primaryStage.setTitle("M.A.N.Y. - J. Word Guess");

        // --- ICON SETZEN ---
        try {
            var iconStream = getClass().getResourceAsStream("/icon.png");
            if (iconStream != null) {
                stage.getIcons().add(new Image(iconStream));
            } else {
                System.out.println("Kein Icon gefunden (icon.png fehlt im resources Ordner).");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Laden des Icons: " + e.getMessage());
        }
        mainScene = new Scene(new VBox(), WIDTH, HEIGHT);

        mainScene.getStylesheets().add(getClass().getResource("/com/example/manyj/style.css").toExternalForm());

        primaryStage.setScene(mainScene);

        showStartScreen();

        primaryStage.show();
    }

    // --- CSS LADEN ---
    private void applyStyles(Scene scene, Region root) {
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);

        updateTheme(root);
    }

    private void loadWords() {
        // Hard Mode
        loadListFromFile("/words.txt", allWordList);

        // Easy Mode
        loadListFromFile("/common.txt", easyWordList);

        // Sicherheitsmaßnahme
        if (easyWordList.isEmpty()) {
            easyWordList.addAll(allWordList);
        }
    }


    private void loadListFromFile(String filename, List<String> targetList) {
        targetList.clear();
        try (var stream = getClass().getResourceAsStream(filename)) {
            if (stream != null) {
                Scanner scanner = new Scanner(stream);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim().toUpperCase();
                    if (line.length() == WORD_LENGTH && line.matches("[A-Z]+")) {
                        targetList.add(line);
                    }
                }
                System.out.println("Geladen: " + filename + " -> " + targetList.size() + " Wörter.");
            } else {
                System.err.println("Datei nicht gefunden: " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTheme(Region root) {
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

        updateTheme(layout);

        mainScene.setRoot(layout);

        mainScene.setOnKeyPressed(null);

        if (isFullScreen) {
            primaryStage.setFullScreen(true);
        }
    }

    private void showSettingsScreen() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));

        Text title = new Text("Settings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.getStyleClass().add("text"); // CSS

        CheckBox darkModCheck = new CheckBox("Dark Mode");
        darkModCheck.setSelected(isDarkMode);
        darkModCheck.setStyle("-fx-font-size: 18px;");
        darkModCheck.getStyleClass().add("text");
        darkModCheck.setOnAction(e -> {
            isDarkMode = darkModCheck.isSelected();
            updateTheme(layout);
        });

        CheckBox hardModeCheck = new CheckBox("Hard Mode");
        hardModeCheck.setSelected(isHardMode);
        hardModeCheck.setStyle("-fx-font-size: 18px;");
        hardModeCheck.getStyleClass().add("text");
        hardModeCheck.setTooltip(new Tooltip("Enables obscure words as solutions"));

        hardModeCheck.setOnAction(e -> {
            isHardMode = hardModeCheck.isSelected();
        });

        CheckBox fullScreenCheck = new CheckBox("Full Screen");
        fullScreenCheck.setSelected(isFullScreen);
        fullScreenCheck.setStyle("-fx-font-size: 18px;");
        fullScreenCheck.getStyleClass().add("text");

        fullScreenCheck.setOnAction(e -> {
            isFullScreen = fullScreenCheck.isSelected();
            primaryStage.setFullScreen(isFullScreen);
        });

        CheckBox muteCheck = new CheckBox("Mute Sounds");
        muteCheck.setSelected(isMuted);
        muteCheck.setStyle("-fx-font-size: 18px;");
        muteCheck.getStyleClass().add("text");
        muteCheck.setOnAction(e -> isMuted = muteCheck.isSelected());

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(e -> showStartScreen());

        layout.getChildren().addAll(title, darkModCheck, fullScreenCheck, hardModeCheck, muteCheck, backButton);

        mainScene.setRoot(layout);
        updateTheme(layout);
    }

    private void startNewGame() {
        // Spielstatus zurücksetzen
        isGameOver = false;
        currentAttempt = 0;
        currentLetter = 0;

        Random rand = new Random();
        if (isHardMode) {
            // Hard
            secretWord = allWordList.get(rand.nextInt(allWordList.size()));
        } else {
            // Easy
            secretWord = easyWordList.get(rand.nextInt(easyWordList.size()));
        }
        System.out.println("DEBUG: Secret Word is " + secretWord);

        rootLayout = new BorderPane();

        mainContainer = new VBox();
        mainContainer.getChildren().add(rootLayout);
        VBox.setVgrow(rootLayout, Priority.ALWAYS);

        StackPane topBar = new StackPane();
        topBar.setPadding(new Insets(10, 20, 10, 20));

        Button btnBack = new Button("Exit");
        btnBack.getStyleClass().add("menu-button");
        btnBack.setPrefHeight(40);
        btnBack.setPrefWidth(80);
        btnBack.setFocusTraversable(false); // WICHTIG: Damit Enter-Taste nicht den Button drückt
        btnBack.setOnAction(e -> showStartScreen());

        Text gameTitle = new Text("Word Guess");
        gameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gameTitle.getStyleClass().add("text"); // Passt sich an Dark Mode an

        topBar.getChildren().addAll(gameTitle, btnBack);
        StackPane.setAlignment(gameTitle, Pos.CENTER);
        StackPane.setAlignment(btnBack, Pos.CENTER_LEFT);

        rootLayout.setTop(topBar);


        // --- GRID (Spielfeld Mitte) ---
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

        // --- KEYBOARD (Unten) ---
        rootLayout.setBottom(createOnScreenKeyboard());

        updateTheme(mainContainer);

        mainScene.setRoot(mainContainer);

        mainScene.setOnKeyPressed(event -> handleInput(event.getCode(), null));

        if (isFullScreen) {
            primaryStage.setFullScreen(true);
        }
    }

    // --- GAME LOGIC ---

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
            if (rowChars.startsWith("Z")) {
                Button enter = new Button("ENTER");

                enter.setFocusTraversable(false);

                enter.setPrefSize(90, 50);
                enter.getStyleClass().add("key-button");
                enter.setStyle("-fx-font-size: 12px;");
                enter.setOnAction(e -> handleInput(KeyCode.ENTER, null));

                Button back = new Button("⌫");

                back.setFocusTraversable(false);

                back.setPrefSize(60, 50);
                back.getStyleClass().add("key-button");
                back.setOnAction(e -> handleInput(KeyCode.BACK_SPACE, null));

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


    private void checkGuess() {
        StringBuilder guessBuilder = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            guessBuilder.append(grid[currentAttempt][i].getLetter());
        }
        String guess = guessBuilder.toString();
        // --- EASTER EGG ---
        if (guess.equals("MANYJ")) {
            playSound("MANYJ.mp3");

            for (int i = 0; i < WORD_LENGTH; i++) {
                grid[currentAttempt][i].setTileStatus("tile-correct");
                grid[currentAttempt][i].setLetter(String.valueOf("MANYJ".charAt(i)));
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.initOwner(primaryStage);
            alert.setTitle("Easter Egg spotted");
            alert.setHeaderText("Secret song unlocked");
            alert.setContentText("Enjoy it");

            try {
                var imageStream = getClass().getResourceAsStream("/icon.png");
                if (imageStream != null) {
                    Image img = new Image(imageStream);
                    ImageView iconView = new ImageView(img);

                    iconView.setFitHeight(60);
                    iconView.setFitWidth(60);

                    alert.setGraphic(iconView);

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(img);
                }
            } catch (Exception e) {
                alert.setGraphic(null);
            }

            DialogPane dialogPane = alert.getDialogPane();

            dialogPane.getStylesheets().add(getClass().getResource("/com/example/manyj/style.css").toExternalForm());

            dialogPane.getStyleClass().add("game-dialog");

            if (isDarkMode) {
                dialogPane.getStyleClass().add("dark-mode");
            }

            alert.showAndWait();

            if (isFullScreen) {
                primaryStage.setFullScreen(true);
            }

            if (currentAudioClip != null) {
                currentAudioClip.stop();
            }

            showEndGameDialog(true);
            return;
        }

        // 1. VALIDIERUNG
        if (!allWordList.contains(guess)) {
            shakeRow(currentAttempt);
            playSound("error.mp3");
            return;
        }

        // 2. Farben berechnen
        int[] letterStatus = new int[WORD_LENGTH]; // 0: grau, 1: gelb, 2: grün
        boolean[] secretUsed = new boolean[WORD_LENGTH];

        // Grüne prüfen
        for (int i = 0; i < WORD_LENGTH; i++) {
            char gChar = guess.charAt(i);
            if (gChar == secretWord.charAt(i)) {
                letterStatus[i] = 2;
                secretUsed[i] = true;
            }
        }

        // Gelbe prüfen
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

        // 3. Grid & Tastatur
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
            playSound("win.mp3");
            showEndGameDialog(true);
        } else {
            currentAttempt++;
            currentLetter = 0;
            if (currentAttempt >= MAX_TRIES) {
                playSound("lose.mp3");
                showEndGameDialog(false);
            }
        }
    }

    // --- Animationen --
    private void shakeRow(int rowData) {
        if (grid[rowData][0].getParent() instanceof HBox rowBox) {
            rowBox.setTranslateX(0);

            TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(50), rowBox);
            tt.setByX(10f);
            tt.setCycleCount(4);
            tt.setAutoReverse(true);

            tt.setOnFinished(e -> rowBox.setTranslateX(0));

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
        dialogLayout.setStyle("-fx-border-color: gray; -fx-border-width: 2;");

        Text message = new Text(won ? "Gewonnen!" : "Verloren: " + secretWord);
        message.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        message.getStyleClass().add("text");

        Button btnRetry = createStyledButton("Retry");
        btnRetry.setOnAction(e -> {
            ((Stage) btnRetry.getScene().getWindow()).close();
            startNewGame();
        });

        Button btnQuit = createStyledButton("Quit");
        btnQuit.setOnAction(e -> {
            ((Stage) btnQuit.getScene().getWindow()).close();
            showStartScreen();
        });

        dialogLayout.getChildren().addAll(message, btnRetry, btnQuit);

        Scene dialogScene = new Scene(dialogLayout);

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
        btn.getStyleClass().add("menu-button");
        return btn;
    }

    // --- INNER CLASS: TILE ---
    private class LetterTile extends StackPane {
        private Text text;

        public LetterTile() {
            this.getStyleClass().add("letter-tile");

            text = new Text("");
            this.getChildren().add(text);
        }

        public void setLetter(String letter) {
            text.setText(letter);
        }

        public String getLetter() {
            return text.getText();
        }

        public void setTileStatus(String cssClass) {
            this.getStyleClass().removeAll("tile-correct", "tile-present", "tile-absent");
            this.getStyleClass().add(cssClass);
        }
    }
    // --- Logo ---
    private VBox createLogo() {
        VBox logoBox = new VBox(5); // 5px Abstand vertikal
        logoBox.setAlignment(Pos.CENTER);
        logoBox.getStyleClass().add("logo-container");

        // Haupttext
        Text mainText = new Text("M.A.N.Y. - J.");
        mainText.getStyleClass().add("logo-main");

        // Die Linie
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(0, 0, 250, 0);
        line.getStyleClass().add("logo-line");

        // Untertext
        Text subText = new Text("WORD GUESS");
        subText.getStyleClass().add("logo-sub");

        logoBox.getChildren().addAll(mainText, line, subText);

        return logoBox;
    }
    // --- Sounds ---
    private void playSound(String fileName) {
        if (isMuted) return;

        try {
            var resource = getClass().getResource("/" + fileName);
            if (resource != null) {
                if (currentAudioClip != null && currentAudioClip.isPlaying()) {
                    currentAudioClip.stop();
                }

                currentAudioClip = new AudioClip(resource.toExternalForm());
                currentAudioClip.play();
            } else {
                System.out.println("Sounddatei nicht gefunden: " + fileName);
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Abspielen von " + fileName);
        }
    }
}