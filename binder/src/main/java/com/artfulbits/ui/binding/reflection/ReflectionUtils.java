package com.artfulbits.ui.binding.reflection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Methods for manipulating classes via reflection. */
@SuppressWarnings("unused")
public final class ReflectionUtils {
  /* [ CONSTANTS ] ================================================================================================= */

  /** Caching of the reflected information. Class-to-fields. */
  private static final Map<Class<?>, List<Field>> sCacheFields = new HashMap<>();

  /** Caching of the reflected information. Class-to-methods. */
  private static final Map<Class<?>, List<Method>> sCacheMethods = new HashMap<>();

  /* [ STATIC METHODS ] ============================================================================================ */

  /**
   * Find in list of fields specific one by its name.
   *
   * @param fields list of fields. Should be sorted by name!
   * @param name field name which we want to find.
   * @return found field, otherwise {@code null}.
   */
  @Nullable
  public static Field findField(@NonNull final List<Field> fields, @NonNull final String name) {
    final int index = Collections.binarySearch(fields, name, SearchByFieldNameComparator.INSTANCE);

    // field found
    if (index >= 0 && index < fields.size()) {
      return fields.get(index);
    }

    return null;
  }

  /**
   * Find in list of methods specific one by its name.
   *
   * @param methods list of methods. Should be sorted by name!
   * @param name field name which we want to find.
   * @return found field, otherwise {@code null}.
   */
  @Nullable
  public static Method findMethod(@NonNull final List<Method> methods, @NonNull final String name) {
    final int index = Collections.binarySearch(methods, name, SearchByMethodNameComparator.INSTANCE);

    // method found
    if (index >= 0 && index < methods.size()) {
      return firstMethod(methods, index);
    }

    return null;
  }

  /**
   * Find in list of methods specific one by its name.
   *
   * @param methods list of methods. Should be sorted by name!
   * @param name field name which we want to find.
   * @return found field, otherwise {@code null}.
   */
  @Nullable
  public static Entry find(@NonNull final List<Entry> methods, @NonNull final String name) {
    final int index = Collections.binarySearch(methods, name, SearchByExecutableNameComparator.INSTANCE);

    // entry found
    if (index >= 0 && index < methods.size()) {
      return firstEntry(methods, index);
    }

    return null;
  }

  /**
   * Extract all inherited fields from class. Results are sorted by name.
   *
   * @param type type to check
   * @return list of found fields.
   */
  @NonNull
  public synchronized static List<Field> getAllFields(@NonNull final Class<?> type) {
    if (sCacheFields.containsKey(type)) {
      return sCacheFields.get(type);
    }

    final ArrayList<Field> results = new ArrayList<>();
    sCacheFields.put(type, results);

    Class<?> i = type;
    while (i != null && i != Object.class) {
      for (final Field field : i.getDeclaredFields()) {
        if (!field.isSynthetic()) {
          results.add(field);
        }
      }

      i = i.getSuperclass();
    }

    Collections.sort(results, ByFieldName.INSTANCE);

    return results;
  }

  /**
   * Compose list of all methods declared in class.
   *
   * @param type type to check
   * @return list of found methods.
   */
  @NonNull
  public synchronized static List<Method> getAllMethods(@NonNull final Class<?> type) {
    if (sCacheMethods.containsKey(type)) {
      return sCacheMethods.get(type);
    }

    final ArrayList<Method> results = new ArrayList<>();
    sCacheMethods.put(type, results);

    Class<?> i = type;
    while (i != null && i != Object.class) {
      for (final Method method : i.getDeclaredMethods()) {
        if (!method.isSynthetic()) {
          results.add(method);
        }
      }

      i = i.getSuperclass();
    }

    Collections.sort(results, ByMethodName.INSTANCE);

    return results;
  }

  /** Get list of all executables inside the class that can be used for binding. */
  @NonNull
  public static List<Entry> getAll(@NonNull final Class<?> type) {
    final List<Field> fields = getAllFields(type);
    final List<Method> methods = getAllMethods(type);

    final List<Entry> results = new ArrayList<>(fields.size() + methods.size());

    for (final Field f : fields) {
      results.add(new FieldFacade(f));
    }

    for (final Method m : methods) {
      results.add(new MethodFacade(m));
    }

    Collections.sort(results, ByExecutableName.INSTANCE);

    return results;
  }

  /**
   * Compares two {@code int} values.
   *
   * @return 0 if lhs = rhs, less than 0 if lhs &lt; rhs, and greater than 0 if lhs &gt; rhs.
   */
  private static int compare(final int lhs, final int rhs) {
    return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
  }

  /** Compose string with method parameters types. */
  @NonNull
  private static String toStringWithTypes(@NonNull final Method m) {
    final StringBuilder sb = new StringBuilder(128);
    sb.append(m.getName()).append("(");

    String separator = "";
    for (final Class<?> c : m.getParameterTypes()) {
      sb.append(separator).append(c.toString());
      separator = ", ";
    }

    return sb.append(")").toString();
  }

  /**
   * Find first entry with the same name.
   *
   * @param methods list of reflected type entries.
   * @param index index of found entry.
   */
  private static Entry firstEntry(@NonNull final List<Entry> methods, final int index) {
    final Entry start = methods.get(index);
    final int firstCandidate = index - 1;

    Entry prev, result = start;

    for (int position = firstCandidate; position >= 0; position--) {
      prev = methods.get(position);

      if (!start.getName().equals(prev.getName())) {
        break;
      }

      result = prev;
    }

    return result;
  }

  /**
   * Find first entry with the same name.
   *
   * @param methods list of reflected type entries.
   * @param index index of found entry.
   */
  private static Method firstMethod(@NonNull final List<Method> methods, final int index) {
    final Method start = methods.get(index);
    final int firstCandidate = index - 1;

    Method prev, result = start;

    for (int position = firstCandidate; position >= 0; position--) {
      prev = methods.get(position);

      if (!start.getName().equals(prev.getName())) {
        break;
      }

      result = prev;
    }

    return result;
  }

	/* [ NESTED DECLARATIONS ] ======================================================================================= */

  private static final class ByExecutableName implements Comparator<Entry> {
    /** Single instance. */
    public final static ByExecutableName INSTANCE = new ByExecutableName();

    /** private constructor. */
    private ByExecutableName() {
    }

    /** Compare names. */
    @Override
    public int compare(final Entry lhs, final Entry rhs) {
      return lhs.getName().compareTo(rhs.getName());
    }
  }

  /** Sort fields by name. */
  private static final class ByFieldName implements java.util.Comparator<Field> {
    /** Single instance. */
    public static final ByFieldName INSTANCE = new ByFieldName();

    /** private constructor. */
    private ByFieldName() {
    }

    /** {@inheritDoc} */
    @Override
    public int compare(final Field lhs, final Field rhs) {
      return lhs.getName().compareTo(rhs.getName());
    }
  }

  /** Sort methods by name. */
  private static final class ByMethodName implements java.util.Comparator<Method> {
    /** Single instance. */
    public final static ByMethodName INSTANCE = new ByMethodName();

    /** private constructor. */
    private ByMethodName() {
    }

    /** {@inheritDoc} */
    @Override
    public int compare(final Method lhs, final Method rhs) {
      final String left = lhs.getName();
      final String right = rhs.getName();
      final int result = left.compareTo(right);

      // compare by name, and than by quantity of arguments
      return (result != 0) ? result :
          ReflectionUtils.compare(lhs.getParameterTypes().length, rhs.getParameterTypes().length);
    }
  }

  /** Search in fields collection by field name. */
  private static final class SearchByExecutableNameComparator implements Comparator<Object> {
    /** Single instance. */
    public final static SearchByExecutableNameComparator INSTANCE = new SearchByExecutableNameComparator();

    /** private constructor. */
    private SearchByExecutableNameComparator() {
    }

    /** {@inheritDoc} */
    @Override
    public int compare(Object lhs, Object rhs) {
      if (lhs instanceof Entry && rhs instanceof String) {
        return ((Entry) lhs).getName().compareTo((String) rhs);
      }

      if (lhs instanceof String && rhs instanceof Entry) {
        return ((String) lhs).compareTo(((Entry) rhs).getName());
      }

      throw new AssertionError("unexpected");
    }
  }

  /** Search in fields collection by field name. */
  private static final class SearchByFieldNameComparator implements Comparator<Object> {
    /** Single instance. */
    public final static SearchByFieldNameComparator INSTANCE = new SearchByFieldNameComparator();

    /** private constructor. */
    private SearchByFieldNameComparator() {
    }

    /** {@inheritDoc} */
    @Override
    public int compare(Object lhs, Object rhs) {
      if (lhs instanceof Field && rhs instanceof String) {
        return ((Field) lhs).getName().compareTo((String) rhs);
      }

      if (lhs instanceof String && rhs instanceof Field) {
        return ((String) lhs).compareTo(((Field) rhs).getName());
      }

      throw new AssertionError("unexpected");
    }
  }

  /** Search in fields collection by field name. */
  private static final class SearchByMethodNameComparator implements Comparator<Object> {
    /** Single instance. */
    public final static SearchByMethodNameComparator INSTANCE = new SearchByMethodNameComparator();

    /** private constructor. */
    private SearchByMethodNameComparator() {
    }

    /** {@inheritDoc} */
    @Override
    public int compare(Object lhs, Object rhs) {
      if (lhs instanceof Method && rhs instanceof String) {
        return ((Method) lhs).getName().compareTo((String) rhs);
      }

      if (lhs instanceof String && rhs instanceof Method) {
        return ((String) lhs).compareTo(((Method) rhs).getName());
      }

      throw new AssertionError("unexpected");
    }
  }

  /** Facade for field. */
  private static final class FieldFacade implements Entry {
    private final Field mF;

    public FieldFacade(@NonNull final Field f) {
      mF = f;

      if (!mF.isAccessible()) {
        mF.setAccessible(true);
      }
    }

    @Override
    public String getName() {
      return mF.getName();
    }

    @Override
    public String getFullName() {
      return mF.getType().toString() + " " + mF.getName();
    }

    @Override
    public AccessibleObject getRawType() {
      return mF;
    }

    @Override
    public Object invoke(final Object receiver, final Object... args) throws IllegalAccessException {

      // DONE: detect do we need Get or Set by number of parameters
      if (null != args && args.length > 0) {
        mF.set(receiver, args[0]);
        return args[0];
      }

      return mF.get(receiver);
    }

    @Override
    public String toString() {
      return getName();
    }
  }

  /** Facade for method. */
  private static final class MethodFacade implements Entry {
    private final Method mM;

    public MethodFacade(@NonNull final Method m) {
      mM = m;

      if (!mM.isAccessible()) {
        mM.setAccessible(true);
      }
    }

    @Override
    public String getName() {
      return mM.getName();
    }

    @Override
    public String getFullName() {
      return toStringWithTypes(mM);
    }

    @Override
    public AccessibleObject getRawType() {
      return mM;
    }

    @Override
    public Object invoke(final Object receiver, final Object... args) throws IllegalAccessException, InvocationTargetException {
      return mM.invoke(receiver, args);
    }

    @Override
    public String toString() {
      final int length = mM.getParameterTypes().length;
      final String params = (length > 0) ? "..." : "";
      return getName() + "(" + params + ")";
    }

  }
}
