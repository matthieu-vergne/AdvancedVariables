package fr.vergne.access;

/**
 * {@link PullReadAccess} is a type of {@link Access} allowing to read the value
 * of a property on a "pull" basis: any {@link Object} can request the value of
 * the property at any time. Implementing this interface implies to ensure that
 * the value of the property can be delivered at any time. If such a control is
 * not wanted, you should consider to use a {@link PushReadAccess}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public interface PullReadAccess<Value> extends Access<Value> {
	/**
	 * 
	 * @return the current value of the property
	 */
	public Value get();
}
