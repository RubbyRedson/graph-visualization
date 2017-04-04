package com.gureev.nd.graph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.util.*;

/**
 * Created by Nick on 4/4/2017.
 */
public class WorkflowWithDiffExample {

    private static final int centerX = 500;
    private static final int centerY = 160;

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


        mxGraphComponent graphComponent = new mxGraphComponent(resultingGraph);
        graphComponent.refresh();
        // Set hierarchical layout, so the nodes are organized horizontally (WEST) or vertically (NORTH)
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.WEST);
        layout.execute(graph.getDefaultParent());
        JScrollPane scrollPane = new JScrollPane(graphComponent);
        JFrame parent = new JFrame();
        parent.setContentPane(scrollPane);
        parent.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        parent.setSize(1000, 320);
        parent.setVisible(true);
    }
}
