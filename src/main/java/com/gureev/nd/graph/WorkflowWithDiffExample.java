package com.gureev.nd.graph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by Nick on 4/4/2017.
 */
public class WorkflowWithDiffExample {

    private static int WIDTH = 1000;
    private static int HEIGHT = 320;
    private static final int centerX = WIDTH/2;
    private static final int centerY = HEIGHT/2;
    private static final int WIDTH_OFFSET = WIDTH / 4;
    private static final int HEIGHT_OFFSET = HEIGHT / 4;

    private static double WIDTH_LIMIT = WIDTH;
    private static double HEIGHT_LIMIT = HEIGHT;

    private static final int ORIENTATION = SwingConstants.WEST;


    private static mxGraph getGraph() {
        mxGraph graph = new mxGraph();

        //Create style
        mxStylesheet stylesheet = graph.getStylesheet();
        updateStyles(stylesheet);
        graph.setAutoSizeCells(true);
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object start = graph.insertVertex(parent, null, "Start", centerX, centerY, 50,
                    50, "ROUNDED;fillColor=white");
            Object v1 = graph.insertVertex(parent, null, "Pre-processor", centerX, centerY, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "Processor", centerX, centerY,
                    80, 30);
            Object v3 = graph.insertVertex(parent, null, "Post-Processor", centerX, centerY,
                    80, 30);
            Object finish = graph.insertVertex(parent, null, "Finish", centerX, centerY, 50,
                    50, "ROUNDED;fillColor=white");
            graph.insertEdge(parent, null, "", start, v1);
            graph.insertEdge(parent, null, "", v1, v2);
            graph.insertEdge(parent, null, "", v2, v3);
            graph.insertEdge(parent, null, "", v3, finish);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        return graph;
    }

    private static mxGraph getSecondGraph() {
        mxGraph graph = new mxGraph();

        //Create style
        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_OPACITY, 50);
        style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
        stylesheet.putCellStyle("ROUNDED", style);
        graph.setAutoSizeCells(true);
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object start = graph.insertVertex(parent, null, "Start", centerX, centerY, 50,
                    50, "ROUNDED;fillColor=white");
            Object v1 = graph.insertVertex(parent, null, "Another Pre-processor", centerX, centerY, 80,
                    30);
            graph.updateCellSize(v1);
            Object v2 = graph.insertVertex(parent, null, "Processor", centerX, centerY,
                    80, 30);
            graph.updateCellSize(v2);
            Object v3 = graph.insertVertex(parent, null, "Second Processor", centerX, centerY,
                    80, 30);
            graph.updateCellSize(v3);
            Object v4 = graph.insertVertex(parent, null, "Another Post-Processor", centerX, centerY,
                    80, 30);
            graph.updateCellSize(v4);
            Object finish = graph.insertVertex(parent, null, "Finish", centerX, centerY, 50,
                    50, "ROUNDED;fillColor=white");
            graph.insertEdge(parent, null, "", start, v1);
            graph.insertEdge(parent, null, "", v1, v2);
            graph.insertEdge(parent, null, "", v2, v3);
            graph.insertEdge(parent, null, "", v3, v4);
            graph.insertEdge(parent, null, "", v4, finish);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        return graph;
    }

    private static void updateStyles(mxStylesheet stylesheet) {
        Hashtable<String, Object> style = new Hashtable<>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_OPACITY, 50);
        style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
        stylesheet.putCellStyle("ROUNDED", style);

        Hashtable<String, Object> vertexNew = new Hashtable<>();
        vertexNew.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        vertexNew.put(mxConstants.STYLE_OPACITY, 50);
        vertexNew.put(mxConstants.STYLE_FONTCOLOR, "white");
        vertexNew.put(mxConstants.STYLE_FILLCOLOR, "green");
        stylesheet.putCellStyle("UPDATED_VERTEX", vertexNew);


        Hashtable<String, Object> vertexDeleted = new Hashtable<>();
        vertexDeleted.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        vertexDeleted.put(mxConstants.STYLE_OPACITY, 50);
        vertexDeleted.put(mxConstants.STYLE_FONTCOLOR, "black");
        vertexDeleted.put(mxConstants.STYLE_FILLCOLOR, "red");
        stylesheet.putCellStyle("DELETED_VERTEX", vertexDeleted);
    }

    private enum Status {NEW, DELETED}

    private static Map<String, Status> compareGraphs(mxGraph first, mxGraph second) {
        Set<String> firstGraphElements = addVertices(first, new HashSet<>());
        firstGraphElements = addEdges(first, firstGraphElements);

        Set<String> secondGraphElements = addVertices(second, new HashSet<>());
        secondGraphElements = addEdges(second, secondGraphElements);

        return getDifferences(firstGraphElements, secondGraphElements);
    }

    private static Map<String, Status> getDifferences(Set<String> firstGraphElements, Set<String> secondGraphElements) {
        Map<String, Status> diffs = new HashMap<>();
        for (String element : firstGraphElements) {
            if (!secondGraphElements.contains(element)) {
                diffs.put(element, Status.DELETED);
            }
        }
        for (String element : secondGraphElements) {
            if (!firstGraphElements.contains(element)) {
                diffs.put(element, Status.NEW);
            }
        }
        return diffs;
    }

    private static Set<String> addVertices(mxGraph graph, Set<String> elements) {
        for (Object child : graph.getChildVertices(graph.getDefaultParent())) {
            mxCell vertex = (mxCell) child;
            elements.add(vertex.getValue().toString());
        }
        return elements;
    }

    private static Set<String> addEdges(mxGraph graph, Set<String> elements) {
        for (Object child : graph.getChildEdges(graph.getDefaultParent())) {
            mxCell edge = (mxCell) child;
            elements.add(formEdgeName(edge));
        }
        return elements;
    }

    private static String formEdgeName(mxCell edge) {
        return "FROM_"+edge.getSource().getValue()+"_TO_"+edge.getTarget().getValue();
    }

    public static void main(String[] args)
    {
        mxGraph graph = getGraph();
        mxGraph secondGraph = getSecondGraph();
        Map<String, Status> differences = compareGraphs(graph, secondGraph);

        graph.addCells(secondGraph.getChildCells(secondGraph.getDefaultParent()));
        mxGraph resultingGraph = graph;
        updateStyles(resultingGraph.getStylesheet());
        resultingGraph.getModel().beginUpdate();
        try
        {
            for (Object child : resultingGraph.getChildVertices(graph.getDefaultParent())) {
                mxCell vertex = (mxCell) child;
                if (differences.containsKey(vertex.getValue().toString())) {
                    switch (differences.get(vertex.getValue().toString())) {
                        case NEW:
                            vertex.setStyle("UPDATED_VERTEX;");
                            break;
                        case DELETED:
                            vertex.setStyle("DELETED_VERTEX");
                            break;
                    }
                }
            }

            for (Object child : resultingGraph.getChildEdges(graph.getDefaultParent())) {
                mxCell edge = (mxCell) child;
                if (differences.containsKey(formEdgeName(edge))) {
                    switch (differences.get(formEdgeName(edge))) {
                        case NEW:
                            edge.setStyle("strokeColor=green;");
                            break;
                        case DELETED:
                            edge.setStyle("strokeColor=red;");
                            break;
                    }
                }
            }
        }
        finally
        {
            resultingGraph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.refresh(); // Refreshes colors of nodes after diff

        // Set hierarchical layout, so the nodes are organized horizontally (WEST) or vertically (NORTH)
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(ORIENTATION);

        // No selection for nodes/edges
        graph.setCellsSelectable(false);
        graph.setCellsEditable(false);
        graph.setCellsDeletable(false);


        layout.execute(graph.getDefaultParent());
        JFrame parent = new JFrame();

        // Wheel is used for zoom
        graphComponent.setWheelScrollingEnabled(false);
        parent.addMouseWheelListener(new ZoomOnScroll(graphComponent));

        // Move graph with mouse
        DragWholeGraph dragWholeGraph = new DragWholeGraph(graph);
        graphComponent.getGraphControl().addMouseListener(dragWholeGraph);
        graphComponent.getGraphControl().addMouseMotionListener(dragWholeGraph);

        // Modify width, height and limiters on resize
        parent.addComponentListener(new ResizeListener());

        parent.setContentPane(graphComponent);
        parent.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        parent.setSize(WIDTH, HEIGHT);
        parent.setVisible(true);
    }

    private static void moveGraph(mxGraph graph, int x, int y) {

        double currentX = graph.getGraphBounds().getX();
        double currentY = graph.getGraphBounds().getY();

        double newX = currentX;
        if (checkWidth(graph, currentX + x))
            newX = currentX + x;

        double newY = currentY;
        if (checkHeight(graph, currentY + y))
            newY = currentY + y;

        graph.getModel().setGeometry(graph.getDefaultParent(),
                new mxGeometry(newX, newY,
                        WIDTH, HEIGHT));
    }

    private static boolean checkWidth(mxGraph graph, double x) {
        return (x > 0 && (x + graph.getGraphBounds().getWidth()) < WIDTH_LIMIT);
    }

    private static boolean checkHeight(mxGraph graph, double y) {
        return (y > 0 && (y + graph.getGraphBounds().getHeight() * 1.3) < HEIGHT_LIMIT);
    }

    static class DragWholeGraph implements MouseListener, MouseMotionListener {

        private mxGraph graph;

        private int initialX;
        private int initialY;

        public DragWholeGraph(mxGraph graph) {
            this.graph = graph;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            initialX = e.getX();
            initialY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            System.out.println("Dragged, diffs " + (initialX - e.getX()) + " " + (initialY - e.getY()));
            moveGraph(graph, initialX - e.getX(), initialY - e.getY());
            initialX = e.getX();
            initialY = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    static class ZoomOnScroll implements MouseWheelListener {
        double scale = 1;
        mxGraphComponent graphComponent;

        public ZoomOnScroll(mxGraphComponent graphComponent) {
            this.graphComponent = graphComponent;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            mxGraph graph = graphComponent.getGraph();
            if (e.getWheelRotation() < 0){
                scale = scale + 0.1;
                graphComponent.getGraph().getView().scaleAndTranslate(scale,
                        (graph.getGraphBounds().getCenterX()
                                -(graph.getGraphBounds().getWidth()/2))/scale,
                        (graph.getGraphBounds().getCenterY()
                                -(graph.getGraphBounds().getHeight()/2))/scale);
            } else {
                if((scale - 0.1) > 0){
                    scale = scale - 0.1;
                    graphComponent.getGraph().getView().scaleAndTranslate(scale,
                            (graph.getGraphBounds().getCenterX()
                                    -(graph.getGraphBounds().getWidth()/2))/scale,
                            (graph.getGraphBounds().getCenterY()
                                    -(graph.getGraphBounds().getHeight()/2))/scale);
                }
            }
            e.consume();
        }

    }

    static class ResizeListener implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
            WIDTH = e.getComponent().getWidth();
            HEIGHT = e.getComponent().getHeight();

            WIDTH_LIMIT = WIDTH;
            HEIGHT_LIMIT = HEIGHT;
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }
}
