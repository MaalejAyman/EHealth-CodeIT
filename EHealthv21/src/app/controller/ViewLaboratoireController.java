/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Laboratoire;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import app.entity.DossierLab;
import app.entity.Laboratoire;
import app.entity.Patient;
import app.entity.Service;
import app.entity.User;
import app.service.LaboratoireService;
import app.service.ServiceDossierLab;
import app.service.ServicePatient;
import app.service.ServiceService;
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
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewLaboratoireController implements Initializable {

    @FXML
    private TableView<DossierLab> table_lab;
    @FXML
    private TableColumn<DossierLab, Integer> id;
    @FXML
    private TableColumn<DossierLab, String> patient;
    @FXML
    private TableColumn<DossierLab, String> patientpre;
    private int iddl = -1;
    private Laboratoire ConnectedLaboratoire;
    @FXML
    private TextField edithf;
    @FXML
    private TextField editadresse;
    @FXML
    private TextField edittel;
    @FXML
    private TextField edithd;
    @FXML
    private ChoiceBox<String> listville;
    @FXML
    private ChoiceBox<Patient> listPatients;
    @FXML
    private ListView<Service> editService;
    @FXML
    private Label text;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
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
            ObservableList<String> listVilles = FXCollections.observableArrayList(listVille);
            listville.setItems(listVilles);
            ServiceService ss = new ServiceService();
            List<Service> listService = ss.getServices();
            ObservableList<Service> listServices = FXCollections.observableArrayList(listService);
            editService.setItems(listServices);
            ServicePatient sp = new ServicePatient();
            List<Patient> listPatient = sp.afficherListePatients();
            ObservableList<Patient> listePatients = FXCollections.observableArrayList(listPatient);
            listPatients.setItems(listePatients);
            editService.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listville.setValue(this.ConnectedLaboratoire.getUser().getVille());
            edittel.setText(this.ConnectedLaboratoire.getUser().getTel() + "");
            edithd.setText(this.ConnectedLaboratoire.getHoraireDebut() + "");
            edithf.setText(this.ConnectedLaboratoire.getHoraireFin() + "");
            editadresse.setText(this.ConnectedLaboratoire.getUser().getAdresse() + "");

            id.setCellValueFactory(new Callback<CellDataFeatures<DossierLab, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(CellDataFeatures<DossierLab, Integer> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getId());
                }
            });
            patient.setCellValueFactory(new Callback<CellDataFeatures<DossierLab, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<DossierLab, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getPatient().getNom());
                }
            });
            patientpre.setCellValueFactory(new Callback<CellDataFeatures<DossierLab, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<DossierLab, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getPatient().getPrenom());
                }
            });
            table_lab.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DossierLab> obs, DossierLab oldVal, DossierLab newVal) -> {
                if (newVal != null) {
                    iddl = newVal.getId();
                    System.out.println(iddl);
                }
            });
            text.setText(this.ConnectedLaboratoire.getNom());
            this.AffichageListeDossierLab();

        } catch (SQLException ex) {
            Logger.getLogger(ViewLaboratoireController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ViewLaboratoireController(Laboratoire value) {
        this.ConnectedLaboratoire = value;

    }

    public ViewLaboratoireController() {
        this(new Laboratoire());
    }

    public void setL(Laboratoire value) {
        ConnectedLaboratoire = value;
    }

    public void setConnectedPatient(Laboratoire p) {
        this.ConnectedLaboratoire = p;
        System.out.println(this.ConnectedLaboratoire.getUser().getType());
    }

    public void AffichageListeDossierLab() throws SQLException {

        ServiceDossierLab sdl = new ServiceDossierLab();
        List<DossierLab> lp = sdl.getDossierLabByLab(55);
        ObservableList<DossierLab> ob = FXCollections.observableArrayList(lp);
        table_lab.setItems(ob);
    }

    @FXML
    private void move(ActionEvent event) throws IOException {
        if (iddl != -1) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/ViewDossierLab.fxml"));
            Parent root;
            ViewDossierLabController controller = new ViewDossierLabController(iddl);
            loader.setController(controller);
            root = loader.load();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Pas de dossier choisi");
            alert.setContentText("Veuillez choisir un dossier!");
            alert.showAndWait();
        }
    }

    @FXML
    public void editLab() throws SQLException {
        try {
            String email = this.ConnectedLaboratoire.getUser().getEmail();
            String nom = this.ConnectedLaboratoire.getNom();
            String adresse = editadresse.getText();
            String ville = listville.getSelectionModel().getSelectedItem();
            int id = this.ConnectedLaboratoire.getUser().getId();
            DateFormat format = new SimpleDateFormat("h:mm", Locale.ENGLISH);
            Date horaireDebut = format.parse(edithd.getText());
            Date horaireFin = format.parse(edithf.getText());
            ObservableList selectedIndicesLab = editService.getSelectionModel().getSelectedIndices();
            ServiceService ss = new ServiceService();
            List<Service> listService = ss.getServices();
            ObservableList<Service> listServices = FXCollections.observableArrayList(listService);
            int tel = Integer.parseInt(edittel.getText());
            int numService = this.ConnectedLaboratoire.getNumService();
            User u = new User(id, email, adresse, ville, tel);
            Laboratoire l = new Laboratoire(id, u, numService, nom, horaireDebut, horaireFin);
            LaboratoireService sp = new LaboratoireService();
            Set<Service> h = new HashSet<Service>();
            for (Object i : selectedIndicesLab) {
                h.add(listServices.get((int) i));
            }
            l.setServices(h);
            sp.updateLaboratoire(l);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Modifier vos données");
            alert.setContentText("Vos données sont mises à jour!");
            alert.showAndWait();
            System.out.println("Ajouté avec success");
        } catch (ParseException ex) {
            Logger.getLogger(ViewPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void createDossierLab() throws SQLException, NullPointerException {
        try {
            Laboratoire laboratoire = this.ConnectedLaboratoire;
            Patient patientx = listPatients.getSelectionModel().getSelectedItem();
            DossierLab dl = new DossierLab(laboratoire, patientx);
            ServiceDossierLab sdl = new ServiceDossierLab();
            sdl.ajouterDossierLab(dl);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Ajout de Dossier");
            alert.setContentText("Le Dossier est ajouté avec success!");
            alert.showAndWait();
            System.out.println("Ajouté avec success");
            AffichageListeDossierLab();
        } catch (NullPointerException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Patient non défini");
            alert.setContentText("Veuiller choisir un patient!");
            alert.showAndWait();
        }
    }

    @FXML
    public void createFolder() throws SQLException {
        if (iddl != -1) {
            String path = "c:/PI/" + iddl;
            File file = new File(path);
            boolean bool = file.mkdir();
            if (bool) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Ajout de Dossier sur PC");
                alert.setContentText("Le Dossier est ajouté avec success!");
                alert.showAndWait();
                System.out.println("Directory created successfully");
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Ajout de Dossier");
                alert.setContentText("Le Dossier est existant!");
                alert.showAndWait();
                System.out.println("Sorry couldn’t create specified directory");
            }
        }else{
            Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Ajout de Dossier");
                alert.setContentText("Veuiller choisir un dossier!");
                alert.showAndWait();
        }

    
    }
    @FXML
    private void logoutLab(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewLogin.fxml"));
        Scene scene = new Scene(parent3);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
