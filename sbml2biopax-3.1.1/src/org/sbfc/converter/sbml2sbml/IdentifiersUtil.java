package org.sbfc.converter.sbml2sbml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.xml.stax.SBMLReader;
import org.sbml.jsbml.xml.stax.SBMLWriter;

import uk.ac.ebi.miriam.db.MiriamLocalProvider;

/**
 * This class will provide utility methods to transform all the annotations in am SBML model to use either only identifiers.org URL
 * or miriam URN.
 * 
 * @author rodrigue
 *
 */
public class IdentifiersUtil {

	static private String urnPrefix = "urn:miriam";
	static private String urlPrefix = "http://identifiers.org";
	
	static private MiriamLocalProvider link; 
	
	static {
		// Creation of the link to the Web Services
        // link = new MiriamLink();
        link = new MiriamLocalProvider();
		
        // Sets the address to access the Web Services
        // link.setAddress("http://www.ebi.ac.uk/miriamws/main/MiriamWebServices");

	}
	
	@SuppressWarnings("unchecked")
	static private void sBaseAnnotationsUpdate(SBase sbase, String annotationPrefix) {

		if (sbase.getCVTermCount() > 0) {
			
			boolean urn2url = false;
			boolean url2urn = false;
			boolean update = false;
			
			if (annotationPrefix == null) {
				update = true;
			} else if (annotationPrefix.equals(urlPrefix)) {
				url2urn = true;
			} else if (annotationPrefix.equals(urnPrefix)) {
				urn2url = true;
			}
			
			for (CVTerm cvTerm : sbase.getCVTerms()) {
				
				List<String> uris = cvTerm.getResources();
				List<String> updatedUris = new ArrayList<String>();
				boolean updated = false;
				
				for (String annotationString : uris) {
					String newURI = annotationString;
					
					if ((url2urn || urn2url) && (!annotationString.startsWith(annotationPrefix))) {

						// using Miriam web service to transform it !?
						if (url2urn) {
							newURI = link.convertURN(annotationString);
						} else if (urn2url) {
							// TODO : add a test to convertURL from identifiers.org when the method is available in the webService
							// newURI = link.convertURL(annotationString);
							continue;
						} 
					} else if (update) {
						
						newURI = link.getMiriamURI(annotationString);
					}

					if (newURI == null) {
						System.out.println("Error : the uri '" + annotationString + "' is not recognized by miriamws !!");
						newURI = annotationString;
					} else if (! newURI.equals(annotationString)) {
						updated = true;
					}
					updatedUris.add(newURI);
				}

				if (updated) {
					uris.clear();
					uris.addAll(updatedUris);
				}
			}
		}
		
		// Going through all children element, including L3 packages elements
		if (sbase.getChildCount() > 0) {

			for (Object treeNode : Collections.list(sbase.children())) {
				
				if (treeNode instanceof SBase) {
					sBaseAnnotationsUpdate((SBase) treeNode, annotationPrefix);					
				}
			}
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	static public SBMLDocument urnToUrl(SBMLDocument doc){
		return documentAnnotationsUpdate(doc, urlPrefix);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	static public SBMLDocument urlToUrn(SBMLDocument doc){
		return documentAnnotationsUpdate(doc, urnPrefix);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	static public SBMLDocument updateAnnotations(SBMLDocument doc){
		return documentAnnotationsUpdate(doc, null);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	static private SBMLDocument documentAnnotationsUpdate(SBMLDocument doc, String annotationPrefix){

		sBaseAnnotationsUpdate(doc, annotationPrefix);

		Model model = doc.getModel();
		sBaseAnnotationsUpdate(model, annotationPrefix);

		return doc;
	}

	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out.println(
			  "Usage : java org.sbml.jsbml.xml.stax.SBMLWriter [-m|-i|-u] sbmlFileName [suffix]");
			System.out.println("\n\t\tThe order of the options is important.");
			System.out.println("\n\t\t-m will update the given sbml file to use miriam urn-uris");
			System.out.println("\n\t\t-i will update the given sbml file to use miriam url-uris (identifiers.org urls)");
			System.out.println("\n\t\t-u will update the given sbml file to the correct and up-to-date miriam urn-uris");

			System.exit(1);
		}

		long init = Calendar.getInstance().getTimeInMillis();
		System.out.println(Calendar.getInstance().getTime());
		
		String annoPrefixOption = args[0];
		String annotationPrefix = urlPrefix;
		String fileName = args[1];
		String fileNameSuffix = "-identifiers.org"; 
		
		if (annoPrefixOption.equals("-m")) {
			fileNameSuffix = "-miriam-urn";
			annotationPrefix = urnPrefix;
		} else if (annoPrefixOption.equals("-u")) {
			fileNameSuffix = "-updated-annotations";
			annotationPrefix = null;			
		}
		
		if (args.length >= 3) {
			fileNameSuffix = args[2];
		}

		String jsbmlWriteFileName = fileName.replaceFirst(".xml", fileNameSuffix + ".xml");
		
		System.out.printf("Reading %s and writing %s\n", 
		  fileName, jsbmlWriteFileName);

		SBMLDocument testDocument;
		long afterRead = 0;
		long afterAnnoUpdate = 0;
		try {
			testDocument = new SBMLReader().readSBMLFile(fileName);
			System.out.printf("Reading done\n");
			System.out.println(Calendar.getInstance().getTime());
			afterRead = Calendar.getInstance().getTimeInMillis();
			
			documentAnnotationsUpdate(testDocument, annotationPrefix);
			afterAnnoUpdate = Calendar.getInstance().getTimeInMillis();
			
			System.out.printf("Starting writing\n");
			
			new SBMLWriter().write(testDocument, jsbmlWriteFileName);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(Calendar.getInstance().getTime());
		long end = Calendar.getInstance().getTimeInMillis();
		long nbSecondes = (end - init)/1000;
		long nbSecondesRead = (afterRead - init)/1000;
		long nbSecondesAnnoUpdate = (afterAnnoUpdate - afterRead)/1000;
		long nbSecondesWrite = (end - afterAnnoUpdate)/1000;
		
		if (nbSecondes > 120) {
			System.out.println("It took " + nbSecondes/60 + " minutes.");
		} else {
			System.out.println("It took " + nbSecondes + " secondes.");			
		}
		System.out.println("Reading : " + nbSecondesRead + " secondes.");
		System.out.println("Writing : " + nbSecondesWrite + " secondes, AnnoUpdate : " + nbSecondesAnnoUpdate + " secondes.");

	}
	
	
	/**
	 * 
	 * @return
	 */
	public static String anyStringAnnotationsUpdate(String anyString) {
		
		// TODO : matches the annotations to change
		
		return anyString;
	}
}
