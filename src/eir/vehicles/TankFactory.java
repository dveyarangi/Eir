/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir.vehicles;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;


/**
 *
 * @author dveyarangi
 */
public class TankFactory {
    
    private final AssetManager assetManager;
    
    private final BulletAppState physics;
    
    
    private static final float TANK_LENGTH = 5;
    private static final float TANK_WIDTH = 5;
    private static final float TANK_HEIGHT = 5;
    private static final float CHASSIS_WIDTH = 1;
    private static final float TURRET_RADIUS = 2f;
    private static final float WHEEL_RADIUS = 3f;
    private static final float WHEEL_WIDTH = 1;
    
    private static final float CANNON_RADIUS = 0.2f;
    private static final float CANNON_LENGTH = 10;
    
    private Material mat;


    
    public TankFactory(AssetManager assetManager,BulletAppState physics)
    {
        this.assetManager = assetManager;
        this.physics = physics;
        mat = createMaterial();
    }
    
    public Tank createTank(String tankId, float x, float y, float z)
    {
        Vector3f location = new Vector3f(x,y,z);

        //create a compound shape and attach the BoxCollisionShape for the car body at 0,1,0
        //this shifts the effective center of mass of the BoxCollisionShape to 0,-1,0
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        BoxCollisionShape box = new BoxCollisionShape(new Vector3f(TANK_WIDTH, TANK_HEIGHT, TANK_LENGTH));
        compoundShape.addChildShape(box, new Vector3f(0, 1, 0));
        VehicleControl vehicle = new VehicleControl(compoundShape, 40000);

        //create vehicle node
        Node vehicleNode=new Node("vehicleNode");
        vehicleNode.addControl(vehicle);
        vehicle.setPhysicsLocation(location);
        //setting suspension values for wheels, this can be a bit tricky
        //see also https://docs.google.com/Doc?docid=0AXVUZ5xw6XpKZGNuZG56a3FfMzU0Z2NyZnF4Zmo&hl=en
        float stiffness = 60.0f;//200=f1 car
        float compValue = .3f; //(should be lower than damp)
        float dampValue = .4f;
        vehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionStiffness(stiffness);
        vehicle.setMaxSuspensionForce(10000.0f);

        //Create four wheels and add them at their locations
        Vector3f wheelDirection = new Vector3f(0, -1, 0); // was 0, -1, 0
        Vector3f wheelAxle = new Vector3f(-1, 0, 0); // was -1, 0, 0
        float radius = 0.5f;
        float restLength = 0.3f;
        float yOff = 0f;
        float xOff = 2*TANK_WIDTH/3;
        float zOff = 2*TANK_WIDTH/3;
        Sphere wheelMesh = new Sphere(16, 16, WHEEL_RADIUS);
//        Cylinder wheelMesh = new Cylinder(16, 16, WHEEL_RADIUS, WHEEL_RADIUS * 0.6f, true);

        Node node1 = new Node("wheel 1 node");
        Geometry wheels1 = new Geometry("wheel 1", wheelMesh);
        node1.attachChild(wheels1);
        wheels1.rotate(0, FastMath.HALF_PI, 0);
        wheels1.setMaterial(mat);
        vehicle.addWheel(node1, new Vector3f(-xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node2 = new Node("wheel 2 node");
        Geometry wheels2 = new Geometry("wheel 2", wheelMesh);
        node2.attachChild(wheels2);
        wheels2.rotate(0, FastMath.HALF_PI, 0);
        wheels2.setMaterial(mat);
        vehicle.addWheel(node2, new Vector3f(xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node3 = new Node("wheel 3 node");
        Geometry wheels3 = new Geometry("wheel 3", wheelMesh);
        node3.attachChild(wheels3);
        wheels3.rotate(0, FastMath.HALF_PI, 0);
        wheels3.setMaterial(mat);
        vehicle.addWheel(node3, new Vector3f(-xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        Node node4 = new Node("wheel 4 node");
        Geometry wheels4 = new Geometry("wheel 4", wheelMesh);
        node4.attachChild(wheels4);
        wheels4.rotate(0, FastMath.HALF_PI, 0);
        wheels4.setMaterial(mat);
        vehicle.addWheel(node4, new Vector3f(xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        vehicleNode.attachChild(node1);
        vehicleNode.attachChild(node2);
        vehicleNode.attachChild(node3);
        vehicleNode.attachChild(node4);

                Node turretNode = new Node(createHeadNodeName(tankId));

        Sphere turretSphere = new Sphere(10, 10, TURRET_RADIUS);
        Geometry turret = new Geometry(tankId + "-turret", turretSphere);
        turret.setLocalTranslation(0, 0, 0);
        turret.setMaterial(mat);
        turretNode.attachChild(turret);
        
        Cylinder cannonCylinder = new Cylinder(6, 6, CANNON_RADIUS, CANNON_LENGTH);

        Geometry cannon = new Geometry(tankId + "-cannon", cannonCylinder);
        cannon.rotate(0, (float)Math.PI/2, 0);
        cannon.setLocalTranslation(CANNON_LENGTH/2,0,0);
        
        cannon.setMaterial(mat);
        turretNode.attachChild(cannon);
        turretNode.setLocalTranslation(0, TANK_HEIGHT + TURRET_RADIUS/5, 0);
        vehicleNode.attachChild(turretNode);
        
        physics.getPhysicsSpace().add(vehicle);
        
        return new Tank(tankId, vehicleNode);
    }
    
    public Material createMaterial() 
    {
        Material mat = new Material(assetManager,
             "Common/MatDefs/Light/Lighting.j3md");
 //       mat.setColor("Color", ColorRGBA.Blue);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient",  ColorRGBA.Orange);
        mat.setColor("Diffuse",  ColorRGBA.Orange);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 12);           
        return mat;
    }
    
    public static String createHeadNodeName(String tankId)
    {
        return tankId + "-head";
    }
}
