/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim;

/**
 *
 * @author Andrew
 */
public interface ModifiableUndirectedGraph<T> extends UndirectedGraph<T> {
    
    public boolean addEdge(T node1, T node2);
    public boolean removeEdge(T node1, T node2);
    
    public boolean addNode(T node);
    public boolean removeNode(T node);
}
