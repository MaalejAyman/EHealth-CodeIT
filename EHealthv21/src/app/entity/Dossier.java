package app.entity;
// Generated 14 avr. 2021 00:12:42 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Dossier generated by hbm2java
 */
public class Dossier  implements java.io.Serializable {


     private Integer id;
     private Medecin medecin;
     private Patient patient;
     private Date datecreation;
     private Set ficheMedicales = new HashSet(0);

    public Dossier() {
    }

	
    public Dossier(Date datecreation) {
        this.datecreation = datecreation;
    }
    public Dossier(Medecin medecin, Patient patient, Date datecreation, Set ficheMedicales) {
       this.medecin = medecin;
       this.patient = patient;
       this.datecreation = datecreation;
       this.ficheMedicales = ficheMedicales;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Medecin getMedecin() {
        return this.medecin;
    }
    
    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }
    public Patient getPatient() {
        return this.patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public Date getDatecreation() {
        return this.datecreation;
    }
    
    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }
    public Set getFicheMedicales() {
        return this.ficheMedicales;
    }
    
    public void setFicheMedicales(Set ficheMedicales) {
        this.ficheMedicales = ficheMedicales;
    }




}


