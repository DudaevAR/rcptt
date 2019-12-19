/*******************************************************************************
 * Copyright (c) 2009, 2019 Xored Software Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Xored Software Inc - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.rcptt.expandbar.runtime.internal;

import org.eclipse.rcptt.tesla.internal.ui.player.SWTUIElement;
import org.eclipse.rcptt.tesla.recording.core.swt.BasicRecordingHelper;

public class ExpandBarRecordingHelper extends BasicRecordingHelper<SWTUIElement> {
	private static ExpandBarRecordingHelper instance;

	private ExpandBarRecordingHelper() {
	}

	public synchronized static ExpandBarRecordingHelper getHelper() {
		if (instance == null) {
			instance = new ExpandBarRecordingHelper();
		}
		return instance;
	}
}
