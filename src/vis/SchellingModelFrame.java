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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sim.Location;
import sim.Resident;
import sim.SchellingModel;

/**
 *
 * @author Andrew Wintenberg
 * @param <T>
 * @param <L>
 */
public class SchellingModelFrame<T extends Resident<T, L>, L extends Location<T, L>> extends JFrame {

    
    private SchellingModel<T,L> model;
    private JComboBox modelSelection;
    private ModelPanel panel;
    private ModelControlPanel controlPanel;
    
    private JPanel panelWrapper;
    private JPanel controlPanelWrapper;
    
    private int width, height;
    
    public SchellingModelFrame(int width, int height) {
        super("Schelling Model Simulation");
        
        this.width = width;
        this.height = height;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container con = this.getContentPane();
        GridBagConstraints gc = new GridBagConstraints();
        con.setLayout(new GridBagLayout());
        
        gc.gridy = 0;
        panelWrapper = new JPanel();
        con.add(panelWrapper, gc);
        
        gc.gridy = 1;
        controlPanelWrapper = new JPanel();
        con.add(controlPanelWrapper, gc);
        
        this.setPreferredSize(new Dimension(width, height));
    }
    
    
    public void setModel(SchellingModel<T,L> model) {
        this.model = model;
    }
    
    public void setModelPanel(ModelPanel panel) {
        if(this.panel != null) {
            panelWrapper.remove(this.panel);
            model.removeUpdateListener(panel.getRepainter());
        }
        this.panel = panel;
        panelWrapper.add(this.panel);
        model.addUpdateListener(panel.getRepainter());
        
        
    }
    public void setModelControlPanel(ModelControlPanel controlPanel) {
        if(this.controlPanel != null) {
            controlPanelWrapper.remove(this.controlPanel);
        }
        this.controlPanel = controlPanel;
        controlPanelWrapper.add(this.controlPanel);
        
    }
    
}
