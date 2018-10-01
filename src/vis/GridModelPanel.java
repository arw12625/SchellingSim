/*
 * The MIT License
 *
 * Copyright 2018 Andrew Wintenberg.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package vis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import sim.ChangeListener;
import sim.Location;
import sim.Resident;
import sim.SimpleResident;
import sim.TorusGraph;

/**
 *
 * @author Andrew
 * @param <T>
 */
public class  GridModelPanel<T> extends ModelPanel {
    
    private final TorusGraph<T> torusGraph;
    private Dimension panelDim;
    private int gridWidth, gridHeight;
    
    private Dimension imageDim;
    private int cellWidth, cellHeight;
    private BufferedImage image;
    
    private ColoringScheme<T> coloringScheme;
    
    private static final int cellGap = 1;
    
    private ChangeListener repainter;
    
    public GridModelPanel(TorusGraph<T> torusGraph, Dimension panelDim, ColoringScheme<T> coloringScheme) {
        super();
        
        this.torusGraph = torusGraph;
        gridWidth = torusGraph.getWidth();
        gridHeight = torusGraph.getHeight();
        this.panelDim = panelDim;
        
        this.coloringScheme = coloringScheme;
        
        //cellWidth = panelDim.width / gridWidth - cellGap;
        //cellHeight = panelDim.height / gridHeight - cellGap;
        
        cellWidth = 4;
        cellHeight = 4;
        imageDim = new Dimension((cellWidth + cellGap) * gridWidth, (cellHeight * cellGap) * gridHeight);
        image = new BufferedImage(imageDim.width, imageDim.height, BufferedImage.TYPE_INT_RGB);
        
        repainter = new ChangeListener() {
            @Override
            public void onChange() {
                GridModelPanel.this.repaint();
            }
        };
    }
    
    @Override
    public Dimension getPreferredSize() {
        return panelDim;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        createGraphImage();
        g.drawImage(image, 0,0, panelDim.width, panelDim.height, null);
    }  
    
    public void createGraphImage() {
        
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,image.getWidth(), image.getHeight());
        for(int i = 0; i < gridHeight; i++) {
            for(int j = 0; j < gridWidth; j++) {
                //int cellY = (int) ((float)i / gridHeight * panelDim.height + cellGap);
                //int cellX = (int) ((float)j / gridWidth * panelDim.width + cellGap);
                int cellY = i * (cellHeight + cellGap);
                int cellX = j * (cellWidth + cellGap);
                Color c = coloringScheme.getColor(torusGraph.getNodeAtIndex(i, j));
                g.setColor(c);
                g.fillRect(cellX, cellY, cellWidth, cellHeight);
            }
        }
    }

    public BufferedImage getGraphImage() {
        return image;
    }
    
    @Override
    public ChangeListener getRepainter() {
        return repainter;
    }
    
    
    
}
