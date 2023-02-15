package de.paul.triebel.billmaker.gui;

import de.paul.triebel.billmaker.App;
import de.paul.triebel.billmaker.generator.BillElementData;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class BillElementPane extends TitledPane {

    private static final List<BillElementPane> panes = new ArrayList<>();
    private final GridPane grid;
    private final TextField nameField, descriptionField, priceField, countField;
    private int textFieldIndex = 0;

    public BillElementPane(EventHandler<BillElementPaneRemoveEvent> removeListener) {
        super();

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(10);

        setText("No Name");
        setContent(grid);

        nameField = addTextField("Name:");
        nameField.textProperty().addListener((__, ___, v) -> setText(v));
        descriptionField = addTextField("Description:");
        priceField = addTextField("Price:");
        countField = addTextField("Count:");

        Button addButton = new Button("-");
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addButton.setOnAction(e -> {
            removeListener.handle(new BillElementPaneRemoveEvent(this));
            panes.remove(this);
        });
        grid.add(addButton, 2, ++textFieldIndex);

        panes.add(this);
    }

    public static List<BillElementPane> getPanes() {
        return panes;
    }

    public BillElementData getDataObject() {
        boolean check = nameField.getText().trim().isEmpty();
        nameField.pseudoClassStateChanged(App.ERROR_CLASS, check);
        boolean checkPass = !check;

        check = descriptionField.getText().trim().isEmpty();
        descriptionField.pseudoClassStateChanged(App.ERROR_CLASS, check);
        checkPass &= !check;

        check = priceField.getText().trim().isEmpty();
        float priceValue = 0;
        if (!check) {
            try {
                priceValue = Float.parseFloat(priceField.getText().replace(',', '.'));
            } catch (NumberFormatException e) {
                check = true;
            }
        }
        priceField.pseudoClassStateChanged(App.ERROR_CLASS, check);
        checkPass &= !check;

        check = countField.getText().trim().isEmpty();
        int countValue = 0;
        if (!check) {
            try {
                countValue = Integer.parseInt(countField.getText());
            } catch (NumberFormatException e) {
                check = true;
            }
        }
        countField.pseudoClassStateChanged(App.ERROR_CLASS, check);
        checkPass &= !check;

        if (checkPass) {
            return new BillElementData(nameField.getText(), descriptionField.getText(), priceValue, countValue);
        }
        return null;
    }

    private TextField addTextField(String labelText) {
        addLabel(labelText);

        TextField textField = new TextField();
        grid.add(textField, 1, textFieldIndex);
        textFieldIndex++;

        return textField;
    }

    private void addLabel(String labelText) {
        Label label = new Label(labelText);
        label.setMinWidth(Region.USE_PREF_SIZE);
        grid.add(label, 0, textFieldIndex);
    }

    public static class BillElementPaneRemoveEvent extends Event {
        public BillElementPaneRemoveEvent(BillElementPane billElementPane) {
            super(EventType.ROOT);
            this.source = billElementPane;
        }

        @Override
        public BillElementPane getSource() {
            return (BillElementPane) super.getSource();
        }
    }
}
