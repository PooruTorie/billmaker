package de.paul.triebel.billmaker;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import de.paul.triebel.billmaker.assets.Assets;
import de.paul.triebel.billmaker.generator.BillData;
import de.paul.triebel.billmaker.generator.BillElementData;
import de.paul.triebel.billmaker.generator.BillGenerator;
import de.paul.triebel.billmaker.gui.BillElementPane;
import de.paul.triebel.billmaker.gui.DateRangePicker;
import de.paul.triebel.billmaker.gui.ErrorAlert;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class App extends Application {

    public static final PseudoClass ERROR_CLASS = PseudoClass.getPseudoClass("error");
    private int textFieldIndex = 1;
    private GridPane grid;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bill Maker");
        primaryStage.getIcons().add(new Image(Assets.getFile("icon.png")));

        GridPane masterGrid = new GridPane();
        masterGrid.setAlignment(Pos.CENTER);
        masterGrid.setHgap(50);
        masterGrid.setPadding(new Insets(25, 25, 25, 25));

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(10);

        Text sceneTitle = new Text("Bill Generator");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        grid.add(sceneTitle, 0, 0, 2, 1);

        CheckBox isInvoice = addCheckbox("Invoice:");
        TextField nameField = addTextField("Name:");
        TextField customerNumberField = addTextField("Customer Number:");
        TextField billNumberField = addTextField("Bill Number:");
        TextField taxNumberField = addTextField("Tax Number:");
        TextField streetField = addTextField("Street:");
        TextField cityField = addTextField("City:");
        TextField plzField = addTextField("PLZ:");
        TextField countryField = addTextField("Country:");
        DatePicker dateField = addDateField("Date:");
        dateField.setValue(LocalDate.now());
        DateRangePicker rangeDateField = addDateRangeField("Performance Period:");
        DatePicker endBillDateField = addDateField("End Bill Date:");

        Button createButton = new Button("Create");
        createButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(createButton, 0, ++textFieldIndex, 2, 1);

        createButton.setOnAction(e -> {
            boolean check = nameField.getText().trim().isEmpty();
            nameField.pseudoClassStateChanged(ERROR_CLASS, check);
            boolean checkPass = !check;

            check = customerNumberField.getText().trim().isEmpty();
            customerNumberField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = billNumberField.getText().trim().isEmpty();
            billNumberField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = taxNumberField.getText().trim().isEmpty();
            taxNumberField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = streetField.getText().trim().isEmpty();
            streetField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = cityField.getText().trim().isEmpty();
            cityField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = countryField.getText().trim().isEmpty();
            countryField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = plzField.getText().trim().isEmpty();
            plzField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = dateField.getValue() == null;
            dateField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = rangeDateField.getSelectedDates().isEmpty();
            rangeDateField.getDatePicker().pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            check = endBillDateField.getValue() == null;
            endBillDateField.pseudoClassStateChanged(ERROR_CLASS, check);
            checkPass &= !check;

            String templateFile = isInvoice.isSelected() ? "quittung.docx" : "rechnung.docx";

            BillData data = new BillData();
            data.setName(nameField.getText());
            data.setCustomerNumber(customerNumberField.getText());
            data.setBillNumber(billNumberField.getText());
            data.setTaxNumber(taxNumberField.getText());
            data.setStreet(streetField.getText());
            data.setCity(cityField.getText());
            data.setPlz(plzField.getText());
            data.setCountry(countryField.getText());
            data.setDate(dateField.getValue());
            data.setStart(rangeDateField.getStartValue());
            data.setEnd(rangeDateField.getEndValue());
            data.setBillEnd(endBillDateField.getValue());

            for (BillElementPane pane : BillElementPane.getPanes()) {
                BillElementData paneData = pane.getDataObject();
                if (paneData != null) {
                    data.getBillElements().add(paneData);
                } else {
                    checkPass = false;
                }
            }

            if (checkPass) {
                FileChooser fileChooser = new FileChooser();

                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files", "*.pdf");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showSaveDialog(primaryStage);

                if (file != null) {
                    try {
                        Document doc = BillGenerator.generate(data, templateFile);
                        doc.save(file.getAbsolutePath(), SaveFormat.PDF);
                        Desktop.getDesktop().open(file);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorAlert alert = new ErrorAlert("Error on Bill generation", ex);
                        alert.show();
                    }
                }
            }
        });

        GridPane scrollContent = new GridPane();
        scrollContent.setAlignment(Pos.CENTER);

        Text elementsTitle = new Text("Bill Elements");
        elementsTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        scrollContent.add(elementsTitle, 0, 0);

        Accordion elementsAccordion = new Accordion();
        scrollContent.add(elementsAccordion, 0, 1);

        Button addButton = new Button("+");
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addButton.setOnAction(e -> {
            BillElementPane newBill = new BillElementPane(billElement -> {
                elementsAccordion.getPanes().remove(billElement.getSource());
                int size = elementsAccordion.getPanes().size();
                if (size > 0) {
                    elementsAccordion.setExpandedPane(elementsAccordion.getPanes().get(size - 1));
                }
            });
            elementsAccordion.getPanes().add(newBill);
            elementsAccordion.setExpandedPane(newBill);
        });
        scrollContent.add(addButton, 0, 2);

        ScrollPane elements = new ScrollPane();
        elements.setFitToWidth(true);
        elements.setMinViewportWidth(200);
        elements.setContent(scrollContent);

        masterGrid.add(grid, 0, 0);
        masterGrid.add(elements, 1, 0);

        Scene scene = new Scene(masterGrid);
        scene.getStylesheets().add(Assets.getResourceExternal("style.css"));
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();

        primaryStage.show();
    }

    private DateRangePicker addDateRangeField(String labelText) {
        addLabel(labelText);

        DateRangePicker dateRangeField = new DateRangePicker();
        grid.add(dateRangeField.getDatePicker(), 1, textFieldIndex);
        textFieldIndex++;

        return dateRangeField;
    }

    private TextField addTextField(String labelText) {
        addLabel(labelText);

        TextField textField = new TextField();
        grid.add(textField, 1, textFieldIndex);
        textFieldIndex++;

        return textField;
    }

    private CheckBox addCheckbox(String labelText) {
        addLabel(labelText);

        CheckBox checkbox = new CheckBox();
        grid.add(checkbox, 1, textFieldIndex);
        textFieldIndex++;

        return checkbox;
    }

    private void addLabel(String labelText) {
        Label label = new Label(labelText);
        label.setMinWidth(Region.USE_PREF_SIZE);
        grid.add(label, 0, textFieldIndex);
    }

    private DatePicker addDateField(String labelText) {
        addLabel(labelText);

        DatePicker dateField = new DatePicker();
        grid.add(dateField, 1, textFieldIndex);
        textFieldIndex++;

        return dateField;
    }
}
