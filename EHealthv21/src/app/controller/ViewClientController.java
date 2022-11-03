/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Medecin;
import app.entity.Patient;
import app.entity.Question;
import app.entity.Rendezvous;
import app.entity.Reponse;
import app.entity.Specialite;
import app.entity.User;
import static app.mail.Mail.sendMail;
import app.service.MedecinService;
import app.service.ServicePatient;
import app.service.ServiceQuestion;
import app.service.ServiceRendezVous;
import app.service.ServiceReponse;
import app.service.ServiceSpecialite;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.mail.MessagingException;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewClientController implements Initializable {

    @FXML
    private TabPane tabtab;
    @FXML
    private Tab tabrdv;
    @FXML
    private TextField input_email_client;
    @FXML
    private TextField input_nom_client;
    @FXML
    private TextField input_prenom_client;
    @FXML
    private TextField input_cin_client;
    @FXML
    private TextField input_adresse_client;
    @FXML
    private TextField input_numtel_client;
    @FXML
    private ChoiceBox<String> input_ville_client;
    @FXML
    private ChoiceBox<String> input_sexe_client;
    @FXML
    private Button btn_modif_profil;
    private Patient ConnectedPatient;
    private int id = -1;
    //Anaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaas
    public static int idQuestion = -1;
    @FXML
    private Label erreurtext;
    @FXML
    private TextField tfquestion;

    private CheckBox cbspecialite;
    @FXML
    private TableView<Question> tvbox;
    @FXML
    private TableColumn<Question, String> colquestion;
    @FXML
    private ComboBox ddlspecialite;
    @FXML
    private Label label;

    @FXML
    private TableColumn<Question, String> colspecialite;
    @FXML
    private TableView<Reponse> tvboxreponses;
    @FXML
    private TableColumn<Reponse, String> colreponse;
    @FXML
    private Button btnvider;
    @FXML
    private TableColumn<?, ?> repquest;

    @FXML
    private Label erreurspecialite;
    // Evaluer  Rendez vous
    public static int idSelectionne = -1;
    @FXML
    private ImageView etoile1;
    @FXML
    private ImageView etoile2;
    @FXML
    private ImageView etoile3;
    @FXML
    private ImageView etoile4;
    @FXML
    private ImageView etoile5;
    @FXML
    private TableView<Rendezvous> tbvrdv;
    @FXML
    private TableColumn<Rendezvous, ?> idrdv;
    private TableColumn<Rendezvous, ?> evaluation;
    @FXML
    private ImageView eval1;
    @FXML
    private Label label_cntrl_saisi;
    @FXML
    private ImageView eval3;
    @FXML
    private ImageView eval2;
    @FXML
    private ImageView eval4;
    @FXML
    private ImageView eval5;
    @FXML
    private TableColumn<Rendezvous, ?> nom_med;
    @FXML
    private Label labelimpossible;
    @FXML
    private TableColumn<Rendezvous, ?> daterdv;
    @FXML
    private TableColumn<Rendezvous, ?> heurerdv;
    @FXML
    private TableColumn<Rendezvous, ?> etatrdv;
    //TOP medecins
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
    private TableColumn<Medecin, String> output_adresseMed;

    @FXML
    private TableColumn<Medecin, String> output_villeMed;

    @FXML
    private TableColumn<Medecin, Integer> output_telMed;

    @FXML
    private TableColumn<Medecin, String> output_specialiteMed;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Anas TOP medecin
        afficherTopMedecin();
        //Anas Question

        colquestion.setCellValueFactory(new PropertyValueFactory<>("text"));

        colspecialite.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Question, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Question, String> q) {
                return new ReadOnlyObjectWrapper(q.getValue().getSpecialite());

            }
        });

        afficherQuestion();

        //Anas Evaluation
        AffichageRdv();
        //Anas END
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
        input_ville_client.setItems(listVilles);

        List<String> list = new ArrayList<String>();
        list.add("Homme");
        list.add("Femme");
        ObservableList<String> sexe = FXCollections.observableArrayList(list);
        input_sexe_client.setItems(sexe);

        System.out.println(this.ConnectedPatient);
        if (this.ConnectedPatient != null) {
            id = this.ConnectedPatient.getUser().getId();
            input_email_client.setText(this.ConnectedPatient.getUser().getEmail());
            input_nom_client.setText(this.ConnectedPatient.getNom());
            input_prenom_client.setText(this.ConnectedPatient.getPrenom());
            input_cin_client.setText(this.ConnectedPatient.getCin() + "");
            input_adresse_client.setText(this.ConnectedPatient.getUser().getAdresse());
            input_numtel_client.setText(this.ConnectedPatient.getUser().getTel() + "");
            if (this.ConnectedPatient.getSexe().equals("0")) {
                input_sexe_client.setValue("Homme");
            } else {
                input_sexe_client.setValue("Femme");
            }
            input_ville_client.setValue(this.ConnectedPatient.getUser().getVille());

        }
    }

    public ViewClientController(Patient value) {
        this.ConnectedPatient = value;

    }

    public ViewClientController() {
        this(new Patient());
    }

    public void setP(Patient value) {
        ConnectedPatient = value;

    }

    public void setConnectedPatient(Patient p) {
        this.ConnectedPatient = p;
        System.out.println(this.ConnectedPatient.getUser().getType());
    }

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

    @FXML
    public void UpdateProfil() throws SQLException {
        boolean test = true;
        if (!isValidEmail(input_email_client.getText())) {
            label_cntrl_saisi.setText("verifier votre mail");
            test = false;
        }
        if (!isValidCin(input_cin_client.getText())) {
            label_cntrl_saisi.setText("verifier votre CIN");
            test = false;
        }
        if (!isValidCin(input_numtel_client.getText())) {
            label_cntrl_saisi.setText("verifier votre numero de telephone");
            test = false;
        }
        if (test && (isBlank(input_email_client.getText()))
                && (isBlank(input_nom_client.getText())) && (isBlank(input_prenom_client.getText()))
                && (isBlank(input_adresse_client.getText()))
                && (isBlank(input_ville_client.getSelectionModel().getSelectedItem()))
                && (isBlank(input_sexe_client.getSelectionModel().getSelectedItem()))
                && (isBlank(input_numtel_client.getText()))
                && (isBlank(input_cin_client.getText()))) {
            String email = input_email_client.getText();
            String nom = input_nom_client.getText();
            String prenom = input_prenom_client.getText();
            String adresse = input_adresse_client.getText();
            String ville = input_ville_client.getSelectionModel().getSelectedItem();
            int tel = Integer.parseInt(input_numtel_client.getText());
            int cin = Integer.parseInt(input_cin_client.getText());
            String sexeP = input_sexe_client.getSelectionModel().getSelectedItem();
            User u = new User(id, email, adresse, ville, tel);
            if (sexeP == "Homme") {
                sexeP = "0";
            } else {
                sexeP = "1";
            }
            Patient p = new Patient(id, u, nom, prenom, cin, sexeP);
            ServicePatient sp = new ServicePatient();
            sp.updatePatient(p);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification de profil");
            alert.setHeaderText("Vos information etait modifié avec succée");
            alert.showAndWait();
        } else {
            label_cntrl_saisi.setText("verifier les champs");
        }
    }

    @FXML
    private void logoutClient(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewLogin.fxml"));
        Scene scene = new Scene(parent3);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
//---------------------------------Anas----------------------------------------------------------------------

    public void afficherQuestion() {
        try {
            ServiceQuestion SQ = new ServiceQuestion();
            List<Question> ql = SQ.getQuestionByIdPatient(ConnectedPatient.getId());
            ObservableList<Question> data = FXCollections.observableArrayList(ql);
            //colid.setCellValueFactory(new PropertyValueFactory<>("id"));

            ServiceSpecialite SS = new ServiceSpecialite();
            //recupperer la list des specialité  pour remplir la drop down list   
            List<String> sl = SS.loadSpecialite();

            ObservableList<String> spec = FXCollections.observableArrayList(sl);
            ddlspecialite.setItems(spec);

            tvbox.setItems(data);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void poserQuestion() throws SQLException, IOException {

        ServicePatient ps = new ServicePatient();
        String specialite = (String) ddlspecialite.getValue();
        System.out.println(specialite);

        Patient p = ps.getPatientById(ConnectedPatient.getId());
// Si le patient est bloqué 
        if (p.getBlocked() == true) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("ERROR");
            alert.setHeaderText("Vous ne pouvez pas poser des question");
            alert.setContentText("A cause de vos questions qui contient des termes de violence , Vous êtes  bloqué. \n cliquer sur ok  pour contacter l'administrateur ");
            try {
                sendMail("ehealthcodeit@gmail.com", 2, "");
            } catch (MessagingException ex) {
                System.out.println(ex.getMessage());
            }

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.FINISH) {

            }
        } else {
            ServiceQuestion sq;
            if (tfquestion.getText().equals("")) {
                erreurtext.setText("Ce champs  est obligatoire");

            } else {
                if (specialite == null) {
                    erreurspecialite.setText("Vous devez choisir une spécialité");

                } else {

                    String text = tfquestion.getText();
                    Specialite sp = new Specialite(specialite);
                    ServiceSpecialite ss = new ServiceSpecialite();

                    sp = ss.getSpecialiteByNom(specialite);

                    Question q = new Question(p, new Specialite(), text);
                    sq = new ServiceQuestion();

                    sq.poserQuestion(p, q, sp);
                    afficherQuestion();

                }
            }
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        idQuestion = -1;
        tfquestion.clear();
        repquest.setText("");
        label.setText("");
        erreurspecialite.setText("");
        erreurtext.setText("");
        //ddlspecialite.setItems(null);

        tvboxreponses.setItems(null);
    }

    @FXML
    private void supprimerQuestion(MouseEvent event) {
        if (idQuestion == -1) {
            erreurtext.setText("Selectionne une question svp");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer question");
        alert.setHeaderText("Veuillez-vous vraiment supprimer cette question ");
        //alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.CANCEL) {
            return;
        }
        ServiceQuestion sq;
        String text = tfquestion.getText();
        Question q = new Question(new Patient(), new Specialite(), text);
        sq = new ServiceQuestion();
        sq.supprimerQuestion(text);
        afficherQuestion();
    }

    @FXML
    private void modifierQuestion(MouseEvent event) throws SQLException {
        String specialite = (String) ddlspecialite.getValue();
        ServiceQuestion sq;
        if (tfquestion.getText().equals("")) {
            erreurtext.setText("Ce champs  est obligatoire");

        } else {
            if (specialite == null) {
                erreurspecialite.setText("Vous devez choisir une spécialité");

            } else {
                String text = tfquestion.getText();
                Specialite sp = new Specialite();
                ServiceSpecialite ss = new ServiceSpecialite();

                sp = ss.getSpecialiteByNom(specialite);

                ServicePatient ps = new ServicePatient();
                Patient p = ps.getPatientById(ConnectedPatient.getId());

                sq = new ServiceQuestion();

                Question q = sq.getQuestionById(idQuestion);
                q.setText(text);
                sq.modifierQuestion(p, q, sp);
                afficherQuestion();
            }
        }
    }
//Quand on séléctionne une spécialité

    @FXML
    void Select(ActionEvent event) {
        //  String s = ddlspecialite.getSelectionModel().getSelectedItem().toString();
        // label.setText(s);
    }

    // Quand on selectionne une Question
    @FXML
    private void selectionner(MouseEvent event) throws SQLException {
        Question quest = tvbox.getSelectionModel().getSelectedItem();
        ViewClientController.idQuestion = quest.getId();
        System.out.println(idQuestion);

        tfquestion.setText(quest.getText());
        // tfquestion.setTextFormatter(quest.getText());

        repquest.setText(quest.getText());
        //   System.out.println(quest.getId());
        ServiceReponse SR = new ServiceReponse();
        List<Reponse> qr = SR.getReponsesByIdQuestion(quest.getId());
        ObservableList<Reponse> data = FXCollections.observableArrayList(qr);
        repquest.setCellValueFactory(new PropertyValueFactory<>("text"));
        tvboxreponses.setItems(data);

    }

    //Evaluation Rendez vous
    @FXML
    private void evaluerService3(MouseEvent event) throws IOException, SQLException {
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        MedecinService SM = new MedecinService();
        Medecin m = rdv.getMedecin();
        ServiceRendezVous RS = new ServiceRendezVous();
        RS.evaluerService(idSelectionne, 3);
        SM.calculNote(m.getId(), 3);
        afficherEvaluation();
        afficherTopMedecin();
    }

    @FXML
    private void evaluerService4(MouseEvent event) throws IOException, SQLException {

        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        MedecinService SM = new MedecinService();
        Medecin m = rdv.getMedecin();

        System.out.println("Id medecin " + m.getId());
        ServiceRendezVous RS = new ServiceRendezVous();
        RS.evaluerService(idSelectionne, 4);
        SM.calculNote(m.getId(), 4);
        afficherEvaluation();
        afficherTopMedecin();
    }

    @FXML
    private void evaluerService2(MouseEvent event) throws IOException, SQLException {
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        MedecinService SM = new MedecinService();
        Medecin m = rdv.getMedecin();
        ServiceRendezVous RS = new ServiceRendezVous();
        RS.evaluerService(idSelectionne, 2);
        SM.calculNote(m.getId(), 2);
        afficherEvaluation();
        afficherTopMedecin();
    }

    @FXML
    private void evaluerService5(MouseEvent event) throws IOException, SQLException {

        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        MedecinService SM = new MedecinService();
        Medecin m = rdv.getMedecin();
        ServiceRendezVous RS = new ServiceRendezVous();
        RS.evaluerService(idSelectionne, 5);
        SM.calculNote(m.getId(), 5);
        afficherEvaluation();
        afficherTopMedecin();

        //  AffichageRdv();
        afficherTopMedecin();
    }

    @FXML
    private void evaluerService1(MouseEvent event) throws IOException, SQLException {
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        MedecinService SM = new MedecinService();
        Medecin m = rdv.getMedecin();
        ServiceRendezVous RS = new ServiceRendezVous();
        RS.evaluerService(idSelectionne, 1);
        SM.calculNote(m.getId(), 1);
        afficherEvaluation();
        afficherTopMedecin();
    }

    @FXML
    private void selectionnerrdv(MouseEvent event
    ) {
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        idSelectionne = rdv.getId();
        if (rdv.getEtat().equalsIgnoreCase("confirmé")) {
            labelimpossible.setText("Impossible d'evaluer un rendez-vous qui n'est pas encore effectué");
            etoile1.setImage(null);
            etoile2.setImage(null);
            etoile3.setImage(null);
            etoile4.setImage(null);
            etoile5.setImage(null);

            eval1.setImage(null);
            eval2.setImage(null);
            eval3.setImage(null);
            eval4.setImage(null);
            eval5.setImage(null);
            return;

        } else {
            labelimpossible.setText("");

            etoile1.setImage(new Image("app/img/empty.png"));
            etoile2.setImage(new Image("app/img/empty.png"));
            etoile3.setImage(new Image("app/img/empty.png"));
            etoile4.setImage(new Image("app/img/empty.png"));
            etoile5.setImage(new Image("app/img/empty.png"));
            idSelectionne = rdv.getId();
            try {
                afficherEvaluation();

            } catch (SQLException ex) {
                Logger.getLogger(ViewClientController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @FXML
    private void changeimage1(MouseEvent event
    ) {
        if (idSelectionne == -1) {
            return;
        }

        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();

        if (rdv.getEtat().equals("confirmé")) {
            return;
        }

        /*   etoile1.setImage(null);
            etoile2.setImage(null);
            etoile3.setImage(null);
            etoile4.setImage(null);
            etoile5.setImage(null); */
        etoile1.setImage(new Image("app/img/filled.png"));

    }

    @FXML
    private void changeimage2(MouseEvent event
    ) {
        if (idSelectionne == -1) {
            return;
        }
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        if (rdv.getEtat().equals("confirmé")) {
            return;
        }

//   System.out.println("RDV=" + rdv.getEtat());
        /* if (rdv.getEtat().equals("confirmé")) {
            etoile1.setImage(null);
            etoile2.setImage(null);
            etoile3.setImage(null);
            etoile4.setImage(null);
            etoile5.setImage(null);
       // } else { */
        etoile1.setImage(new Image("app/img/filled.png"));
        etoile2.setImage(new Image("app/img/filled.png"));
        //}
    }

    @FXML
    private void changeimage3(MouseEvent event
    ) {
        if (idSelectionne == -1) {
            return;
        }

        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();

        if (rdv.getEtat().equals("confirmé")) {
            return;
        }

        /*if (rdv.getEtat().equals("confirmé")) {
            etoile1.setImage(null);
            etoile2.setImage(null);
            etoile3.setImage(null);
            etoile4.setImage(null);
            etoile5.setImage(null);
        } else {  */
        etoile1.setImage(new Image("app/img/filled.png"));
        etoile2.setImage(new Image("app/img/filled.png"));
        etoile3.setImage(new Image("app/img/filled.png"));

        //}
    }

    @FXML
    private void changeimage4(MouseEvent event
    ) {
        if (idSelectionne == -1) {
            return;
        }

        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        if (rdv.getEtat().equals("confirmé")) {
            return;

        }

        /*       if (rdv.getEtat().equals("confirmé")) {
            etoile1.setImage(null);
            etoile2.setImage(null);
            etoile3.setImage(null);
            etoile4.setImage(null);
            etoile5.setImage(null);
      } else { */
        etoile1.setImage(new Image("app/img/filled.png"));
        etoile2.setImage(new Image("app/img/filled.png"));
        etoile3.setImage(new Image("app/img/filled.png"));
        etoile4.setImage(new Image("app/img/filled.png"));

        //     }
    }

    @FXML
    private void changeimage5(MouseEvent event
    ) {
        if (idSelectionne == -1) {
            return;
        }
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();

        if (rdv.getEtat().equals("confirmé")) {
            return;
        }

        etoile1.setImage(new Image("app/img/filled.png"));
        etoile2.setImage(new Image("app/img/filled.png"));
        etoile3.setImage(new Image("app/img/filled.png"));
        etoile4.setImage(new Image("app/img/filled.png"));
        etoile5.setImage(new Image("app/img/filled.png"));

    }

    @FXML
    private void resetimage1(MouseEvent event
    ) {
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        //  System.out.println(rdv.getEtat());
        if (rdv.getEtat().equals("confirmé")) {
            return;
        }

        etoile1.setImage(new Image("app/img/empty.png"));
        etoile2.setImage(new Image("app/img/empty.png"));
        etoile3.setImage(new Image("app/img/empty.png"));
        etoile4.setImage(new Image("app/img/empty.png"));
        etoile5.setImage(new Image("app/img/empty.png"));

    }

    @FXML
    private void resetimage2(MouseEvent event
    ) {

        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        //    System.out.println(rdv.getEtat());

        if (rdv.getEtat().equals("confirmé")) {
            return;
        }

        etoile2.setImage(new Image("app/img/empty.png"));
        etoile3.setImage(new Image("app/img/empty.png"));
        etoile4.setImage(new Image("app/img/empty.png"));
        etoile5.setImage(new Image("app/img/empty.png"));

    }

    @FXML
    private void resetimage3(MouseEvent event
    ) {
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        //      System.out.println(rdv.getEtat());

        if (rdv.getEtat().equals("confirmé")) {
            return;
        }
        etoile3.setImage(new Image("app/img/empty.png"));
        etoile4.setImage(new Image("app/img/empty.png"));
        etoile5.setImage(new Image("app/img/empty.png"));

    }

    @FXML
    private void resetimage4(MouseEvent event
    ) {
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
//        System.out.println(rdv.getEtat());

        if (rdv.getEtat().equals("confirmé")) {
            return;
        }
        etoile4.setImage(new Image("app/img/empty.png"));
        etoile5.setImage(new Image("app/img/empty.png"));

    }

    @FXML
    private void resetimage5(MouseEvent event
    ) {
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        //  System.out.println(rdv.getEtat());

        if (rdv.getEtat().equals("confirmé")) {
            return;
        }
        etoile5.setImage(new Image("app/img/empty.png"));

    }

    private void AffichageRdv() {
        ServiceRendezVous RS = new ServiceRendezVous();
        List<Rendezvous> lrdv = RS.getRendezVousByUser(ConnectedPatient.getId()); //  afficherMesRendezVous(28);

        ObservableList<Rendezvous> data = FXCollections.observableArrayList(lrdv);
        idrdv.setCellValueFactory(new PropertyValueFactory<>("id"));
        etatrdv.setCellValueFactory(new PropertyValueFactory<>("etat"));
        daterdv.setCellValueFactory(new PropertyValueFactory<>("date"));
        heurerdv.setCellValueFactory(new PropertyValueFactory<>("heure"));
        tbvrdv.setItems(data);
    }

    private void afficherEvaluation() throws SQLException {
        ServiceRendezVous sr = new ServiceRendezVous();
        Rendezvous rdv = tbvrdv.getSelectionModel().getSelectedItem();
        idSelectionne = rdv.getId();
        int ev = sr.getEvaluationById(idSelectionne);

        if (ev == 1) {
            eval1.setImage(new Image("app/img/filled.png"));
            eval2.setImage(new Image("app/img/empty.png"));
            eval3.setImage(new Image("app/img/empty.png"));
            eval4.setImage(new Image("app/img/empty.png"));
            eval5.setImage(new Image("app/img/empty.png"));

        }
        if (ev == 2) {
            eval1.setImage(new Image("app/img/filled.png"));
            eval2.setImage(new Image("app/img/filled.png"));
            eval3.setImage(new Image("app/img/empty.png"));
            eval4.setImage(new Image("app/img/empty.png"));
            eval5.setImage(new Image("app/img/empty.png"));

        }

        if (ev == 3) {
            eval1.setImage(new Image("app/img/filled.png"));
            eval2.setImage(new Image("app/img/filled.png"));
            eval3.setImage(new Image("app/img/filled.png"));
            eval4.setImage(new Image("app/img/empty.png"));
            eval5.setImage(new Image("app/img/empty.png"));

        }

        if (ev == 4) {
            eval1.setImage(new Image("app/img/filled.png"));
            eval2.setImage(new Image("app/img/filled.png"));
            eval3.setImage(new Image("app/img/filled.png"));
            eval4.setImage(new Image("app/img/filled.png"));
            eval5.setImage(new Image("app/img/empty.png"));

        }

        if (ev == 5) {
            eval1.setImage(new Image("app/img/filled.png"));
            eval2.setImage(new Image("app/img/filled.png"));
            eval3.setImage(new Image("app/img/filled.png"));
            eval4.setImage(new Image("app/img/filled.png"));
            eval5.setImage(new Image("app/img/filled.png"));

        }

    }
//End evaluation

    private void afficherTopMedecin() {
        try {
            output_nomMed.setCellValueFactory(new PropertyValueFactory<>("nom"));
            output_prenomMed.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            output_sexeMed.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Medecin, String> p) {
                    if (p.getValue().getSexe().equals("0")) {
                        return new ReadOnlyObjectWrapper("Homme");
                    } else {
                        return new ReadOnlyObjectWrapper("Femme");
                    }
                }
            });

            output_adresseMed.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Medecin, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getAdresse());
                }

            });
            output_specialiteMed.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Medecin, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getSpecialite().getNomSpec());
                }

            });
            output_emailMed.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Medecin, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getEmail());
                }

            });
            output_telMed.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medecin, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Medecin, Integer> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getTel());
                }

            });
            output_villeMed.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medecin, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Medecin, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getUser().getVille());
                }

            });
            MedecinService sm = new MedecinService();
            List<Medecin> lp = sm.afficherTopMedecin();
            ObservableList<Medecin> ob = FXCollections.observableArrayList(lp);
            table_medecin.setItems(ob);

        } catch (SQLException ex) {
            Logger.getLogger(ViewClientController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }
}
