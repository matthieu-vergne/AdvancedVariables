package fr.vergne.data.access;

import fr.vergne.data.access.impl.SimpleAccessProvider;
import fr.vergne.data.access.impl.advanced.ControlledProperty;
import fr.vergne.data.access.impl.advanced.FlowController;

/**
 * {@link PropertyAccess} is the root interface of all the access interfaces.
 * These accesses are designed based on the perspective of the user of the data.
 * If the user decides <i>when</i> to access the property, the access provided
 * to the user is said to be <b>active</b>. At the opposite, if the user is
 * waiting for the value to be considered (provided or requested) then the
 * access is said to be <b>passive</b>. For the <i>which</i> dimension, we reuse
 * the notions of <b>write</b> operation, if the user is the source of the data,
 * and <b>read</b> operation, if the user is the target of the data. The four
 * configurations are described further in their own interfaces:
 * <ul>
 * <li>{@link ActiveReadAccess} to read a value on demand</li>
 * <li>{@link ActiveWriteAccess} to write a value on demand</li>
 * <li>{@link PassiveReadAccess} to read a value when generated</li>
 * <li>{@link PassiveWriteAccess} to write a value when requested</li>
 * </ul>
 * If one want to manage different {@link PropertyAccess}es on the same
 * property, he can create a class which implements the corresponding
 * {@link PropertyAccess} interfaces, like the {@link ControlledProperty} and
 * {@link FlowController} examples. However, when several properties need to be
 * managed, one cannot implement several time the same interface for different
 * properties. In such a case, one implementation needs to be made for each
 * property separately, or you can use a {@link PropertyAccessProvider} to deal
 * with such multiplicity in a centralized manner. The
 * {@link SimpleAccessProvider} provides an implementation able to deal with
 * basic needs.<br/>
 * <br/>
 * Notice that this {@link PropertyAccess} interface is the common root for all
 * four types of accesses. No one should extend nor implement
 * {@link PropertyAccess}. Please look at its extensions and their Javadoc to
 * know their details and choose among them for further extension or
 * implementation.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 *            the type of value to access
 */
public interface PropertyAccess<Value> {

}
