/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim;

import java.util.List;

/**
 *
 * @author Andrew
 */
public class TorusGraph<T> extends GraphImpl<T> {
    
    private final List<List<T>> indexedNodes;
    private final int width,height;
    
    public TorusGraph(int width, int height, List<List<T>> indexedNodes) {
        super();
        this.indexedNodes = indexedNodes;
        this.height = height;
        this.width = width;
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                addNode(indexedNodes.get(i).get(j));
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                addEdge(indexedNodes.get(i).get(j), indexedNodes.get((i + 1)%height).get(j));
                addEdge(indexedNodes.get(i).get(j), indexedNodes.get(i).get((j + 1)%width));
                addEdge(indexedNodes.get(i).get(j), indexedNodes.get((i + 1)%height).get((j + 1)%width));
                addEdge(indexedNodes.get(i).get(j), indexedNodes.get((i +height - 1)%height).get((j + 1)%width));
            }
        }
       
    }
    
    public T getNodeAtIndex(int i, int j) {
        return indexedNodes.get(i).get(j);
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    
}
