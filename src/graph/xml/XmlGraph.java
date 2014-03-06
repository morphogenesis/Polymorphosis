package graph.xml;

import core.App;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;

/**
 * Created on 3/5/14.
 */
public class XmlGraph {
	public static ArrayList<Node> nodes;
	public static ArrayList<Edge> edges;
	protected static XmlMap Map = new XmlMap();
	protected static void writeToXML() {
		File file = new File(App.filepath);
		File staticFile = new File(App.staticFilepath);
		System.out.println("Writing XML file..." + file.getPath());
		System.out.println("Writing static XML file..." + staticFile.getPath());
		try {
			JAXBContext jc = JAXBContext.newInstance(XmlMap.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(Map, System.out);
			m.marshal(Map, file);
			m.marshal(Map, staticFile);
		} catch (JAXBException e) { e.printStackTrace(); }
		System.out.println("Finished writing to XML file.");
	}
	protected static void readFromXML() {
		System.out.println("Reading from XML file...");
		XmlMap map;
		try {
			JAXBContext jc = JAXBContext.newInstance(XmlMap.class);
			Unmarshaller um = jc.createUnmarshaller();
			map = (XmlMap) um.unmarshal(new File(App.staticFilepath));
			if (map.getNodes() != null) for (Node n : map.getNodes()) { System.out.println("N [" + n.getId() + "] " + n.getName()); }
			if (map.getEdges() != null) for (Edge e : map.getEdges()) { System.out.println("E [" + e.getFrom() + " <-> " + e.getTo() + "]"); }
			Map = map;
		} catch (JAXBException e) { e.printStackTrace(); }
		System.out.println("Finished reading from XML file.");
	}
}
