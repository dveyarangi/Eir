/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir.vehicles;

import com.jme3.scene.Node;

/**
 */
public class Tank {
    
    private String tankId;
    
    private Node pivotNode;
    
    private Node turretNode;
   
    public Tank(String tankId, Node pivotNode)
    {
        this.tankId = tankId;
        this.pivotNode = pivotNode;
        
        
        this.turretNode = (Node)pivotNode.getChild(TankFactory.createHeadNodeName(tankId));
        if(turretNode == null)
            throw new IllegalArgumentException("No turret node found.");
    }
    
    public String getTankId() { return tankId; }
    
    public Node getNode() { return pivotNode; }
    
    public void rotateTurret(float a)
    {
        turretNode.rotate(0, a, 0);
    }
    
}
