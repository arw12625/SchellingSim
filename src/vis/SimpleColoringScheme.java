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

import java.awt.Color;
import java.util.Map;
import sim.Location;
import sim.SimpleResident;
import vis.ColoringScheme;

/**
 *
 * @author Andrew Wintenberg
 * @param <E>
 * @param <T>
 * @param <L>
 */
public class SimpleColoringScheme<E extends Enum<E>, T extends SimpleResident<E,T,L>, L extends Location<T,L>> implements ColoringScheme<L>{

    private Map<E, Color> colorMap;
    private Color vacantColor;
    
    public static final Color defaultColor = Color.BLACK;
    
    public SimpleColoringScheme(Map<E, Color> colorMap, Color vacantColor) {
        this.colorMap = colorMap;
        this.vacantColor = vacantColor;
    }
    
    @Override
    public Color getColor(L loc) {
        if(loc.isVacant()) {
            return vacantColor;
        } else {
            return colorMap.get(loc.getResident().getType());
        }
    }
    
}
