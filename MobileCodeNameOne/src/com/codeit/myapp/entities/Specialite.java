package com.codeit.myapp.entities;
// Generated 14 avr. 2021 00:12:42 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Specialite generated by hbm2java
 */
public class Specialite implements java.io.Serializable {

    private Integer id;
    private String nomSpec;
    private Set questions = new HashSet(0);
    private Set medecins = new HashSet(0);

    public Specialite() {
    }

    public Specialite(int id, String nomSpec) {
        this.id = id;
        this.nomSpec = nomSpec;
    }

    public Specialite(String nomSpec) {
        this.nomSpec = nomSpec;
    }

    public Specialite(String nomSpec, Set questions, Set medecins) {
        this.nomSpec = nomSpec;
        this.questions = questions;
        this.medecins = medecins;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomSpec() {
        return this.nomSpec;
    }

    public void setNomSpec(String nomSpec) {
        this.nomSpec = nomSpec;
    }

    public Set getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set questions) {
        this.questions = questions;
    }

    public Set getMedecins() {
        return this.medecins;
    }

    public void setMedecins(Set medecins) {
        this.medecins = medecins;
    }

    @Override
    public String toString() {
        return nomSpec ;
    }
    

}