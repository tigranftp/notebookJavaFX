package com.example.notebook;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static com.example.notebook.CSVReaderSaver.readCSV;
import static com.example.notebook.CSVReaderSaver.saveCSV;

public class HelloController {


    private Map<String, CustomMenuItem> curMapOfMenuItems;
    private Map<String, CheckBox> curMapOfCheckBoxes;
    ArrayList<LinkClass> actualList;

    ObservableList<LinkClass> filteredList;
    @FXML
    private TableView<LinkClass> main_table;

    @FXML
    private Button add_button;


    @FXML
    private TextField linkTextField;
    @FXML
    private MenuButton menuButton;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField categoryTextField;


    @FXML
    private TableColumn link;
    @FXML
    private TableColumn description;
    @FXML
    private TableColumn category;
    @FXML
    private VBox mainVBOX;

    @FXML
    public void initialize() throws FileNotFoundException {
        ArrayList<LinkClass> initList = readCSV();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        actualList = new ArrayList<>(initList);
        filteredList = FXCollections.observableArrayList(actualList);
        curMapOfMenuItems = new HashMap<>();
        curMapOfCheckBoxes = new HashMap<>();
        AddElemsTOMenuButtonAfterInitList();
        main_table.setItems(filteredList);



        link.setCellValueFactory(new PropertyValueFactory<>("link"));

        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        category.setCellValueFactory(new PropertyValueFactory<>("category"));


        link.prefWidthProperty().bind(main_table.widthProperty().multiply(0.333));
        description.prefWidthProperty().bind(main_table.widthProperty().multiply(0.333));
        category.prefWidthProperty().bind(main_table.widthProperty().multiply(0.333));
        mainVBOX.setVgrow(main_table, Priority.ALWAYS);
    }

    @FXML
    protected void on_click_add_button() {
        ArrayList<String> missingFields = CheckMissingFields();

        if (!missingFields.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Missing some fields!!!");
            errorAlert.setContentText("Missing fields: " + missingFields.toString().replace("[", "").replace("]", ""));
            errorAlert.showAndWait();
            return;
        }

        LinkClass link = new LinkClass(linkTextField.getText(), descriptionTextField.getText(), categoryTextField.getText());
        if (!curMapOfMenuItems.containsKey(categoryTextField.getText())) {
            AddElemTOMenuButton(categoryTextField.getText());
        }
        actualList.add(link);
        UpdateMenu();
        // main_table.setItems(actualList);

        linkTextField.clear();
        descriptionTextField.clear();
        categoryTextField.clear();


    }

    protected ArrayList<String> CheckMissingFields() {
        ArrayList<String> missingFields = new ArrayList<>();
        if (linkTextField.getText().isEmpty()) {
            missingFields.add("Link");
        }
        if (descriptionTextField.getText().isEmpty()) {
            missingFields.add("Description");
        }
        if (categoryTextField.getText().isEmpty()) {
            missingFields.add("Category");
        }
        return missingFields;
    }

    protected void AddElemsTOMenuButtonAfterInitList() {
        actualList.forEach(lc -> {
            if (curMapOfMenuItems.containsKey(lc.getCategory())) {
                return;
            }
            AddElemTOMenuButton(lc.getCategory());
        });
    }


    protected void AddElemTOMenuButton(String cat) {
        CheckBox cb = new CheckBox(cat);
        cb.fire();
        cb.setOnAction(e -> {
            UpdateMenu();
        });
        CustomMenuItem item = new CustomMenuItem(cb);
        item.setHideOnClick(false);
        menuButton.getItems().addAll(item);
        curMapOfCheckBoxes.put(cat, cb);
        curMapOfMenuItems.put(cat, item);
    }

    protected void UpdateMenu() {
       ArrayList<LinkClass> newList = new ArrayList<>();
        actualList.forEach(lc -> {
            if (CheckCategory(lc.getCategory())){
                newList.add(lc);
            }
        });

        filteredList.setAll(newList);

    }

    protected Boolean CheckCategory(String cat) {
        AtomicBoolean res = new AtomicBoolean(false);
        curMapOfCheckBoxes.forEach((key, cb) -> {
            if (Objects.equals(cat, cb.getText())) {
                if (cb.isSelected()) {
                    res.set(true);
                }
            }
        });
        return res.get();
    }


    @FXML
    protected void on_click_delete_button() {
        LinkClass selectedItem = main_table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        actualList.remove(selectedItem);
        UpdateMenu();
        AtomicReference<Boolean> needToDelete = new AtomicReference<>(true);
        main_table.getItems().forEach(lc -> {
            if (Objects.equals(lc.getCategory(), selectedItem.getCategory())) {
                needToDelete.set(false);
            }
        });
        if (needToDelete.get()) {
            menuButton.getItems().remove(curMapOfMenuItems.get(selectedItem.getCategory()));
            curMapOfMenuItems.remove(selectedItem.getCategory());
            curMapOfCheckBoxes.remove(selectedItem.getCategory());
        }

    }

    @FXML
    protected void onClose() {
        saveCSV(actualList);
    }
}

