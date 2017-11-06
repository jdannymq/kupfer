package ldap;

import com.novell.ldap.*;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* @author christian
* Clase para Buscar Usuarios el Filtro es el usuario que se desea buscar.
*/

public class Buscar {

     private String searchBase = "o=utpl,c=ec";
     private int searchScope = LDAPConnection.SCOPE_SUB;
     private String filtro;
     private LDAPSearchResults searchResults;

     /**
     * Metodo para buscar un usuario dentro del servidor LDAP
     * @param LDAPConnection lc
     * @param String strFiltro
     */

     public Buscar(LDAPConnection lc, String strFiltro) {
          filtro = "(uid="+ strFiltro + ")";
          try {
               searchResults = lc.search(searchBase, searchScope, filtro, null, false);
               //Recorre Todos los Usuarios de la Base
               while (searchResults.hasMore()) {
                    LDAPEntry nextEntry = null;
                    try {
                         nextEntry = searchResults.next();
                    } catch (LDAPException e) {
                         System.out.println("Error: " + e.toString());
                         continue;
                    }
                    LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
                    Iterator allAttributes = attributeSet.iterator();
                    //Recore los atributos del usuario
                    while (allAttributes.hasNext()) {
                         LDAPAttribute attribute = (LDAPAttribute) allAttributes.next();
                         String attributeName = attribute.getName();
                         Enumeration allValues = attribute.getStringValues();
                         if (allValues != null) {
                              while (allValues.hasMoreElements()) {
                                   String value = (String) allValues.nextElement();
                                   System.out.println(attributeName + ":  " + value);
                              }
                         }
                    }
                    System.out.println("------------------------------");
                    lc.disconnect();
               }
          } catch (LDAPException ex) {
               Logger.getLogger(Buscar.class.getName()).log(Level.SEVERE,null, ex);
          }
     }
}

