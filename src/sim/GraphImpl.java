/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andrew
 */
public class GraphImpl<T> implements ModifiableUndirectedGraph<T> {
    
    private List<T> nodes;
    private Map<T, List<T>> edges;
    
    private List<T> immutableNodes;
    
    public GraphImpl() {
        nodes = new ArrayList<>();
        edges = new HashMap<>();
        
        immutableNodes = Collections.unmodifiableList(nodes);
    }
    
    public GraphImpl(List<T> initialNodes) {
        this();
        for(T node : initialNodes) {
            addNode(node);
        }
    }
    
    @Override
    public boolean addEdge(T node1, T node2) {
        edges.get(node1).add(node2);
        edges.get(node2).add(node1);
        return true;
    }

    @Override
    public boolean removeEdge(T node1, T node2) {
        edges.get(node1).remove(node2);
        edges.get(node2).remove(node1);
        return true;
    }

    @Override
    public boolean addNode(T node) {
        nodes.add(node);
        edges.put(node, new ArrayList<>());
        return true;
    }

    @Override
    public boolean removeNode(T node) {
        for(List<T> nodeList : edges.values()) {
            nodeList.remove(node);
        }
        edges.remove(node);
        nodes.remove(node);
        return true;
    }

    @Override
    public List<T> getNeighbors(T node) {
        return edges.get(node);
    }

    @Override
    public List<T> getNodes() {
        return immutableNodes;
    }

    
}
