package test;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Uses reflection to test a class's method with provided inputs and expected
 * results. The names of the class and method are specified as String
 * parameters. The {@code className} parameter should include its package name.
 * <p>
 * If one of the test cases fail, the program will print out the failed test and
 * exit.
 */
@SuppressWarnings("unchecked")
public class Test {

	private final static String SUCCESS = "Congrats! All tests passed.";

	/**
	 * Test method as if calling {@code className.methodName()} assuming only
	 * one parameter for the tested method.
	 * 
	 * @param <T>
	 *            method input type
	 * @param <R>
	 *            method return type
	 * @param className
	 *            the class to be tested
	 * @param methodName
	 *            the method to be tested
	 * @param inputs
	 *            an array containing all test cases' inputs
	 * @param expected
	 *            an array containing all expected results
	 */
	public static <T, R> void assertEqual(String className, String methodName,
			T[] inputs, R[] expected) {
		validateLengths(inputs, expected);
		try {
			// use reflection to get the type of class indicated by "className"
			Class<?> c = Class.forName(className);
			Class<T> inputType = (Class<T>) inputs[0].getClass();
			Method m = c.getMethod(methodName, inputType);
			// static methods will ignore this instance
			Object o = c.newInstance();
			for (int i = 0; i < inputs.length; i++) {
				R e = expected[i];
				R output = (R) m.invoke(o, inputs[i]);
				if (e.getClass().isArray()) {
					int n = Array.getLength(e);
					for (int j = 0; j < n; j++) {
						if (!Array.get(e, j).equals(Array.get(output, j))) {
							System.out.println("test failed for "
									+ Arrays.asList(inputs[i]) + " expected "
									+ Arrays.asList(e) + ", output "
									+ Arrays.asList(output));
							System.exit(-1);
						}
					}
				} else if (!e.equals(output)) {
					System.out.println("test:\n " + inputs[i] + "failed, "
							+ " expected " + expected + ", output " + output);
					System.exit(-1);
				}

			}
			System.out.println(SUCCESS);
		} catch (ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method as if calling {@code className.methodName()} assuming the
	 * tested method has only one parameter.
	 * 
	 * @param <T>
	 *            method input type
	 * @param <R>
	 *            method return type
	 * @param className
	 *            the class to be tested
	 * @param methodName
	 *            the method to be tested
	 * @param tests
	 *            a map containing the test inputs and expected results
	 */
	public static <T, R> void assertEqual(String className, String methodName,
			Map<T, R> tests) {
		T[] inputs = (T[]) tests.keySet().toArray();
		R[] expected = (R[]) tests.values().toArray();
		assertEqual(className, methodName, inputs, expected);
	}

	/**
	 * Test method as if calling {@code className.methodName()} assuming the
	 * tested method has only one parameter.
	 * 
	 * @param <T>
	 *            method input type
	 * @param <R>
	 *            method return type
	 * @param className
	 *            the class to be tested
	 * @param methodName
	 *            the method to be tested
	 * @param inputs
	 *            a list containing all test cases' inputs
	 * @param expected
	 *            a list containing all expected results
	 */
	public static <T, R> void assertEqual(String className, String methodName,
			List<T> inputs, List<R> expected) {
		T[] inputs1 = (T[]) inputs.toArray();
		R[] expected1 = (R[]) expected.toArray();
		assertEqual(className, methodName, inputs1, expected1);

	}

	/**
	 * Test method as if calling {@code className.methodName()}, the method can
	 * have multiple parameters.
	 * 
	 * @param <T>
	 *            method input type
	 * @param <R>
	 *            method return type
	 * @param className
	 *            the class to be tested
	 * @param methodName
	 *            the method to be tested
	 * @param inputs
	 *            a list containing all test cases' inputs
	 * @param expected
	 *            a list containing all expected results
	 */
	public static <R> void assertMPEqual(String className, String methodName,
			List<Object[]> inputs, List<R> expected) {
		Object[][] inputs1 = (Object[][]) inputs.toArray();
		R[] expected1 = (R[]) expected.toArray();
		assertMPEqual(className, methodName, inputs1, expected1);
	}

	/**
	 * Test method as if calling {@code className.methodName()}, the method can
	 * have multiple parameters.
	 * 
	 * @param <T>
	 *            method input type
	 * @param <R>
	 *            method return type
	 * @param className
	 *            the class to be tested
	 * @param methodName
	 *            the method to be tested
	 * @param inputs
	 *            an array containing all test cases' inputs
	 * @param expected
	 *            an array containing all expected results
	 */
	public static <R> void assertMPEqual(String className, String methodName,
			Object[][] inputs, R[] expected) {
		validateLengths(inputs, expected);
		try {
			Class<?> c = Class.forName(className);
			int n = inputs[0].length;
			Class<?>[] inputTypes = new Class<?>[n];
			for (int i = 0; i < n; i++) {
				inputTypes[i] = inputs[0][i].getClass();
			}
			Method m = c.getMethod(methodName, inputTypes);
			Object o = c.newInstance();
			for (int i = 0; i < inputs.length; i++) {
				R e = expected[i];
				R output = (R) m.invoke(o, inputs[i]);
				if (e.getClass().isArray()) {
					int n1 = Array.getLength(e);
					for (int j = 0; j < n1; j++) {
						if (!Array.get(e, j).equals(Array.get(output, j))) {
							System.out.println("test failed for "
									+ Arrays.asList(inputs[i]) + " expected "
									+ Arrays.asList(e) + ", output "
									+ Arrays.asList(output));
							System.exit(-1);
						}
					}
				} else if (!e.equals(output)) {
					System.out.println("test:\n " + inputs[i] + "failed, "
							+ " expected " + expected + ", output " + output);
					System.exit(-1);
				}

			}
			System.out.println(SUCCESS);

		} catch (ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method as if calling {@code className.methodName()} assuming the
	 * tested method has multiple input parameters.
	 * 
	 * @param <T>
	 *            method input type
	 * @param <R>
	 *            method return type
	 * @param className
	 *            the class to be tested
	 * @param methodName
	 *            the method to be tested
	 * @param tests
	 *            a map containing the test inputs and expected results
	 */
	public static <T, R> void assertMPEqual(String className, String methodName,
			Map<T, R> tests) {
		Object[][] inputs = (Object[][]) tests.keySet().toArray();
		R[] expected = (R[]) tests.values().toArray();
		assertMPEqual(className, methodName, inputs, expected);
	}

	private static <T, R> void validateLengths(T[] inputs, R[] expected) {
		if (inputs.length != expected.length) {
			System.out.println("inputs and expected lengths do not match.");
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		System.out.println("Hello World.");
	}
}
