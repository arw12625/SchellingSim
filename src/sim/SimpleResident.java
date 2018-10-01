/*
 * The MIT License
 *
 * Copyright 2018 Andrew.
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

import java.util.List;

/**
 *
 * @author Andrew
 * @param <E>
 * @param <T>
 * @param <L>
 */
public class SimpleResident<E extends Enum<E>,T extends SimpleResident<E,T,L>, L extends Location<T,L>> extends Resident<T,L> {
    
    private final float desiredRatioSame;
    private final E type;
    private float satisfaction;
    
    public SimpleResident(float desiredRatioSame, E type) {
        super();
        this.desiredRatioSame = desiredRatioSame;
        this.type = type;
    }
    
    public E getType() {
        return type;
    }
    
    @Override
    public boolean isSatisfiedWithLocation(List<L> neighboringLocations) {
        int sameCount = 0;
        int totalCount = 0;
        
        for(Location<T,L> location : neighboringLocations) {
            if(!location.isVacant()) {
                totalCount++;
                if(location.getResident().getType().equals(this.getType())) {
                    sameCount++;
                }
            }
        }
        
        satisfaction = (float)sameCount / totalCount;
        
        return (satisfaction >= desiredRatioSame);
    }
    
    public float getSatisfaction() {
        return satisfaction;
    }
     

    
}
