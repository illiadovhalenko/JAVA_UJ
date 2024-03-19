package uj.wmii.pwj.collections;

import java.util.*;

public class ListMerger {
    public static List<Object> mergeLists(List<?> l1, List<?> l2) {
        if (l1 == null && l2 == null)
            return Collections.emptyList();
        List<Object> result = new LinkedList<>();
        Iterator<?> i1 = l1.listIterator();
        Iterator<?> i2 = l2.listIterator();
        while (i1.hasNext() && i2.hasNext()) {
            result.add(i1.next());
            result.add(i2.next());
        }
        while (i1.hasNext())
            result.add(i1.next());
        while (i2.hasNext())
            result.add(i2.next());
        return Collections.unmodifiableList(result);
    }
}
