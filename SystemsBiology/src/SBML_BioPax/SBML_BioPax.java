package SBML_BioPax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.biopax.paxtools.io.SimpleIOHandler;
import org.biopax.paxtools.model.BioPAXLevel;
import org.biopax.paxtools.model.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import SBML_BioPax.ArmishBitBucket.SBML2BioPAXConverter;

public class SBML_BioPax {
	public static void main(String[] args){
		String sbmlFile = "./Files/SBML.xml";
		String bpFile = "./Files/SBML_BioPax.owl";
		try{
			// Load SBML
			SBMLDocument sbmlDocument = SBMLReader.read(new File(sbmlFile));
			//Convert to BioPax
			SBML2BioPAXConverter sbml2BioPAXConverter = new SBML2BioPAXConverter();
	        Model bpModel = sbml2BioPAXConverter.convert(sbmlDocument);
	        
	        SimpleIOHandler bpHandler = new SimpleIOHandler(BioPAXLevel.L3);
	        bpHandler.convertToOWL(bpModel, new FileOutputStream(bpFile));
			
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(XMLStreamException xse){
			xse.printStackTrace();
		}
	}
}
