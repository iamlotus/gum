package jinlo.gum.core.reduce;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Is all elements satisfy predicate, same like {@link AllMatch} except empty input returns false
 *
 * @param <T>
 */
public class AllMatchNotEmpty<T> implements Reducer<T, Boolean> {
    private Predicate<T> predicate;

    public AllMatchNotEmpty(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    private static class Flag{
        private boolean isEmpty=true;
        private boolean hasUnmatch=false;
    }

    @Override
    public Boolean reduce(Stream<T> elements) {
        if (elements == null) {
            return false;
        } else {
            final Flag flag=new Flag();
            Iterator<T> it=elements.iterator();
            while (it.hasNext()){
                T e=it.next();
                flag.isEmpty=false;
                if (!predicate.test(e)){
                    flag.hasUnmatch=true;
                    break;
                }
            }


            return !flag.isEmpty&&!flag.hasUnmatch;
        }
    }
}
