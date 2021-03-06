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
package org.eclipse.rcptt.ui.resources.viewers;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.rcptt.core.scenario.Context;
import org.eclipse.rcptt.internal.ui.Images;
import org.eclipse.rcptt.ui.editors.IQ7Editor;
import org.eclipse.rcptt.ui.editors.IQ7Viewer;

public class WorkspaceViewer implements IQ7Viewer<Context> {

	public IQ7Editor<Context> createEditor() {
		return new WorkspaceContextEditor();
	}

	public ImageDescriptor getImage(Context context) {
		return Images.getImageDescriptor(Images.WORKSPACE);
	}

	public String getLabel(Context context) {
		return "Workspace";
	}

	public boolean isCaptureSupported() {
		return true;
	}

	public boolean isApplySupported() {
		return true;
	}
}
