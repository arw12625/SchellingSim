/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import vis.SimpleColoringScheme;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import sim.ChangeListener;
import sim.GridCoordinateLocation;
import sim.LastMovingScheme;
import sim.Location;
import sim.MovingPriorities;
import sim.MovingScheme;
import sim.RandomMovingScheme;
import sim.SatisfactionMovingPriorities;
import sim.SchellingModel;
import sim.SimpleResident;
import sim.TorusGraph;
import sim.UndirectedGraph;
import vis.DefaultModelControlPanel;
import vis.GridModelPanel;
import vis.ModelPanel;
import vis.SchellingModelFrame;

/**
 *
 * @author Andrew
 */
public class SchellingSim {

    public enum ResidentTypes {
        RED, BLUE, GREEN;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int width = 1000, height = 1000;
        
        List<TestLocation> locationList = new ArrayList<>(width * height);
        List<List<TestLocation>> indexedNodes = new ArrayList<>(height);
        for(int i = 0; i < height; i++) {
            List<TestLocation> row = new ArrayList<>(width);
            for(int j = 0; j < width; j++) {
                TestLocation loc = new TestLocation(i,j);
                locationList.add(loc);
                row.add(loc);
            }
            indexedNodes.add(row);
        }
        TorusGraph<TestLocation> torusGraph = 
                new TorusGraph<>(width, height, indexedNodes);
        
        int numRed = (int) (width * height * 0.45);
        int numBlue = (int) (width * height * 0.45);
        int numGreen = (int) (width * height * 0.0);
        float desiredRatio = 0.8f;
        List<TestResident> residentList = new ArrayList<>(numRed + numBlue + numGreen);
        
        
        for(int i = 0; i < numRed; i++) {
            TestResident resident = new TestResident(desiredRatio, ResidentTypes.RED);
            int randomRow, randomCol;
            do {
                randomRow = ThreadLocalRandom.current().nextInt(0, height);
                randomCol = ThreadLocalRandom.current().nextInt(0, width);
            } while(!torusGraph.getNodeAtIndex(randomRow, randomCol).isVacant());
            torusGraph.getNodeAtIndex(randomRow, randomCol).changeResident(resident);
            residentList.add(resident);
        }
        
        for(int i = 0; i < numGreen; i++) {
            TestResident resident = new TestResident(desiredRatio, ResidentTypes.GREEN);
            int randomRow, randomCol;
            do {
                randomRow = ThreadLocalRandom.current().nextInt(0, height);
                randomCol = ThreadLocalRandom.current().nextInt(0, width);
            } while(!torusGraph.getNodeAtIndex(randomRow, randomCol).isVacant());
            torusGraph.getNodeAtIndex(randomRow, randomCol).changeResident(resident);
            residentList.add(resident);
        }
        for(int i = 0; i < numBlue; i++) {
            TestResident resident = new TestResident(desiredRatio, ResidentTypes.BLUE);
            int randomRow, randomCol;
            do {
                randomRow = ThreadLocalRandom.current().nextInt(0, height);
                randomCol = ThreadLocalRandom.current().nextInt(0, width);
            } while(!torusGraph.getNodeAtIndex(randomRow, randomCol).isVacant());
            torusGraph.getNodeAtIndex(randomRow, randomCol).changeResident(resident);
            residentList.add(resident);
        }
        
        MovingScheme<TestResident,TestLocation> movingScheme = new RandomMovingScheme<>();
        MovingPriorities<TestResident, TestLocation> movingPriorities = new SatisfactionMovingPriorities<>();
        //MovingScheme<SimpleResident<ResidentTypes>> movingScheme = new LastMovingScheme<>();
        SchellingModel<TestResident,TestLocation> model = new SchellingModel<>(locationList, residentList, torusGraph, movingScheme, movingPriorities);
        
        JFrame frame = new JFrame("Schelling Simulation");
        frame.setPreferredSize(new Dimension(1200,1000));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        Map<ResidentTypes, Color> colorMap = new HashMap<>();
        /*colorMap.put(ResidentTypes.RED, Color.RED); 
        colorMap.put(ResidentTypes.BLUE, Color.BLUE);
        colorMap.put(ResidentTypes.GREEN, Color.GREEN);
        */
        colorMap.put(ResidentTypes.RED, new Color(210, 40, 80)); 
        colorMap.put(ResidentTypes.BLUE, new Color(70, 50, 210));
        colorMap.put(ResidentTypes.GREEN, new Color(50, 200, 50));
        GridModelPanel<TestLocation> gridModelPanel = new GridModelPanel<>(torusGraph, new Dimension(800, 800),
                new SimpleColoringScheme<>(colorMap, Color.BLACK));
        ChangeListener statsCallback = new ChangeListener() {
            @Override
            public void onChange() {
                //System.out.println("Iterations: " + iter);
                System.out.println("Satisfaction: " + model.computeAverageSatisfaction());
                System.out.println(computeStats(residentList, torusGraph));
                BufferedImage image = gridModelPanel.getGraphImage();
                try {
                    ImageIO.write(image, "PNG", new BufferedOutputStream(new FileOutputStream(
                            new File(String.format("res/dist_%02d.png", (int)(desiredRatio*100))))));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        DefaultModelControlPanel controlPanel = new DefaultModelControlPanel(model, gridModelPanel.getRepainter(), statsCallback);
        
        //model.addUpdateListener(gridModelPanel.getRepainter());        
        
        Container con = frame.getContentPane();
        con.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        gc.gridy = 0;
        con.add(gridModelPanel, gc);
        
        gc.gridy = 1;
        con.add(controlPanel, gc);
        
        frame.pack();
        frame.setVisible(true);

        /*
        int maxIter = 60;
        int iter;
        boolean changed = true;
        for(iter = 0; iter < maxIter && changed; iter++) {
            changed = model.update();
        }
         
        System.out.println("Iterations: " + iter);
        System.out.println("Satisfaction: " + model.computeAverageSatisfaction());
        System.out.println(computeStats(residentList, torusGraph));
        */
    }
    
    private static SchellingStats computeStats(List<TestResident> residentList,
            TorusGraph<TestLocation> graph) {
            
        SchellingStats stats = new SchellingStats();
        
        float sumRatioSame = 0;
        float sumRatioDiff = 0;
        float sumRatioVaca = 0;
        for(TestResident res : residentList) {
            
            List<TestLocation> neighboringLocs = 
                    graph.getNeighbors(res.getResidenceLocation());
            int numSame = 0;
            int numDiff = 0;
            int numVaca = 0;
            for(TestLocation loc : neighboringLocs) {
                if(!loc.isVacant()) {
                    if(loc.getResident().getType().equals(res.getType())) {
                        numSame++;
                    } else {
                        numDiff++;
                    }
                } else {
                    numVaca++;
                }
            }
            sumRatioSame += (float)numSame / neighboringLocs.size();
            sumRatioDiff += (float)numDiff / neighboringLocs.size();
            sumRatioVaca += (float)numVaca / neighboringLocs.size();
        
        }
        stats.averageSimilarity = sumRatioSame / residentList.size();
        stats.averageDisparity = sumRatioDiff / residentList.size();
        stats.averageVacancy = sumRatioVaca / residentList.size();
        
        return stats;
    }
    
    
    public static class SchellingStats {
        
        public float averageSimilarity;
        public float averageDisparity;
        public float averageVacancy;
        
        public SchellingStats() {
            averageSimilarity = 0;
            averageDisparity = 0;
            averageVacancy = 0;
                    
        }
        
        @Override
        public String toString() {
            return "Similarity: " + averageSimilarity
                    + "\nDisparity: " + averageDisparity
                    + "\nVacancy: " + averageVacancy;
        }
    }
    
    private static class TestResident extends SimpleResident<ResidentTypes, TestResident, TestLocation> {
        public TestResident(float ratio, ResidentTypes type) {
            super(ratio, type);
        }
    }
    
    private static class TestLocation extends GridCoordinateLocation<TestResident, TestLocation> {
        public TestLocation(int i, int j) {
            super(i,j);
        }
    }
}
