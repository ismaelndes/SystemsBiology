/*
 * $Id: BioPaxModel.java 247 2012-08-22 15:35:00Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/models/BioPaxModel.java $
 *
 *
 * ==============================================================================
 * Copyright (c) 2010 the copyright is held jointly by the individual
 * authors. See the file AUTHORS for the list of authors
 *
 * This file is part of The System Biology Format Converter (SBFC).
 *
 * SBFC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SBFC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SBFC.  If not, see<http://www.gnu.org/licenses/>.
 * 
 * ==============================================================================
 * 
 */

package org.sbfc.converter.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.biopax.paxtools.io.SimpleIOHandler;
import org.biopax.paxtools.model.Model;
import org.sbfc.converter.exceptions.ReadModelError;


/**
 * Creates the link between GeneralModel and the BioPaxModel defined in PaxTools
 * 
 * @author jpettit
 *
 */
public class BioPaxModel implements GeneralModel {

	
	private Model model;

	public BioPaxModel(Model model) {
		super();
		this.model = model;
	}
	
	
	public Model getModel() {
		return model;
	}

	public void setModelFromFile(String fileName) {
		try {
			this.model = modelFromFile(fileName);
		} catch (ReadModelError e) {
			e.printStackTrace();
		}
	}
	
	public void setModelFromString(String modelString) {
		try {
			this.model = modelFromString(modelString);
		} catch (ReadModelError e) {
			e.printStackTrace();
		}
		
	}

	

	/* (non-Javadoc)
	 * Convert BioPaxModel into String
	 * @param model
	 * @see org.sbfc.converter.models.GeneralModel#modelToString(org.sbfc.converter.models.GeneralModel)
	 */
	public void modelToFile(String fileName) {	

		SimpleIOHandler export = new SimpleIOHandler(getModel().getLevel());

		try {
			export.convertToOWL(model,new FileOutputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}


	public String getFileType() {
		return ".owl";
	}


	public Model modelFromFile(String fileName) throws ReadModelError {
		// TODO
		return null;
	}


	public Model modelFromString(String modelString) throws ReadModelError {
		// TODO
		return null;
	}


	public String modelToString() 
	{
		String resModel=null;
		SimpleIOHandler export = new SimpleIOHandler(getModel().getLevel());
		
		try {
			File tempFile = File.createTempFile("BioPaxConvert", "temp");
			FileOutputStream out  = new FileOutputStream(tempFile);
			export.convertToOWL(model,out); 
			resModel = readFileAsString(tempFile.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resModel;
	}
	
	private static String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    FileInputStream f = new FileInputStream(filePath);
	    f.read(buffer);
	    
	    return new String(buffer);
	}




	
	
	

}
