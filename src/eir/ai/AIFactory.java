/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir.ai;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dveyarangi
 */
public class AIFactory {
    
    private AssetManager assetManager;
    
    private int beaconCount = 1;     
    
    private Material mat;
    public AIFactory(AssetManager assetManager)
    {
        this.assetManager = assetManager;
        
        this.mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
    
    }
    
    /**
     * First attempt of extracting nav points from heightmap
     */
    public List <CriticalPoint> extractCriticalPoints(float [] heightmap, Vector3f scale)
    {
        List <CriticalPoint> points = new LinkedList <CriticalPoint> ();
        
        float [] heights = heightmap;
        int size = (int)Math.sqrt(heightmap.length);
        int halfsize = size / 2;
        for(int x = 1; x < size-1; x ++) {
            for(int y = 1; y < size-1; y ++)
            {
                float height = heights[x+y*size];
                
                float height_px = heights[(x-1) + y*size];
                float height_nx = heights[(x+1) + y*size];
                float height_py = heights[x + (y-1)*size];
                float height_ny = heights[x + (y+1)*size];
                
                int xslope = height_px > height && height_nx > height ? -1 : 
                            (height_px < height && height_nx < height ? 1 : 0);
                
                int yslope = height_py > height && height_ny > height ? -1 : 
                            (height_py < height && height_ny < height ? 1 : 0);
                
                
                if(xslope == 0 || yslope == 0)
                    continue; // not critical
                
                // game world location of the point:
                Vector3f loc = new Vector3f((x - halfsize) * scale.x, height,
                                            (y - halfsize) * scale.z);
                
                 CriticalPoint point = null;
                if(xslope == 1 && yslope == 1)
                    point = CriticalPoint.createMaximum(loc);
                else
                if(xslope == -1 && yslope == -1)
                    point = CriticalPoint.createMinimum(loc);
                else
                 if(xslope == -yslope)
                   point = CriticalPoint.createSaddle(loc);
                
                points.add(point);
            }
        }

        
        return points;
    }
 
    /**
     * Create debug dummy.
     * @param x
     * @param y
     * @param z
     * @return 
     */
    public Geometry createBeacon(float x, float y, float z)
    {
        Sphere mesh = new Sphere(8, 8, 1);
        
                 
        Geometry beacon = new Geometry("beacon_" + beaconCount++, mesh);    
        beacon.setMaterial(mat);
        beacon.setLocalTranslation(x, y, z);
        
        return beacon;
    }

}
