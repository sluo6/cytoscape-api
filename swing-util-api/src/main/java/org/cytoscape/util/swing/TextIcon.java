package org.cytoscape.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.UIManager;

/*
 * #%L
 * Cytoscape Swing Utility API (swing-util-api)
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2006 - 2018 The Cytoscape Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 2.1 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

/**
 * Renders any text/font as an icon.
 */
public class TextIcon implements Icon {
	
	private final Color TRANSPARENT_COLOR = new Color(255, 255, 255, 0);
	
	private final String text;
	private final Font font;
	private final Color color;
	private final int width;
	private final int height;
	
	public TextIcon(String text, Font font, Color color, int width, int height) {
		this.text = text;
		this.font = font;
		this.color = color;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * The icon color is the target component's foreground.
	 */
	public TextIcon(String text, Font font, int width, int height) {
		this(text, font, null, width, height);
	}
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        
        int xx = c.getWidth();
        int yy = c.getHeight();
        g2d.setPaint(TRANSPARENT_COLOR);
        g2d.fillRect(0, 0, xx, yy);
        
        Color fg = color != null ? color : c.getForeground();
        
        if (c instanceof AbstractButton) {
	        	if (!c.isEnabled())
	        		fg = UIManager.getColor("Label.disabledForeground");
	        	else if (((AbstractButton) c).getModel().isPressed())
	        		fg = fg.darker();
        }
        
        g2d.setPaint(fg);
        g2d.setFont(font);
        drawText(g2d, x, y);
        
        g2d.dispose();
	}
	
	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}
	
	private void drawText(Graphics g, int x, int y) {
		FontMetrics fm = g.getFontMetrics(font);
		Rectangle2D rect = fm.getStringBounds(text, g);

		int textHeight = (int) rect.getHeight();
		int textWidth = (int) rect.getWidth();

		// Center text horizontally and vertically
		int xx = x + (getIconWidth() - textWidth) / 2;
		int yy = y + (getIconHeight() - textHeight) / 2 + fm.getAscent();

		g.drawString(text, xx, yy);
	}
}
