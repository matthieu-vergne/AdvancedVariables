package fr.vergne.data.access;

import fr.vergne.data.access.impl.ControlledProperty;
import fr.vergne.data.access.impl.FlowController;
import fr.vergne.data.access.impl.SimpleAccessProvider;

/**
 * A basic interaction with a variable would be summarized by having the
 * possibility to read/write its value on the fly. Said another way, their would
 * have a <i>user</i> who interacts with a <i>property</i> by requesting its
 * current value (read) or providing it its new value (write). But if we go
 * further in the analysis, two decisions are made:
 * <ul>
 * <li>which data should flow?</li>
 * <li>when the data should flow?</li>
 * </ul>
 * The first point corresponds to choosing whether we read or write the data.
 * When reading, this is the data of the property which flows to the user, while
 * when writing it is the opposite. The second point corresponds to choosing
 * when this operation should occur.<br/>
 * <br/>
 * With a simple variable interaction, the decision is fully made by the user:
 * the data which flows is the one from the user (write) or the variable (read),
 * and the flow should occur now, so that at the next instruction the variable
 * already has its new value (write) or its value has been stored or used in
 * another computation (read). However, producing an immediate flow is not
 * always wanted. For instance, when dealing with a GUI, one could click a
 * button which requires some computation to be done, such as changing the size
 * of the window. In such a case, all other components should be resized
 * correspondingly to fit in the window. These components (or the layout
 * manager) would be the users of the property "window size", because they need
 * it to know which size to apply to themselves. However, while they should
 * consider any update of this property, it would be a burden to read it from an
 * infinite loop to recompute it everytime or to know when it is updated. This
 * is where the decision between what should flow and when it should flow
 * becomes separated: what should flow is the window size, because the
 * components need it, but when it should flow is decided by the window, when it
 * is resized. Delegating both controls to the components would lead them to run
 * constantly, while delegating all to the window would lead to implement all
 * the components' logics into the window. <br/>
 * <br/>
 * To avoid these issues, one should be able to tell separately which data
 * should flow and when it should do it. In Swing, this is managed by using the
 * listener pattern: the window can provide several properties, like its size,
 * and it can notify some components when it is resized, as long as they
 * register a listener which will take care of this notification. In this case,
 * the components would register a listener on the window such that, when the
 * window is resized, the components are notified and can react correspondingly,
 * in other words read the window size at this moment and adapt their own size
 * to fit well. Regarding the control, which data flows is decided by the
 * components (the window notifies the components without knowing what they will
 * do once notified) but the instant it flows is decided by the window (the
 * components have no idea of when they will receive a notification). <br/>
 * <br/>
 * In this project, we deal with these two dimensions (which + when) by
 * specifying specific {@link PropertyAccess}es from the perspective of the
 * user. If the user decides <i>when</i> to access the property, the access
 * provided to the user is said to be <b>active</b>. At the opposite, if the
 * user is waiting for the value to be considered (provided or requested) then
 * the access is said to be <b>passive</b>. For the <i>which</i> dimension, we
 * reuse the notions of <b>write</b> operation, if the user is the source of the
 * data, and <b>read</b> operation, if the user is the target of the data. The
 * four configurations are described further in their own interfaces:
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
 * One could consider that such access management could be a burden to master
 * and could prefer to make it by hand by creating dedicated classes and
 * methods, which would have the added advantage to be further customizable. The
 * aim of this {@link PropertyAccess} interface and its extensions is however to
 * centralize the experience gathered in data access to provide a simple basis
 * which can be combined easily to obtain advanced interactions that other
 * programmers would not necessarily think about nor put effort in designing.
 * For instance, managing listeners is easy but can become verbose and
 * repetitive, thus annoying to manage and leading to an arguable economy of
 * design effort and code writing. These interfaces and their implementations
 * come as supporting tools to facilitate the implementation of advanced
 * interactions, providing support for data flow optimization without the burden
 * of creating additional classes or methods.<br/>
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
