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
public class WorkflowExample extends JFrame
{
    private static final long serialVersionUID = -2707712944901661771L;
    
    private static final int centerX = 500;
    private static final int centerY = 160;



    public WorkflowExample()
    {
        super("Test Workflow");

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


        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        // Set hierarchical layout, so the nodes are organized horizontally
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.WEST);
        layout.execute(graph.getDefaultParent());
    }

    public static void main(String[] args)
    {
        WorkflowExample frame = new WorkflowExample();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 320);
        frame.setVisible(true);
    }

}
