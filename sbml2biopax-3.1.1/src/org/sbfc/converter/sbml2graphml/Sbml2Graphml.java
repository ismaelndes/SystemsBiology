package org.sbfc.converter.sbml2graphml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SimpleSpeciesReference;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;


/**
 * Class enabling the conversion to a SBML model file to a GraphML file.
 * 
 * For the moment only the basic structure of GraphML is implemented,
 * but the idea is to have also additional comments in the file for others purposes.
 * 
 * 
 * More details about GraphML, see the spec at http://graphml.graphdrawing.org/specification/dtd.html#top
 * @author jalowcki
 *
 */
public class Sbml2Graphml {

	private String sbmlFileName;
	
	public Sbml2Graphml(String input) {
		
		sbmlFileName = input;
		
	}
	
	/**
	 * Creates and returns a new {@link SBMLDocument}
	 * 
	 * @return a {@link SBMLDocument} or null if the document is not a valid SBML file.
	 */
	public SBMLDocument getSBMLDocument() {

		SBMLDocument document = null;
		SBMLReader reader = new SBMLReader();
		try {
			document = reader.readSBML(sbmlFileName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return document;
	}
	
	
	/**
	 * Close a graphML tag
	 * @return </graphml> w/ carriage return
	 */
	public String closeGraphMLTag() {
		return "</graphml>\n";
	}
	
	
	/**
	 * Close the graph tag
	 * @return </graph> w/ carriage return
	 */
	public String closeGraphTag() {
		return "</graph>\n";
	}
	
	
	/**
	 * Open a graphML tag
	 * @return <graphml> w/ carriage return
	 */
	public static String openGraphMLTag() {
		return "<graphml>\n";
	}
	
	
	/**
	 * Open a graph tag
	 * This tag includes the edgedefault attribute (optional according to the spec), by default equals to "directed"
	 * @return <graph> w/ carriage return
	 */
	public static String openGraphTag(String edgeDefault) {
		if (edgeDefault == null || !edgeDefault.matches("directed|undirected")) {
			edgeDefault = "directed";
		}
		return "<graph edgedefault=\"" + edgeDefault + "\">\n";
	}
	
	
	/**
	 * The desc tag is used to put a human readable description of the graph.
	 * It is an optionnal tag in GraphML.
	 * @return <desc> ...comment... </desc> w/ carriage return
	 */
	public String writeDescTags(String comment) {
		if (comment.equals("") || comment == null) {
			comment = "no description available";
		}
		return "<desc>" + comment + "</desc>\n";
	}
	
	
	/**
	 * Write a graphML edge (or arc) for the given {@link SimpleSpeciesReference}
	 * Edges will have source and target attributes. Edges direction is decided by the give edge type
	 * (product, reactant or modifier)
	 * 
	 * TODO can be improved with id and desc tags
	 * @param {@link SimpleSpeciesReference} ssr
	 * @param {@link String} edge type
	 * @return <egde source="..." target="..."/> w/ carriage return
	 */
	public String writeEdgeTag(Reaction reaction, SimpleSpeciesReference ssr, String type) {
		
		String source = ssr.getSpecies();
		String target = reaction.getId();
		
		if (type.matches("product")) {
			
			source = target;
			target = ssr.getSpecies();
		}
		
		return "<edge source=\"" + source + "\" target=\"" + target + "\"/>\n";
	}
	
	
	/**
	 * Here we build the GraphML file tag to tag.
	 * @param {@link SBMLDocument} sbmlDoc
	 */
	public void writeGraphML(SBMLDocument sbmlDoc, String outputFile) {
		
		String graph = "";
		
		graph += openGraphMLTag();
		
		// getting the SBML model
		Model model = sbmlDoc.getModel();
		
		// writing some description notes
		String comment = model.getName() + " " + model.getNotesString();
		graph += writeIndents(1) + writeDescTags(comment);
		
		// writing the graph
		graph += writeIndents(1) + openGraphTag("directed");
		
		// writing nodes first
		// species give nodes
		ListOf<Species> los = model.getListOfSpecies();
		for (Species sp : los) {
			
			// we put the name of the Species in description of the node
			graph += writeIndents(2) + writeDescTags(sp.getName());
			graph += writeIndents(2) + writeNodeTag(sp.getId());
			
		}

		// Looping over the reactions to obtain nodes and edges
		// we build the String for graphML edges
		String edges = "";

		// process nodes are nodes in graphML
		ListOf<Reaction> lor = model.getListOfReactions();
		for (Reaction reac : lor) {
			
			// we put the name of the Reaction in description of the node
			graph += writeIndents(2) + writeDescTags(reac.getName());
			graph += writeIndents(2) + writeNodeTag(reac.getId());
		
			// Description for reaction
			edges += writeIndents(2) + writeDescTags(reac.getName());
			
			ListOf<SpeciesReference> lore = reac.getListOfReactants();
			if (lore.size() > 0) {
				for (SpeciesReference spRef : lore) {
					edges += writeIndents(2) + writeEdgeTag(reac, spRef, "reactant");
				}
			}
			
			lore = reac.getListOfProducts();
			if (lore.size() > 0) {
				for (SpeciesReference spRef : lore) {
					edges += writeIndents(2) + writeEdgeTag(reac, spRef, "product");
				}
			}
			
			ListOf<ModifierSpeciesReference> lomoref = reac.getListOfModifiers();
			if (lore.size() > 0) {
				for (ModifierSpeciesReference moRef : lomoref) {
					edges += writeIndents(2) + writeEdgeTag(reac, moRef, "modifier");
				}
			}
			
			//TODO add locator tags? this can be a way to have web links in svg files
			
		}
		
		
		// then writing arcs
		graph += edges;
		
		
		graph += writeIndents(1) + closeGraphTag();
		
		
		graph += closeGraphMLTag();
		
		// writing the file
		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter(outputFile));
			buff.write(graph);
			buff.close();
		} catch (IOException ioe) {
			System.out.println("Error in writeGraphML...");
			ioe.printStackTrace();
		}
		
		return;
	}
	
	/**
	 * Write a the given number of indentations
	 * @param number
	 * @return indent
	 */
	public String writeIndents(int number) {
		String indentPattern = " ";
		String indent = "";
		for (int i = 0; i < number; i++) {
			indent += indentPattern;
		}
		return indent;
	}
	
	
	/**
	 * Write a node tag with the attribute id
	 * @return <node id=... /> w/ carriage return
	 */
	public String writeNodeTag(String idAttr) {
		return "<node id=\"" + idAttr + "\"/>\n";
	}
	
	
	public static void main(String[] args) {
		
		// taking a SBML file as input
		if (args.length != 1) {
			
			System.out.println("Bad usage: path of a SBML file as unique argument");
			return;
		}
		
		String sbmlFileName = args[0];
		
		Sbml2Graphml sbml = new Sbml2Graphml(sbmlFileName);

		// reading SBML file input and creating SBMLDocument
		SBMLDocument sbmlDocument = sbml.getSBMLDocument();

		if (sbmlDocument == null) {
			System.exit(1);
		}
		
		//Building the output filename path and the output filename
		String outputFilePath = sbmlFileName.replaceAll("[_A-Za-z0-9-]+.xml$", "");
		String outputFile = outputFilePath + "graphML_" + sbmlFileName.replaceAll("^.*/", "");
		
		sbml.writeGraphML(sbmlDocument, outputFile);
		System.out.println("Writing done");
		return;
		
	}
	

}
