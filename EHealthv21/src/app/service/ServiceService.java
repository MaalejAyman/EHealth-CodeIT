/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author meria
 */
public class ServiceService {
    Connection cnx;
    PreparedStatement ste;
    
    public ServiceService() {
        cnx = Database.getInstance().getConn();
    }
    
    public Service getServiceById(int id) throws SQLException {
        Service ls = new Service();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM service where id="+id;
        ResultSet rs = st.executeQuery(req);
          if(rs.next()){
              ls = new Service(rs.getInt(1),rs.getString(2));
          }
        return ls;
   }
    public List<Service> getServicesByIdLab(int id) throws SQLException {
        List<Service> ls = new ArrayList<Service>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM laboratoire_service where laboratoire_id="+id;
        ResultSet rs = st.executeQuery(req);
          while(rs.next()){
              ls.add(this.getServiceById(rs.getInt("service_id")));
          }
        return ls;
   }
    
   public List<Service> getServices() throws SQLException {
        List<Service> ls = new ArrayList<Service>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Service";
        ResultSet rs = st.executeQuery(req);
          while(rs.next()){
              ls.add(new Service(rs.getInt(1),rs.getString(2)));
          }
        return ls;
   }
    
}
