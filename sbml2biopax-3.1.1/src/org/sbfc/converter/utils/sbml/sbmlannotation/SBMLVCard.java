/*
 * $Id: SBMLVCard.java 21 2011-05-09 09:24:07Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/utils/sbml/sbmlannotation/SBMLVCard.java $
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

/**
 * Stores a VCard.
 * 
 * @author Arnaud Henry, Nicolas Rodriguez
 *
 */
public class SBMLVCard {
	private String family;
	private String given;
	private String email;
	private String orgname;
	
	/**
	 * Creates an <code>SBMLVCard</code> with the given informations.
	 * 
	 * @param g the first name.
	 * @param f the family name.
	 * @param e the email address.
	 * @param o the organisation name.
	 */
	public SBMLVCard(String g, String f, String e, String o){
		family = new String(f);
		given = new String(g);
		email = new String(e);
		orgname = new String(o);
	}
	
	/**
	 * Returns the family name.
	 * 
	 * @return the family name.
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * Return the first name.
	 * 
	 * @return the first name.
	 */
	public String getGiven() {
		return given;
	}

	/**
	 * Returns the email address.
	 * 
	 * @return the email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the organisation name.
	 * 
	 * @return the organisation name.
	 */
	public String getOrgname() {
		return orgname;
	}
	
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
	
		buffer.append("Name: " + family + " " + given).append("\n");
		
		if (email != null && email.trim().length() > 0) {
			buffer.append("Email: " + email).append("\n");
		}
		
		if (orgname != null && orgname.trim().length() > 0) {
			buffer.append("Organisation: " + orgname).append("\n");
		}

		return buffer.toString();
	}
	
}