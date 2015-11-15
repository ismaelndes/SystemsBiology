package systemsbiology.BioPax_SBGN;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.biopax.paxtools.io.BioPAXIOHandler;
import org.biopax.paxtools.io.SimpleIOHandler;
import org.biopax.paxtools.io.sbgn.L3ToSBGNPDConverter;
import org.biopax.paxtools.io.sif.SimpleInteractionConverter;
import org.biopax.paxtools.model.Model;


public class BioPax_SBGN {

	public static void main(String[] args) {
		// Fast configure of log4j
				org.apache.log4j.BasicConfigurator.configure();
				
				// Take example file
				String BioPax = "./Files/BioPaxL3.owl";
				String SBGN = "./Files/BioP_SBGN.sbgn";
				File BioPFile = new File(BioPax);
				File sbgn = new File(SBGN);
				try{
					InputStream in = new FileInputStream(BioPFile);
					OutputStream out = new FileOutputStream(sbgn);
				
					// Convert to SIF
					BioPAXIOHandler handler = new SimpleIOHandler();
					Model model = handler.convertFromOWL(in);
					
					// ONLY FOR L3 BioPax
					L3ToSBGNPDConverter conv = new L3ToSBGNPDConverter();
					conv.writeSBGN(model, out); // Not Working well
				}catch(IOException ioe){
					System.err.println("Couldn't open the BioPax file.");
				}

	}

}
