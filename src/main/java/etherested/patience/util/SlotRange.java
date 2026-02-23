package etherested.patience.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

// represents a parseable range of inventory slot indices (e.g., "0-3", "1,2,5-9")
public class SlotRange implements Iterable<Integer> {
    private final List<Integer> slots;

    public SlotRange() {
        this.slots = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    public SlotRange(List<Integer> slots) {
        this.slots = slots;
    }

    public static SlotRange parse(String input) {
        List<Integer> result = new ArrayList<>();

        for (String part : input.split(",")) {
            part = part.trim();
            if (part.contains("-")) {
                String[] range = part.split("-");
                int start = Integer.parseInt(range[0].trim());
                int end = Integer.parseInt(range[1].trim());
                IntStream.rangeClosed(start, end).forEach(result::add);
            } else {
                result.add(Integer.parseInt(part));
            }
        }

        return new SlotRange(result);
    }

    public List<Integer> getSlots() {
        return slots;
    }

    @Override
    public @NotNull Iterator<Integer> iterator() {
        return slots.iterator();
    }

    @Override
    public String toString() {
        if (slots.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        int start = -1;
        int end = -1;

        for (int slot : slots) {
            if (start == -1) {
                start = end = slot;
            } else if (slot == end + 1) {
                end = slot;
            } else {
                appendRange(sb, start, end);
                sb.append(",");
                start = end = slot;
            }
        }

        appendRange(sb, start, end);
        return sb.toString();
    }

    private void appendRange(StringBuilder sb, int start, int end) {
        if (start == end) {
            sb.append(start);
        } else {
            sb.append(start).append("-").append(end);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotRange that = (SlotRange) o;
        return slots.equals(that.slots);
    }

    @Override
    public int hashCode() {
        return slots.hashCode();
    }
}
