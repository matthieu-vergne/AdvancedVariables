package fr.vergne.data.access.impl.advanced;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.PassiveReadAccess;
import fr.vergne.data.access.PassiveWriteAccess;
import fr.vergne.data.access.impl.SimplePassiveWriteAccess;

/**
 * A {@link Puller} is a common pattern where some sources of data aims at
 * providing values to some other entities on demand. Thus, the sources specify
 * how to retrieve the current value through
 * {@link #setValueGenerator(ValueGenerator)} and the entities will use
 * {@link #get()} to retrieve it when needed.<br/>
 * <br/>
 * While a {@link CheckableFlowController} can do the same thing than a
 * {@link Puller}, the {@link Puller} is specialized for a one way flow
 * controlled by the reader. Thus, it does not provide any
 * {@link PassiveReadAccess} nor additional methods to control the transfer.
 * Consequently, it is recommended for simpler use and performance purpose.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class Puller<Value> extends SimplePassiveWriteAccess<Value> implements
		ActiveReadAccess<Value>, PassiveWriteAccess<Value> {

	public Puller(ValueGenerator<Value> generator) {
		super(generator);
	}

	@Override
	public Value get() {
		return getValueGenerator().generateValue();
	}

}
