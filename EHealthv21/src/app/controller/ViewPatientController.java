/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Laboratoire;
import app.entity.Medecin;
import app.entity.Patient;
import app.entity.Specialite;
import app.entity.Service;

import app.entity.User;
import app.mail.Mail;
import static app.mail.Mail.sendMail;
import app.service.LaboratoireService;
import app.service.MedecinService;
import app.service.ServicePatient;
import app.service.ServiceRendezVous;
import app.service.ServiceUser;
import app.service.ServiceService;
import app.service.ServiceSpecialite;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.mail.MessagingException;
import javax.print.ServiceUI;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewPatientController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabListePatient;
    @FXML
    private Tab tabListeLaboratoire;
    @FXML
    private TableView<Patient> table_patient;
    @FXML
    private TableColumn<Patient, String> output_email;
    @FXML
    private TableColumn<Patient, String> output_nom;
    @FXML
    private TableColumn<Patient, String> output_prenom;
    @FXML
    private TableColumn<Patient, String> output_sexe;
    @FXML
    private TableColumn<Patient, Integer> output_cin;
    @FXML
    private TableColumn<Patient, String> output_adresse;
    @FXML
    private TableColumn<Patient, String> output_ville;
    @FXML
    private TableColumn<Patient, Integer> output_tel;
    @FXML
    private TableColumn<Patient, String> output_bloque;
    @FXML
    private TextField modif_email;
    @FXML
    private TextField modif_nom;
    @FXML
    private TextField modif_prenom;
    @FXML
    private TextField modif_cin;
    @FXML
    private TextField modif_adresse;
    @FXML
    private ChoiceBox<String> modif_ville;
    @FXML
    private TextField modif_tel;
    @FXML
    private ChoiceBox<String> modif_sexe;
    @FXML
    private Label name_user;
    private int id = -1;
    @FXML
    private TableView<Medecin> table_medecin;
    @FXML
    private TableColumn<Medecin, String> output_emailMed;
    @FXML
    private TableColumn<Medecin, String> output_nomMed;
    @FXML
    private TableColumn<Medecin, String> output_prenomMed;
    @FXML
    private TableColumn<Medecin, String> output_sexeMed;
    @FXML
    private TableColumn<Medecin, Integer> output_numSMed;
    @FXML
    private TableColumn<Medecin, String> output_adresseMed;
    @FXML
    private TableColumn<Medecin, String> output_villeMed;
    @FXML
    private TableColumn<Medecin, Integer> output_telMed;
    @FXML
    private TableColumn<Medecin, String> output_specialiteMed;
    @FXML
    private TextField modif_emailMed;
    @FXML
    private TextField modif_nomMed;
    @FXML
    private TextField modif_prenomMed;
    @FXML
    private TextField modif_adresseMed;
    @FXML
    private TextField modif_telMed;
    @FXML
    private BarChart<Integer, String> barChart;
    @FXML
    private ChoiceBox<String> modif_sexeMed;
    @FXML
    private Label name_Med;
    @FXML
    private ChoiceBox<String> modif_villeMed;
    @FXML
    private TableView<Laboratoire> table_laboratoire;
    @FXML
    private TableColumn<Laboratoire, String> output_emailLab;
    @FXML
    private TableColumn<Laboratoire, String> output_nomLab;
    @FXML
    private TableColumn<Laboratoire, Integer> output_numServiceLab;
    @FXML
    private TableColumn<Laboratoire, String> output_adresseLab;
    @FXML
    private TableColumn<Laboratoire, String> output_villeLab;
    @FXML
    private TableColumn<Laboratoire, Integer> output_telLab;
    @FXML
    private TextField modif_emailLab;
    @FXML
    private TextField modif_nomLab;
    @FXML
    private TextField modif_NumServiceLab;
    @FXML
    private TextField modif_adresseLab;
    @FXML
    private TextField modif_telLab;
    @FXML
    private ListView<Service> modif_Service;
    @FXML
    private Label name_Lab;
    @FXML
    private TextField modif_HoraireOLab;
    @FXML
    private TextField modif_HoraireFLab;
    @FXML
    private ChoiceBox<String> modif_villeLab;
    @FXML
    private TextField modif_numServiceMed;
    @FXML
    private TextField modif_HoraireOMed;
    @FXML
    private TextField modif_HoraireFMed;
    @FXML
    private Tab tablisteMedecin;
    @FXML
    private PieChart pieChart;
    @FXML
    private Button btn_logout;
    @FXML
    private NumberAxis RdvAxis;
    @FXML
    private CategoryAxis patientAxis;
    @FXML
    private Button btn_modif;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_block;
    @FXML
    private Button btn_clear;
    @FXML
    private Button btn_modifMed;
    @FXML
    private Button btn_deleteMed;
    @FXML
    private Button btn_clearMed;
    @FXML
    private Button btn_modifLab;
    @FXML
    private Button btn_delete2;
    @FXML
    private Button btn_clearLab;

    /**
     * Initializes the controller class.
     *///
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            ServiceRendezVous srv = new ServiceRendezVous();
            ServiceUser su = new ServiceUser();
            barChart.getData().addAll(srv.loadChart());
            List<PieChart.Data> listP = su.pieChartStat();
            for (PieChart.Data d : listP) {
                pieChart.getData().add(d);

            }

            List<String> listVille = new ArrayList<String>();
            listVille.add("Ariana");
            listVille.add("Tunis");
            listVille.add("Ben Arous");
            listVille.add("Manouba");
            listVille.add("Nabeul");
            listVille.add("Zaghouan");
            listVille.add("Bizerte");
            listVille.add("Béja");
            listVille.add("Jendouba");
            listVille.add("Kef");
            listVille.add("Siliana");
            listVille.add("Sousse");
            listVille.add("Monastir");
            listVille.add("Mahdia");
            listVille.add("Sfax");
            listVille.add("Kairouan");
            listVille.add("Kasserine");
            listVille.add("Sidi Bouzid");
            listVille.add("Kasserine");
            listVille.add("Gabès");
            listVille.add("Mednine");
            listVille.add("Tataouine");
            listVille.add("Gafsa");
            listVille.add("Tozeur");
            listVille.add("Kebili");
            List<String> list = new ArrayList<String>();
            list.add("Homme");
            list.add("Femme");
            ObservableList<String> sexe = FXCollections.observableArrayList(list);
            ObservableList<String> listVilles = FXCollections.observableArrayList(listVille);

            modif_ville.setItems(listVilles);

            modif_villeMed.setItems(listVilles);
            modif_villeLab.setItems(listVilles);

            ServiceService ss = new ServiceService();
            List<Service> listService = ss.getServices();
            ObservableList<Service> listServices = FXCollections.observableArrayList(listService);

            modif_Service.setItems(listServices);
            modif_Service.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            ServiceSpecialite ssp = new ServiceSpecialite();
            List<Specialite> listSpecialite = ssp.getSpecialites();
            ObservableList<Specialite> listSpecialites = FXCollections.observableArrayList(listSpecialite);

            modif_sexe.setItems(sexe);
            modif_sexeMed.setItems(sexe);

            output_nomLab.setCellValueFactory(new PropertyValueFactory<>("nom"));
            output_adresseLab.setCellValueFactory(new Callback<CellDataFeatures<Laboratoire, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Laboratoire, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getAdresse());
                }

            });
            output_numServiceLab.setCellValueFactory(new PropertyValueFactory<>("numService"));
            output_emailLab.setCellValueFactory(new Callback<CellDataFeatures<Laboratoire, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Laboratoire, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getEmail());
                }

            });
            output_telLab.setCellValueFactory(new Callback<CellDataFeatures<Laboratoire, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(CellDataFeatures<Laboratoire, Integer> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getTel());
                }

            });
            output_villeLab.setCellValueFactory(new Callback<CellDataFeatures<Laboratoire, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Laboratoire, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getVille());
                }

            });

            output_nomMed.setCellValueFactory(new PropertyValueFactory<>("nom"));
            output_prenomMed.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            output_sexeMed.setCellValueFactory(new Callback<CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Medecin, String> p) {
                    if (p.getValue().getSexe().equals("0")) {
                        return new ReadOnlyObjectWrapper("Homme");
                    } else {
                        return new ReadOnlyObjectWrapper("Femme");
                    }
                }
            });
            output_numSMed.setCellValueFactory(new PropertyValueFactory<>("numService"));
            output_adresseMed.setCellValueFactory(new Callback<CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Medecin, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getAdresse());
                }

            });
            output_specialiteMed.setCellValueFactory(new Callback<CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Medecin, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getSpecialite().getNomSpec());
                }

            });
            output_emailMed.setCellValueFactory(new Callback<CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Medecin, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getEmail());
                }

            });
            output_telMed.setCellValueFactory(new Callback<CellDataFeatures<Medecin, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(CellDataFeatures<Medecin, Integer> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getTel());
                }

            });
            output_villeMed.setCellValueFactory(new Callback<CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Medecin, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getVille());
                }

            });

            output_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            output_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            output_sexe.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
                    if (p.getValue().getSexe().equals("0")) {
                        return new ReadOnlyObjectWrapper("Homme");
                    } else {
                        return new ReadOnlyObjectWrapper("Femme");
                    }
                }
            });
            output_bloque.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Patient, String> p) {
                    if (p.getValue().getBlocked()) {
                        return new ReadOnlyObjectWrapper("Oui");
                    } else {
                        return new ReadOnlyObjectWrapper(" ");
                    }
                }
            });

            output_cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
            output_adresse.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getAdresse());
                }

            });
            output_email.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getEmail());
                }

            });
            output_tel.setCellValueFactory(new Callback<CellDataFeatures<Patient, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(CellDataFeatures<Patient, Integer> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getTel());
                }

            });
            output_ville.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getVille());
                }

            });

            table_patient.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Patient> obs, Patient oldVal, Patient newVal) -> {
                if (newVal != null) {
                    name_user.setText(newVal.getPrenom() + " " + newVal.getNom());
                    modif_adresse.setText(newVal.getUser().getAdresse());
                    modif_ville.setValue(newVal.getUser().getVille());
                    modif_cin.setText(newVal.getCin() + "");
                    modif_email.setText(newVal.getUser().getEmail());
                    modif_nom.setText(newVal.getNom());
                    modif_prenom.setText(newVal.getPrenom());
                    modif_tel.setText(newVal.getUser().getTel() + "");
                    if (newVal.getSexe().equals("0")) {
                        modif_sexe.setValue("Homme");
                    } else {
                        modif_sexe.setValue("Femme");
                    }
                    id = newVal.getUser().getId();
                }
            }
            );

            table_medecin.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Medecin> obs, Medecin oldVal, Medecin newVal) -> {
                if (newVal != null) {
                    name_Med.setText(newVal.getPrenom() + " " + newVal.getNom());
                    modif_adresseMed.setText(newVal.getUser().getAdresse());
                    modif_villeMed.setValue(newVal.getUser().getVille());
                    modif_numServiceMed.setText(newVal.getNumService() + "");
                    DateFormat format = new SimpleDateFormat("h:mm", Locale.ENGLISH);
                    //aymen
                    modif_HoraireOMed.setText(newVal.getHoraireDebut().toString());
                    modif_HoraireFMed.setText(newVal.getHoraireFin().toString());
                    modif_emailMed.setText(newVal.getUser().getEmail());
                    modif_nomMed.setText(newVal.getNom());
                    modif_prenomMed.setText(newVal.getPrenom());
                    modif_telMed.setText(newVal.getUser().getTel() + "");
                    if (newVal.getSexe().equals("0")) {
                        modif_sexeMed.setValue("Homme");
                    } else {
                        modif_sexeMed.setValue("Femme");
                    }
                    id = newVal.getUser().getId();
                }
            }
            );

            table_laboratoire.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Laboratoire> obs, Laboratoire oldVal, Laboratoire newVal) -> {
                if (newVal != null) {
                    name_Lab.setText(" " + newVal.getNom());
                    modif_adresseLab.setText(newVal.getUser().getAdresse());
                    modif_villeLab.setValue(newVal.getUser().getVille());
                    modif_NumServiceLab.setText(newVal.getNumService() + "");
                    DateFormat format = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
                    //aymen
                    modif_HoraireOLab.setText(newVal.getHoraireDebut().toString());
                    modif_HoraireFLab.setText(newVal.getHoraireFin().toString());
                    modif_emailLab.setText(newVal.getUser().getEmail());
                    modif_nomLab.setText(newVal.getNom());
                    modif_telLab.setText(newVal.getUser().getTel() + "");
                    id = newVal.getUser().getId();
                }
            }
            );
            AffichageListePatients();
            AffichageListeMedecins();
            AffichageListeLaboratoires();
        } catch (SQLException ex) {
            Logger.getLogger(ViewPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// controle saisi

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public boolean isValidCin(String cin) {
        String emailRegex = "[0-9]{8}";

        Pattern pat = Pattern.compile(emailRegex);
        if (cin == null) {
            return false;
        }
        return pat.matcher(cin).matches();
    }

    public boolean isBlank(String cin) {
        boolean test = true;
        if (cin == null) {
            test = false;
        }
        return test;
    }

    public boolean isValidnumService(String cin) {
        String emailRegex = "[0-9]{5}";

        Pattern pat = Pattern.compile(emailRegex);
        if (cin == null) {
            return false;
        }
        return pat.matcher(cin).matches();
    }
//fin controle saisi
    //fontion dajout

    public void AffichageListePatients() throws SQLException {

        ServicePatient sp = new ServicePatient();
        List<Patient> lp = sp.afficherListePatients();
        ObservableList<Patient> ob = FXCollections.observableArrayList(lp);
        table_patient.setItems(ob);
    }

    public void AffichageListeMedecins() throws SQLException {

        MedecinService sm = new MedecinService();
        List<Medecin> lp = sm.getMedecins();
        ObservableList<Medecin> ob = FXCollections.observableArrayList(lp);
        table_medecin.setItems(ob);
    }

    public void AffichageListeLaboratoires() throws SQLException {

        LaboratoireService sm = new LaboratoireService();
        List<Laboratoire> lp = sm.getLaboratoires();
        ObservableList<Laboratoire> ob = FXCollections.observableArrayList(lp);
        table_laboratoire.setItems(ob);
    }

    @FXML
    public void clear() {
        modif_adresse.clear();
        name_user.setText("");
        modif_cin.clear();
        modif_ville.setValue("");
        modif_email.clear();
        modif_nom.clear();
        modif_prenom.clear();
        modif_tel.clear();

    }

    @FXML
    public void clearMed() {
        modif_adresseMed.clear();
        name_Med.setText("");
        modif_numServiceMed.clear();
        modif_villeMed.setValue("");
        modif_sexeMed.setValue("");
        modif_emailMed.clear();
        modif_nomMed.clear();
        modif_prenomMed.clear();
        modif_telMed.clear();
        modif_HoraireFMed.clear();
        modif_HoraireOMed.clear();

    }

    @FXML
    public void clearLab() {
        modif_adresseLab.clear();
        name_Lab.setText("");
        modif_NumServiceLab.clear();
        modif_villeLab.setValue("");
        modif_emailLab.clear();
        modif_nomLab.clear();
        modif_telLab.clear();
        modif_HoraireFLab.clear();
        modif_HoraireOLab.clear();

    }

    @FXML
    private void DeletePatientC(ActionEvent eve) throws SQLException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Supprimer ce compte ");
        alert.setHeaderText("Voulez vous vraiment supprimer cet utilisateur ?");
        alert.setContentText("");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (id != -1) {
                clear();
                ServicePatient sp = new ServicePatient();
                sp.DeletePatient(id);
                id = -1;
            }
            AffichageListePatients();
        }
    }

    @FXML
    private void DeleteMedecinC(ActionEvent eve) throws SQLException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Supprimer ce compte ");
        alert.setHeaderText("Voulez vous vraiment supprimer cet utilisateur ?");
        alert.setContentText("");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (id != -1) {
                clearMed();
                MedecinService sp = new MedecinService();
                sp.DeleteMedecin(id);
                id = -1;
            }
            AffichageListeMedecins();
        }

    }

    @FXML
    private void DeleteLabC(ActionEvent eve) throws SQLException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Supprimer ce compte ");
        alert.setHeaderText("Voulez vous vraiment supprimer cet utilisateur ?");
        alert.setContentText("");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (id != -1) {
                clearLab();
                LaboratoireService sp = new LaboratoireService();
                sp.DeleteLaboratoire(id);
                id = -1;
            }
            AffichageListeLaboratoires();
        }
    }

    @FXML

    public void UpdatePatient() throws SQLException {
        boolean test = true;
        if (!isValidEmail(modif_email.getText())) {
            name_user.setText("verifier votre mail");
            test = false;
        }
        if (!isValidCin(modif_cin.getText())) {
            name_user.setText("verifier votre CIN");
            test = false;
        }
        if (!isValidCin(modif_tel.getText())) {
            name_user.setText("verifier votre numero de telephone");
            test = false;
        }
        if (test && (isBlank(modif_email.getText()))
                && (isBlank(modif_nom.getText())) && (isBlank(modif_prenom.getText()))
                && (isBlank(modif_adresse.getText()))
                && (isBlank(modif_ville.getSelectionModel().getSelectedItem()))
                && (isBlank(modif_sexe.getSelectionModel().getSelectedItem()))
                && (isBlank(modif_tel.getText()))
                && (isBlank(modif_cin.getText()))) {
            String email = modif_email.getText();
            String nom = modif_nom.getText();
            String prenom = modif_prenom.getText();
            String adresse = modif_adresse.getText();
            String ville = modif_ville.getSelectionModel().getSelectedItem();
            int tel = Integer.parseInt(modif_tel.getText());
            int cin = Integer.parseInt(modif_cin.getText());
            String sexeP = modif_sexe.getSelectionModel().getSelectedItem();
            User u = new User(id, email, adresse, ville, tel);
            if (sexeP == "Homme") {
                sexeP = "0";
            } else {
                sexeP = "1";
            }
            Patient p = new Patient(id, u, nom, prenom, cin, sexeP);
            ServicePatient sp = new ServicePatient();
            sp.updatePatient(p);
            AffichageListePatients();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification de profil");
            alert.setHeaderText("Vos information etait modifié avec succée");
            alert.showAndWait();
        } else {
            name_user.setText("verifier les champs");
        }

    }

    @FXML

    public void UpdateMedecin() throws SQLException {
        try {
            boolean test = true;
            if (!isValidEmail(modif_emailMed.getText())) {
                test = false;
            }
            if (!isValidnumService(modif_numServiceMed.getText())) {
                test = false;
            }
            if (!isValidCin(modif_telMed.getText())) {
                test = false;
            }
            if (test && (isBlank(modif_emailMed.getText()))
                    && (isBlank(modif_nomMed.getText())) && (isBlank(modif_prenomMed.getText()))
                    && (isBlank(modif_adresseMed.getText()))
                    && (isBlank(modif_villeMed.getSelectionModel().getSelectedItem()))
                    && (isBlank(modif_sexeMed.getSelectionModel().getSelectedItem()))
                    && (isBlank(modif_telMed.getText()))
                    && (isBlank(modif_numServiceMed.getText()))) {
                String email = modif_emailMed.getText();
                String nom = modif_nomMed.getText();
                String prenom = modif_prenomMed.getText();
                String adresse = modif_adresseMed.getText();
                String ville = modif_villeMed.getSelectionModel().getSelectedItem();
                DateFormat format = new SimpleDateFormat("h:mm", Locale.ENGLISH);
                Date horaireDebut = format.parse(modif_HoraireOMed.getText());
                Date horaireFin = format.parse(modif_HoraireFMed.getText());
                int tel = Integer.parseInt(modif_telMed.getText());
                int numService = Integer.parseInt(modif_numServiceMed.getText());
                String sexeP = modif_sexeMed.getSelectionModel().getSelectedItem();
                User u = new User(id, email, adresse, ville, tel);
                if (sexeP == "Homme") {
                    sexeP = "0";
                } else {
                    sexeP = "1";
                }
                Medecin m = new Medecin(id, u, nom, prenom, sexeP, horaireDebut, horaireFin, numService);
                MedecinService sp = new MedecinService();
                sp.updateMedecin(m);
                AffichageListeMedecins();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Modification de profil");
                alert.setHeaderText("Vos information etait modifié avec succée");
                alert.showAndWait();
            } else {
                name_Med.setText("Verifier les champs");
            }

        } catch (ParseException ex) {
            Logger.getLogger(ViewPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML

    public void UpdateLaboratoire() throws SQLException {
        try {
            boolean test = true;

            if (!isValidEmail(modif_emailLab.getText())) {
                name_Lab.setText("verifier votre mail");
                test = false;
            }
            if (!isValidnumService(modif_NumServiceLab.getText())) {
                name_Lab.setText("verifier votre numero  de service");
                test = false;
            }
            if (!isValidCin(modif_telLab.getText())) {
                name_Lab.setText("verifier votre numero de telephone");
                test = false;
            }
            if (test
                    && (isBlank(modif_nomLab.getText()))
                    && (isBlank(modif_adresseLab.getText()))
                    && (isBlank(modif_villeLab.getSelectionModel().getSelectedItem()))
                    && (isBlank(modif_HoraireOLab.getText()))
                    && (isBlank(modif_HoraireFLab.getText()))) {
                String email = modif_emailLab.getText();
                String nom = modif_nomLab.getText();
                String adresse = modif_adresseLab.getText();
                String ville = modif_villeLab.getSelectionModel().getSelectedItem();
                DateFormat format = new SimpleDateFormat("h:mm", Locale.ENGLISH);
                Date horaireDebut = format.parse(modif_HoraireOLab.getText());
                Date horaireFin = format.parse(modif_HoraireFLab.getText());
                ObservableList selectedIndicesLab = modif_Service.getSelectionModel().getSelectedIndices();
                ServiceService ss = new ServiceService();
                List<Service> listService = ss.getServices();
                ObservableList<Service> listServices = FXCollections.observableArrayList(listService);
                int tel = Integer.parseInt(modif_telLab.getText());
                int numService = Integer.parseInt(modif_NumServiceLab.getText());
                User u = new User(id, email, adresse, ville, tel);
                Laboratoire l = new Laboratoire(id, u, numService, nom, horaireDebut, horaireFin);
                LaboratoireService sp = new LaboratoireService();
                Set<Service> h = new HashSet<Service>();
                for (Object i : selectedIndicesLab) {
                    h.add(listServices.get((int) i));
                }
                l.setServices(h);
                sp.updateLaboratoire(l);
                AffichageListeLaboratoires();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Modification de profil");
                alert.setHeaderText("Vos information etait modifié avec succée");
                alert.showAndWait();
            } else {
                name_Lab.setText("Vérifier les champs");
            }
        } catch (ParseException ex) {
            Logger.getLogger(ViewPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML

    private void refresh() throws SQLException {
        AffichageListeLaboratoires();
        AffichageListeMedecins();
        AffichageListePatients();
    }

    @FXML

    private void logout(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewLogin.fxml"));
        Scene scene = new Scene(parent3);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML

    private void RedirectToAjoutPatient(ActionEvent event) throws IOException {
        Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewAjoutPatient.fxml"));
        Scene scene = new Scene(parent3);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML

    private void RedirectToAjoutMedecin(ActionEvent event) throws IOException {
        Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewAjoutMedecin.fxml"));
        Scene scene = new Scene(parent3);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML

    private void RedirectToAjoutLab(ActionEvent event) throws IOException {
        Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewAjoutLab.fxml"));
        Scene scene = new Scene(parent3);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
//---------------------------Anas Partie Admin-----------------------------------------------------------------------

    @FXML
    private void bloquerDebloquerPatient(ActionEvent event) throws SQLException, MessagingException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Blocage/Déblocage ce compte ");
        alert.setHeaderText("Voulez vous vraiment bloquer/débloquer cet utilisateur ?");
        alert.setContentText("");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (id != -1) {
                Patient pat = table_patient.getSelectionModel().getSelectedItem();
                ServicePatient sp = new ServicePatient();
                ServiceUser su = new ServiceUser();
                User u = su.getUserById(pat.getId());
                if (pat.getBlocked()) {
                    sp.deblockPatient(pat.getId());
                    AffichageListePatients();
                } else {
                    sp.blockPatient(pat.getId());
                    System.out.println("EMAIL =" + u.getEmail());
                    AffichageListePatients();
                    try {
                        sendMail(u.getEmail(), 1, "");
                    } catch (MessagingException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
                
            }
        }

    }
}
