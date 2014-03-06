package graph;

import core.App;
import graph.xml.Edge;
import graph.xml.Node;
import graph.xml.XmlGraph;
import graph.xml.XmlMap;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;
import util.Ball;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 3/5/14.
 */
public class GraphObject extends XmlGraph {
	App p5;
	private static HashMap<Integer, Node> nodeIndex = new HashMap<>();
	private static HashMap<Integer, ArrayList<Node>> edgeIndex = new HashMap<>();
	public static float totalArea = 0;
	public GraphObject(App p5) {
		this.p5 = p5;
		nodes = new ArrayList<>(); edges = new ArrayList<>();
	}
	public static void build() {
		Map = new XmlMap();
		Map.setNodes(nodes);
		Map.setEdges(edges);
		edgeIndex = new HashMap<>();
		nodeIndex = new HashMap<>();
		for (Node n : nodes) {
			totalArea += n.size;
			int id = nodes.indexOf(n);
			n.setId(id);
			nodeIndex.put(id, n);
		}
		for (Edge e : edges) {
			ArrayList<Node> nlist = edgeIndex.get(e.getFrom());
			if (nlist == null) { nlist = new ArrayList<>(); edgeIndex.put(e.getFrom(), nlist); }
//			nlist.add(nodeIndex.get(e.getTo()));
			nlist.add(nodes.get(e.getTo()));
		}
		writeToXML();
	}
	public static void rebuild() {
		readFromXML();
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
		App.PHYSICS.reset();
		App.METABALL.clearMetaballs();
		edgeIndex = new HashMap<>();
		nodeIndex = new HashMap<>();
		for (Node n : Map.getNodes()) {
			n.setParticle2D(new VerletParticle2D(n.getX(), n.getY()));
			n.setBehavior2D(new AttractionBehavior2D(n.getParticle2D(), n.getRadius(), -1));
			n.setBall(new Ball(n.getX(), n.getY(), n.getRadius()));
			if (n.getId() == 0) { GraphEditor.activeNode = n; App.GRAPH.lockNode(); }
			addNode(n);
		}
		for (Edge e : edges) e.update(); for (Edge e : Map.getEdges()) {
			e.setA(getNode(e.getFrom()));
			e.setB(getNode(e.getTo()));
			e.setSpring2D(new VerletSpring2D(e.getA().getParticle2D(), e.getB().getParticle2D(), e.getLength(), 0.001f));
			addEdge(e);
			ArrayList<Node> nlist = edgeIndex.get(e.getFrom());
			if (nlist == null) { nlist = new ArrayList<>(); edgeIndex.put(e.getFrom(), nlist); }
			nlist.add(nodeIndex.get(e.getTo()));
		}

		update();
		build();
	}
	protected static Node getNode(int id) {
		return nodeIndex.get(id);
	}
	protected static Edge getEdge(Node a, Node b) {
		for (Edge e : edges) { if ((e.getA() == a && e.getB() == b) || (e.getA() == b && e.getB() == a)) { return e; } }
		return null;
	}

	public static void addEdge(Edge e) {
		if (getEdge(e.getA(), e.getB()) == null) {
			edges.add(e);
			App.PHYSICS.addSpring(e);
			build();
		}
	}
	public static void addNode(Node n) {
		nodes.add(n);
		nodeIndex.put(n.getId(), n);
		App.PHYSICS.addParticle(n);
		App.METABALL.addMetaball(n.getBall());
		build();
	}
	public static void removeEdge(Edge e) {
		edges.remove(e);
		edgeIndex.remove(e.getFrom());
		App.PHYSICS.removeSpring(e);
		build();
	}
	public static void removeNode(Node n) {
		int id = n.getId();
		int num = Node.getNumberOfGNodes();
		Node.setNumberOfGNodes(num - 1);
		nodes.remove(n);
		nodeIndex.remove(n.getId());
		App.PHYSICS.removeParticle(n);
		App.METABALL.removeMetaball(n.getBall());
		for (Node na : nodes) {
			int naId = na.getId();
			if (naId > id) { na.setId(naId - 1); }
		}
	}
	public static void update() {
		totalArea = 0;
		for (Node n : nodes) { n.update(); totalArea += n.size; }
		for (Edge e : edges) e.update();
	}
}
