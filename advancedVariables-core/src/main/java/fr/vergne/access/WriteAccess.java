package fr.vergne.access;

/**
 * A {@link WriteAccess} allows to re-affect directly the value of a property.
 * This is the typical equivalent of "a = b", but here written "a.set(b)". Any
 * {@link Object} having a {@link WriteAccess} can modify the property's value.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public interface WriteAccess<Value> extends Access<Value> {

	/**
	 * 
	 * @param value
	 *            the new value of the property
	 */
	public void set(Value value);
}
