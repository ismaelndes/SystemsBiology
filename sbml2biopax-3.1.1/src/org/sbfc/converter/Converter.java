/*
 * $Id: Converter.java 216 2012-03-30 16:08:11Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/Converter.java $
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.sbfc.converter.models.GeneralModel;


public class Converter {

	public Converter() {
		super();
	}

	public static void convertFromFile(String inputModelType, String converterType, String inputFileName) {

//		String retModel = null;
		//Let's instantiate the proper Converter
		/*
		Class[] convertTypes;
		Class classConvert=null;*/
		
		String convertPackage = "org.sbfc.converter";
		
		Map<String, String> converterOptions = new HashMap<String, String>();
		converterOptions.put("save.result", "yes");
		
		// TODO : Search for the class in the CLASSPATH or define the full class name in an XML configuration file
		// For the moment, I am doing a hack to get the good package
		
		if (converterType.contains("XPP")) {
			convertPackage += ".sbml2xpp";
		} else if (converterType.contains("APM")) {
			convertPackage += ".sbml2apm";
		} else if (converterType.contains("Octave")) {
			convertPackage += ".sbml2octave";
		} else if (converterType.contains("BioPAX")) {
			convertPackage += ".sbml2biopax";
		} else if (converterType.contains("SbgnML")) {
			convertPackage += ".sbml2sbgnml";			
		} else if (converterType.contains("Dot")) {
			convertPackage += ".sbml2dot";
			converterOptions.put("export", "png svg");
		} else if (converterType.contains("SBML2SBML")) {
			convertPackage += ".sbml2sbml";
			
			// setting the options
			if (converterType.length() > 10) {
				String levelAndVersion = converterType.substring(10);
				
				System.out.println("Level and version = " + levelAndVersion);
				
				if (levelAndVersion.equals("L3V1")) {
					converterOptions.put("sbml.target.level", "3");
					converterOptions.put("sbml.target.version", "1");
				} else if (levelAndVersion.equals("L2V4")) {
					converterOptions.put("sbml.target.level", "2");
					converterOptions.put("sbml.target.version", "4");
				} else if (levelAndVersion.equals("L2V1")) {
					converterOptions.put("sbml.target.level", "2");
					converterOptions.put("sbml.target.version", "1");
				} else if (levelAndVersion.equals("L1V2")) {
					converterOptions.put("sbml.target.level", "1");
					converterOptions.put("sbml.target.version", "2");
				}  

			}

			converterType = "SBML2SBML";
		} // end hack
		
		
		//Instantiating the converter
		GeneralConverter converter=null;
		try {
			converter = (GeneralConverter) Class.forName(convertPackage+"."+converterType).newInstance();
			converter.setOptions(converterOptions);
		} catch (Exception e1) {
			//Creating an error report
			int pos = inputFileName.lastIndexOf(".");
			FileOutputStream fconv;
			try {
				// We can not have the result file extension at this point because
				//There's been an error while instantiating the converter itself... 
				//We create a file InputModel.errorLog
				fconv = new FileOutputStream (inputFileName.substring(0,pos)+".errorLog");
				PrintStream convprint = new PrintStream(fconv);
				convprint.println ("######################################################\n" +
						"The converter "+converterType+" you asked for can not be found...\n");
				e1.printStackTrace(convprint);
				fconv.close();
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			};
		}
		try {
			/*//Let's get all available Classes in models
			ClassDigger digger = new ClassDigger();
			convertTypes = digger.digPackage(convertPackage);

			//Choosing the right Model to instantiate
			for(Class convertClass: convertTypes) {
				if(convertClass.getName().endsWith(converterType)) {
					classConvert = convertClass;
				}
			}
			Class[] modelTypes;
			Class inputClassModel=null;


			//Let's get all available Classes in models
			modelTypes = digger.digPackage(modelPackage);


			//Choosing the right Model to instantiate
			for(Class modelClass: modelTypes) {
				if(modelClass.getName().compareTo(modelPackage+"."+inputModelType) == 0) {
					inputClassModel = modelClass;
				}
			}*/

			String modelPackage = "org.sbfc.converter.models";
			//Instantiating the inputModel
			GeneralModel inputModel = (GeneralModel) Class.forName(modelPackage+"."+inputModelType).newInstance();
			inputModel.setModelFromFile(inputFileName);
			//Converting the Model
			GeneralModel result = converter.convert(inputModel);

			if (converterOptions.get("save.result").equals("yes")) {
				//Creating the OutputFile
				int pos = inputFileName.lastIndexOf(".");
				result.modelToFile(inputFileName.substring(0,pos) + converter.getResultExtension());
			}
			
		} catch (Exception e) {
			//Replacing the result file by an error report
			int pos = inputFileName.lastIndexOf(".");
			FileOutputStream fconv;
			try {
								
				fconv = new FileOutputStream (inputFileName.substring(0,pos)+converter.getResultExtension());
				PrintStream convprint = new PrintStream(fconv);
				convprint.println ("######################################################\n" +
						"#Something went wrong during the conversion !\n" +
						"#Try to validate your input file before converting it\n\n");
				e.printStackTrace(convprint);
				fconv.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e.printStackTrace();
			};
		}
	}



	public static String convertFromString(String inputModelType, String converterType, String modelString) {

		String retModel = null;
		//Let's instantiate the proper Converter
		/*
		Class[] convertTypes;
		Class classConvert=null;*/
		try {
			/*//Let's get all available Classes in models
			ClassDigger digger = new ClassDigger();
			convertTypes = digger.digPackage(convertPackage);

			//Choosing the right Model to instantiate
			for(Class convertClass: convertTypes) {
				if(convertClass.getName().endsWith(converterType)) {
					classConvert = convertClass;
				}
			}*/

			String convertPackage = "org.sbfc.converter";

			// TODO : Search for the class in the CLASSPATH or define the full classpath in an XML configuration file
			// For the moment, I am doing a hack to get the good package
			
			if (converterType.contains("XPP")) {
				convertPackage += ".sbml2xpp";
			} else if (converterType.contains("APM")) {
				convertPackage += ".sbml2APM";
			} else if (converterType.contains("Octave")) {
				convertPackage += ".sbml2octave";
			} else if (converterType.contains("BioPax")) {
				convertPackage += ".sbml2biopax";
			} // end hack
			
			// TODO : add a method to get the converter object so that we can reuse it
			
			//Instantiating the converter
			GeneralConverter converter = (GeneralConverter) Class.forName(convertPackage+"."+converterType).newInstance();


			/*
			Class[] modelTypes;
			Class inputClassModel=null;


			//Let's get all available Classes in models
			modelTypes = digger.digPackage(modelPackage);


			//Choosing the right Model to instantiate
			for(Class modelClass: modelTypes) {
				if(modelClass.getName().compareTo(modelPackage+"."+inputModelType) == 0) {
					inputClassModel = modelClass;
				}
			}*/

			String modelPackage = "org.sbfc.converter.models";
			//Instantiating the inputModel
			GeneralModel inputModel = (GeneralModel) Class.forName(modelPackage+"."+inputModelType).newInstance();
			inputModel.setModelFromString(modelString);
			//Converting the Model
			GeneralModel result = converter.convert(inputModel);

			retModel = result.modelToString();

		}catch (Exception e) {
			e.printStackTrace();
		}
		return retModel;
	}

	public static void main(String args[]) {
		if(args.length < 3) {
			// TODO : Allow to do several conversion at the same time ?
			
			System.out.println("Wrong number of arguments :\n"+
					"Usage: Converter.java [InputModelClass] [ConverterClass] [ModelFile]");
			
		}
		else {
			convertFromFile(args[0], args[1], args[2]);
			System.exit(0);
		}
	}

}
