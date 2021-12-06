package org.springframework.ntfh.interfaces;

/**
 * Should be implemented by any card that exists on the game table (characters,
 * market cards, usable cards, enemy cards...). It forces them to have a front
 * and a back image, as well as a location in the desk where the card is
 * located.
 * 
 * @author andrsdt
 * 
 */
public interface PhysicalCard {

    /**
     * Location enum tellign where the card is located in the desk, among the
     * available options for that card
     * 
     * @author andrsdt
     */
    public Location getLocation();

    /**
     * String route to the front image of the card
     * 
     * @author andrsdt
     */
    public String getFrontImage();

    /**
     * String route to the back image of the card
     * 
     * @author andrsdt
     */
    public String getBackImage();
}
