
package com.googlecode.reaxion.test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Matrix4f;
import com.jme.math.Vector3f;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.effects.ProjectedTextureUtil;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.ImageBasedHeightMap;

/**
 * <code>TestProjectedTexture</code>
 *
 * @author Rikard Herlitz (MrCoder)
 */
public class ProjectionTest extends SimpleGame {
    private static final Logger logger = Logger
            .getLogger(ProjectionTest.class.getName());
    
	private TerrainPage terrain;

	private Texture projectedTexture1;
	private Vector3f projectorAim1 = new Vector3f();

	public static void main( String[] args ) {
		ProjectionTest app = new ProjectionTest();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void simpleUpdate() {

		projectorAim1.set(5*(timer.getTimeInSeconds()%10), 0.0f, 0);
		//update texture matrix
		ProjectedTextureUtil.updateProjectedTexture( projectedTexture1, 30.0f, 1.5f, 1.0f, 1000.0f, new Vector3f(projectorAim1.x+1, 20, 0), projectorAim1, Vector3f.UNIT_Y );
	}

	@Override
	protected void simpleInitGame() {
		try {
            try {
                ResourceLocatorTool.addResourceLocator(
                        ResourceLocatorTool.TYPE_TEXTURE,
                        new SimpleResourceLocator(ProjectionTest.class
                                .getClassLoader().getResource(
                                        "jmetest/data/model/msascii/")));
            } catch (URISyntaxException e1) {
                logger.log(Level.WARNING, "unable to setup texture directory.", e1);
            }

            display.setTitle( "Projected Texture Test" );

			cam.getLocation().set( new Vector3f( 50, 50, 0 ) );
			cam.lookAt( new Vector3f(), Vector3f.UNIT_Y );
		

			//create terrain
			URL grayScale = ProjectionTest.class.getClassLoader().getResource( "jmetest/data/texture/terrain.png" );
			ImageBasedHeightMap heightMap = new ImageBasedHeightMap( new javax.swing.ImageIcon( grayScale ).getImage() );
			Vector3f terrainScale = new Vector3f( .5f, .05f, .5f );
			terrain = new TerrainPage( "image icon", 33, (heightMap.getSize()) + 1, terrainScale, heightMap.getHeightMap() );
			terrain.setDetailTexture( 1, 16 );
			terrain.setModelBound( new BoundingBox() );
			terrain.updateModelBound();
			terrain.setLocalTranslation( new Vector3f( 0, 0, 0 ) );
			rootNode.attachChild( terrain );


			TextureState ts = display.getRenderer().createTextureState();
			ts.setEnabled( true );
			
			//create a texture to use for projection
			projectedTexture1 = TextureManager.loadTexture( ProjectionTest.class.getClassLoader().getResource(
					"jmetest/data/images/Monkey.png" ), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear );
			ts.setTexture( projectedTexture1, 1 );

			//this is were we set the texture up for projection
            projectedTexture1.setMatrix(new Matrix4f());
            projectedTexture1.setWrap(Texture.WrapMode.BorderClamp );
            projectedTexture1.setEnvironmentalMapMode( Texture.EnvironmentalMapMode.EyeLinear );
            projectedTexture1.setApply( Texture.ApplyMode.Combine );
            projectedTexture1.setCombineFuncRGB( Texture.CombinerFunctionRGB.Add );
            projectedTexture1.setCombineSrc0RGB( Texture.CombinerSource.CurrentTexture );
            projectedTexture1.setCombineOp0RGB( Texture.CombinerOperandRGB.SourceColor );
            projectedTexture1.setCombineSrc1RGB( Texture.CombinerSource.Previous );
            projectedTexture1.setCombineOp1RGB( Texture.CombinerOperandRGB.SourceColor );
            projectedTexture1.setCombineScaleRGB( Texture.CombinerScale.One );


			terrain.setRenderState( ts );

			rootNode.setRenderQueueMode( com.jme.renderer.Renderer.QUEUE_OPAQUE );
		} catch( Exception e ) {
			logger.logp(Level.SEVERE, this.getClass().toString(),
                    "simpleInitGame()", "Exception", e);
		}
	}
}
