/*
 * $Id: MiriamAnnotation.java 251 2013-03-14 16:49:43Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/utils/sbml/sbmlannotation/MiriamAnnotation.java $
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Holds the uri and id of a Miriam annotation found in an SBML file.
 * 
 * @author rodrigue
 *
 */
public class MiriamAnnotation {

	String uri;
	String identifiers_org_uri;
	String id;
	
	
	
	public MiriamAnnotation(String id, String uri) {
		this.id = id;
		this.uri = uri;
	}
	
	public MiriamAnnotation(String id, String uri, String identifiers_orgURI) {
		this(id, uri);
		this.identifiers_org_uri = identifiers_orgURI;
	}

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the full annotation URI, including the identifier part.
	 * 
	 * @return the full annotation URI, including the identifier part.
	 */
	public String getFullURI() {
		
		if (uri.startsWith("http://identifiers.org")) 
		{
			return uri + id;
		}
		else if (uri.startsWith("urn:")) 
		{
			try 
			{
				return uri + ":" + URLEncoder.encode(id, "UTF-8");
			}
			catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
		}
		
		return null;
	}

	/**
	 * Returns the identifiers_org_uri
	 *
	 * @return the identifiers_org_uri
	 */
	public String getIdentifiers_orgURI() {
		return identifiers_org_uri;
	}

	/**
	 * Sets the identifiers_org_uri.
	 *
	 * @param identifiers_org_uri the identifiers_org_uri to set
	 */
	public void setIdentifiers_orgURI(String identifiers_org_uri) {
		this.identifiers_org_uri = identifiers_org_uri;
	}
}
