package org.mospower;

/**
 * Interface which allows getting access to inner data for TripletDeque
 */
public interface Containerable{

    Object[] getContainerByIndex(int cIndex);

}
