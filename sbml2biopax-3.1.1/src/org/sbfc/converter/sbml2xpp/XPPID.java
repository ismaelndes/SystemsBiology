/*
 * $Id: XPPID.java 36 2011-05-11 12:21:02Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/sbml2xpp/XPPID.java $
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
package org.sbfc.converter.sbml2xpp;

import java.util.HashMap;

/**
 * Checks and Converts the SBML elements id's into compliant XPP id. 
 * 
 * @author rodrigue
 * @author Kedar Nath Natarajan
 */
public class XPPID {

	private static HashMap<String, XPPID> xppIdMap = new HashMap<String, XPPID>();
	private static HashMap<String, Integer> xppIdReduceMap = new HashMap<String, Integer>();
	private static HashMap<String, XPPID> sbmlIdMap = new HashMap<String, XPPID>();
	
	static {
		sbmlIdMap.put("time", new XPPID("time"));
		sbmlIdMap.put("Time", new XPPID("time"));
		sbmlIdMap.put("floor", new XPPID("floor"));
		// sbmlIdMap.put("T", new XPPID("T"));
		// sbmlIdMap.put("t", new XPPID("t"));
	}

	private String sbmlId;
	private String xppId;
	private String xppIdUpercase;

	public XPPID(String sbmlId) {
		this.sbmlId = sbmlId;
		this.xppId = sbmlId;
		
		if (xppId.equalsIgnoreCase("time")) {
			this.xppId = "t";
		} else if (xppId.equalsIgnoreCase("floor")) {
			this.xppId = "flr";
		} else if (xppId.equalsIgnoreCase("t")) { // xpp reserved keywords
			this.xppId = sbmlId + "_renamed";
		} else if (xppId.equalsIgnoreCase("pi")) { // xpp reserved keywords
			this.xppId = sbmlId + "_ren";
		} // TODO : do the same for all the xpp reserved keyword ??

		this.xppIdUpercase = sbmlId.toUpperCase();
	}

	public XPPID(String sbmlId, String sbmlReactionId) {
		this.sbmlId = sbmlId + "_" + sbmlReactionId;
		this.xppId = sbmlId;
		this.xppIdUpercase = xppId.toUpperCase();
	}

	/**
	 * Checks that the xppId comply to the constraints made by XPP.
	 * 
	 * - 9 character maximum - no differences between upper case and lower case
	 * character in XPP
	 * 
	 * If necessary, xppId is updated, and the current XPPID is added to the map
	 * 
	 */
	public void checkXPPId() {

		if (xppId.length() > 9) {
			xppId = xppId.substring(0, 9);
			xppIdUpercase = xppId.toUpperCase();
		}

		// System.out.println("XPPID : checkXppId : sbmlId = " + sbmlId +
		// ", xppId = " + xppId + ", xppID = " + xppIdUpercase);

		boolean isUnique = false;
		Integer idNb = xppIdReduceMap.get(xppIdUpercase);
		int i = 1;

		if (idNb != null) {
			i = idNb;
		}

		while (!isUnique) {
			XPPID conflictingId = xppIdMap.get(xppIdUpercase);

			if (conflictingId == null) {

				isUnique = true;
				break;
			}
			reduceId(i);
			i++;
		}

		// System.out.println("XPPID : checkXppId : sbmlId = " + sbmlId +
		// ", xppId = " + xppId);

		xppIdUpercase = xppId.toUpperCase();
		xppIdMap.put(xppIdUpercase, this);
		xppIdReduceMap.put(xppIdUpercase, i);
		sbmlIdMap.put(sbmlId, this);
	}

	/**
	 * Checks if Id is unique or else reduces to 7 and adds "_i {1..100} " <br />  
	 * Iterates till id is unique or till ( i < 100 )
	 * @param i : iterates i from 1 to 100 
	 */
	private void reduceId(int i) {

		// System.out.println("XPPID : reduceId : xppId = " + xppId + ", i = " +
		// i);

		if (i < 10) {
			if (xppId.length() > 7) {
				xppId = xppId.substring(0, 7);
			}

			xppId += "_" + i;

		} else if (i < 100) {
			if (xppId.length() > 6) {
				xppId = xppId.substring(0, 6);
			}

			xppId = xppId + "_" + i;

		} else {
			if (xppId.length() > 4) {
				xppId = xppId.substring(0, 4);
			}

			xppId = xppId + "_" + i;
			// id will not be valid if number goes over 10 000 !!
		}

		xppIdUpercase = xppId.toUpperCase();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XPPID) {
			return ((XPPID) obj).getXppId().equalsIgnoreCase(xppId);
		} else if (obj instanceof String) {
			return ((String) obj).equalsIgnoreCase(xppId);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return xppIdUpercase.hashCode();
	}

	@Override
	public String toString() {
		return xppId;
	}

	/**
	 * Gets the SBMLid
	 * @return : SBMLid
	 */
	public String getSbmlId() {
		return sbmlId;
	}

	/**
	 * Gets the XPPid
	 * @return : XPPid
	 */
	public String getXppId() {
		return xppId;
	}

	/**
	 * Changes XPPid to Upper Case
	 * @return : XPPid to Upper Case
	 */
	public String getXppIdUpercase() {
		return xppIdUpercase;
	}

	/**
	 * Gets the SBMLid corresponding to XPPid 
	 * 
	 * @param xppId : 
	 * @return : SBMLId
	 */
	public static String getSBMLId(String xppId) {

		XPPID id = xppIdMap.get(xppId.toUpperCase());

		if (id == null) {
			return null;
		}

		return id.getSbmlId();
	}

	/**
	 * Gets the XPPid corresponding to SBMLid
	 * 
	 * @param sbmlId
	 * @return : XPPid 
	 */
	public static String getXPPId(String sbmlId) {

		XPPID id = sbmlIdMap.get(sbmlId);

		if (id == null) {
			return null;
		}

		return id.getXppId();

	}

}
