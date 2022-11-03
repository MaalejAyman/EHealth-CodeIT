/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Analyse;
import app.entity.DossierLab;
import app.entity.Patient;
import app.service.ServiceAnalyse;
import app.service.ServiceDossierLab;
import app.service.ServicePatient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Amine Chelly
 */
public class ViewDossierLabController implements Initializable {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font whiteFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.WHITE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    static String path;

    @FXML
    private TableView<Analyse> tabAnalyse;
    @FXML
    private TableColumn<Analyse, String> description;
    @FXML
    private TableColumn<Analyse, Date> date;
    @FXML
    private TextField desc_Analyse;
    static int id = -1;
    static int i = 1;
    private int iddossier;
    @FXML
    private DatePicker data_Analyse;
    @FXML
    private TextArea newdes;
    @FXML
    private DatePicker newdate;
    @FXML
    private ImageView qrc;
    String datee;
    String dascr;

    public ViewDossierLabController(int iddossier) {
        this.iddossier = iddossier;

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(iddossier);
        try {

            qrcode();
            description.setCellValueFactory(new Callback<CellDataFeatures<Analyse, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Analyse, String> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getDescription());
                }
            });
            date.setCellValueFactory(new Callback<CellDataFeatures<Analyse, Date>, ObservableValue<Date>>() {
                public ObservableValue<Date> call(CellDataFeatures<Analyse, Date> p) {

                    return new ReadOnlyObjectWrapper(p.getValue().getDate());
                }
            });
            tabAnalyse.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Analyse> obs, Analyse oldVal, Analyse newVal) -> {
                if (newVal != null) {
                    desc_Analyse.setText(newVal.getDescription() + "");
                    data_Analyse.setValue(LocalDate.of(newVal.getDate().getYear() + 1900, newVal.getDate().getMonth() + 1, newVal.getDate().getDate()));
                    id = newVal.getId();
                }
                datee = String.valueOf(data_Analyse.getValue());
                dascr = desc_Analyse.getText();
            }
            );
            AffichageListeAnalyse();
        } catch (SQLException ex) {
            Logger.getLogger(ViewDossierLabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriterException ex) {
            Logger.getLogger(ViewDossierLabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void AffichageListeAnalyse() throws SQLException {

        ServiceAnalyse sdl = new ServiceAnalyse();
        List<Analyse> lp = sdl.getAnalyseByLab(iddossier);
        ObservableList<Analyse> ob = FXCollections.observableArrayList(lp);
        tabAnalyse.setItems(ob);
    }

    @FXML
    public void AjoutAnalyse() throws SQLException, ParseException {
        try {
            ServiceDossierLab sdl = new ServiceDossierLab();
            DossierLab dl = sdl.getDossierLabById(iddossier);
            String des = newdes.getText();
            DateFormat formatd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date datea = formatd.parse(newdate.getValue().toString());
            Analyse a = new Analyse(dl, des, datea);
            ServiceAnalyse as = new ServiceAnalyse();
            as.ajouterAnalyse(a);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Ajout d'analyse");
            alert.setContentText("Votre analyse est ajouté avec success!");
            alert.showAndWait();
            System.out.println("Ajouté avec success");
            AffichageListeAnalyse();
            newdes.setText("");
            newdate.setValue(null);
        } catch (ParseException ex) {
            Logger.getLogger(ViewPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void updateAnalyse() throws SQLException, ParseException {
        try {
            String descAn = desc_Analyse.getText();
            String abc = String.valueOf(data_Analyse.getValue());
            Date dateanalyse = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(abc);
            Analyse a = new Analyse(descAn, dateanalyse);
            a.setId(ViewDossierLabController.id);
            ServiceAnalyse sa = new ServiceAnalyse();
            sa.majAnalyse(a);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Modification d'analyse");
            alert.setContentText("Votre analyse est modifié avec success!");
            alert.showAndWait();
            AffichageListeAnalyse();
            desc_Analyse.setText("");
            data_Analyse.setValue(null);
            id = -1;
        } catch (ParseException ex) {
            System.out.println("fail");
            Logger.getLogger(ViewPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void DeleteAnalyse(ActionEvent eve) throws SQLException {
        if (id != -1) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Supprimer cet Analyse ");
            alert.setHeaderText("Voulez vous vraiment supprimer cet Analyse ?");
            alert.setContentText("");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (id != -1) {
                    ServiceAnalyse sp = new ServiceAnalyse();
                    sp.DeleteAnalyse(id);
                    id = -1;
                }
                id = -1;
                AffichageListeAnalyse();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Suppression");
            alert.setContentText("Votre devez choisir un Analyse à supprimer!");
            alert.showAndWait();
        }
    }

   @FXML
    public void createPDF() throws FileNotFoundException, DocumentException, SQLException {
        try {
            if ( datee != null ){
            path = "c:/PI/" + iddossier + "/analyse" + i + ".pdf";
            ServiceDossierLab sdl = new ServiceDossierLab();
            DossierLab dl = sdl.getDossierLabById(iddossier);
            Patient p = dl.getPatient();
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addMetaData(document, p);
            addContentPage(document, p, datee, dascr);
            document.close();
            int j = i;
            i++;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Enregistrement PDF");
            alert.setContentText("Votre analyse est enregistré en format PDF avec success au nom analyse" + j + ".pdf!");
            alert.showAndWait();}
            else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Enregistrement PDF");
            alert.setContentText("Veuiller choisir un Analyse");
            alert.showAndWait();
            }
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Enregistrement PDF");
            alert.setContentText("Veuiller créer un dossier su PC pour le dossier de ce patient");
            alert.showAndWait();
        }
    }
    private static void addMetaData(Document document, Patient p) throws DocumentException {
        document.add(Chunk.NEWLINE);
        document.addTitle("Analyse");
        document.addSubject("Resultat d'un Analyse");
        document.addKeywords("Laboratoire, Patient, Analyse");
        document.addAuthor("Laboratorie");
        document.addCreator("Laboratoire");
    }

    private static void addContentPage(Document document, Patient p, String date, String desc)
            throws DocumentException {
        String nom = p.getNom();
        String prenom = p.getPrenom();
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  "));
        preface.add(new Paragraph("          Analyse", catFont));
        preface.add(new Paragraph("  "));
        preface.add(new Paragraph(
                "Cet Analyse est fait pour le/la patient(e): " + nom + " " + prenom + " à la date " + date,
                smallBold));
        preface.add(new Paragraph("  "));
        preface.add(new Paragraph(
                "" + desc,
                smallBold));
        document.add(preface);
    }

    @FXML
    private void backtostart(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/app/views/ViewLaboratoire.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void qrcode() throws SQLException, WriterException {
        ServiceDossierLab sdl = new ServiceDossierLab();
        DossierLab dl = sdl.getDossierLabById(iddossier);
        Patient p = dl.getPatient();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String tel = "Nom: " + p.getNom() + " " + p.getPrenom() + "                       Telephone: " + p.getUser().getTel() + "                      Email: " + p.getUser().getEmail();
        int width = 150;
        int height = 150;
        BufferedImage bufferedImage = null;
        try {
            BitMatrix byteMatrix = qrCodeWriter.encode(tel, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    if (byteMatrix.get(j, k)) {
                        graphics.fillRect(j, k, 1, 1);
                    }
                }
            }
            System.out.println("success");
        } catch (WriterException ex) {
            // Logger.getLogger(JavaFX_QRCodeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        qrc.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
    }

}
