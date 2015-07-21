package org.cytoscape.util.swing;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public final class LookAndFeelUtil {

	public static final Color INFO_COLOR = new Color(39, 67, 167);
	public static final Color WARN_COLOR = new Color(204, 144, 7);
	public static final Color ERROR_COLOR = new Color(161, 19, 0);
	
	public static final float INFO_FONT_SIZE = 11.0f;
	
	static final float AQUA_TITLED_BORDER_FONT_SIZE = 11.0f;
	
	public static boolean isAquaLAF() {
		return UIManager.getLookAndFeel() != null && "Mac OS X".equals(UIManager.getLookAndFeel().getName());
	}
	
	public static boolean isNimbusLAF() {
		return UIManager.getLookAndFeel() != null && "Nimbus".equals(UIManager.getLookAndFeel().getName());
	}
	
	public static boolean isWinLAF() {
		return UIManager.getLookAndFeel() != null && "Windows".equals(UIManager.getLookAndFeel().getName());
	}
	
	public static Border createPanelBorder() {
		// Try to create Aqua recessed borders on Mac OS
		Border border = isAquaLAF() ? UIManager.getBorder("InsetBorder.aquaVariant") : null;
		
		if (border == null) {
			if (isWinLAF())
				border = new TitledBorder("");
			else
				border = BorderFactory.createTitledBorder("SAMPLE").getBorder();
		}
			
		if (border == null)
			border = BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground"));
		
		return border;
	}
	
	public static Border createTitledBorder(final String title) {
		final Border border;
		
		if (title == null || title.trim().isEmpty()) {
			final Border aquaBorder = isAquaLAF() ? UIManager.getBorder("InsetBorder.aquaVariant") : null;
			border = aquaBorder != null ? aquaBorder : BorderFactory.createTitledBorder("SAMPLE").getBorder();
		} else {
			final Border aquaBorder = isAquaLAF() ? UIManager.getBorder("TitledBorder.aquaVariant") : null;
			final TitledBorder tb = aquaBorder != null ?
					BorderFactory.createTitledBorder(aquaBorder, title) : BorderFactory.createTitledBorder(title);
			
			if (isAquaLAF())
				tb.setTitleFont(UIManager.getFont("Label.font").deriveFont(AQUA_TITLED_BORDER_FONT_SIZE));
			
			border = tb;
		}
		
		return border;
	}
	
	public static JPanel createOkCancelPanel(final JButton okBtn, final JButton cancelBtn) {
		return createOkCancelPanel(okBtn, cancelBtn, new JButton[0]);
	}
	
	public static JPanel createOkCancelPanel(final JButton okBtn, final JButton cancelBtn, JButton... otherBtns) {
		final JPanel panel = new JPanel();
		
		final GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(false);
		layout.setAutoCreateGaps(true);
		
		final SequentialGroup hg = layout.createSequentialGroup();
		final ParallelGroup vg = layout.createParallelGroup(Alignment.CENTER, false);
		
		if (otherBtns != null) {
			for (int i = 0; i < otherBtns.length; i++) {
				final JButton btn = otherBtns[i];
				hg.addComponent(btn);
				vg.addComponent(btn);
			}
		}
		
		hg.addGap(0, 0, Short.MAX_VALUE);
		
		final JButton btn1 = isMac() ? cancelBtn : okBtn;
		final JButton btn2 = isMac() ? okBtn : cancelBtn;
		
		if (btn1 != null) {
			hg.addComponent(btn1);
			vg.addComponent(btn1);
		}
		if (btn2 != null) {
			hg.addComponent(btn2);
			vg.addComponent(btn2);
		}
		
		layout.setHorizontalGroup(hg);
		layout.setVerticalGroup(vg);
		
		if (okBtn != null && cancelBtn != null)
			equalizeSize(okBtn, cancelBtn);
		
		return panel;
	}
	
	public static void setDefaultOkCancelKeyStrokes(final JRootPane rootPane, final Action okAction,
			final Action cancelAction) {
		final InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		if (okAction != null) {
			final String OK_ACTION_KEY = "OK_ACTION_KEY";
			final KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
			inputMap.put(enterKey, OK_ACTION_KEY);
			rootPane.getActionMap().put(OK_ACTION_KEY, okAction);
		}
		
		if (cancelAction != null) {
			final String CANCEL_ACTION_KEY = "CANCEL_ACTION_KEY";
			final KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
			inputMap.put(escapeKey, CANCEL_ACTION_KEY);
			rootPane.getActionMap().put(CANCEL_ACTION_KEY, cancelAction);
		}
	}
	
	/**
	 * Resizes the given components making them equal in size.
	 */
	public static void equalizeSize(final JComponent... components) {
		if (components == null || components.length == 0)
			return;
		
		final Dimension prefSize = components[0].getPreferredSize();
		final Dimension maxSize = components[0].getMaximumSize();
		
		for (JComponent c : components) {
			ensureSize(prefSize, c.getPreferredSize());
			ensureSize(maxSize, c.getMaximumSize());
		}
		
		for (JComponent c : components) {
			c.setPreferredSize(prefSize);
			c.setMaximumSize(maxSize);
		}
	}
	
	public static boolean isMac() {
		return System.getProperty("os.name").startsWith("Mac OS X");
	}
	
	/**
	 * Enlarges, if necessary, the given current size to cover the given other size.
	 * <p>
	 * If both the width and height of <code>currentSize</code> are larger than the width and height of
	 * <code>minSize</code>, respectively, calling this method has no effect.
	 * </p>
	 * 
	 * @param currentSize Size to be enlarged if necessary.
	 * @param minSize Minimal required size of <code>currentSize</code>.
	 */
	private static void ensureSize(final Dimension currentSize, final Dimension minSize) {
		if (currentSize.height < minSize.height)
			currentSize.height = minSize.height;
		
		if (currentSize.width < minSize.width)
			currentSize.width = minSize.width;
	}
	
	private LookAndFeelUtil() {
	}
}
