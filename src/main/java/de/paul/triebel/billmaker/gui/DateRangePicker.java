package de.paul.triebel.billmaker.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class DateRangePicker {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObservableSet<LocalDate> selectedDates;
    private final DatePicker datePicker;

    public DateRangePicker() {
        this.selectedDates = FXCollections.observableSet(new TreeSet<>());
        this.datePicker = new DatePicker();
        datePicker.setEditable(false);
        initRangeSelectionMode();
    }

    private static Set<LocalDate> getTailEndDatesToRemove(Set<LocalDate> dates, LocalDate date) {

        TreeSet<LocalDate> tempTree = new TreeSet<>(dates);

        tempTree.add(date);

        int higher = tempTree.tailSet(date).size();
        int lower = tempTree.headSet(date).size();

        if (lower <= higher) {
            return tempTree.headSet(date);
        } else {
            return tempTree.tailSet(date);
        }

    }

    private static Set<LocalDate> getRangeGaps(LocalDate min, LocalDate max) {
        Set<LocalDate> rangeGaps = new LinkedHashSet<>();

        if (min == null || max == null) {
            return rangeGaps;
        }

        LocalDate lastDate = min.plusDays(1);
        while (lastDate.isAfter(min) && lastDate.isBefore(max)) {
            rangeGaps.add(lastDate);
            lastDate = lastDate.plusDays(1);

        }
        return rangeGaps;
    }

    private void initRangeSelectionMode() {
        this.datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                LocalDate start = getStartValue();
                LocalDate end = getEndValue();
                return ((start == null) ? "" : DATE_FORMAT.format(start)) + " - " + ((end == null) ? "" : DATE_FORMAT.format(end));
            }

            @Override
            public LocalDate fromString(String string) {
                return ((string == null) || string.isEmpty()) ? null : LocalDate.parse(string, DATE_FORMAT);
            }
        });

        EventHandler<MouseEvent> mouseClickedEventHandler = (MouseEvent clickEvent) -> {
            if (clickEvent.getButton() == MouseButton.PRIMARY) {
                if (!this.selectedDates.contains(this.datePicker.getValue())) {
                    this.selectedDates.add(datePicker.getValue());

                    this.selectedDates.addAll(getRangeGaps((LocalDate) this.selectedDates.toArray()[0], (LocalDate) this.selectedDates.toArray()[this.selectedDates.size() - 1]));
                } else {
                    this.selectedDates.remove(this.datePicker.getValue());
                    this.selectedDates.removeAll(getTailEndDatesToRemove(this.selectedDates, this.datePicker.getValue()));
                }

                this.datePicker.setValue(getStartValue());
            }
            this.datePicker.show();
            clickEvent.consume();
        };

        this.datePicker.setDayCellFactory((DatePicker param) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                } else {
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                }

                if (!selectedDates.isEmpty() && selectedDates.contains(item)) {
                    if (Objects.equals(item, selectedDates.toArray()[0]) || Objects.equals(item, selectedDates.toArray()[selectedDates.size() - 1])) {
                        setStyle("-fx-background-color: rgba(3, 169, 1, 0.7);");
                    } else {
                        setStyle("-fx-background-color: rgba(3, 169, 244, 0.7);");
                    }
                } else {
                    setStyle(null);
                }
            }
        });
    }

    public ObservableSet<LocalDate> getSelectedDates() {
        return this.selectedDates;
    }

    public DatePicker getDatePicker() {
        return this.datePicker;
    }

    public LocalDate getStartValue() {
        if (getSelectedDates().isEmpty()) {
            return null;
        }
        LocalDate[] arr = getSelectedDates().toArray(new LocalDate[0]);
        return arr[0];
    }

    public LocalDate getEndValue() {
        if (getSelectedDates().isEmpty()) {
            return null;
        }
        LocalDate[] arr = getSelectedDates().toArray(new LocalDate[0]);
        return arr[arr.length - 1];
    }
}
