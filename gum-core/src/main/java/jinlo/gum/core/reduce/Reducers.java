package jinlo.gum.core.reduce;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Reducers {

    private static None NONE = new None();

    private static First FIRST = new First();

    /**
     * 不作任何reduce，直接转为List
     *
     * @param <T>
     */
    public static <T> Reducer<T, List<T>> none() {
        return NONE;
    }

    /**
     * 查找第一个元素
     *
     * @param <T>
     */
    public static <T> Reducer<T, T> first() {
        return FIRST;
    }

    /**
     * 查找第一个满足条件的元素
     *
     * @param <T>
     * @return
     */
    public static <T> Reducer<T, T> firstOf(Predicate<T> predicate) {
        return new FirstOf<>(predicate);
    }

    /**
     * 查找所有满足条件的元素
     *
     * @param <T>
     * @return
     */
    public static <T> Reducer<T, List<T>> collect(Predicate<T> predicate) {
        return new Collect<>(predicate);
    }


    /**
     * 查找是否所有元素都满足断言，注意如果传入为空，返回是true. 参见{@link Stream#allMatch(Predicate)}
     *
     * @param <T>
     * @return
     */
    public static <T> Reducer<T, Boolean> allMatch( Predicate<T> predicate) {
        return new AllMatch<>(predicate);
    }

    /**
     * 查找是否所有元素都满足断言，注意如果传入为空，返回是false.
     *
     * @param <T>
     * @return
     */
    public static <T> Reducer<T, Boolean> allMatchNotEmpty( Predicate<T> predicate) {
        return new AllMatchNotEmpty<>(predicate);
    }
}
