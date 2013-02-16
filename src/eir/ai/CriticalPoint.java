/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir.ai;

import com.jme3.math.Vector3f;

/**
 *
 * @author dveyarangi
 */
public class CriticalPoint {
    
    /** critical point type enum */
    enum CPType { MAXIMUM, MINIMUM, SADDLE };
    
    /**
     * critical point type
     */
    private CPType type;
    
    /**
     * critical point game world location
     */
    private Vector3f location;
        
    
    public static CriticalPoint createMaximum(Vector3f point) { return new CriticalPoint(CPType.MAXIMUM, point); }
    public static CriticalPoint createMinimum(Vector3f point) { return new CriticalPoint(CPType.MINIMUM, point); }
    public static CriticalPoint createSaddle(Vector3f point) { return new CriticalPoint(CPType.SADDLE, point); }
    
    private CriticalPoint(CPType type, Vector3f location)
    {
        this.type = type;
        this.location = location;
    }
    
    public Vector3f getLocation() { return location; }
}
