package eir;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.terrain.geomipmap.TerrainQuad;
import eir.terrain.TerrainFactory;
import eir.vehicles.Helicopter;
import eir.vehicles.HelicopterFactory;
import eir.vehicles.Tank;
import eir.vehicles.TankFactory;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    boolean wireframe = false;
    boolean triPlanar = false;
    boolean wardiso = false;
    boolean minnaert = false;
    protected BitmapText hintText;
    PointLight pl;
    Geometry lightMdl;


    private Tank tank;
    @Override
    public void initialize() {
        super.initialize();

 //       loadHintText();
    }
       


    @Override
    public void simpleInitApp() {
 //       setupKeys();

        BulletAppState physics = new BulletAppState();
       
        stateManager.attach(physics);
        
        
        TerrainFactory terrainFactory = new TerrainFactory(assetManager, getCamera());
        TerrainQuad terrain = terrainFactory.createTerrain();
        
        CollisionShape terrainShape =
            CollisionShapeFactory.createMeshShape((Node) terrain);
        RigidBodyControl landscape = new RigidBodyControl(terrainShape, 0);
        terrain.addControl(landscape);
        BoundingVolume box = terrain.getWorldBound();
        
        physics.getPhysicsSpace().add(terrain);
        
        rootNode.attachChild(terrain);
        rootNode.attachChild(terrainFactory.createSky());

        physics.getPhysicsSpace().setGravity(new Vector3f(0,-10f,0));
        TankFactory factory = new TankFactory(assetManager, physics);
        
        int TANKS = 5;
        
        Platoon [] teams = new Platoon []{
            new Platoon(-200, -200),
            new Platoon(200, 200) };
        
        for(Platoon platoon : teams) {
            for(int i = 0; i < TANKS; i ++) {
                float tankX = platoon.getSpawnPoint().x + 30f*(float)Math.cos(i * Math.PI*2/TANKS);
                float tankZ = platoon.getSpawnPoint().y + 30f*(float)Math.sin(i * Math.PI*2/TANKS);
                float tankY = terrain.getHeightmapHeight(new Vector2f(tankX, tankZ))+20;
                tank = factory.createTank("tank1", tankX, tankY, tankZ);
                rootNode.attachChild(tank.getNode());
            }
        }
        //Material debugMat = assetManager.loadMaterial("Common/Materials/VertexColor.j3m");
        //terrain.generateDebugTangents(debugMat);
        
        //heli
        //----------------------------------------------------------------------
        HelicopterFactory heliFactory = new HelicopterFactory(assetManager, physics);
        float heliX = 0f;
        float heliZ = 0f;
        float heliY = terrain.getHeightmapHeight(new Vector2f(heliX, heliZ))+100;
        Helicopter heli = heliFactory.createHelicopter("heliId", heliX, heliY, heliZ);
        rootNode.attachChild(heli.getNode());
        //----------------------------------------------------------------------
        
        
        DirectionalLight light = new DirectionalLight();
        light.setDirection((new Vector3f(-0.1f, -0.1f, -0.1f)).normalize());
        rootNode.addLight(light);

        cam.setLocation(new Vector3f(0, 300, 0));
        cam.lookAtDirection(new Vector3f(0, -1, 0).normalizeLocal(), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(400);
        
        rootNode.attachChild(createAxisMarker(20));
    }

 /*   public void loadHintText() {
        hintText = new BitmapText(guiFont, false);
        hintText.setSize(guiFont.getCharSet().getRenderedSize());
        hintText.setLocalTranslation(0, getCamera().getHeight(), 0);
        hintText.setText("Hit T to switch to wireframe,  P to switch to tri-planar texturing");
        guiNode.attachChild(hintText);
    }*/

 /*   private void setupKeys() {
        flyCam.setMoveSpeed(50);
        inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(actionListener, "wireframe");
        inputManager.addMapping("triPlanar", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "triPlanar");
        inputManager.addMapping("WardIso", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addListener(actionListener, "WardIso");
        inputManager.addMapping("Minnaert", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addListener(actionListener, "Minnaert");
    }*/
/*    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean pressed, float tpf) {
            if (name.equals("wireframe") && !pressed) {
                wireframe = !wireframe;
                if (!wireframe) {
                    terrain.setMaterial(matWire);
                } else {
                    terrain.setMaterial(matTerrain);
                }
            } else if (name.equals("triPlanar") && !pressed) {
                triPlanar = !triPlanar;
                if (triPlanar) {
                    matTerrain.setBoolean("useTriPlanarMapping", true);
                    // planar textures don't use the mesh's texture coordinates but real world coordinates,
                    // so we need to convert these texture coordinate scales into real world scales so it looks
                    // the same when we switch to/from tr-planar mode
                    matTerrain.setFloat("DiffuseMap_0_scale", 1f / (float) (512f / grassScale));
                    matTerrain.setFloat("DiffuseMap_1_scale", 1f / (float) (512f / dirtScale));
                    matTerrain.setFloat("DiffuseMap_2_scale", 1f / (float) (512f / rockScale));
                    matTerrain.setFloat("DiffuseMap_3_scale", 1f / (float) (512f / rockScale));
                    matTerrain.setFloat("DiffuseMap_4_scale", 1f / (float) (512f / rockScale));
                } else {
                    matTerrain.setBoolean("useTriPlanarMapping", false);
                    matTerrain.setFloat("DiffuseMap_0_scale", grassScale);
                    matTerrain.setFloat("DiffuseMap_1_scale", dirtScale);
                    matTerrain.setFloat("DiffuseMap_2_scale", rockScale);
                    matTerrain.setFloat("DiffuseMap_3_scale", rockScale);
                    matTerrain.setFloat("DiffuseMap_4_scale", rockScale);
                }
            }
        }
    };*/

    protected Node createAxisMarker(float arrowSize) {

        Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redMat.getAdditionalRenderState().setWireframe(true);
        redMat.setColor("Color", ColorRGBA.Red);
        
        Material greenMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        greenMat.getAdditionalRenderState().setWireframe(true);
        greenMat.setColor("Color", ColorRGBA.Green);
        
        Material blueMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blueMat.getAdditionalRenderState().setWireframe(true);
        blueMat.setColor("Color", ColorRGBA.Blue);

        Node axis = new Node();

        // create arrows
        Geometry arrowX = new Geometry("arrowX", new Arrow(new Vector3f(arrowSize, 0, 0)));
        arrowX.setMaterial(redMat);
        Geometry arrowY = new Geometry("arrowY", new Arrow(new Vector3f(0, arrowSize, 0)));
        arrowY.setMaterial(greenMat);
        Geometry arrowZ = new Geometry("arrowZ", new Arrow(new Vector3f(0, 0, arrowSize)));
        arrowZ.setMaterial(blueMat);
        axis.attachChild(arrowX);
        axis.attachChild(arrowY);
        axis.attachChild(arrowZ);

        //axis.setModelBound(new BoundingBox());
        return axis;
    }
    
    public void simpleUpdate(float tpf)
    {
        tank.rotateTurret(tpf);
    }
    

}
