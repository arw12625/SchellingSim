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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.SwingWorker;
import javax.swing.text.NumberFormatter;
import sim.ChangeListener;
import sim.SchellingModel;

/**
 *
 * @author Andrew Wintenberg
 */
public class DefaultModelControlPanel extends ModelControlPanel {

    private final SchellingModel model;
    private final ChangeListener onRefresh;
    private final ChangeListener onFinish;
    private boolean running;
    private int numStepsPlay = 1000;

    public DefaultModelControlPanel(SchellingModel model, ChangeListener onRefresh, ChangeListener onFinish) {
        super();

        this.model = model;
        this.onRefresh = onRefresh;
        this.onFinish = onFinish;

        JButton nextButton = new JButton("Next Step");
        this.add(nextButton);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    running = true;
                    (new MultiStepWorker(1)).execute();
                }
            }
        });

        JButton playButton = new JButton("Play");
        this.add(playButton);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    running = true;
                    (new MultiStepWorker(numStepsPlay)).execute();
                }
            }
        });

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);

        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false); //this is the key

        JFormattedTextField numStepsField = new JFormattedTextField(numberFormatter);
        numStepsField.setColumns(10);
        numStepsField.setValue(numStepsPlay);
        this.add(numStepsField);
        numStepsField.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                numStepsPlay = ((Number)numStepsField.getValue()).intValue();
            }
        });
        
    }

    private class MultiStepWorker extends SwingWorker<Boolean, Boolean> {

        private int numSteps;

        public MultiStepWorker(int numSteps) {
            this.numSteps = numSteps;
        }

        @Override
        protected Boolean doInBackground() {
            int step = 0;
            boolean finished = false;
            boolean modelChanged = false;
            while (!finished && !isCancelled()) {
                modelChanged = model.update();
                publish(!modelChanged);
                finished = !modelChanged || ++step >= numSteps;
            }
            return !modelChanged;
        }

        @Override
        protected void process(List<Boolean> obj) {
            onRefresh.onChange();
            if (!obj.isEmpty()) {
                System.out.println("Equilibrium: " + obj.get(obj.size() - 1));
            }
        }

        @Override
        protected void done() {
            try {
                Boolean result = get();
                System.out.println("Iter: " + model.getIter());
                System.out.println("Final Equilibrium: " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            onFinish.onChange();
            running = false;
        }

    }

}
