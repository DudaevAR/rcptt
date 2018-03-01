/*******************************************************************************
 * Copyright (c) 2009, 2014 Xored Software Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Xored Software Inc - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.rcptt.tesla.recording.jface.text;

import org.eclipse.swt.widgets.Widget;

import org.eclipse.rcptt.tesla.core.protocol.raw.Element;
import org.eclipse.rcptt.tesla.internal.ui.player.SWTUIElement;
import org.eclipse.rcptt.tesla.jface.text.TextEditorMapper;
import org.eclipse.rcptt.tesla.recording.core.TeslaRecorder;
import org.eclipse.rcptt.tesla.recording.core.swt.IRecorderDescriberExtension;
import org.eclipse.rcptt.tesla.recording.core.swt.IRecordingDescriber;
import org.eclipse.rcptt.tesla.recording.core.swt.RecordingWidgetDescriber;

public class RecordingDescriberExtension implements IRecorderDescriberExtension {
	public IRecordingDescriber getDescriber(Widget widget,
			IRecordingDescriber previous, int x, int y, boolean fromAssert) {
		if (!TextEditorMapper.isTextEditorStyledText(widget)) {
			return previous;
		}
		return new RecordingDescriber(widget);
	}

	private static class RecordingDescriber extends RecordingWidgetDescriber {

		public RecordingDescriber(SWTUIElement element) {
			super(element);
		}

		public RecordingDescriber(Widget widget) {
			super(widget);
		}

		@Override
		public Element searchForElement(TeslaRecorder recorder) {
			TexteditorRecordingProcessor processor = recorder
					.getProcessor(TexteditorRecordingProcessor.class);
			if (processor == null) {
				return super.searchForElement(recorder);
			}
			try {
				processor.useTextViewer.set(1);
				return super.searchForElement(recorder);
			} finally {
				processor.useTextViewer.set(0);
			}
		}
	}
}
