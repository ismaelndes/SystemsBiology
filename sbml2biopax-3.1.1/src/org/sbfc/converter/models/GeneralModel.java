/*
 * $Id: GeneralModel.java 21 2011-05-09 09:24:07Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/models/GeneralModel.java $
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



/**
 * Interface that all Models need to implement in order to reach the standardized IO
 * 
 * @author jpettit
 *
 */
public interface GeneralModel {
	
	/**
	 * Sets the Model from a file on the file system.
	 * 
	 * @param fileName path to the file containing the model
	 */
	public void setModelFromFile(String fileName);
	
	/**
	 * Sets the model from a String
	 * 
	 * @param modelString Model
	 */
	public void setModelFromString(String modelString);
	
	
	/**
	 * Writes the Model in a new file
	 * 
	 * @param fileName path at which the new file will be created
	 */
	public void modelToFile(String fileName);
	
	/**
	 * Returns the Model as a String
	 * 
	 * @return Model
	 */
	public String modelToString();
	
	
	/**
	 * Returns the usual model file type extension (ex: .xml for SBML, .owl for BIOPAX)
	 * 
	 * @return file type extension
	 */
	public String getFileType();
	
	

}
