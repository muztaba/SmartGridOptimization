package Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by seal on 7/19/15.
 */
public class CollectionUtils {
    public static Set<Integer> toSet(List<Integer> list) {
        Set<Integer> set = new HashSet<>();
        for (int i : list) {
            set.add(i);
        }
        return set;
    }

    public static <T> List<T> toList(Set<T> set) {
        List<T> list = new ArrayList<>();
        for (T i : set) {
            list.add(i);
        }
        return list;
    }
}
