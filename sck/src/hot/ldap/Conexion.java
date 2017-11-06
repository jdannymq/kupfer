package ldap;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author christian
 */
public class Conexion {

    private int ldapPort;
    private int ldapVersion;
    private LDAPConnection lc;
    private String login;
    private String ldapHost = "172.16.30.162";

    public Conexion() {
    }

    /**
     * Este metodo permite realizar la conexion al servidor de LDAP
     * Para el usuario manager
     * @param strManager
     * @param strPassword
     */
    public LDAPConnection ConexionManager(String strManager, String strPassword) {
        login = "cn=" + strManager + ",o=utpl,c=ec";
        System.out.println("" + login);
        ldapPort = LDAPConnection.DEFAULT_PORT;
        System.out.println("puerto: " + ldapPort);
        ldapVersion = LDAPConnection.LDAP_V3;
        System.out.println("Vesion: " + ldapVersion);
        System.out.println("HOST: " + ldapHost);
        try {
            lc = new LDAPConnection();
            lc.connect(ldapHost, ldapPort);
            System.out.println("====Conectado al Servidor LDAP====");
            lc.bind(ldapVersion, login, strPassword.getBytes("UTF8"));
            System.out.println("Autenticado en el servidor....");
        //LDAPSchema schema = lc.fetchSchema(getSchema = lc.getSchemaDN());
//            Enumeration enu = schema.getAttributeSchemas();
//            while (enu.hasMoreElements()) {
//                LDAPAttributeSchema attr = (LDAPAttributeSchema) enu.nextElement();
//                System.out.println(attr.getNames()[0]);
//                System.out.println(attr.getSyntaxString());
//            }
//            enu = schema.getObjectClassSchemas();
        //CerrarConLDAP();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LDAPException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }

    /**
     * Este metodo permite realizar la conexion al servidor de LDAP
     * Para el usuario manager
     * @param strUser
     * @param strPassword
     */
    public LDAPConnection ConexionUser(String strUser, String strPassword) {
        login = "uid=" + strUser + ",ou=People,o=utpl,c=ec";
        System.out.println("" + login);
        ldapPort = LDAPConnection.DEFAULT_PORT;
        System.out.println("puerto: " + ldapPort);
        ldapVersion = LDAPConnection.LDAP_V3;
        System.out.println("Vesion: " + ldapVersion);
        System.out.println("HOST: " + ldapHost);
        try {
            lc = new LDAPConnection();
            lc.connect(ldapHost, ldapPort);
            System.out.println("====Conectado al Servidor LDAP====");
            lc.bind(ldapVersion, login, strPassword.getBytes("UTF8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LDAPException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }

    public void CerrarConLDAP(LDAPConnection lc) {
        try {
            lc.disconnect();
            System.out.println("Conexion Cerrada Correctamente...");
        } catch (LDAPException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
