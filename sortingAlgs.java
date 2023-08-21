// Author: Noah Prentice
// Date: 21 August 2023
// Based on: https://en.wikipedia.org/wiki/Sorting_algorithm

// Description: I figured I should get familiar with as many basic sorting algorithms as possible. So, I decided to implement the main ones I found 
// on the Wikipedia page above in both Python (for which I was already proficient) and Java (which I am still learning). I have a couple of
// different algorithms here: bubble sort, insertion sort, merge sort, quick sort, heap sort, and a modified quick sort to guarantee O(n log n).

package Sorts;
import java.util.*;
import java.lang.Integer;
import java.lang.Math;

public class sortingAlgs {
    public static void main(String[] args) {

        // This function takes a single, chosen algorithm and prints a shuffled list, sorts it, and prints the sorted list.

        System.out.println("1 = Bubble Sort, 2 = Insertion Sort, 3 = Merge Sort, 4 = Quick Sort, 5 = Heap Sort, 6 = Better Quick Sort");
        Scanner scan1 = new Scanner(System.in);
        System.out.println("What method for sorting would you like to use? ");
        int method = scan1.nextInt();
        System.out.println("How large of a list would you like to use? ");
        int listLength = scan1.nextInt();
        scan1.close();
        ArrayList<Integer> startList = new ArrayList<Integer>();
        for (int i = 0; i < listLength; i++) {
            startList.add(i);
        }
        Collections.shuffle(startList);
        System.out.println(startList);
        if (method == 1) {
            System.out.println(bubbleSort(startList));
        }
        else if (method == 2) {
            System.out.println(insertionSort(startList));
        }
        else if (method == 3) {
            System.out.println(mergeSort(startList));
        }
        else if (method == 4) {
            System.out.println(quickSort(startList));
        }
        else if (method == 5) {
            System.out.println(heapSort(startList));
        }
        else if (method == 6) {
            System.out.println(betterQuickSort(startList, 0));
        }
        else {
            System.out.println("Input error.");
            return;
        }
    }

//
// ~~~~~~~~~~ BUBBLE SORT ~~~~~~~~~~
//

    public static SortPair oneBubbleRun(ArrayList<Integer> list) {

        // This function completes one run of the bubble sort algorithm. In other words, it loops through the given list and compares each entry with
        // the one after it. If the pair is out of order, it swaps them. The swapped value indicates whether any swaps were made. 

        Boolean swapped = false;
        for (int i = 0; i < list.size()-1; i++) { // Loop through the list except the last entry
            Integer currentEntry = list.get(i);
            Integer nextEntry = list.get(i + 1);
            if (Integer.compare(currentEntry, nextEntry) > 0) { // If the pair is out of order, we swap them and indicate that a swap has occurred. 
                list.set(i, nextEntry);
                list.set(i + 1, currentEntry);
                swapped = true;
            }
        }
        SortPair myPair = new SortPair(list, swapped);
        return myPair; // We return both the resulting list and the swapped value.
    }

    public static ArrayList<Integer> bubbleSort(ArrayList<Integer> list) {

        // This function completes the full bubble sort algorithm. It repeatedly does the above process, looping through the list and swapping pairs
        // of entries, until the full list is sorted (swapped = False). The repetition is achieved via recursion.
    
        SortPair newPair = oneBubbleRun(list);

        // Base case: the list was already sorted. The indication that we are in this case is that swapped = False.
        if (newPair.pairSwap == false) {
            return newPair.pairList;
        }

        // Recursive case: the list was not sorted. Then we sort the list after it went through the single run.
        else {
            return bubbleSort(newPair.pairList);
        }
    }

//
// ~~~~~~~~~~ INSERTION SORT ~~~~~~~~~~
//

    public static ArrayList<Integer> insert(ArrayList<Integer> list, int index) {

        // This function takes a particular element of our list and compares it with each entry to its left. If our element comes across a larger
        // entry, it places itself to the right of that entry. Otherwise, it shifts each element to the right until it finds a larger entry or 
        // reaches the end of the list. Go watch an animation for a clearer explanation.

        if (index == 0) { // Since the leftmost entry has nothing to its right, we instantly return the original list in that case.
            return list;
        }
        Integer element = list.get(index);
        for (int i = 1; i < index + 1; i++) { // We "test" each entry to the left of our element.
            Integer testEntry = list.get(index - i);
            if (element <= testEntry) {
                list.set(index - i + 1, testEntry); // If our test entry is smaller, we shift it to the right.
                if (index - i == 0) { // If our element then reaches the end of the list, we make it the first entry.
                    list.set(0, element);
                }
            }
            else { // If our test entry is larger, we place our element to its right.
                list.set(index - i + 1, element);
                return list;
            }
        }
        return list;
    }

    public static ArrayList<Integer> insertionSort(ArrayList<Integer> list) {

        // This function does the actual insertion sort. It does the above process to every entry of the list, via a loop.

        for (int i = 0; i < list.size(); i++) {
            list = insert(list, i);
        }
        return list;
    }

//
// ~~~~~~~~~~ MERGE SORT ~~~~~~~~~~
//

    public static ArrayList<Integer> merge(ArrayList<Integer> left, ArrayList<Integer> right) {

        // This function takes two sorted lists, a left list and a right list, and merges them into one sorted list. This is the meat and potatoes of
        // how the whole merge sort algorithm works. Going until both lists are empty, we add the smallest entry between them to a result list and
        // remove it from its original place. 

        ArrayList<Integer> result = new ArrayList<Integer>();
        while (left.size() != 0 && right.size() != 0) { // If neither list is empty, we have to compare the smallest entry of left with that of right.
            if (left.get(0) < right.get(0)) {
                result.add(left.get(0));
                left.remove(0);
            }
            else {
                result.add(right.get(0));
                right.remove(0);
            }
        }
        // Once one list is empty, we check if both are empty. If one isn't, we know it contains the largest elements, already sorted, so we just add
        // its remaining entries to result sequentially and remove them from the original list.
        while (right.size() != 0) {
            result.add(right.get(0));
            right.remove(0);
        }
        while (left.size() != 0) {
            result.add(left.get(0));
            left.remove(0);
        }
        return result;
    }


    public static ArrayList<Integer> mergeSort(ArrayList<Integer> list) {

        // This function actually does the merge sort algorithm. Noticing that lists of 0 or 1 elements are already sorted, we can break the list
        // down into these lists and then merge them together using the process above. This will result in one sorted list.

        // Base case: our list has size 0 or 1. In this case it is already done, so we return it.
        if (list.size() <= 1) {
            return list;
        }

        // Recursive case: our list has length larger than 1. Then we split our list in half and sort the two other lists recursively.
        else {
            ArrayList<Integer> left = new ArrayList<Integer>();
            ArrayList<Integer> right = new ArrayList<Integer>();
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size()/2) {
                    left.add(list.get(i));
                }
                else {
                    right.add(list.get(i));
                }
            }
            left = mergeSort(left);
            right = mergeSort(right);
            return merge(left, right); // We then merge the two sorted lists and return that.
        }
    }

//
// ~~~~~~~~~~ QUICK SORT ~~~~~~~~~~
//

    public static ArrayList<Integer> quickSort(ArrayList<Integer> list) {

        // None of these next functions require a second. For quick sort, we choose a random element, called the pivot, and we place all elements
        // smaller than the pivot in one list, and the remaining elements in another. We sort these smaller lists recursively and then combine.
        // This algorithm has a dependency, where it requires choosing a random integer. 

        // Base case: our list has length 0 or 1. Like merge sort, we're done.
        if (list.size() <= 1) {
            return list;
        }

        // Recursive case: our list has size larger than 1. Then we separate into two lists, again like merge sort. Unlike merge sort, this isn't
        // done based on the index of the elements, but rather based on their relationship to the chosen pivot (e.g. larger, smaller, equal).
        Random rand = new Random();
        int pivot = rand.nextInt(list.size());
        ArrayList<Integer> left = new ArrayList<Integer>();
        ArrayList<Integer> right = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < list.get(pivot)) { // If an entry is smaller than the pivot, it goes in the left list.
                left.add(list.get(i));
            }
            else { // Otherwise, it goes in the right list.
                right.add(list.get(i));
            }
        }
        left = quickSort(left); // We then recursively sort these two lists and combine.
        left.addAll(quickSort(right));
        return left;
    }

//
// ~~~~~~~~~~ HEAP SORT ~~~~~~~~~~
//

    public static ArrayList<Integer> heapSort(ArrayList<Integer> list) {

        // Heap sort is super simple. A (min-)heap is a data structure which, by default, keeps the smallest entry first. So, we turn our list into
        // a heap, and repeatedly add the first element to the list while removing it from the heap. Because heaps are used, this function has a
        // dependency.

        PriorityQueue<Integer> heap = new PriorityQueue<Integer>(list); // Turn our list into a heap.
        for (int i = 0; i < list.size(); i++) { // Replace the original list with the ordered heap elements.
            list.set(i, heap.poll());
        }
        return list;
    }

//
// ~~~~~~~~~~ BETTER QUICK SORT ~~~~~~~~~~
//

    public static ArrayList<Integer> betterQuickSort(ArrayList<Integer> list, int depth) {

        // The better quick sort algorithm does exactly what quick sort does, but it has a limit to the depth of recursion. If our recursion exceeds
        // this limit, then we use heapsort on the smaller lists. As a result, this function has three dependencies, one for calculating the depth
        // limit, one for the random integer selection of quick sort, and one for the heaps required for heap sort.


        if (list.size() <= 1) {
            return list;
        }
        else if (depth > 1 + 2*Math.floor(Math.log(list.size())/Math.log(2))) {
            return heapSort(list);
        }
        else {
            Random rand = new Random();
            int pivot = rand.nextInt(list.size());
            ArrayList<Integer> left = new ArrayList<Integer>();
            ArrayList<Integer> right = new ArrayList<Integer>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) < list.get(pivot)) {
                    left.add(list.get(i));
                }
                else {
                    right.add(list.get(i));
                }
            }
            left = betterQuickSort(left, depth + 1);
            left.addAll(betterQuickSort(right, depth + 1));
            return left;
        }
    }
}