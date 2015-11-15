/*
 * $Id: SBMLModel.java 89 2011-06-14 15:25:10Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/models/SBMLModel.java $
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

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbfc.converter.exceptions.ReadModelError;
import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;


/**
 * Class creating the link between GeneralModel and the SBML definition in JSBML
 * 
 * @author jpettit
 * @author rodrigue
 *
 */
public class SBMLModel implements GeneralModel {

	private SBMLDocument document;
	private String fileName;
	
	/**
	 * Class constructor
	 */
	public SBMLModel() {
		super();
	}


	public Model getModel() {
		if (document != null) {
			return document.getModel();
		}
		
		return null;
	}

	public SBMLDocument getSBMLDocument() {
		if (document != null) {
			return document;
		}
		
		return null;
	}
	
	public void setModelFromFile(String fileName) {
		try {
			this.document = modelFromFile(fileName);
		} catch (ReadModelError e) {
			e.printStackTrace();
		}
	}
	

	public void setModelFromString(String modelString) {
		try {
			this.document = modelFromString(modelString);
		} catch (ReadModelError e) {
			e.printStackTrace();
		}
		
	}


	public String getFileType() {
		return ".xml";
	}


	public void modelToFile(String fileName) {

		try {
			JSBML.writeSBML(document, fileName);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SBMLException e) {
			e.printStackTrace();
		}
	}


	public SBMLDocument modelFromFile(String fileName) throws ReadModelError {
		
		try {
			document = JSBML.readSBML(fileName);
			this.fileName = fileName;
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}


	public SBMLDocument modelFromString(String modelString) throws ReadModelError{
		
		try {
			document = JSBML.readSBMLFromString(modelString);
			fileName = null;
		} catch (XMLStreamException e) {
			throw new ReadModelError();
		}
		return document;
	}


	public String modelToString() {
		String reString =null;
		
		try {
			reString = JSBML.writeSBMLToString(document);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (SBMLException e) {
			e.printStackTrace();
		}

		return reString;
	}


	/**
	 * Returns the file name that was used to set this {@link GeneralModel} if
	 * it was set using the {@link #setModelFromFile(String)} method, null otherwise.
	 * 
	 * @return the file name that was used to set this {@link GeneralModel} if
	 * it was set using the {@link #setModelFromFile(String)} method, null otherwise.
	 */
	public String getModelFileName() {
		return fileName;
	}

	
	
	
	
	

}
