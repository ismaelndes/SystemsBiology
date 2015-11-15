/*
 * $Id: SBML2BioPAX_l2.java 247 2012-08-22 15:35:00Z niko-rodrigue $
 * $URL: https://sbfc.svn.sourceforge.net/svnroot/sbfc/trunk/src/org/sbfc/converter/sbml2biopax/SBML2BioPAX_l2.java $
 *
 *
 * ==============================================================================
 * Copyright (c) 2010-2011 the copyright is held jointly by the individual
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

package org.sbfc.converter.sbml2biopax;

import java.util.Set;

import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.BioPAXFactory;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level2.ControlType;
import org.biopax.paxtools.model.level2.Level2Element;
import org.biopax.paxtools.model.level2.SpontaneousType;
import org.biopax.paxtools.model.level2.XReferrable;
import org.biopax.paxtools.model.level2.bioSource;
import org.biopax.paxtools.model.level2.complex;
import org.biopax.paxtools.model.level2.control;
import org.biopax.paxtools.model.level2.conversion;
import org.biopax.paxtools.model.level2.dataSource;
import org.biopax.paxtools.model.level2.openControlledVocabulary;
import org.biopax.paxtools.model.level2.pathway;
import org.biopax.paxtools.model.level2.physicalEntity;
import org.biopax.paxtools.model.level2.physicalEntityParticipant;
import org.biopax.paxtools.model.level2.process;
import org.biopax.paxtools.model.level2.xref;
import org.biopax.paxtools.model.level3.ConversionDirectionType;




/**
 * Convert an SBML file into a BioPax owl file.
 * 
 * @author Arnaud Henry
 * @author Nicolas Rodriguez
 * @author Jean-Baptiste Pettit
 * @author Camille Laibe
 * 
 * @version 2.3
 * 
 */
public class SBML2BioPAX_l2 extends SBML2BioPAX {

	public SBML2BioPAX_l2() 
	{
		super();
		biopaxLevel = 2;
		
		PATHWAY_CLASS_NAME = "pathway";
		BIO_SOURCE = "bioSource";
		CONTROL = "control";
		BIOCHEMICAL_REACTION = "biochemicalReaction";
		COMPLEX_DIS_ASSEMBLY = "complexDisAssembly";
		COMPLEX_ASSEMBLY = "complexAssembly";
		TRANSPORT = "transport";
		PUBLICATION_XREF_CLASS_NAME = "publicationXref";
		UNIFICATION_XREF_CLASS_NAME = "unificationXref";
		PROVENANCE_CLASS_NAME = "dataSource";
		CELLULAR_LOCATION_VOCABULARY = "openControlledVocabulary";
		PHYSICAL_ENTITY_PARTICIPANT = "physicalEntityParticipant";
	}


	protected void addComment(BioPAXElement biopaxElement, String commentString) 
	{
		if (biopaxElement instanceof Level2Element) 
		{
			((Level2Element) biopaxElement).addCOMMENT(commentString);
		}
		
	}
	
	protected void setXrefId(BioPAXElement xref, String annotationIdentifier) 
	{
		if (xref instanceof xref)
		{
			((xref) xref).setID(annotationIdentifier);
		}		
	}

	protected void setXrefDb(BioPAXElement xref, String annotationDBname) 
	{
		if (xref instanceof xref)
		{
			((xref) xref).setDB(annotationDBname);
		}
	}

	protected void addPathwayComponent(BioPAXElement pathway, BioPAXElement bioReaction) 
	{
		if (pathway instanceof pathway && bioReaction instanceof process) 
		{
			((pathway) pathway).addPATHWAY_COMPONENTS((process) bioReaction);
		}		
	}

	protected void setOrganismToPathway(BioPAXElement pathway, BioPAXElement biosource) 
	{
		if (pathway instanceof pathway && biosource instanceof bioSource) 
		{
			((pathway) pathway).setORGANISM((bioSource) biosource);
		}
	}

	protected void addXref(BioPAXElement biopaxElement, BioPAXElement xref) 
	{
		if (biopaxElement instanceof XReferrable && xref instanceof xref) 
		{
			((XReferrable) biopaxElement).addXREF((xref) xref);
		}		
	}


	protected void addDataSource(BioPAXElement pathway, BioPAXElement datasource) 
	{
		if (pathway instanceof pathway && datasource instanceof dataSource) 
		{
			((pathway) pathway).addDATA_SOURCE((dataSource) datasource);
		}
		
	}

	protected void setDisplayName(BioPAXElement named, String displayName) 
	{
		// does not exist in L2 ?!
	}

	protected void addName(BioPAXElement named, String name) 
	{
		// no name for physicalEntity in L2
		if (named instanceof dataSource) 
		{
			((dataSource) named).addNAME(name);
		}
	}

	protected void addTerm(BioPAXElement vocab, String term) 
	{
		if (vocab instanceof openControlledVocabulary)
		{
			((openControlledVocabulary) vocab).addTERM(term);
		}
	}

	protected boolean isComplex(Model bioPaxModel, String species) 
	{
		if (bioPaxModel.getByID(species) instanceof complex)
		{
			return true;
		}
		
		return false;
	}

	protected void setConversionDirection(BioPAXElement bioReaction,
			ConversionDirectionType direction) 
	{
		if (bioReaction instanceof conversion)
		{
			if (direction.equals(ConversionDirectionType.LEFT_TO_RIGHT))
			{
				((conversion) bioReaction).setSPONTANEOUS(SpontaneousType.L_R);
			}
			else if (direction.equals(ConversionDirectionType.RIGHT_TO_LEFT))
			{
				((conversion) bioReaction).setSPONTANEOUS(SpontaneousType.R_L);
			}
		}
	}

	protected void setSpontaneous(BioPAXElement bioReaction, boolean spontaneous) 
	{
		// nothing to do for L2
	}

	protected BioPAXElement setStoichiometry(BioPAXElement bioReaction,
			BioPAXElement physicalEntityParticipant, double stoichiometry, String stochiometryId, BioPAXFactory elementFactory) 
	{

		if (bioReaction instanceof conversion && physicalEntityParticipant instanceof physicalEntityParticipant)
		{
			((physicalEntityParticipant) physicalEntityParticipant).setSTOICHIOMETRIC_COEFFICIENT(stoichiometry);
		}
		else 
		{
			println("setStoichiometry problem !! " + physicalEntityParticipant + "; " + bioReaction);
		}
		
		return null;
	}

	protected void addMemberPhysicalEntity(BioPAXElement physicalEntityParticipant,
			BioPAXElement physicalEntity) 
	{
		if (physicalEntityParticipant instanceof physicalEntityParticipant && physicalEntity instanceof physicalEntity)
		{
			((physicalEntityParticipant) physicalEntityParticipant).setPHYSICAL_ENTITY((physicalEntity) physicalEntity);
		}
		else 
		{
			println("addMemberPhysicalEntity problem !! " + physicalEntityParticipant + "; " + physicalEntity);
		}
	}

	protected void setCellularLocation(BioPAXElement physicalEntityParticipant, BioPAXElement cellularLocation) 
	{
		if (physicalEntityParticipant instanceof physicalEntityParticipant && cellularLocation instanceof openControlledVocabulary)
		{
			((physicalEntityParticipant) physicalEntityParticipant).setCELLULAR_LOCATION((openControlledVocabulary) cellularLocation);
		}
		else 
		{
			println("setCellularLocation problem !! " + physicalEntityParticipant + "; " + cellularLocation);
		}
	}

	protected void addRight(BioPAXElement bioReaction, BioPAXElement physicalEntityParticipant) 
	{
		if (bioReaction instanceof conversion && physicalEntityParticipant instanceof physicalEntityParticipant)
		{
			((conversion) bioReaction).addRIGHT((physicalEntityParticipant) physicalEntityParticipant);
		}
	}

	protected void addLeft(BioPAXElement bioReaction, BioPAXElement physicalEntityParticipant) 
	{
		if (bioReaction instanceof conversion && physicalEntityParticipant instanceof physicalEntityParticipant)
		{
			((conversion) bioReaction).addLEFT((physicalEntityParticipant) physicalEntityParticipant);
		}
	}

	@SuppressWarnings("unchecked")
	protected void addControl(Set<? extends BioPAXElement> setOfcontrol, BioPAXElement reacControl)
	{
		if (reacControl instanceof control)
		{
			((Set<control>) setOfcontrol).add((control) reacControl);
		}
	}

	protected Set<? extends BioPAXElement> getListOfControl(BioPAXElement bioReaction) 
	{
		if (bioReaction instanceof conversion)
		{
			return ((conversion) bioReaction).isCONTROLLEDOf();
		}
		else 
		{
			println("getListOfControl problem !! " + bioReaction);
			
		}
		
		return null;
	}

	protected void setControlType(BioPAXElement reacControl, String controlType) 
	{
		if (reacControl instanceof control)
		{
			//Creating the controlType
			ControlType bioPaxcontrolType;

			if (controlType.equals(CONTROL)) 
			{
				bioPaxcontrolType = ControlType.INHIBITION;	
			}
			else 
			{
				bioPaxcontrolType = ControlType.ACTIVATION;
			}

			((control) reacControl).setCONTROL_TYPE(bioPaxcontrolType);
		}
	}

	
	protected void addControlled(BioPAXElement reacControl, BioPAXElement bioReaction) 
	{
		if (reacControl instanceof control && bioReaction instanceof process) 
		{
			((control) reacControl).addCONTROLLED((process) bioReaction);
		}
	}

	protected void addController(BioPAXElement reacControl, BioPAXElement physicalEntityParticipant) 
	{
		if (reacControl instanceof control && physicalEntityParticipant instanceof physicalEntity) 
		{
			((control) reacControl).addCONTROLLER((physicalEntityParticipant) physicalEntityParticipant);
		}		
	}


}
