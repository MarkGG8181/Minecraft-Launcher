package dev.lexi.launcher.gui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class CustomDropdown {

    private String[] items;

    public CustomDropdown(String[] items) {
        this.items = items;
    }

    public ComboBox<String> createDropdown() {
        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getItems().addAll(items);
        dropdown.setStyle("""
            -fx-background-color: #1e1e1e;
            -fx-background-radius: 20;
            -fx-border-color: transparent;
            -fx-padding: 5;
            -fx-text-fill: white;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 3);
        """);

        dropdown.setCellFactory(createCustomCellFactory());
        dropdown.setButtonCell(createCustomCellFactory().call(null));

        dropdown.skinProperty().addListener((observable, oldSkin, newSkin) -> {
            if (newSkin instanceof ComboBoxListViewSkin<?>) {
                ListView<?> popupList = (ListView<?>) ((ComboBoxListViewSkin<?>) newSkin).getPopupContent();
                popupList.setStyle("""
                    -fx-control-inner-background: #1e1e1e;
                    -fx-accent: #3c3c3c;
                    -fx-text-fill: white;
                    -fx-background-radius: 15;
                    -fx-padding: 5; /* Ensure inner padding fits within the radius */
                """);

                popupList.setCellFactory((Callback) createCustomCellFactory());
                popupList.setPrefHeight(Math.min(items.length * 30, 150));

                popupList.skinProperty().addListener((obs, oldPopupSkin, newPopupSkin) -> {
                    popupList.lookupAll(".scroll-bar").forEach(scrollBar -> {
                        if (scrollBar instanceof ScrollBar bar) {
                            bar.setStyle("""
                                -fx-background-color: #1e1e1e;
                                -fx-background-radius: 15;
                                -fx-padding: 0;
                            """);
                            bar.setPrefWidth(14);
                            bar.setTranslateX(-4);

                            Region thumb = (Region) bar.lookup(".thumb");
                            if (thumb != null) {
                                thumb.setStyle("""
                                    -fx-background-color: #3c3c3c;
                                    -fx-background-radius: 15;
                                """);
                            }

                            Region incrementButton = (Region) bar.lookup(".increment-button");
                            Region decrementButton = (Region) bar.lookup(".decrement-button");
                            if (incrementButton != null) {
                                incrementButton.setStyle("""
                                    -fx-background-color: transparent;
                                    -fx-padding: 5;
                                    -fx-background-radius: 15;
                                """);
                                incrementButton.setManaged(true);
                                incrementButton.setVisible(true);
                                incrementButton.setTranslateY(-2);
                            }
                            if (decrementButton != null) {
                                decrementButton.setStyle("""
                                    -fx-background-color: transparent;
                                    -fx-padding: 5;
                                    -fx-background-radius: 15;
                                """);
                                decrementButton.setManaged(true);
                                decrementButton.setVisible(true);
                                decrementButton.setTranslateY(2);
                            }
                        }
                    });
                });
            }
        });

        return dropdown;
    }

    private Callback<ListView<String>, ListCell<String>> createCustomCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                            setStyle("""
                                -fx-background-color: #1e1e1e;
                                -fx-text-fill: white;
                                -fx-background-radius: 15;
                                -fx-padding: 5;
                            """);
                        }
                    }
                };
            }
        };
    }
}
