package app.entity;
// Generated 14 avr. 2021 00:12:42 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Patient generated by hbm2java
 */
public class Patient implements java.io.Serializable {

    private int id;
    private User user;
    private String nom;
    private String prenom;
    private int cin;
    private String sexe;
    private boolean blocked;
    private Integer nbAlert;
    private boolean blockedRdv;
    private Set dossierLabs = new HashSet(0);
    private Set rendezvouses = new HashSet(0);
    private Set dossiers = new HashSet(0);
    private Set questions = new HashSet(0);

    public Patient() {
    }

    public Patient(int id, User user, String nom, String prenom, int cin, String sexe, boolean blockedRdv, boolean blocked) {
        this.id = id;
        this.user = user;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.sexe = sexe;
        this.blockedRdv = blockedRdv;
        this.blocked = blocked;
    }

    public Patient(int id, User user, String nom, String prenom, int cin, String sexe, boolean blockedRdv) {
        this.id = id;
        this.user = user;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.sexe = sexe;
        this.blockedRdv = blockedRdv;
    }

    public Patient(User user, String nom, String prenom, int cin, String sexe, boolean blockedRdv) {
        this.user = user;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.sexe = sexe;
        this.blockedRdv = blockedRdv;
    }

    public Patient(String nom, String prenom, int cin, String sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.sexe = sexe;
    }

    public Patient(User user, String nom, String prenom, int cin, String sexe, boolean blocked, Integer nbAlert, boolean blockedRdv, Set dossierLabs, Set rendezvouses, Set dossiers, Set questions) {
        this.user = user;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.sexe = sexe;
        this.blocked = blocked;
        this.nbAlert = nbAlert;
        this.blockedRdv = blockedRdv;
        this.dossierLabs = dossierLabs;
        this.rendezvouses = rendezvouses;
        this.dossiers = dossiers;
        this.questions = questions;
    }

    public Patient(int id, User user, String nom, String prenom, int cin, String sexe) {
        this.id = id;
        this.user = user;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.sexe = sexe;
    }
    
    @Override
    public String toString() {
        return "Patient: " + nom +" "+ prenom +", CIN: "+ cin ;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getCin() {
        return this.cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public String getSexe() {
        return this.sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public boolean getBlocked() {
        return this.blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Integer getNbAlert() {
        return this.nbAlert;
    }

    public void setNbAlert(Integer nbAlert) {
        this.nbAlert = nbAlert;
    }

    public boolean isBlockedRdv() {
        return this.blockedRdv;
    }

    public void setBlockedRdv(boolean blockedRdv) {
        this.blockedRdv = blockedRdv;
    }

    public Set getDossierLabs() {
        return this.dossierLabs;
    }

    public void setDossierLabs(Set dossierLabs) {
        this.dossierLabs = dossierLabs;
    }

    public Set getRendezvouses() {
        return this.rendezvouses;
    }

    public void setRendezvouses(Set rendezvouses) {
        this.rendezvouses = rendezvouses;
    }

    public Set getDossiers() {
        return this.dossiers;
    }

    public void setDossiers(Set dossiers) {
        this.dossiers = dossiers;
    }

    public Set getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set questions) {
        this.questions = questions;
    }

}
