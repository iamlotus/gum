package jinlo.gum.core.model;

import jinlo.gum.core.annotation.Extension;

/**
 * Application use gum framework define some core entities, each of them contains sufficient information
 * to parse {@link BusinessCode}. Every business maintains its owner {@link BusinessCodeParser} which receives
 * one entity and returns BusinessCode, the parser can be shared in different scenarios of same business.
 * <p>
 * The gum framework will iterate on BusinessCodeParsers of all registered BusinessTemplates to find a BusinessCode,
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
}
