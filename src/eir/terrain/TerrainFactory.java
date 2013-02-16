/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

/**
 *
 * @author dveyarangi
 */
public class TerrainFactory {
    
    
    private float grassScale = 64;
    private float dirtScale = 16;
    private float rockScale = 128;
    
    private AssetManager assetManager;
    private Camera camera;
    
    public TerrainFactory(AssetManager assetManager, Camera camera)
    {
        this.assetManager = assetManager;
        this.camera = camera;
    }
    
    public TerrainQuad createTerrain()
    {
       
    
        TerrainQuad terrain;
 
          
        // First, we load up our textures and the heightmap texture for the terrain
        
        // TERRAIN TEXTURE material
        Material matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matTerrain.setBoolean("useTriPlanarMapping", false);
        matTerrain.setFloat("Shininess", 0.0f);

        // ALPHA map (for splat textures)
        matTerrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alpha1.png"));
        matTerrain.setTexture("AlphaMap_1", assetManager.loadTexture("Textures/Terrain/splat/alpha2.png"));
        // this material also supports 'AlphaMap_2', so you can get up to 12 diffuse textures
        
        // HEIGHTMAP image (for the terrain heightmap)
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
        
        // DIRT texture, Diffuse textures 0 to 3 use the first AlphaMap
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap", dirt);
        matTerrain.setFloat("DiffuseMap_0_scale", dirtScale);
        
        // GRASS texture
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_1", grass);
        matTerrain.setFloat("DiffuseMap_1_scale", grassScale);

        // ROCK texture
        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_2", rock);
        matTerrain.setFloat("DiffuseMap_2_scale", rockScale);

        // BRICK texture
        Texture brick = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
        brick.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_3", brick);
        matTerrain.setFloat("DiffuseMap_3_scale", rockScale);

        // RIVER ROCK texture, this texture will use the next alphaMap: AlphaMap_1
        Texture riverRock = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        riverRock.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_4", riverRock);
        matTerrain.setFloat("DiffuseMap_4_scale", rockScale);
        
        // diffuse textures 4 to 7 use AlphaMap_1
        // diffuse textures 8 to 11 use AlphaMap_2

        Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
        normalMap0.setWrap(Texture.WrapMode.Repeat);
        Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
        normalMap1.setWrap(Texture.WrapMode.Repeat);
        Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
        normalMap2.setWrap(Texture.WrapMode.Repeat);
        //matTerrain.setTexture("NormalMap", normalMap0);
        matTerrain.setTexture("NormalMap_1", normalMap2);
        matTerrain.setTexture("NormalMap_2", normalMap2);
        matTerrain.setTexture("NormalMap_4", normalMap2);

        createSky();

        // CREATE HEIGHTMAP
        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.5f);
            heightmap.load();
            heightmap.smooth(0.9f, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * Here we create the actual terrain. The tiles will be 65x65, and the total size of the
         * terrain will be 513x513. It uses the heightmap we created to generate the height values.
         */
        /**
         * Optimal terrain patch size is 65 (64x64).
         * The total size is up to you. At 1025 it ran fine for me (200+FPS), however at
         * size=2049, it got really slow. But that is a jump from 2 million to 8 million triangles...
         */
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());//, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
        TerrainLodControl control = new TerrainLodControl(terrain, camera);
        control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
        terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        terrain.setModelBound(new BoundingBox());
        terrain.updateModelBound();
        terrain.setLocalTranslation(0, 0, 0);
        terrain.setLocalScale(1f, 1f, 1f);
        
        return terrain;
    }
    
    
    public Spatial createSky() {
        Texture west = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_west.jpg");
        Texture east = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_east.jpg");
        Texture north = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_north.jpg");
        Texture south = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_south.jpg");
        Texture up = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_up.jpg");
        Texture down = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg");

        return SkyFactory.createSky(assetManager, west, east, north, south, up, down);
    }
    
    
    public static float [] createGaussianKernel(int dim, float sigma)
    {
        float [] kernel = new float [dim*dim];
        double A = 1 / (2*Math.PI*sigma*sigma);
        double B = -1 / (2*sigma*sigma);
        
        int radius = dim / 2;
        
        float sum = 0;
        // calculating the gaussian values:
        for(int i = -radius; i <= radius; i ++) {
            for(int j = -radius; j <= radius; j ++) {
                float val = (float)(A * Math.exp((i*i + j*j) * B));
                sum += val;
                kernel[i + radius + (j + radius) * dim] = val;
            }
        }
        
        // normalizing:
        for(int i = 0; i < kernel.length; i ++) {
            kernel[i] /= sum;
        }
        
        return kernel;
    }

    public float [] smoothGaussian(float [] map)
    {
        float [] smoothed = new float [map.length];
        int kernelSize = 7;
        int kernelRadius = kernelSize / 2;
        float [] kernel = createGaussianKernel(kernelSize, 2f);
  
        int mapSize = (int)Math.sqrt(map.length);

         for(int x = kernelRadius; x < mapSize-kernelRadius; x ++) {
            for(int y = kernelRadius; y < mapSize-kernelRadius; y ++) {
                float val = 0;
                for(int i = -kernelRadius; i <= kernelRadius; i ++) {
                    for(int j = -kernelRadius; j <= kernelRadius; j ++) {
                        val += kernel[i + kernelRadius + (j + kernelRadius) * kernelSize] * map[x+i + (y+j)*mapSize];
                    }
                }
                
                smoothed[x + y*mapSize] = val;
            }
            
         }
        return smoothed;
    }
}
