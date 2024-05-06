package cs1302.api;

import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.*;
import java.net.URL;
import java.nio.charset.*;


import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    Stage stage;
    Scene scene;
    VBox root;

    HBox userDate;
    Label prompt;
    ChoiceBox<String> userCountry;
    Button calculate;
    String countryHolidays;

    HBox next;
    Text nextHoliday;

    HBox countDownDays;
    Text days;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        root.setSpacing(20);
        root.setMaxHeight(720.0);
        root.setMaxWidth(1280.0);
        root.setPrefHeight(180.0);
        root.setPrefWidth(500.0);


        userDate = new HBox();
        prompt = new Label("Enter Your Country: ");
        userCountry = new ChoiceBox<>();
        calculate = new Button("Get Next Holiday!");
        countryHolidays = "";
        userDate.setAlignment(Pos.CENTER);

        next = new HBox();
        nextHoliday = new Text("Please enter the country you live in");
        nextHoliday.setTextAlignment(TextAlignment.CENTER);
        next.setAlignment(Pos.CENTER);

        countDownDays = new HBox();
        days = new Text("");
        days.setTextAlignment(TextAlignment.CENTER);
        countDownDays.setAlignment(Pos.CENTER);
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        root.getChildren().addAll(userDate, next, countDownDays);
        userDate.getChildren().addAll(prompt, userCountry, calculate);
        userCountry.getItems().addAll(
            "(AR) Argentina", "(AU) Australia", "(BE) Belgium", "(BR) Brazil", "(CA) Canada",
            "(CN) China", "(FR) France", "(DE) Germany", "(IT) Italy", "(ID) Indonesia",
            "(IE) Ireland", "(JP) Japan", "(MX) Mexico", "(NO) Norway", "(NL) Netherlands",
            "(NZ) New Zealand", "(KR) South Korea", "(ES) Spain", "(GB) United Kingdom",
            "(US) United States"
            );

        next.getChildren().addAll(nextHoliday);

        countDownDays.getChildren().addAll(days);


        calculate.setOnAction(e -> daysRemaining());
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;



        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    /**
     *Makes a pop-up error message.
     *
     *@param url The iTunes URL
     *@param err A string that should be the error
     */
    private void err(String url, String err) {
        TextArea body = new TextArea("URI: " + url + "\n\nException: " + err);
        Alert errorMessage = new Alert(AlertType.ERROR);
        errorMessage.getDialogPane().setContent(body);
        errorMessage.showAndWait();
    }

    public String  getHoliday() {
        String nextHolidayDate = "";
        String nextHolidayNameLocal = "";
        String nextHolidayNameEnglish = "";
        try {
            String countryCode = userCountry.getValue().substring(1,3);
            String country = URLEncoder.encode(countryCode, StandardCharsets.UTF_8);
            countryHolidays = "https://date.nager.at/api/v3/NextPublicHolidays/" + country;
            HttpRequest input = HttpRequest.newBuilder().uri(URI.create(countryHolidays)).build();
            HttpResponse<String> inputs = HTTP_CLIENT.send(input, BodyHandlers.ofString());

            HoliDate[] inputResults = GSON.fromJson(inputs.body(), HoliDate[].class);
            nextHolidayDate = inputResults[1].date;
            nextHolidayNameLocal = inputResults[1].localName;
            nextHolidayNameEnglish = inputResults[1].name;
            this.nextHoliday.setText("Next holiday (localname): " + nextHolidayNameLocal +
                                     "\nNext holiday (English): " + nextHolidayNameEnglish +
                                     "\nDate: " + nextHolidayDate);


            return nextHolidayDate;

        } catch (IOException e) {
            Platform.runLater(() -> err(countryHolidays, e.toString()));
        } catch (InterruptedException e) {
            Platform.runLater(() -> err(countryHolidays, e.toString()));
        }
        this.nextHoliday.setText("Next holiday (localname): " + nextHolidayNameLocal +
                                 "\nNext holiday (English): " + nextHolidayNameEnglish +
                                 "\nDate: " + nextHolidayDate);
        return nextHolidayDate;
    }

    public void  daysRemaining() {
         try {
             String holidayDate = getHoliday();
             String date = URLEncoder.encode(holidayDate, StandardCharsets.UTF_8);
             String digiDateURL = "https://digidates.de/api/v1/countdown/" + date;
             HttpRequest inp = HttpRequest.newBuilder().uri(URI.create(digiDateURL)).build();
             HttpResponse<String> inps = HTTP_CLIENT.send(inp, BodyHandlers.ofString());

             DigiDate inpResults = GSON.fromJson(inps.body(), DigiDate.class);
             this.days.setText("Days Left: " + inpResults.daysonly);
         } catch (IOException e) {
             Platform.runLater(() -> err(countryHolidays, e.toString()));
         } catch (InterruptedException e) {
             Platform.runLater(() -> err(countryHolidays, e.toString()));
         }
    }


    public class HoliDate {
        String date;
        String localName;
        String name;
    } // HoliDate

    public class DigiDate {
        int daysonly;


            int years;
            int months;
            int days;

    } // DigiDate

} // ApiApp
