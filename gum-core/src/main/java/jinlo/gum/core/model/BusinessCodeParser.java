package jinlo.gum.core.model;

import jinlo.gum.core.annotation.Extension;

import java.util.Set;

/**
 * Application use gum framework define some core entities, each of them contains sufficient information
 * to parse {@link BusinessCode}. Business maintains its owner {@link BusinessCodeParser} which receives
 * one entity and returns BusinessCode, the parser can be shared in different scenarios of same business.
 * <p>
 * The gum framework will iterate on all registed BusinessCodeParsers to find a BusinessCode,
 * then determines how to call {@link Extension} by it.
 * <p>
 * All parsers must be <b>EXCLUSIVE</b>: to any entity, there must be one and only one parser knows it, otherwise there will be exception
 */
public interface BusinessCodeParser extends InstanceRecgonizer {

    /**
     * parse from instance, which has passed {@link InstanceRecgonizer#knows(Object)} check。
     *
     * @param instance
     * @return Business code, can not be null
     * @throws IllegalArgumentException
     */
    BusinessCode parse(Object instance) throws IllegalArgumentException;

    /**
     * All {@link BusinessCode} may be passed from this parser. If any code parsed from this parser is out of the range,
     * there will be runtime exception. <p>
     * It is guaranteed the the result will not be modified, it is recommended to wrap result with {@link java.util.Collections#unmodifiableSet(Set)}
     * <p>
     * The invocation on the method must be <b>idempotent<b/> since the result may be cached.
     * @return all possible results
     */
    Set<BusinessCode> range();
}
