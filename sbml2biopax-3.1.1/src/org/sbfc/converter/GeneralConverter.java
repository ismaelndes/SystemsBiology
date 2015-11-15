/*
 * $Id: GeneralConverter.java 65 2011-06-01 09:22:46Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/GeneralConverter.java $
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

package org.sbfc.converter;

import java.util.Map;

import org.sbfc.converter.models.GeneralModel;


/**
 * Interface GeneralConverter
 * 
 * All converters must implement this Interface
 * 
 * @author jpettit
 * @author rodrigue
 *
 */
public abstract class GeneralConverter {
	
	protected Map<String, String> options;

	/**
	 * Method that actually converts a GeneralModel into another
	 * @param model
	 * @return
	 */
	public abstract GeneralModel convert(GeneralModel model);
	
	/**
	 * Return the extension of the Result file
	 * @return String
	 */
	public abstract String getResultExtension();
	
	/**
	 * Sets the options of the converter
	 * 
	 * @param options
	 */
	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

} 