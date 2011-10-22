package org.apache.log4j.internal;

import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

/**
 * This appender writes log messaged into a file named .log4j which is located
 * in the current application's workspace directory.
 * 
 * @author ted stockwell
 */
public class WorkspaceAppender extends RollingFileAppender {

	public WorkspaceAppender() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.log4j.FileAppender#activateOptions()
	 */
	public void activateOptions() {
		IPath logPath = Platform.getStateLocation(Platform
				.getBundle("org.apache.log4j"));
		logPath = logPath.removeLastSegments(2);
		logPath = logPath.append(".log4j");
		logPath = logPath.makeAbsolute();
		String sPath = logPath.toString();
		setFile(sPath);

		super.activateOptions();
	}

}
