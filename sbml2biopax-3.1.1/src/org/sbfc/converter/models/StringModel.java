/*
 * $Id$
 * $URL$
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public abstract class StringModel implements GeneralModel {
	
	private String model;
	private String fileName;
	
	public void modelToFile(String fileName) {
		
		//Printing String to a file
		FileOutputStream file;
		try {
			file = new FileOutputStream (fileName);
			PrintStream printFile = new PrintStream(file);
			printFile.println (model);
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String modelToString() {
		return model;
	}
	
	public String getModel() {
		return model;
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
	
	public void setModelFromFile(String fileName) {
		try {
			//Reading file and putting it in a String
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String result="";
			String line;
			while ((line = in.readLine()) != null) {
				result+= line+"\n";
				in.close();
				this.model = result;
				
			}
			this.fileName = fileName;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setModelFromString(String modelString) {
		this.model = modelString;
		this.fileName = null;
	}

}
