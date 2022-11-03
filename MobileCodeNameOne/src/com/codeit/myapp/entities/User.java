package com.codeit.myapp.entities;
// Generated 14 avr. 2021 00:12:42 by Hibernate Tools 4.3.1


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {

    private Integer id;
    private String email;
    private Serializable roles;
    private String password;
    private String adresse;
    private String ville;
    private int tel;
    private boolean isVerified;
    private String type;
    private Medecin medecin;
    private Patient patient;
    private Set resetPasswordRequests = new HashSet(0);
    private Laboratoire laboratoire;

    public User() {
    }

    public User(String password) {
        this.password = password;
    }

    public User(int id, String email, Serializable roles, String password, String adresse, String ville, int tel, boolean isVerified, String type) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.adresse = adresse;
        this.ville = ville;
        this.tel = tel;
        this.isVerified = isVerified;
        this.type = type;
    }

    public User(String email, Serializable roles, String password, String adresse, String ville, int tel, boolean isVerified, String type) {
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.adresse = adresse;
        this.ville = ville;
        this.tel = tel;
        this.isVerified = isVerified;
        this.type = type;
    }

    public User(String email, Serializable roles, String password, String adresse, String ville, int tel, boolean isVerified, String type, Medecin medecin, Patient patient, Set resetPasswordRequests, Laboratoire laboratoire) {
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.adresse = adresse;
        this.ville = ville;
        this.tel = tel;
        this.isVerified = isVerified;
        this.type = type;
        this.medecin = medecin;
        this.patient = patient;
        this.resetPasswordRequests = resetPasswordRequests;
        this.laboratoire = laboratoire;
    }
public User(int id, String email, Serializable roles, String password, String adresse, String ville, int tel, boolean isVerified, String type, Medecin medecin,Laboratoire laboratoire) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.adresse = adresse;
        this.ville = ville;
        this.tel = tel;
        this.isVerified = isVerified;
        this.type = type;
        this.medecin = medecin;
        this.laboratoire = laboratoire;
    }
    public User(Integer id, String email, String adresse, String ville, int tel) {
        this.id = id;
        this.email = email;
        this.adresse = adresse;
        this.ville = ville;
        this.tel = tel;
    }

    public User(String email, String adresse, String ville, int tel) {
        this.email = email;
        this.adresse = adresse;
        this.ville = ville;
        this.tel = tel;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /*public String hashPassword() {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2i);
        this.password = argon2.hash(4, 65536, 1, this.password);
        return this.password;
    }*/

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Serializable getRoles() {
        return this.roles;
    }

    public void setRoles(Serializable roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return this.ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getTel() {
        return this.tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public boolean isIsVerified() {
        return this.isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Set getResetPasswordRequests() {
        return this.resetPasswordRequests;
    }

    public void setResetPasswordRequests(Set resetPasswordRequests) {
        this.resetPasswordRequests = resetPasswordRequests;
    }

    public Laboratoire getLaboratoire() {
        return this.laboratoire;
    }

    public void setLaboratoire(Laboratoire laboratoire) {
        this.laboratoire = laboratoire;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", roles=" + roles + ", password=" + password + ", adresse=" + adresse + ", ville=" + ville + ", tel=" + tel + ", isVerified=" + isVerified + ", type=" + type + ", medecin=" + medecin + ", patient=" + patient + ", resetPasswordRequests=" + resetPasswordRequests + ", laboratoire=" + laboratoire + '}';
    }

}
