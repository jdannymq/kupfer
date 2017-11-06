package conexion;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;

import modelo.SapSystem;

/**
 *
 * @author aruizp
 */
public class Connect {
	static String SAP_SERVER = "SAP_SERVER";
	private JCoRepository repos; //
	private JCoDestination dest;

	public Connect(SapSystem system) {
		MyDestinationDataProvider myProvider = new MyDestinationDataProvider(system);
		//com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
                
                if (!com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered()) {
                    com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
		}
                
		try {
			dest = JCoDestinationManager.getDestination(SAP_SERVER);
			repos = dest.getRepository();
		} catch (JCoException e) {
			throw new RuntimeException(e);
		}

	}

	public JCoFunction getFunction(String functionStr) {
		JCoFunction function = null;
		try {
			function = repos.getFunction(functionStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Problema obteniendo el objeto JCO.Function.");
		}
		if (function == null) {
			throw new RuntimeException("No es posible recibir la funcion. ");
		}

		return function;
	}

	
	public void execute(JCoFunction function) {
		try {
			JCoContext.begin(dest);
			function.execute(dest);

		} catch (JCoException e) {
			e.printStackTrace();
		} finally {
			try {
				JCoContext.end(dest);
			} catch (JCoException e) {
				e.printStackTrace();
			}
		}
	}

}
