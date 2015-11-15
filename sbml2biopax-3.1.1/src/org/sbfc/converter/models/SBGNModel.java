package org.sbfc.converter.models;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.sbfc.converter.exceptions.ReadModelError;
import org.sbfc.converter.sbml2sbgnml.SbmlToSbgnML;
import org.sbgn.SbgnUtil;
import org.sbgn.bindings.Sbgn;
import org.sbml.jsbml.SBMLDocument;

/**
 * 
 * @author jalowcki
 *
 */
public class SBGNModel implements GeneralModel{
	
	private Sbgn sbgnModel;
	private String fileName;
	
	public SBGNModel(){
		super();
	}
	
	public SBGNModel(Sbgn model) {
		this.sbgnModel = model;
	}
	

	@Override
	/**
	 * The extension is .sbgn
	 */
	public String getFileType() {
		
		return ".sbgn";
	}

	@Override
	public void modelToFile(String fileName) {
		File f = new File(fileName);
		try {
			SbgnUtil.writeToFile(sbgnModel, f);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String modelToString() {
		// TODO Is that necessary to do this function?
		return null;
	}

	@Override
	public void setModelFromFile(String fileName) {
		try {
			this.sbgnModel = modelFromFile(fileName);
		} catch (ReadModelError e) {
			e.printStackTrace();
		}
	}


	@Override
	public void setModelFromString(String modelString) {
		try {
			this.sbgnModel = modelFromString(modelString);
		} catch (ReadModelError e) {
			e.printStackTrace();
		}		
	}

	
	private Sbgn modelFromString(String modelString) throws ReadModelError{
		// TODO Auto-generated method stub

		// Is that a possible option? Compulsory?
		
		return null;
	}


	/**
	 * Return a {@link Sbgn} model from a filename
	 * @param fileName2
	 * @return
	 */
	private Sbgn modelFromFile(String fileName2) throws ReadModelError{
		// we have to load a SBML model from the file...
		SBMLModel model = new SBMLModel();
		
		SBMLDocument sbmlDoc = model.modelFromFile(fileName2);

		// ... and then call a Sbgn object.
		SbmlToSbgnML converter = new SbmlToSbgnML();
		
		Sbgn sbgnModel = converter.convertSBGNML(sbmlDoc);
		
		return sbgnModel;
	}
	
}
