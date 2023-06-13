package com.irostub.standard.bot.restaurant;

import gui.ava.html.image.generator.HtmlImageGenerator;
import gui.ava.html.image.util.SynchronousHTMLEditorKit;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HtmlToImage extends HtmlImageGenerator {
    @Override
    protected JEditorPane createJEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setSize(this.getDefaultSize());
        editorPane.setEditable(false);
        SynchronousHTMLEditorKit kit = new SynchronousHTMLEditorKit();
        editorPane.setEditorKitForContentType("text/html", kit);
        editorPane.setContentType("text/html");
        editorPane.setFont(new Font("NanumSquare", Font.PLAIN, 16));
        editorPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("page")) {
                    HtmlToImage.this.onDocumentLoad();
                }

            }
        });
        return editorPane;
    }
}
