package com.googlecode.reaxion.game.overlay;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFont.Align;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Rectangle;

/**
 * Provides a scrolling-menu structure for use in overlays provided the entries
 * to encapsulate.
 * @author Khoa Ha
 *
 */
public class ScrollMenu extends Overlay {
	
	protected String[] entries;
	
	protected int width;
	protected int entryHeight;
	protected int numEntries;
	
	protected ColorRGBA backgroundColor;
	protected ColorRGBA separatorColor;
	protected ColorRGBA selectedColor;
	
	protected boolean scrollBarOn = false;
	protected ColorRGBA scrollBackgroundColor;
	protected ColorRGBA scrollBarColor;
	protected int scrollWidth;
	protected boolean scrollOnRight;
	
	protected ColorRGBA textColor;
	protected ColorRGBA textSelectedColor;
	
	protected BitmapFont font;
	protected int fontSize;
	protected Align fontAlign;
	
	private Node scrollContainer;
	private Node backContainer;
	private Node textContainer;
	private Node separatorContainer;
	private BitmapText[] text;
	
	/**
	 * Current item rendered at the top of the menu
	 */
	private int offset;
	/**
	 * Index of currently selected item
	 */
	private int currentIndex;
	
	public ScrollMenu(int width, int entryHeight, int numEntries, String[] entries) {
		this(width, entryHeight, numEntries, new ColorRGBA(0,0,0,.5f), new ColorRGBA(.5f,.5f,.5f,.5f), new ColorRGBA(.5f,.5f,.5f,.75f),
				new ColorRGBA(.75f,.75f,.75f,1), new ColorRGBA(1,1,1,1), entries);
    }
	
	public ScrollMenu(int width, int entryHeight, int numEntries, ColorRGBA backgroundColor, ColorRGBA separatorColor, ColorRGBA selectedColor,
			ColorRGBA textColor, ColorRGBA textSelectedColor, String[] entries) {
		this(width, entryHeight, numEntries, backgroundColor, separatorColor, selectedColor, textColor, textSelectedColor,
				FontUtils.eurostile, 20, Align.Left, entries);
    }
	
	public ScrollMenu(int width, int entryHeight, int numEntries, ColorRGBA backgroundColor, ColorRGBA separatorColor, ColorRGBA selectedColor,
			ColorRGBA textColor, ColorRGBA textSelectedColor, BitmapFont font, int fontSize, Align fontAlign, String[] entries) {
		super("ScrollMenu");
		this.width = width;
		this.entryHeight = entryHeight;
		this.numEntries = numEntries;
		this.backgroundColor = backgroundColor;
		this.separatorColor = separatorColor;
		this.selectedColor = selectedColor;
		this.textColor = textColor;
		this.textSelectedColor = textSelectedColor;
		this.font = font;
		this.fontSize = fontSize;
		this.fontAlign = fontAlign;
		
		setEntries(entries);
		
		container = new Node("container");
		attachChild(container);
		
		init();
		update();
    }
	
	/**
	 * Creates the basic elements.
	 */
	private void init() {
		reset();
		
		// create text fields
		textContainer = new Node("textContainer");
		text = new BitmapText[numEntries];
		for (int i=0; i<text.length; i++) {
			text[i] = new BitmapText(font, false);
			text[i].setSize(fontSize);
			text[i].setAlignment(fontAlign);
			text[i].setBox(new Rectangle(-width/2, i*entryHeight, width, entryHeight));
			textContainer.attachChild(text[i]);
		}
		
		// create separators
		separatorContainer = new Node("separatorContainer");
		for (int i=0; i<numEntries; i++) {
			Quad q = drawRect(width, 1, separatorColor);
			q.setLocalTranslation(0, i*entryHeight, 0);
			separatorContainer.attachChild(q);
		}
		
		// create container for back color
		backContainer = new Node("backContainer");
		
		// create container for scrollbar
		scrollContainer = new Node("scrollContainer");
		
		// attach elements
		container.attachChild(scrollContainer);
		container.attachChild(backContainer);
		container.attachChild(textContainer);
		container.attachChild(separatorContainer);
	}
	
	/**
	 * Draws the graphical menu. Must be called whenever states change.
	 */
	public void update() {
		for (int i=0; i<numEntries; i++) {
			// update text fields
			if (offset + i == currentIndex)
				text[i].setDefaultColor(textSelectedColor);
			else
				text[i].setDefaultColor(textColor);
			if (offset+i >= 0 && offset+i <entries.length)
				text[i].setText(entries[offset+i]);
			else
				text[i].setText("");
			text[i].update();
		}
		
		// check where to draw the selected region
		if (currentIndex >= offset && currentIndex - offset < numEntries) {
			// region is visible, draw accordingly
			backContainer.detachAllChildren();
			// top region
			if (currentIndex != offset) {
				int height = entryHeight*(currentIndex - offset);
				Quad d1 = drawRect(width, height, backgroundColor);
				d1.setLocalTranslation(0, -entryHeight + height/2, 0);
				backContainer.attachChild(d1);
			}
			// lower region
			if (currentIndex != offset + numEntries - 1) {
				int height = entryHeight*(numEntries - (currentIndex - offset) - 1);
				Quad d2 = drawRect(width, height, backgroundColor);
				d2.setLocalTranslation(0, entryHeight*(currentIndex - offset) + height/2, 0);
				backContainer.attachChild(d2);
			}
			// selected region
			Quad s = drawRect(width, entryHeight, selectedColor);
			s.setLocalTranslation(0, entryHeight*(currentIndex - offset - 1) + entryHeight/2, 0);
			backContainer.attachChild(s);
		} else {
			// whole region is unselected
			Quad d = drawRect(width, entryHeight*numEntries, backgroundColor);
			d.setLocalTranslation(0, entryHeight*numEntries/2, 0);
			backContainer.attachChild(d);
		}
		
		// update scrollbar
		if (scrollBarOn) {
			int height = numEntries*entryHeight/entries.length;
			int scrollPos = ((numEntries+1)*entryHeight - height*3)*(currentIndex-1)/entries.length + height*3/2 - entryHeight;
			scrollContainer.detachAllChildren();
			// top region
			if (currentIndex != 0) {
				int h = entryHeight + scrollPos-height/2;
				Quad d1 = drawRect(scrollWidth, h, scrollBackgroundColor);
				d1.setLocalTranslation(0, -entryHeight + h/2, 0);
				scrollContainer.attachChild(d1);
			}
			// lower region
			if (currentIndex != entries.length - 1) {
				int h = -entryHeight + numEntries*entryHeight - (scrollPos+height/2);
				Quad d2 = drawRect(scrollWidth, h, scrollBackgroundColor);
				d2.setLocalTranslation(0, -entryHeight + numEntries*entryHeight - h/2, 0);
				scrollContainer.attachChild(d2);
			}
			// selected region
			Quad s = drawRect(scrollWidth, height, scrollBarColor);
			s.setLocalTranslation(0, scrollPos, 0);
			scrollContainer.attachChild(s);
		}
		
		updateRenderState();
		
	}
	
	/**
	 * Returns the current index.
	 * @return Index number of current selection
	 */
	public int getCurrentIndex() {
		//  Flip for consistency
		return entries.length - currentIndex - 1;
	}
	
	/**
	 * Returns the currently selected entry.
	 * @param visible Whether or not the entry must be within visible range
	 * @return The contents of the selected entry
	 */
	public String getSelectedEntry(boolean visible) {
		if (visible) {
			if (currentIndex >= offset && currentIndex - offset < numEntries)
				return entries[currentIndex];
			else
				return null;
		} else
			return entries[currentIndex];
	}
	
	/**
	 * Modifies the current index according to {@code amount}.
	 * @param amount New index value
	 * @param relative Whether {@code amount} should be added to the index or
	 * replace it
	 * @return the new index
	 */
	public int changeIndex(int amount, boolean relative) {
		if (!relative)
			currentIndex = Math.max(0, Math.min(amount, entries.length - 1));
		else
			currentIndex = Math.max(0, Math.min(currentIndex-amount, entries.length - 1));
		return currentIndex;
	}
	
	/**
	 * Adjusts the offset so that the current item is always visible.
	 */
	public void remainVisible() {
		if (currentIndex >= offset + numEntries)
			offset = currentIndex - numEntries + 1;
		else if (currentIndex < offset)
			offset = currentIndex;
	}
	
	public void setEntries(String[] entries) {
		// reverse order of entries to preserve "up"
		List<String> list = Arrays.asList(entries);
		Collections.reverse(list);
		this.entries = (String[]) list.toArray();
	}
	
	/**
	 * Move current index and offset back to top of list.
	 */
	public void reset() {
		// set default positions
		currentIndex = entries.length - 1;
		offset = currentIndex - numEntries + 1;
	}
	
	public void enableScrollBar() {
		enableScrollBar(new ColorRGBA(0,0,0,1), new ColorRGBA(1,1,1,1), 8, true);
	}
	
	/**
	 * Enables a scroll bar displaying progress in relation to all entries.
	 */
	public void enableScrollBar(ColorRGBA scrollBackgroundColor, ColorRGBA scrollBarColor, int scrollWidth, boolean scrollOnRight) {
		scrollBarOn = true;
		this.scrollBackgroundColor = scrollBackgroundColor;
		this.scrollBarColor = scrollBarColor;
		this.scrollWidth = scrollWidth;
		this.scrollOnRight = scrollOnRight;
		
		// position scrollbar
		if (scrollOnRight)
			scrollContainer.setLocalTranslation(width/2+scrollWidth/2, 0, 0);
		else
			scrollContainer.setLocalTranslation(-width/2-scrollWidth/2, 0, 0);
	}

}
