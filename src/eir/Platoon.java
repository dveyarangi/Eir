/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir;

import com.jme3.math.Vector2f;

/**
 *
 * @author dveyarangi
 */
public class Platoon {
    
    private Vector2f spawnPoint;
    
    public Platoon(float startx, float starty)
    {
        spawnPoint = new Vector2f(startx, starty);
    }
    
    public Vector2f getSpawnPoint() { return spawnPoint; }
}
