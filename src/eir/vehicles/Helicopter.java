/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir.vehicles;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Ni
 */
public class Helicopter implements PhysicsTickListener
{
    //------------------------------------------------
    // fields
    //------------------------------------------------
    private Node pivotNode;
    private RigidBodyControl control;
    private String heliId;
    private float impulseUp;
    private float torqueZ;      // roll
    private float torqueY;      // yaw
    private float torqueX;      // pitch
    
    //------------------------------------------------
    // constructors
    //------------------------------------------------   
    public Helicopter( String heliId, Node pivotNode, RigidBodyControl control)
    {
        this.heliId = heliId;
        this.pivotNode = pivotNode;
        this.control = control;
        this.impulseUp = 10f;
        this.torqueZ = 0f;
    }
    
    //------------------------------------------------
    // getters/setters
    //------------------------------------------------
    public Node getNode() {return pivotNode;}
    public RigidBodyControl getControl() {return control;}
    
    //------------------------------------------------
    // from super (PhysicsTickListener)
    //------------------------------------------------
    public void prePhysicsTick(PhysicsSpace space, float tpf) 
    {
        // maintain height
        control.applyImpulse(new Vector3f(0, this.impulseUp * tpf, 0), Vector3f.ZERO);
        control.applyTorqueImpulse(new Vector3f(this.torqueX * tpf, this.torqueY * tpf, this.torqueZ * tpf));
    }

    public void physicsTick(PhysicsSpace space, float tpf) 
    {
    }
}

