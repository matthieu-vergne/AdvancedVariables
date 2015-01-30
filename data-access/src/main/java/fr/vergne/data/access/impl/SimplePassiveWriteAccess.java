package fr.vergne.data.access.impl;

import fr.vergne.data.access.PassiveWriteAccess;

/**
 * This {@link SimplePassiveWriteAccess} provides the necessary implementation
 * to manage the {@link ValueGenerator} of a {@link PassiveWriteAccess}. It
 * provides the necessary controls and can be reused in other implementations to
 * reduce the effort.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class SimplePassiveWriteAccess<Value> implements
		PassiveWriteAccess<Value> {

	private ValueGenerator<Value> generator;

	public SimplePassiveWriteAccess(ValueGenerator<Value> generator) {
		setValueGenerator(generator);
	}

	@Override
	public void setValueGenerator(ValueGenerator<Value> generator) {
		this.generator = generator;
	}

	@Override
	public ValueGenerator<Value> getValueGenerator() {
		return generator;
	}

}
