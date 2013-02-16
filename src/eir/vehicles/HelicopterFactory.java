/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir.vehicles;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Ni
 */
public class HelicopterFactory 
{
    //------------------------------------------------
    // constants
    //------------------------------------------------
    private static final float HELI_LENGTH = 5f;
    private static final float HELI_WIDTH = 3f;
    private static final float HELI_HEIGHT = 2f;
    
    //------------------------------------------------
    // fields
    //------------------------------------------------
    private final AssetManager assetManager;
    private final BulletAppState physics;
    private final Material heliMaterial;
    
    //------------------------------------------------
    // constructor
    //------------------------------------------------ 
    public HelicopterFactory(AssetManager assetManager, BulletAppState physics)
    {
        this.assetManager = assetManager;
        this.physics = physics;
        
        this.heliMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        heliMaterial.setBoolean("UseMaterialColors", true);
        heliMaterial.setColor("Ambient",  ColorRGBA.Red);
        heliMaterial.setColor("Diffuse",  ColorRGBA.Red);
        heliMaterial.setColor("Specular", ColorRGBA.White);
        heliMaterial.setFloat("Shininess", 12);
    }
    
    //------------------------------------------------
    // methods
    //------------------------------------------------
    public Helicopter createHelicopter(String heliId, float x, float y, float z)
    {
        // pos
        Vector3f location = new Vector3f(x,y,z);
        
        // collision and control
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        BoxCollisionShape box = new BoxCollisionShape(new Vector3f(HELI_WIDTH, HELI_HEIGHT, HELI_LENGTH));
        compoundShape.addChildShape(box, new Vector3f(0, 0, 0));
        RigidBodyControl control = new RigidBodyControl(compoundShape);
        
        // create mesh and geo
        Box boxMesh = new Box(HELI_WIDTH, HELI_HEIGHT, HELI_LENGTH);
        Geometry boxGeo = new Geometry("box", boxMesh);
        boxGeo.setMaterial(heliMaterial);
        
        //node
        Node node = new Node("vehicleNode");
        node.addControl(control);
        node.attachChild(boxGeo);
        control.setPhysicsLocation(location);
        
        // temp?
        //----------------------------------------------
        control.setLinearDamping(0.3f);
        control.setAngularDamping(0.3f);
        //----------------------------------------------
        
        physics.getPhysicsSpace().add(control);
        Helicopter heli = new Helicopter(heliId, node, control);
        physics.getPhysicsSpace().addTickListener(heli);
        
        return heli;
    }
}
