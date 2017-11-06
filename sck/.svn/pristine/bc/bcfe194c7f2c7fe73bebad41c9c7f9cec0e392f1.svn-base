package ldap;


import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.jfree.util.Log;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;
public class ValidacionLDAP {
        private String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";
        private String MY_HOST = "ldap://10.1.19.42:389/";
        public ValidacionLDAP() {}
        public boolean Authenticate(String domain, String user, String pass) {           
                Hashtable<String, String> env = new Hashtable<String, String>();
                
                if (pass.compareTo("") == 0 || user.compareTo("") == 0)
                        return false;
                        
                env.put(Context.INITIAL_CONTEXT_FACTORY,INITCTX);
                env.put(Context.PROVIDER_URL, MY_HOST);
                env.put(Context.SECURITY_AUTHENTICATION, "simple");
                env.put(Context.SECURITY_PRINCIPAL,new String(domain+"\\"+user));
                env.put(Context.SECURITY_CREDENTIALS,new String(pass));
                try {
                        DirContext ctx = new InitialDirContext(env);
                        return true;
                } 
                catch (NamingException e) {
                	System.out.println("no !!!!!!!!!!Existe");
                        return false;
                }
                   
        }
        
        

}