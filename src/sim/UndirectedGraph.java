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
 * @param <T>
 */
public interface UndirectedGraph<T> {
    
    public List<T> getNodes();
    public List<T> getNeighbors(T node);
    
}
