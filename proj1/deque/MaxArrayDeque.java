package deque;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {
    private final Comparator<Item> comparator;

    public MaxArrayDeque(Comparator<Item> c) { // 自定义的比较法则
        comparator = c;
    }

    public Item max(Comparator<Item> c) {  // 自定义比较法则的max函数
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 1; i < size(); i++) {
            if (c.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }

    public Item max() {  // 使用默认比较法则的max函数
        return max(comparator);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof MaxArrayDeque)) {
            return false;
        }
        if (((MaxArrayDeque<?>) o).max() != max()) {
            return false;
        }
        return super.equals(o);
    }
}