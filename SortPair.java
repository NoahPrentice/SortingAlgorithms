package Sorts;
import java.util.ArrayList;
import java.lang.Integer;
public class SortPair {
    public ArrayList<Integer> pairList;
    public Boolean pairSwap;
    public int pairIndex;

    public SortPair(ArrayList<Integer> list, Boolean swap) {
        pairList = list;
        pairSwap = swap;
        pairIndex = 0;
    }

    public SortPair(ArrayList<Integer> list, int index) {
        pairList = list;
        pairIndex = index;
    }
}