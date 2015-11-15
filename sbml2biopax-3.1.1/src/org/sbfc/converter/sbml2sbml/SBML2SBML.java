package org.sbfc.converter.sbml2sbml;

import org.sbfc.converter.GeneralConverter;
import org.sbfc.converter.exceptions.ReadModelError;
import org.sbfc.converter.models.GeneralModel;
import org.sbfc.converter.models.SBMLModel;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.libsbml.SBMLReader;
import org.sbml.libsbml.SBMLWriter;


/**
 * Converts an SBML file into an other SBML level and version.
 * 
 * The converter name that you pass to SBFC as to be SBML2SBML_LxVx where LxVx is the targeted
 * SBML level and version.  
 * 
 * @author rodrigue
 * @version 1.0
 *
 */
public class SBML2SBML extends GeneralConverter {

	int targetLevel = -1;
	int targetVersion = -1;

    private static boolean isLibSBMLAvailable = false;

	static {
		try {
            System.loadLibrary("sbmlj");

            Class.forName("org.sbml.libsbml.libsbml");
        
            isLibSBMLAvailable = true;

		} catch (SecurityException e) {
        	e.printStackTrace();

            System.out
                    .println("SecurityException exception catched : Could not load libsbml library.");
        } catch (UnsatisfiedLinkError e) {
        	e.printStackTrace();
            System.out
                    .println("UnsatisfiedLinkError exception catched : Could not load libsbml library.");
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();

            System.out
                    .println("ClassNotFoundException exception catched : Could not load libsbml class file.");

        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out
                    .println("Could not load libsbml.\n "
                            + "Control that the libsbmlj.jar that you are using is synchronized with your current libSBML installation.");

        }
	}

	@Override
	public GeneralModel convert(GeneralModel model) {
		
		if (! (model instanceof SBMLModel)) {
			return null;
		}
		SBMLModel sbmlModel = (SBMLModel) model;
		
		SBMLDocument sbmlDocument = sbmlModel.getSBMLDocument();
		
		int currentLevel = sbmlDocument.getLevel();
		int currentVersion = sbmlDocument.getVersion();
		
		try {
			targetLevel = Integer.parseInt(options.get("sbml.target.level"));
			targetVersion = Integer.parseInt(options.get("sbml.target.version"));
			
		} catch (NumberFormatException e) {
			// return as we are not able to get the target level and version
			System.out.println("SBML2SBML : cannot read the target level and version : " + e.getMessage());
			return null;
		}
		
		if (targetLevel != currentLevel || targetVersion != currentVersion) {

			String currentSBML = sbmlModel.modelToString();
			
			if (isLibSBMLAvailable) {
				// Code using libSBML directly
				org.sbml.libsbml.SBMLReader libSBMLReader = new SBMLReader();
				
				org.sbml.libsbml.SBMLDocument libSBMLdoc = libSBMLReader.readSBMLFromString(currentSBML);
				
				System.out.println("SBML2SBML : trying to convert to SBML level " + targetLevel + " version " + targetVersion);
				
				System.out.println("SBML2SBML : L1V2 compatibility = " + libSBMLdoc.checkL1Compatibility());
				
				boolean isSetLVSuccesfull = libSBMLdoc.setLevelAndVersion(targetLevel, targetVersion);
				
				// libsbml.LIBSBML_OPERATION_SUCCESS
				
				System.out.println("SBML2SBML : setLevelAndVersion worked = " + isSetLVSuccesfull);				
				
				// TODO : if setLevelAndVersion returned false, the conversion is not possible
				// and we need to return the list of errors found by libSBML
				
				org.sbml.libsbml.SBMLWriter libSBMLWriter = new SBMLWriter();

				String targetSBML = libSBMLWriter.writeSBMLToString(libSBMLdoc);

				System.out.println("SBML2SBML : converted model : \n" + targetSBML.substring(0, 150));
				
				SBMLModel targetModel = new SBMLModel();
				try {
					targetModel.modelFromString(targetSBML);

					return targetModel;

				} catch (ReadModelError e) {
					e.printStackTrace();
					return null;
				}
			}
			
			// if libsbml is not available or there has been an exception
			return null;
			
			/*
			// Code with the libSBML WS
			LibSBMLServiceLocator service = new LibSBMLServiceLocator();

			try {

				LibSBML client = service.getLibSBML();
			
				String targetSBML = client.convertSBML(currentSBML, targetLevel, targetVersion);
				
				SBMLModel targetModel = new SBMLModel();
				targetModel.modelFromString(targetSBML);
				
				return targetModel;
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			} catch (ServiceException e) {
				e.printStackTrace();
				return null;
			} catch (ReadModelError e) {
				e.printStackTrace();
				return null;
			}
			*/
		}
		
		// we are here because the targeted level and version are the same as the original model so we return it
		return model;
	}

	@Override
	public String getResultExtension() {
		return "-L" + targetLevel + "V" + targetVersion + ".xml";
	}

}
