package org.architectdrone.archevo.misc;

import java.util.BitSet;

//From https://stackoverflow.com/a/2473719
public class Bits {
    public static BitSet convert(int value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0) {
            if (value % 2 != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    public static int convert(BitSet bits) {
        int value = 0;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1 << i) : 0;
        }
        return value;
    }

    public static int covert_subset(BitSet bits, int low_index, int high_index)
    {
        return convert(bits.get(low_index, high_index));
    }
}