package graph;

import core.App;
import core.Gui;
import graph.xml.*;
import processing.core.PApplet;
import processing.core.PConstants;
import toxi.geom.Circle;
import toxi.geom.Rect;
import toxi.geom.Vec2D;

import java.util.ArrayList;


/**
 * Created on 3/5/14.
 */
public class GraphEditor extends GraphObject {

	public static boolean update_graph;
	public static boolean draw_graph = true;
	public static boolean draw_graph_list = true;
	public static boolean draw_graph_nodes = true;
	public static boolean draw_graph_edges = true;
	public final ArrayList<Node> selectedNodes = new ArrayList<>();
	public final ArrayList<Node> lockedNodes = new ArrayList<>();
	public final ArrayList<Edge> adjacentEdges = new ArrayList<>();
	public static Node activeNode;
	public static Node hoveredNode;

	public GraphEditor(App p5) { super(p5); }

	public void draw() {
		update();
		if (draw_graph_edges) { for (Edge e : edges) {e.draw(p5);} }
		if (draw_graph_nodes) { for (Node n : nodes) { n.draw(p5); } }
	}

	public void createNewBranch(float num, boolean split) {
		if (hasActiveNode()) {
			Node parent = activeNode;
			float size = parent.size;
			for (int i = 1; i <= num; i++) {
				Vec2D pos = Vec2D.fromTheta(i * PApplet.TWO_PI / num).scaleSelf(size).addSelf(parent.getParticle2D());
				Node child = new Node(parent.getName() + i, size, pos);
				addNode(child);
				build();
//				Graph.buildEdge(new Edge(parent, child));
				addEdge(new Edge(parent, child));
				selectedNodes.add(child);
			} if (split) parent.size = (size / (num + 1));
		}
	}
	public void createNewEdge() {
		for (Node n : selectedNodes) {
			Edge e = new Edge(n, activeNode);
			addEdge(e);
		}
	}
	public void createNewNode(String name, float size, Vec2D pos) {
		addNode(new Node(name, size, pos));
	}
	public void deleteEdges() {
		for (Edge e : adjacentEdges) { removeEdge(e); }
		deselectEdges();
	}
	public void deleteNode(Node n) {
		while (hasActiveNode()) {
			ArrayList<Edge> rels = new ArrayList<>();
			for (Edge e : XmlGraph.edges) {
				if (e.getA() == n) {rels.add(e); continue;}
				if (e.getB() == n) {rels.add(e);}
			}
			for (Edge re : rels) { removeEdge(re); }
			removeNode(n);
			clearSelection();
			deselectNode();
		}
	}
	public void editSize(float size) {
		if (hasActiveNode()) activeNode.size = (size);
		for (Node g : selectedNodes) {g.size = (size);}
	}
	public boolean hasActiveNode() {return activeNode != null; }
	public void lockNode() {
		if (lockedNodes.contains(activeNode)) {
			lockedNodes.remove(activeNode);
			activeNode.getParticle2D().unlock();
		}
		else {
			activeNode.getParticle2D().lock(); lockedNodes.add(activeNode);
		}
	}
	public void mouseDragged(Vec2D mousePos) { if ((hasActiveNode()) && (Gui.isEditMode)) moveNode(mousePos); }
	public void mouseMoved(Vec2D mousePos) {highlightNodeNearPosition(mousePos);}

	public void mousePressed(Vec2D mousePos) { selectNodeNearPosition(mousePos); }
	public void mouseReleased() {releaseNode(); }
	public void mouseWheel(float e) {
		if (hasActiveNode()) {
			float size = activeNode.size;
			float scale = 0.1f;
			if (size >= 3) {scale = 1;}
			if (size >= 20) {scale = 5;}
			if (size >= 50) {scale = 12;}
			if (size >= 100) {scale = 25;}
			if (e > 0) {activeNode.size = (size - scale);}
			else if (e < 0) {activeNode.size = (size + scale);}
			if (activeNode.size <= 1) {activeNode.size = 2;}
		} for (Node n : selectedNodes) {
			n.size = (activeNode.size);
		}
	}
	private void highlightNodeNearPosition(Vec2D mousePos) {
		hoveredNode = null;
		Circle c = new Circle(mousePos, 20);

		for (Node n : XmlGraph.nodes) {
			Rect r = new Rect(App.WIDTH - 200, 50 + (n.getId() * 14), 160, 12);
			if (c.containsPoint(n.getParticle2D())) { hoveredNode = n; break; }
			else if ((draw_graph_list) && (r.containsPoint(mousePos))) { hoveredNode = n; break; }
		}
	}
	private void moveNode(Vec2D mousePos) { activeNode.getParticle2D().lock(); activeNode.getParticle2D().set(mousePos); }

	protected void selectNodeNearPosition(Vec2D mousePos) {
		if (!App.isShiftDown) {
			clearSelection();
		}
		else if (hasActiveNode()) {selectedNodes.add(activeNode); deselectNode(); }
		for (Node n : XmlGraph.nodes) {
			Rect r = new Rect(App.WIDTH - 200, 50 + (n.getId() * 14), 160, 12);
			Circle c = new Circle(n.getX(), n.getY(), 20);
			if (c.containsPoint(mousePos)) {
				setActiveNode(n); break;
			}
			else if ((draw_graph_list) && (r.containsPoint(mousePos))) {
				setActiveNode(n); break;
			}
			else { deselectNode(); }
		}
	}
	protected void setActiveNode(Node n) {
		activeNode = n;
		System.out.println("num" + n.getId());
		selectAdjacentEdges();
	}
	protected void releaseNode() { if (hasActiveNode()) { if (!lockedNodes.contains(activeNode)) activeNode.getParticle2D().unlock(); } }
	protected void selectAdjacentEdges() {
		Node n = activeNode;
		deselectEdges();
		ArrayList<Edge> edges = new ArrayList<>();
		for (Edge e : XmlGraph.edges) {
			if (e.getA() == n) { edges.add(e); continue;}
			if (e.getB() == n) { edges.add(e); }
		} adjacentEdges.addAll(edges);
	}
	protected void deselectNode() { releaseNode(); activeNode = null; adjacentEdges.clear(); }
	protected void clearSelection() { selectedNodes.clear(); adjacentEdges.clear(); }
	protected void deselectEdges() { adjacentEdges.clear(); }
}
/*	public void deleteNode() {
		while (hasActiveNode()) {
			Node n = activeNode;
			ArrayList<Edge> rels = new ArrayList<>();
			for (Edge e : Graph.edges) {
				if (e.getA() == n) { rels.add(e); System.out.println("getA[" + e.getA().getId() + "] ==> getB[" + e.getB().getId() + "] <= MATCH"); continue;}
				if (e.getB() == n) { rels.add(e);*//*System.out.println("getA[" + e.getA().getId() + "] ==> getB[" + e.getB().getId() + "] <= MATCH");*//*}
			}
			Graph.removeEdges(rels); Graph.removeNode(n);
			clearSelection();
			deselectNode();
		}
	}*/
