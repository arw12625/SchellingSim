/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 * @param <T>
 * @param <L>
 */
public class SchellingModel<T extends Resident<T, L>, L extends Location<T, L>> {

    private List<L> locations;
    private List<T> residents;
    private UndirectedGraph<L> locationGraph;
    private List<L> vacancies;
    private MovingScheme<T, L> movingScheme;
    private MovingPriorities<T, L> movingPriorities;
    private List<ChangeListener> updateListeners;
    private int iter;

    public SchellingModel(List<L> locations, List<T> residents,
            UndirectedGraph<L> locationGraph, MovingScheme<T, L> movingScheme,
            MovingPriorities<T, L> movingPriorities) {
        this.locations = locations;
        this.residents = residents;
        this.locationGraph = locationGraph;
        this.movingScheme = movingScheme;
        this.movingPriorities = movingPriorities;
        this.iter = 0;
        vacancies = new ArrayList<>();
        computeVacancies();

        this.updateListeners = new ArrayList<>();
    }

    public final void computeVacancies() {
        for (L location : locations) {
            if (location.isVacant()) {
                vacancies.add(location);
            }
        }
    }

    public boolean update() {
        boolean changed = false;
        System.out.println("Updating Model");
        List<T> dissatisfiedRes = new ArrayList<>();
        for (T resident : residents) {
            List<L> neighboringLocs = locationGraph.getNeighbors(resident.getResidenceLocation());

            if (!resident.isSatisfiedWithLocation(neighboringLocs)) {
                dissatisfiedRes.add(resident);
            }
        }
        movingPriorities.orderMovingPriorities(dissatisfiedRes);
        for (T resident : dissatisfiedRes) {
            int newLocIndex = movingScheme.getMovingLocationIndex(vacancies, resident);
            moveResidentToVacancy(resident, newLocIndex);
            changed = true;
        }
        if (changed) {
            for (ChangeListener cl : updateListeners) {
                cl.onChange();
            }
        }
        iter++;
        return changed;
    }
    
    public int getIter() {
        return iter;
    }

    private void moveResidentToVacancy(T resident, int vacancyIndex) {
        L oldLoc = resident.getResidenceLocation();
        L newLoc = vacancies.get(vacancyIndex);
        vacancies.remove(vacancyIndex);
        newLoc.changeResident(resident);
        oldLoc.removeResident();
        vacancies.add(oldLoc);
    }

    public float computeAverageSatisfaction() {
        int numSatisfied = 0;
        for (T resident : residents) {
            List<L> neighboringLocs = locationGraph.getNeighbors(resident.getResidenceLocation());
            if (resident.isSatisfiedWithLocation(neighboringLocs)) {
                numSatisfied++;
            }
        }
        return (float) numSatisfied / residents.size();
    }

    public void addUpdateListener(ChangeListener cl) {
        this.updateListeners.add(cl);
    }

    public void removeUpdateListener(ChangeListener cl) {
        this.updateListeners.remove(cl);
    }

}
