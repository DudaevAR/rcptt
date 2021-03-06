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
package org.eclipse.rcptt.core.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.rcptt.core.workspace.Q7Utils;
import org.eclipse.rcptt.internal.core.RcpttPlugin;
import org.eclipse.rcptt.util.FileUtil;
import org.eclipse.rcptt.util.StreamUtil;

public abstract class BasePersistenceModel implements IPersistenceModel {

	protected final Map<String, File> files = new HashMap<String, File>();

	protected abstract void doExtractAll(InputStream contents)
			throws IOException;

	protected abstract void doExtractFile(String fName, InputStream contents)
			throws IOException;

	protected abstract void doReadIndex(InputStream contents);

	protected abstract void doStoreTo(File file) throws FileNotFoundException,
			IOException;

	protected final Resource element;
	protected final File root;
	protected final IPath rootPath;
	private boolean modified = false;
	private byte[] internalContent = null;
	private Set<String> extractions = new HashSet<String>();
	private boolean disposed = false;

	public boolean isContentEntryRequired() {
		return true;
	}

	public boolean isAllowEmptyMetadataContent() {
		return false;
	}

	public BasePersistenceModel(Resource element) {
		this.element = element;
		IPath nonExistent = null;
		while (true) {
			RcpttPlugin default1 = RcpttPlugin.getDefault();
			assert default1 != null;
			String uri = (element == null || element.getURI() == null) ? "_"
					: element.getURI().toString();
			nonExistent = default1
					.getStateLocation()
					.append("attachments")
					.append(FileUtil.limitSize(FileUtil.getID(uri))
							+ System.currentTimeMillis());
			if (!nonExistent.toFile().exists()) {
				break;
			}
		}
		assert nonExistent != null;
		rootPath = nonExistent;
		this.root = rootPath.toFile();
		// Make index
		try {
			readIndex();
		} catch (CoreException e) {
			RcpttPlugin.log(e);
		}
	}

	public Resource getResource() {
		return this.element;
	}

	public void setInternalContent(byte[] internalContent) {
		this.internalContent = internalContent;
		try {
			readIndex();
		} catch (CoreException e) {
			RcpttPlugin.log(e);
		}
	}

	private void initialize() {
		assert !disposed;
		root.mkdirs();
	}

	public boolean hasElements() {
		return !files.isEmpty();
	}

	public String[] getNames() {
		return files.keySet().toArray(new String[files.size()]);
	}

	private void readIndex() throws CoreException {
		assert !disposed;
		InputStream contents = getContentsStream();
		if (contents == null) {
			return;
		}
		try {
			doReadIndex(contents);
		} finally {
			try {
				contents.close();
			} catch (IOException e) {
			}
		}
	}

	protected InputStream getContentsStream() {
		InputStream input = null;
		IFile file = element != null ? Q7Utils.getLocation(element) : null;
		if (file != null && !file.exists()) {
			return null;
		}
		if (internalContent != null) {
			input = new ByteArrayInputStream(internalContent);
		}

		InputStream contents;
		try {
			if (input == null && file == null) {
				return null;
			}
			if (input == null) {
				input = file.getContents();
			}
			contents = new BufferedInputStream(input);
		} catch (CoreException e) {
			// Ignore file not found exception
			if (e.getStatus().getCode() != 271) {
				RcpttPlugin.log(e);
			}
			return null;
		}
		return contents;
	}

	protected File putFileItem(String name, IPath filePath) {
		return files.put(name, filePath.toFile());
	}

	public void dispose() {
		if (disposed)
			return;
		removeAll();
		disposed = true;
	}

	public InputStream read(String name) {
		assert !disposed;
		File file = files.get(name);
		if (file == null) {
			return null;
		}
		waitUntilExtracted(name);
		if (!file.exists()) {
			extractFile(name);
			waitUntilExtracted(name);
		}
		if (file.exists()) {
			try {
				return getInput(file);
			} catch (FileNotFoundException e) {
				RcpttPlugin.log(e);
			}
		}

		return null;
	}

	private void waitUntilExtracted(String name) {
		synchronized (extractions) {
			while (extractions.contains(name)) {
				try {
					extractions.wait(100);
				} catch (InterruptedException e) {
					RcpttPlugin.log(e);
				}
			}
		}
	}

	protected BufferedInputStream getInput(File file)
			throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}

	private void extractFile(String fName) {
		initialize();
		InputStream contents = null;
		try {
			synchronized (extractions) {
				if (extractions.contains(fName)) {
					return;
				}
				extractions.add(fName);
			}
			contents = getContentsStream();
			if (contents == null) {
				return;
			}
			doExtractFile(fName, contents);
		} catch (Throwable e1) {
			RcpttPlugin.log(e1);
		} finally {
			synchronized (extractions) {
				extractions.remove(fName);
				extractions.notifyAll();
			}
			try {
				if (contents != null) {
					contents.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public void delete(String name) {
		File file = files.get(name);
		if (file != null && file.exists()) {
			file.delete();
		}
		files.remove(name);
	}

	public boolean restore(String name) {
		File file = files.get(name);
		if (file != null && file.exists()) {
			file.delete();
			extractFile(name);
			return file.exists();
		}
		return false;
	}

	public OutputStream store(String name) {
		modified = true;
		return internalStore(name);
	}

	protected OutputStream internalStore(String name) {
		initialize();
		IPath path = rootPath.append(new Path(name));
		File file = path.toFile();
		file.getParentFile().mkdirs();
		files.put(name, file);
		try {
			return new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			RcpttPlugin.log(e);
			return null;
		}
	}

	public void extractAll() {
		initialize();

		InputStream contents = null;
		try {
			contents = getContentsStream();
			if (contents == null) {
				return;
			}
			doExtractAll(contents);
		} catch (Throwable e1) {
			RcpttPlugin.log(e1);
		} finally {
			try {
				if (contents != null) {
					contents.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public File storeToTemporaty() {
		initialize();

		File file = rootPath.append(
				".q7.content.temporary" + System.currentTimeMillis()).toFile();
		try {
			doStoreTo(file);
		} catch (Exception e) {
			RcpttPlugin.log(e);
		}
		return file;
	}

	public void removeAll() {
		files.clear();
		if (root != null) {
			FileUtil.deleteFiles(root.listFiles());
			root.delete();
		}
	}

	/**
	 * Return true if some resources are copied. Copy all resources except
	 * PersistenceManager.CONTENT_ENTRY
	 */
	public boolean copyFrom(IPersistenceModel originalModel) {
		removeAll();
		String[] names = originalModel.getNames();
		for (String name : names) {
			InputStream inputStream = originalModel.read(name);
			OutputStream outputStream = store(name);
			try {
				FileUtil.copy(inputStream, outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return names.length > 0;
	}

	public boolean isModified() {
		return modified;
	}

	public void updateMetadata() {
	}

	public void updateAttributes() {
	}

	public void setUnmodified() {
		modified = false;
	}

	public int size(String teslaContentEntry) {
		InputStream stream = read(teslaContentEntry);
		int result = 0;
		if (stream != null) {
			try {

				byte[] buffer = new byte[4096];
				int len = 0;
				while ((len = stream.read(buffer)) > 0) {
					result += len;
				}
			} catch (IOException e) {
				result = 0;
			} finally {
				StreamUtil.closeSilently(stream);
			}
		}
		return result;
	}

	public void rename(String oldName, String newName) {
		InputStream read = read(oldName);
		if (read != null) {
			OutputStream store = store(newName);
			try {
				FileUtil.copy(read, store);
				delete(oldName);
			} catch (IOException e) {
				RcpttPlugin.log(e);
			}
		}
	}
}
