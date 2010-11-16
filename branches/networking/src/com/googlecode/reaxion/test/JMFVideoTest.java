package com.googlecode.reaxion.test;
/*
 * Copyright (c) 2003-2006 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import java.io.File;
import java.util.Vector;

import javax.media.Format;
import javax.media.MediaLocator;
import javax.media.PackageManager;
import javax.media.PlugInManager;
import javax.media.format.AudioFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;

import org.llama.jmf.ByteBufferRenderer;
import org.llama.jmf.JMFVideoImage;

import com.jme.animation.SpatialTransformer;
import com.jme.app.SimpleGame;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;

/**
 * @author Tijl Houtbeckers
 * 
 * Simple class to show how to use JMFVideoImage
 * 
 */

public class JMFVideoTest extends SimpleGame {

	Texture tex;
	JMFVideoImage image;
	Spatial spatial;

	@Override
	protected void simpleInitGame() {
		initFobs();

		// enable for some extra debug data
		ByteBufferRenderer.printframes = true;

		// set to false if you do not want to use FOBS optimizations (eg. if you
		// have non-fobs input, or an incomplete FOBS install).
		ByteBufferRenderer.useFOBSOptimization = true;

		// set to true if you want to use the FOBS patch
		ByteBufferRenderer.useFOBSPatch = false;

		// ////////////////////////////
		/* change to your file path */
		// ////////////////////////////
		MediaLocator file;
		try {
			file = new MediaLocator(new File("src/com/googlecode/reaxion/resources/video/yoshstory.mov").toURL());
			image = new JMFVideoImage(file, true, JMFVideoImage.SCALE_FIT);
			image.setFormat(Image.Format.RGBA8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (image.isFlipped()) {
			// quick hack to flip the picture, ok since we have no lights.
			spatial = new Quad("quad", 15, -15);
			System.out.println("using flipped quad");
		} else {
			spatial = new Quad("quad", 15, 15);
			System.out.println("using normal quad");
		}

		rootNode.attachChild(spatial);
		tex = new Texture2D();
		tex.setMinificationFilter(MinificationFilter.BilinearNoMipMaps);
		tex.setImage(image);
		TextureState ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		ts.setTexture(tex);
		spatial.setRenderState(ts);

		lightState.setEnabled(false); // nice and bright

		image.startMovie();

	}

	@Override
	protected void simpleUpdate() {
		if (!image.update(tex, false)) {
			// if we did not update the texture, wait some time, but when the
			// next frame is ready, wait no longer.
			image.waitSome(3);
		}

	}

	private void setupBoxTransform(Spatial box) {
		SpatialTransformer spt = new SpatialTransformer(1);
		spt.setObject(box, 0, -1);
		Quaternion rotRef = new Quaternion();

		float rotation = 0;
		float time = 5, timeDiv = 10;
		// iterate over the range, go over by one step to accommodate
		// mathematical error
		for (float timeElp = 0; timeElp < (time + (time / timeDiv)); timeElp += (time / timeDiv)) {
			rotation = (timeElp / time) * 360;

			if (rotation > 360)
				rotation = 360;// lock to 360

			rotRef.fromAngleAxis((float) (Math.PI / 180) * rotation, new Vector3f(1, 1, 1));

			spt.setRotation(0, timeElp, rotRef);
		}

		Vector3f scaleRef = new Vector3f();

		float scaling;

		// iterate over the range, go over by one step to accommodate
		// mathematical error
		for (float timeElp = 0; timeElp < (time + (time / timeDiv)); timeElp += (time / timeDiv)) {

			if (timeElp < (time / 2))
				scaling = (timeElp / time) * 5;
			else
				scaling = ((1 - timeElp / time) * 3);

			scaleRef.x = scaling;
			scaleRef.y = scaling;
			scaleRef.z = scaling;

			spt.setScale(0, timeElp, scaleRef);
		}

		box.addController(spt);
	}


	public static void main(String[] arg) {
		JMFVideoTest app = new JMFVideoTest();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}


	private boolean isFobsInited(Vector rendList){
		String rname = null;
		for (int i = 0; i < rendList.size(); i++) {
			rname = (String) (rendList.elementAt(i));
			if (rname.equals("org.llama.jmf.ByteBufferRenderer")) {
				return true;
			}
		}
		return false;
	}

	private void initFobs(){
		Vector rendererlist=PlugInManager.getPlugInList(null,null,PlugInManager.RENDERER);
		if(isFobsInited(rendererlist))return;
		Format[] ffmpegformat = new VideoFormat[] { new VideoFormat("FFMPEG_VIDEO") };
		Format[] ffmpegdesc = new ContentDescriptor[] {new ContentDescriptor("video.ffmpeg")};
		Format[] ffmpegaudio = new Format[] { new AudioFormat("FFMPEG_AUDIO") };
		Format[] supportedInputFormats = new VideoFormat[] {

				new VideoFormat("iv31"), //CODEC_ID_INDEO3
				new VideoFormat("iv32"),

				new VideoFormat("msvc"), //CODEC_ID_MSVIDEO1
				new VideoFormat("cram"),
				new VideoFormat("wham"),

				new VideoFormat("wmv1"), //CODEC_ID_WMV1

				new VideoFormat("wmv2"), //CODEC_ID_WMV2

				new VideoFormat("mpeg"), //CODEC_ID_MPEG1VIDEO
				new VideoFormat("mpg1"),
				new VideoFormat("mpg2"),
				new VideoFormat("pim1"),
				new VideoFormat("vcr2"),

				new VideoFormat("mjpa"), //CODEC_ID_MJPEG
				new VideoFormat("mjpb"),
				new VideoFormat("mjpg"),
				new VideoFormat("ljpg"),
				new VideoFormat("jpgl"),
				new VideoFormat("avdj"),

				new VideoFormat("svq1"), //CODEC_ID_SVQ1
				new VideoFormat("svqi"),

				new VideoFormat("svq3"), //CODEC_ID_SVQ3

				new VideoFormat("mp4v"), //CODEC_ID_MPEG4
				new VideoFormat("divx"),
				new VideoFormat("dx50"),
				new VideoFormat("xvid"),
				new VideoFormat("mp4s"),
				new VideoFormat("m4s2"),
				new VideoFormat("div1"),
				new VideoFormat("blz0"),
				new VideoFormat("ump4"),


				new VideoFormat("h264"), //CODEC_ID_H264

				new VideoFormat("h263"), //CODEC_ID_H263

				new VideoFormat("u263"), //CODEC_ID_H263P
				new VideoFormat("viv1"),

				new VideoFormat("i263"), //CODEC_ID_i263

				new VideoFormat("dvc"), //CODEC_ID_DVVIDEO
				new VideoFormat("dvcp"),
				new VideoFormat("dvsd"),
				new VideoFormat("dvhs"),
				new VideoFormat("dvs1"),
				new VideoFormat("dv25"),


				new VideoFormat("vp31"), //CODEC_ID_VP3

				new VideoFormat("rpza"), //CODEC_ID_RPZA

				new VideoFormat("cvid"), //CODEC_ID_CINEPAK

				new VideoFormat("smc"), //CODEC_ID_SMC


				new VideoFormat("mp42"), // CODEC_ID_MSMPEG4V2
				new VideoFormat("div2"),

				new VideoFormat("mpg4"), // CODEC_ID_MSMPEG4V1

				new VideoFormat("div3"), // CODEC_ID_MSMPEG4V3
				new VideoFormat("mp43"),
				new VideoFormat("mpg3"),
				new VideoFormat("div5"),
				new VideoFormat("div6"),
				new VideoFormat("div4"),
				new VideoFormat("ap41"),
				new VideoFormat("col1"),
				new VideoFormat("col0")

		};
		Format[] frgb = new VideoFormat[] {new RGBFormat()};
		Format[] alinear=new AudioFormat[]{new AudioFormat("LINEAR")};
		//register the renderer
		PlugInManager.addPlugIn("org.llama.jmf.ByteBufferRenderer",frgb,null,PlugInManager.RENDERER);
		Vector plist=PlugInManager.getPlugInList(null,null,PlugInManager.RENDERER);
		//move the plugin to the top of the list
		Object last=plist.lastElement();
		plist.insertElementAt(last,0);
		plist.remove(plist.lastIndexOf(last));
		PlugInManager.setPlugInList(plist,PlugInManager.RENDERER);
		System.out.println("RENDERERn"+plist.toString());
		
		//register the demultiplexer

		Format[] In;
		Format[] Out;
		In=ffmpegdesc;
		Out=null;
		PlugInManager.addPlugIn("com.omnividea.media.parser.video.Parser",
				In,
				Out,
				PlugInManager.DEMULTIPLEXER);
		plist=PlugInManager.getPlugInList(null,null,PlugInManager.DEMULTIPLEXER);
		System.out.println("DEMULTIPLEXERn"+plist.toString());

		//register the codecs
		In=ffmpegformat;
		Out=frgb;
		PlugInManager.addPlugIn("com.omnividea.media.codec.video.NativeDecoder",
				In,
				Out,
				PlugInManager.CODEC);

		In=ffmpegaudio;
		Out=alinear;

		PlugInManager.addPlugIn("com.omnividea.media.codec.audio.NativeDecoder",
				In,
				Out,
				PlugInManager.CODEC);

		In=supportedInputFormats;
		Out=frgb;
		PlugInManager.addPlugIn("com.omnividea.media.codec.video.JavaDecoder",
				In,
				Out,
				PlugInManager.CODEC);
		plist=PlugInManager.getPlugInList(null,null,PlugInManager.CODEC);
		System.out.println("CODECSn"+plist.toString());

		//register the package
		Vector  packagePrefix = PackageManager.getProtocolPrefixList();
		String myPackagePrefix = new String("com.omnividea");
		packagePrefix.add(0,myPackagePrefix);
		PackageManager.setProtocolPrefixList(packagePrefix);

		System.out.println(PackageManager.getProtocolPrefixList().toString());

		return;
	}

}
