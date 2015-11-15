package systemsbiology.BioPax_SIF;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.biopax.paxtools.io.BioPAXIOHandler;
import org.biopax.paxtools.io.SimpleIOHandler;
import org.biopax.paxtools.model.BioPAXLevel;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.io.sif.InteractionRule;
import org.biopax.paxtools.io.sif.SimpleInteractionConverter;


/**
 * @author Fernando Moreno Jabato
 * @copyleft all bugs reserved.  
 */
public class BioPax_SIF {
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		// Fast configure of log4j
		org.apache.log4j.BasicConfigurator.configure();
		
		// Take example file
		String BioPax = "./Files/BioPaxL2.owl";
		String SIF = "./Files/BioP_Sif.sif";
		File BioPFile = new File(BioPax);
		File Sif = new File(SIF);
		try{
			InputStream in = new FileInputStream(BioPFile);
			OutputStream out = new FileOutputStream(Sif);
		
			// Convert to SIF
			BioPAXIOHandler handler = new SimpleIOHandler(); // auto-detects Level
			Model model = handler.convertFromOWL(in);
			
			// Config converter
			SimpleInteractionConverter sic = null;

			if (BioPAXLevel.L2.equals(model.getLevel())) {
				sic = new SimpleInteractionConverter(
					new org.biopax.paxtools.io.sif.level2.ComponentRule(),
					new org.biopax.paxtools.io.sif.level2.ConsecutiveCatalysisRule(), 
					new org.biopax.paxtools.io.sif.level2.ControlRule(),
					new org.biopax.paxtools.io.sif.level2.ControlsTogetherRule(), 
					new org.biopax.paxtools.io.sif.level2.ParticipatesRule());
			} else if (BioPAXLevel.L3.equals(model.getLevel())) {
				sic = new SimpleInteractionConverter(
						new org.biopax.paxtools.io.sif.level3.ComponentRule(),
						new org.biopax.paxtools.io.sif.level3.ConsecutiveCatalysisRule(),
						new org.biopax.paxtools.io.sif.level3.ControlRule(),
						new org.biopax.paxtools.io.sif.level3.ControlsTogetherRule(),
						new org.biopax.paxtools.io.sif.level3.ParticipatesRule());
			} else {
				System.err.println("SIF converter does not yet support BioPAX level: " 
						+ model.getLevel());
				System.exit(0);
			}
	
			// Write SIF
			sic.writeInteractionsInSIF(model, out);
			
		}catch(IOException ioe){
			System.err.println("Couldn't open the BioPax file.");
		}
		
	}
}
