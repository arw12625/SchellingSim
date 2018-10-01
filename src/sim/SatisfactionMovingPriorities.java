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
package sim;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andrew Wintenberg
 * @param <E>
 * @param <T>
 * @param <L>
 */
public class SatisfactionMovingPriorities<E extends Enum<E>, T extends SimpleResident<E, T, L>, L extends Location<T, L>> implements MovingPriorities<T, L> {

    @Override
    public void orderMovingPriorities(List<T> movers) {
        Map<T,Float> satisMap = new HashMap<>();
        for(T res : movers) {
            satisMap.put(res, res.getSatisfaction());
        }
        Collections.sort(movers, new Comparator<T>() {
            public int compare(T left, T right) {
                return Float.compare(satisMap.get(left), satisMap.get(right));
            }
        });
    }

}
