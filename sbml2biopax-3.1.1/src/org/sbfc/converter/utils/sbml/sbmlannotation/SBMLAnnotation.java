/*
 * $Id: SBMLAnnotation.java 21 2011-05-09 09:24:07Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/utils/sbml/sbmlannotation/SBMLAnnotation.java $
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

package org.sbfc.converter.utils.sbml.sbmlannotation;

import com.hp.hpl.jena.ontology.Individual;

/**
 * Stores one annotation. 
 * 
 * @author Arnaud Henry, Nicolas Rodriguez
 *
 */
public class SBMLAnnotation {
	
	private Individual xref;
	private String annotationType; //dc, rdf, dcterms, vcard, bqbio
	private String relationType;
	private String db;
	private String uri;
	
	//dc:
	// isVersionOf
	// relation
	// hasPart
	
	//bqmodel:
	// is -> BioModels
	// isDescribedBy -> pubMed
	
	//bqbiol:
	// is -> unificationXref
	// hasPart -> complex
	// isPart -> molecule of a complex
	// isVersionOf -> relationshipXref
	// hasVersion -> relationshipXref ?what the difference with isVersionOf?  SBMLressource <-version-> Reference
	// isHomologTo -> relationshipXref
	
	//Constructors
	
	/**
	 * Creates a new <code>SBMLAnnotation</code>.
	 * 
	 * @param a the annotation type.
	 * @param r the relation type.
	 * @param u the uri.
	 */
	public SBMLAnnotation(String a, String r, String u){
		annotationType = a;
		relationType   = r;
		uri            = u;
	}
	
	/**
	 * Creates a new <code>SBMLAnnotation</code>.
	 * 
	 * @param a the annotation type.
	 * @param r the relation type.
	 * @param u the uri.
	 * @param d the database name.
	 */
	public SBMLAnnotation(String a, String r, String u, String d){
		annotationType = a;
		relationType   = r;
		uri            = u;
		db             = d;
	}
	
	//Individual
	
	/**
	 * Sets the individual.
	 * 
	 * @param ind the individual.
	 */
	public void setIndividual(Individual ind){
		xref = ind;
	}
	
	/**
	 * Returns the individual.
	 * 
	 * @return the individual.
	 */
	public Individual getIndividual(){
		return xref;
	}
	
	//Annotation type
	/**
	 * Sets the annotation type.
	 * 
	 * @param atype
	 */
	public void setAnnotationType(String atype) {
		annotationType = new String(atype);
	}

	/**
	 * Returns the annotation type.
	 * 
	 * @return the annotation type.
	 */
	public String getAnnotationType() {
		return annotationType;
	}

	/**
	 * Returns the relation type.
	 * 
	 * @return the relation type.
	 */
	public String getRelationType() {
		return relationType;
	}

	/**
	 * @return
	 */
	public String getURI() {
		return uri;
	}
	/**
	 * Returns the database name.
	 * 
	 * @return the db the database name.
	 */
	public String getDb() {
		return db;
	}
	
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
	
		buffer.append("\nType: " + annotationType + " " + relationType).append("\n");
		
		if (uri != null) {
			buffer.append("URI: " + uri).append("\n");
		}
		if (db != null) {
			buffer.append("database: " + db).append("\n");
		}
		
		if (xref != null) {
			buffer.append(xref).append("\n");
		}
		
		return buffer.toString();
	}
	
}


