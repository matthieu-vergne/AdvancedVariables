package fr.vergne.data.access;

/**
 * {@link ActiveWriteAccess} provides a write access to the value of a property
 * in an active way, such that the user decides when the value is modified.<br/>
 * <br/>
 * If the control of the time when the writing occurs should be delegated, you
 * should consider to use a {@link PassiveWriteAccess}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public interface ActiveWriteAccess<Value> extends PropertyAccess<Value> {

	/**
	 * 
	 * @param value
	 *            the new value of the property
	 */
	public void set(Value value);
}
