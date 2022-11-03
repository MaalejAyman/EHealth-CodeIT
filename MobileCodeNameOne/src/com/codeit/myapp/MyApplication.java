package com.codeit.myapp;

import com.codename1.components.SpanLabel;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;
import com.codename1.ui.Toolbar;
import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.UIBuilder;
import com.codename1.ui.validation.GroupConstraint;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename
 * One</a> for the purpose of building native mobile applications using Java.
 */
public class MyApplication {

    private Form current;
    private Resources theme;
    private Form home;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if (err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });
    }

    public void start() {
        if (current != null) {
            current.show();
            return;
        }
        Form form = new Form();
        UIBuilder uibuilder = new UIBuilder();
        Container c = uibuilder.createContainer(theme, "login");
        Form f = (Form) uibuilder.findByName("login", c);
        TextField email = (TextField) uibuilder.findByName("email", c);
        TextField password = (TextField) uibuilder.findByName("password", c);
        Button btn_cnx = (Button) uibuilder.findByName("btn_cnx", c);
        Button frgt_pass = (Button) uibuilder.findByName("frgt_pass", c);

        btn_cnx.addActionListener(e -> {
            login();
        });
        Validator val = new Validator();
        val.setShowErrorMessageForFocusedComponent(true);
        val.addConstraint(email,
                new GroupConstraint(
                        new LengthConstraint(2),
                        new RegexConstraint("^[a-zA-Z0-9_+&*-]+(?:\\."
                                + "[a-zA-Z0-9_+&*-]+)*@"
                                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                                + "A-Z]{2,7}$", "Veuiller saisir un email valide"))).
                addSubmitButtons(btn_cnx);
        f.show();
        /*
        TextModeLayout tm = new TextModeLayout(4, 2);
        home = new Form("Home", new BorderLayout());
        Container content = new Container(tm);
        content.add(tm.createConstraint().horizontalSpan(2), new SpanLabel("This form adapts to OS differences in iOS and Android"));
        TextComponent name = new TextComponent().labelAndHint("Name");
        content.add(tm.createConstraint().horizontalSpan(2), name);
        TextComponent bio = new TextComponent().labelAndHint("Bio").multiline(true).rows(3);
        content.add(tm.createConstraint().horizontalSpan(2), bio);
        PickerComponent gender = PickerComponent.createStrings("Male", "Female", "Other", "Unspecified").label("Gender");
        content.add(gender);
        PickerComponent dateOfBirth = PickerComponent.createDate(null).label("Birthday");
        content.add(dateOfBirth);
        content.setScrollableY(true);
        Button submit = new Button("Submit");
        FontImage.setMaterialIcon(submit, FontImage.MATERIAL_DONE);
        submit.addActionListener(e -> {
            showOKForm(name.getField().getText());
        });
        home.add(CENTER, content);
        home.add(SOUTH, submit);
        
        Validator val = new Validator();
        val.setShowErrorMessageForFocusedComponent(true);
        val.addConstraint(name, 
                new GroupConstraint(
                        new LengthConstraint(2), 
                        new RegexConstraint("^([a-zA-Z ]*)$", "Please only use latin characters for name"))).
                addSubmitButtons(submit);

        home.show();*/

    }

    private void login() {
        Form f1 = new Form(new FlowLayout(CENTER, CENTER));
        f1.add(new Label("Acceuil"));
        Form f2 = new Form(new FlowLayout(CENTER, CENTER));
        f2.add(new Label("Profil"));
        Form f3 = new Form(new FlowLayout(CENTER, CENTER));
        f3.add(new Label("Questions"));
        Form f4 = new Form(new FlowLayout(CENTER, CENTER));
        f4.add(new Label("a propos de nous"));
        Form f5 = new Form(new FlowLayout(CENTER, CENTER));
        f5.add(new Label("Mes rendez-vous"));
        Form home = new Form("E-Health");
        Toolbar tb = home.getToolbar();
        tb.addMaterialCommandToSideMenu("Acceuil", FontImage.MATERIAL_WEB, (ActionListener) (ActionEvent evt) -> {
            f1.show();
        });
        tb.addMaterialCommandToSideMenu("Profil", FontImage.MATERIAL_SETTINGS, e -> {
            f2.show();
        });
        tb.addMaterialCommandToSideMenu("a propos de nous", FontImage.MATERIAL_INFO, e -> {
            f4.show();
        });
        tb.addMaterialCommandToSideMenu("Question", FontImage.MATERIAL_FORUM, (ActionListener) (ActionEvent evt) -> {
            f3.show();
        });
        tb.addMaterialCommandToSideMenu("Mes Rendez-Vous", FontImage.MATERIAL_LIST, (ActionListener) (ActionEvent evt) -> {
            f5.show();
        });
        f1.getToolbar().setBackCommand("", (ActionListener) (ActionEvent evt) -> {
            home.showBack();
        });
        f2.getToolbar().setBackCommand("", (ActionListener) (ActionEvent evt) -> {
            home.showBack();
        });
        f3.getToolbar().setBackCommand("", (ActionListener) (ActionEvent evt) -> {
            home.showBack();
        });
        f4.getToolbar().setBackCommand("", (ActionListener) (ActionEvent evt) -> {
            home.showBack();
        });
        f5.getToolbar().setBackCommand("", (ActionListener) (ActionEvent evt) -> {
            home.showBack();
        });
        home.add(new Label("Acceuil"));
        home.show();

    }

    private void showOKForm(String name) {
        Form f = new Form("Thanks", BoxLayout.y());
        f.add(new SpanLabel("Bonjour" + name + "Bienvenu Dans Votre Compte"));
        f.getToolbar().setBackCommand("", e -> home.showBack());
        f.show();
    }

    public void stop() {
        current = getCurrentForm();
        if (current instanceof Dialog) {
            ((Dialog) current).dispose();
            current = getCurrentForm();
        }
    }

    public void destroy() {
    }

}
