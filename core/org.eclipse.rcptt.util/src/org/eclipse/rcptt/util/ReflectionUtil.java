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
package org.eclipse.rcptt.util;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class ReflectionUtil {
	public static Object callMethod(Object instance, String name,
			List<Class<?>> argTypes, List<Object> args, boolean checked)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Class<?> clazz = instance.getClass();
		Method method = findMethod(clazz, name, argTypes);
		if (method == null) {
			if (checked) {
				throw new NoSuchMethodException(format("Clas: $s, method: %s",
						clazz, name));
			}
			return null;
		}

		method.setAccessible(true);
		try {
			return method.invoke(instance,
					args.toArray(new Object[args.size()]));
		} catch (IllegalAccessException e) {
			if (checked) {
				throw e;
			}
			return null;
		} catch (InvocationTargetException e) {
			if (checked) {
				throw e;
			}
			return null;
		}
	}

	/**
	 * Safe call for a method without args, returns null in case of any error
	 * 
	 * @param instance
	 * @param name
	 * @return
	 */
	public static Object callMethod(Object instance, String name) {
		try {
			return callMethod(instance, name,
					Collections.<Class<?>> emptyList(), emptyList(), false);
		} catch (Exception e) {
			return null;
		}
	}

	public static Object getField(Object instance, String name) {
		try {
			return getField(instance, name, false);
		} catch (NoSuchFieldException e) {
			return null; // Can't happen
		} catch (IllegalAccessException e) {
			return null; // Can't happen
		}
	}

	/**
	 * Returns field value.
	 * 
	 * @param instance
	 * @param name
	 * @param checked
	 *            Controls error handling when field is not found or value
	 *            cannot be retrieved. When <code>true</code>, throws
	 *            {@link NoSuchFieldException} or {@link IllegalAccessException}
	 *            , otherwise returns <code>null</code>
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static Object getField(Object instance, String name, boolean checked)
			throws NoSuchFieldException, IllegalAccessException {
		Class<? extends Object> clazz = instance.getClass();
		Field field = findField(clazz, name);

		if (field == null) {
			if (checked) {
				throw new NoSuchFieldException(format("Class: %s, field: %s",
						clazz, name));
			}
			return null;
		}
		field.setAccessible(true);
		try {
			return field.get(instance);
		} catch (IllegalAccessException e) {
			if (checked) {
				throw e;
			}
			return null;
		}
	}

	/**
	 * searches for declared field in given class and all superclasses
	 * 
	 * @param clazz
	 * @param name
	 * @return {@link Field} or <code>null</code>
	 */
	public static Field findField(Class<?> clazz, String name) {
		if (clazz == null) {
			return null;
		}
		try {
			return clazz.getDeclaredField(name);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return findField(clazz.getSuperclass(), name);
		}
	}

	public static Method findMethod(Class<?> clazz, String name) {
		return findMethod(clazz, name, Collections.<Class<?>> emptyList());
	}

	public static Method findMethod(Class<?> clazz, String name,
			List<Class<?>> params) {
		if (clazz == null) {
			return null;
		}

		try {
			return clazz.getDeclaredMethod(name,
					params.toArray(new Class<?>[params.size()]));
		} catch (NoSuchMethodException e) {
			return findMethod(clazz.getSuperclass(), name, params);
		} catch (SecurityException e) {
			return null;
		}
	}

	public static boolean isInstanceOf(Class<?> clazz, String className) {
		if (clazz == null || clazz.getName().equals("java.lang.Object")) {
			return false;
		}
		if (clazz.getName().equals(className)) {
			return true;
		}
		// go through interfaces
		for (Class<?> interfaze : clazz.getInterfaces()) {
			if (isInstanceOf(interfaze, className)) {
				return true;
			}
		}
		// go through super classes
		if (isInstanceOf(clazz.getSuperclass(), className)) {
			return true;
		}
		return false;
	}

}
