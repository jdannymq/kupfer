package conexion;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import java.io.Serializable;
import java.util.Hashtable;

public class LoginController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";
    //private String MY_HOST = "ldap://kupfer.kupfer.cl:389/";
	private String MY_HOST = "LDAP://10.1.19.43/dc=kupfer,dc=kupfer,dc=cl";
    private String DOMAIN_KUPFER = "KUPFER";
    
    public LoginController(){
    	
    	
    }
    
  
    public Integer authenticateSession(String username, String password) {
    	 Integer resultadoLogin = 0;
    	try {
            
            if (!username.equals("adminstrador")) {
                resultadoLogin = authenticate(username, password);
                if (resultadoLogin != 1) {
                    System.out.println(" resultadoLogin : "+ resultadoLogin);
                    return resultadoLogin;
                }
            } else if (!password.equals("123456")) {
            	resultadoLogin = 4;
            	System.out.println("clave de administrador no corresponde.");
            	return resultadoLogin;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }catch (Throwable e) {
            e.printStackTrace();
        }
		/*
		 * Reload user as password of authentication principal will be null after authorization and
		 * password is needed for token generation
		 */
        return resultadoLogin;
    }


    private Integer authenticate(String user, String pass) {
    	
        Hashtable<String, String> env = new Hashtable<String, String>();

        if (pass.compareTo("") == 0 || user.compareTo("") == 0)
        	return 3;
        env.put(Context.INITIAL_CONTEXT_FACTORY,INITCTX);
        env.put(Context.PROVIDER_URL, MY_HOST);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL,new String(DOMAIN_KUPFER+"\\"+user));
        env.put(Context.SECURITY_CREDENTIALS,new String(pass));
        try {
            DirContext ctx = new InitialDirContext(env);
            return 1;
        }
        catch(CommunicationException e){
                	System.out.println("Conexion deshabilitada");
                	e.printStackTrace();
            return 2;
        }
        catch (NamingException e) {
                	System.out.println("Autentificacion invalida");
                	e.printStackTrace();
                	//return 1;
             return 3;
        }
    }


}
