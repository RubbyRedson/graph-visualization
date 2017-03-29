package com.gureev.nd.graph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.util.Hashtable;

/**
 * Created by Nick on 3/22/2017.
 */
public class WorkflowExample
{
    private static final int centerX = 500;
    private static final int centerY = 160;

    private static mxGraph getGraph() {
        mxGraph graph = new mxGraph();

        //Create style
        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_OPACITY, 50);
        style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
        stylesheet.putCellStyle("ROUNDED", style);

        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object start = graph.insertVertex(parent, null, "Start", centerX, centerY, 50,
                    50, "ROUNDED;fillColor=green");
            Object v1 = graph.insertVertex(parent, null, "Pre-processor", centerX, centerY, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "Processor", centerX, centerY,
                    80, 30);
            Object v3 = graph.insertVertex(parent, null, "Post-Processor", centerX, centerY,
                    80, 30);
            Object finish = graph.insertVertex(parent, null, "Finish", centerX, centerY, 50,
                    50, "ROUNDED;fillColor=red");
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

    public static void main(String[] args)
    {
        mxGraph graph = getGraph();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        // Set hierarchical layout, so the nodes are organized horizontally (WEST) or vertically (NORTH)
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.NORTH);
        layout.execute(graph.getDefaultParent());
        JScrollPane scrollPane = new JScrollPane(graphComponent);
        JFrame parent = new JFrame();
        parent.setContentPane(scrollPane);
        parent.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        parent.setSize(1000, 320);
        parent.setVisible(true);
    }

}
