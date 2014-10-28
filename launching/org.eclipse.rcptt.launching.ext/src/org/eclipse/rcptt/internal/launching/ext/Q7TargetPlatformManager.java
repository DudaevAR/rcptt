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
package org.eclipse.rcptt.internal.launching.ext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.rcptt.launching.IQ7Launch;
import org.eclipse.rcptt.launching.target.ITargetPlatformHelper;
import org.eclipse.rcptt.launching.target.TargetPlatformManager;

public class Q7TargetPlatformManager {

	private static Map<String, ITargetPlatformHelper> cachedHelpers = new HashMap<String, ITargetPlatformHelper>();

	public synchronized static ITargetPlatformHelper loadTarget(
			ILaunchConfiguration config, IProgressMonitor monitor)
			throws CoreException {
		String location = config.getAttribute(IQ7Launch.AUT_LOCATION, "");

		File loc = new File(location);
		if (!loc.exists()) {
			return null;
		}

		String targetPlatform = config.getAttribute(IQ7Launch.TARGET_PLATFORM,
				"");

		ITargetPlatformHelper cached = cachedHelpers.get(targetPlatform);
		if (cached != null) {
			return cached;
		}

		monitor.beginTask("Initialize target platform...", 2);
		ITargetPlatformHelper info = TargetPlatformManager.getTargetPlatform(
				targetPlatform, new SubProgressMonitor(monitor, 1), false);
		if (info != null) {
			if (!info.isResolved()) {
				info.resolve(monitor);
			}
		}

		if (!info.isValid()) {
			return null;
		} else {
			monitor.worked(1);
		}
		monitor.done();
		cachedHelpers.put(targetPlatform, info);
		return info;
	}

	/**
	 * Return existing target or re evaluate and create one new
	 * 
	 * @param config
	 * @param shell
	 * @return
	 * @throws CoreException
	 */
	public synchronized static ITargetPlatformHelper getTarget(
			ILaunchConfiguration config, IProgressMonitor monitor)
			throws CoreException {
		String location = config.getAttribute(IQ7Launch.AUT_LOCATION, "");

		File loc = new File(location);
		if (!loc.exists()) {
			return newTargetPlatform(config, monitor, location);
		}

		String targetPlatform = config.getAttribute(IQ7Launch.TARGET_PLATFORM,
				"");

		monitor.beginTask("Initialize target platform...", 2);
		ITargetPlatformHelper info = TargetPlatformManager.getTargetPlatform(
				targetPlatform, new SubProgressMonitor(monitor, 1), true);

		if (!info.isValid()) {
			// Update runtime version
			info.delete();
			info = newTargetPlatform(config,
					new SubProgressMonitor(monitor, 1), location);
		} else {
			monitor.worked(1);
		}
		monitor.done();
		cachedHelpers.put(targetPlatform, info);
		return info;
	}

	private synchronized static ITargetPlatformHelper newTargetPlatform(
			ILaunchConfiguration config, IProgressMonitor monitor,
			String location) throws CoreException {

		ITargetPlatformHelper info = Q7TargetPlatformManager
				.createTargetPlatform(location, monitor);
		if (info != null && info.isValid()) {
			info.setTargetName(getTargetPlatformName(config));
			info.save();
			cachedHelpers.put(info.getName(), info);
		}
		return info;
	}

	private static void throwOnError(IStatus status) throws CoreException {
		if (status.matches(IStatus.ERROR))
			throw new CoreException(status);
		if (!status.isOK())
			Q7ExtLaunchingPlugin.log(status);
	}

	public synchronized static ITargetPlatformHelper createTargetPlatform(
			String location, IProgressMonitor monitor) throws CoreException {
		boolean isOk = false;
		if (monitor.isCanceled()) {
			throw new CoreException(Status.CANCEL_STATUS);
		}
		ITargetPlatformHelper platform = null;
		try {
			monitor.beginTask("Create AUT configuration", 100);
			platform = TargetPlatformManager
					.createTargetPlatform(location, new SubProgressMonitor(monitor,
							50));
			if (!platform.isValid())
				throw new CoreException(platform.getStatus());
			IStatus rv = Q7TargetPlatformInitializer.initialize(platform,
					new SubProgressMonitor(monitor, 50));
			throwOnError(rv);
			isOk = true;
			return platform;
		} catch (CoreException e) {
			throw e;
		} catch (Throwable e) {
			throw new CoreException(new Status(IStatus.ERROR, Q7ExtLaunchingPlugin.PLUGIN_ID,
					"Target platform initialization failed", e));
		} finally {
			if (!isOk && platform != null)
				platform.delete();
			monitor.done();
		}
	}

	/**
	 * Return target platform name. This method will by default use launch
	 * configuratio name.
	 * 
	 * @param config
	 * @return
	 */
	public static String getTargetPlatformName(ILaunchConfiguration config) {
		return "AUT " + config.getName() + " (Target Platform)";
	}

	public static String getTargetPlatformName(String name) {
		return "AUT " + name + " (Target Platform)";
	}

	public synchronized static void initialize() {
		DebugPlugin
				.getDefault()
				.getLaunchManager()
				.addLaunchConfigurationListener(
						new ILaunchConfigurationListener() {
							public void launchConfigurationRemoved(
									ILaunchConfiguration configuration) {
								String target = getTargetPlatformName(configuration);
								if (target != null) {
									TargetPlatformManager
											.deleteTargetPlatform(target);
									cachedHelpers.remove(target);
								}
							}

							public void launchConfigurationChanged(
									ILaunchConfiguration configuration) {
								// String target =
								// getTargetPlatformName(configuration);
								// if (target != null) {
								// cachedHelpers.remove(target);
								// }
							}

							public void launchConfigurationAdded(
									ILaunchConfiguration configuration) {
							}
						});
	}

	public synchronized static void setHelper(String name,
			ITargetPlatformHelper info) {
		cachedHelpers.put(name, info);
	}

	public synchronized static ITargetPlatformHelper getHelper(String name) {
		return cachedHelpers.get(name);
	}

	public synchronized static void delete(String name) {
		cachedHelpers.remove(name);
	}

	public synchronized static void clear() {
		cachedHelpers.clear();
	}
}
