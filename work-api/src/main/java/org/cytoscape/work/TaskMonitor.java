package org.cytoscape.work;

/*
 * #%L
 * Cytoscape Work API (work-api)
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2006 - 2013 The Cytoscape Consortium
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
 * Used by <code>Task</code>s to modify its user interface.
 *
 * @author Pasteur
 * @CyAPI.Api.Interface
 * @CyAPI.InModule work-api
 */
public interface TaskMonitor {
	/**
	 * Sets the title of the <code>Task</code>.
	 * The title is a succinct description of the <code>Task</code>'s
	 * purpose. This method should only be called once and at the beginning
	 * of the <code>run</code> method.
	 * @param title Succinct description of the Task's purpose.
	 */
	public void setTitle(String title);

	/**
	 * Sets the progress completed by the <code>Task</code>.
	 *
	 * @param progress A value between <code>0.0</code> and <code>1.0</code>.
	 */
	public void setProgress(double progress);

	/**
	 * Sets the status message that describes what a <code>Task</code> is currently doing.
	 * This method can be called throughout the course of the <code>run</code> method.
	 * @param statusMessage String that describe what a Task is currently doing.
	 */
	public void setStatusMessage(String statusMessage);

	/**
	 * Used by the {@code showMessage} method to indicate the severity of the message.
	 */
	public static enum Level {
		/**
		 * A message that is informational to the user.
		 */
		INFO,

		/**
		 * A message that warns the user about recoverable errors the task has experienced.
		 */
		WARN,

		/**
		 * A message that informs the user that a non-recoverable error has occured.
		 * Typically after an error message has been shown, the task should stop
		 * execution.
		 */
		ERROR
	}

	/**
	 * Shows a message to the user. This method can be called throughout
	 * the course of the <code>run</code> method.
	 * This method should <i>not</i> be used for debugging purposes.
	 * Either use {@code System.out.println} or a logger to issue
	 * debug messages.
	 */
	public void showMessage(Level level, String message);
}
