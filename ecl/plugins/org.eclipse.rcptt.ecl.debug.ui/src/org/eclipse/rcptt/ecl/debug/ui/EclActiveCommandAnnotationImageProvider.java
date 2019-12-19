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

package org.eclipse.rcptt.ecl.debug.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.texteditor.IAnnotationImageProvider;

public class EclActiveCommandAnnotationImageProvider implements IAnnotationImageProvider {

	public Image getManagedImage(Annotation annotation) {
		return null;
	}

	public String getImageDescriptorId(Annotation annotation) {
		return null;
	}
	public ImageDescriptor getImageDescriptor(String imageDescritporId) {
		return null;
	}
	
}
