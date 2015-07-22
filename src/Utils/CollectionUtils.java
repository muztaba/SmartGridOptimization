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

    public static List<Integer> toList(Set<Integer> set) {
        List<Integer> list = new ArrayList<>();
        for (int i : set) {
            list.add(i);
        }
        return list;
    }
}
