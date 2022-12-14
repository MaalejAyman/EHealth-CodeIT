package com.codeit.myapp.entities;
// Generated 14 avr. 2021 00:12:42 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Service generated by hbm2java
 */
public class Service implements java.io.Serializable {

    private Integer id;
    private String nomService;
    private Set laboratoires = new HashSet(0);

    public Service() {
    }

    public Service(int id, String nomService) {
        this.id = id;
        this.nomService = nomService;
    }

    public Service(String nomService) {
        this.nomService = nomService;
    }

    public Service(String nomService, Set laboratoires) {
        this.nomService = nomService;
        this.laboratoires = laboratoires;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomService() {
        return this.nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public Set getLaboratoires() {
        return this.laboratoires;
    }

    public void setLaboratoires(Set laboratoires) {
        this.laboratoires = laboratoires;
    }

    @Override
    public String toString() {
        return nomService ;
    }

}
