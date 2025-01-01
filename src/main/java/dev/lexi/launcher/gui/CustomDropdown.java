package dev.lexi.launcher.gui;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.util.Duration;

public class CustomDropdown {

    private final String[] items;

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
                    -fx-padding: 5;
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

                                thumb.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                                    ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), thumb);
                                    scaleUp.setToX(1.2);
                                    scaleUp.setToY(1.2);
                                    scaleUp.play();
                                });

                                thumb.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                                    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), thumb);
                                    scaleDown.setToX(1);
                                    scaleDown.setToY(1);
                                    scaleDown.play();
                                });
                            }
                        }
                    });
                });

                popupList.setOpacity(0);
                popupList.setTranslateY(-10);

                dropdown.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
                    if (isNowShowing) {
                        Platform.runLater(() -> {
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), popupList);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);

                            TranslateTransition translateIn = new TranslateTransition(Duration.millis(200), popupList);
                            translateIn.setFromY(-10);
                            translateIn.setToY(0);

                            ParallelTransition openTransition = new ParallelTransition(fadeIn, translateIn);
                            openTransition.play();
                        });
                    } else {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), popupList);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);

                        TranslateTransition translateOut = new TranslateTransition(Duration.millis(200), popupList);
                        translateOut.setFromY(0);
                        translateOut.setToY(-10);

                        ParallelTransition closeTransition = new ParallelTransition(fadeOut, translateOut);
                        closeTransition.setOnFinished(e -> {
                            popupList.setTranslateY(-10);
                            popupList.setOpacity(0);
                        });
                        closeTransition.play();
                    }
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