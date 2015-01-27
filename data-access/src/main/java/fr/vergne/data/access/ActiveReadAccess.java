package fr.vergne.data.access;

/**
 * {@link ActiveReadAccess} provides a read access to the value of a property in
 * an active way, such that the user decides when the value is read.
 * Consequently, this value should be ready for reading at any time.<br/>
 * <br/>
 * If the control of the time when the reading occurs should be delegated, you
 * should consider to use a {@link PassiveReadAccess}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public interface ActiveReadAccess<Value> extends PropertyAccess<Value> {
	/**
	 * 
	 * @return the current value of the property
	 */
	public Value get();
}
