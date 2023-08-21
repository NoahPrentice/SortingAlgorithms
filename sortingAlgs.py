# Author: Noah Prentice
# Date: 21 August 2023
# Based on: https://en.wikipedia.org/wiki/Sorting_algorithm

# Description: I figured I should get familiar with as many basic sorting algorithms as possible. So, I decided to implement the main ones I found 
# on the Wikipedia page above in both Python (for which I was already proficient) and Java (which I am still learning). I have a couple of
# different algorithms here: bubble sort, insertion sort, merge sort, quick sort, heap sort, and a modified quick sort to guarantee O(n log n).

import random
import heapq
import math

def main ():
    
    # This function takes a single, chosen algorithm and prints a shuffled list, sorts it, and prints the sorted list.
    
    print("1 = Bubble Sort, 2 = Insertion Sort, 3 = Merge Sort, 4 = Quick Sort, 5 = Heap Sort, 6 = Better Quick Sort")
    method = int(input("What method for sorting would you like to use? "))
    listLength = int(input("How large of a list would you like to use? "))
    myList = []
    for i in range(listLength):
        myList.append(i)
    random.shuffle(myList)
    print(myList)
    if method == 1:
        print(bubbleSort(myList))
    elif method == 2:
        print(insertionSort(myList))
    elif method == 3:
        print(mergeSort(myList))
    elif method == 4:
        print(quickSort(myList))
    elif method == 5:
        print(heapSort(myList))
    elif method == 6:
        print(betterQuickSort(myList, 0))
    else:
        print("Input error.")
        return
    
#
# ~~~~~~~~~~ BUBBLE SORT ~~~~~~~~~~
#

def oneBubbleRun(list):
    
    # This function completes one run of the bubble sort algorithm. In other words, it loops through the given list and compares each entry with
    # the one after it. If the pair is out of order, it swaps them. The swapped value indicates whether any swaps were made. 
    
    swapped = False
    for i in range(len(list) - 1): # Loop through the list. Since the last entry can't be compared to the one after it, we don't look at it.
        currentEntry = list[i]
        nextEntry = list[i + 1]
        if currentEntry > nextEntry: # If the pair is out of order, we swap them and indicate that a swap has occurred. 
            list[i] = nextEntry
            list[i + 1] = currentEntry
            swapped = True
    return [list, swapped] # We return both the resulting list and the swapped value.

def bubbleSort(list):
    
    # This function completes the full bubble sort algorithm. It repeatedly does the above process, looping through the list and swapping pairs
    # of entries, until the full list is sorted (swapped = False). The repetition is achieved via recursion.
    
    pair = oneBubbleRun(list)
    # Base case: the list was already sorted. The indication that we are in this case is that swapped = False.
    if pair[1] == False: # If the list was sorted, then we return it.
        return pair[0]
    # Recursive case: the list was not sorted. Then we sort the list after it went through the single run.
    else:
        return bubbleSort(pair[0])

#
# ~~~~~~~~~~ INSERTION SORT ~~~~~~~~~~
#

def insert(list, index):

    # This function takes a particular element of our list and compares it with each entry to its left. If our element comes across a larger
    # entry, it places itself to the right of that entry. Otherwise, it shifts each element to the right until it finds a larger entry or 
    # reaches the end of the list. Go watch an animation for a clearer explanation.

    if index == 0: # Since the leftmost entry has nothing to its right, we instantly return the original list in that case.
        return list
    element = list[index]
    for i in range(1, index + 1): # We "test" each entry to the left of our element.
        testEntry = list[index - i] 
        if element <= testEntry: # If our test entry is smaller, we shift it to the right.
            list[index - i + 1] = testEntry
            if index - i == 0: # If our element then reaches the end of the list, we make it the first entry.
                list[0] = element
        else: # If our test entry is larger, we place our element to its right.
            list[index - i + 1] = element
            return list
    return list

def insertionSort(list):

    # This function does the actual insertion sort. It does the above process to every entry of the list, via a loop.

    for i in range(len(list)):
        list = insert(list, i)
    return list

#
# ~~~~~~~~~~ MERGE SORT ~~~~~~~~~~
#

def merge(left, right):

    # This function takes two sorted lists, a left list and a right list, and merges them into one sorted list. This is the meat and potatoes of
    # how the whole merge sort algorithm works. Going until both lists are empty, we add the smallest entry between them to a result list and
    # remove it from its original place. 

    result = []
    while len(left) != 0 and len(right) != 0: # If neither list is empty, we have to compare the smallest entry of left with that of right.
        if left[0] <= right[0]:
            result.append(left.pop(0))
        else: 
            result.append(right.pop(0))
    # Once one list is empty, we check if both are empty. If one isn't, we know it contains the largest elements, already sorted, so we just add
    # its remaining entries to result sequentially and remove them from the original list.
    while len(right) != 0:
        result.append(right.pop(0))
    while len(left) != 0:
        result.append(left.pop(0))
    return result

def mergeSort(list):

    # This function actually does the merge sort algorithm. Noticing that lists of 0 or 1 elements are already sorted, we can break the list
    # down into these lists and then merge them together using the process above. This will result in one sorted list.

    # Base case: our list has size 0 or 1. In this case it is already done, so we return it.
    if len(list) <= 1:
        return list
    
    # Recursive case: our list has length larger than 1. Then we split our list in half and sort the two other lists recursively.
    left = []
    right = []
    for i in range(len(list)):
        if i < len(list)/2:
            left.append(list[i])
        else:
            right.append(list[i])
    left = mergeSort(left)
    right = mergeSort(right)
    return merge(left, right) # We then merge the two sorted lists and return that.


#
# ~~~~~~~~~~ QUICK SORT ~~~~~~~~~~
#

def quickSort(list):

    # None of these next functions require a second. For quick sort, we choose a random element, called the pivot, and we place all elements
    # smaller than the pivot in one list, and the remaining elements in another. We sort these smaller lists recursively and then combine.
    # This algorithm has a dependency, where it requires choosing a random integer. 

    # Base case: our list has length 0 or 1. Like merge sort, we're done.
    if len(list) <= 1:
        return list
    
    # Recursive case: our list has size larger than 1. Then we separate into two lists, again like merge sort. Unlike merge sort, this isn't
    # done based on the index of the elements, but rather based on their relationship to the chosen pivot (e.g. larger, smaller, equal).
    pivot = random.randint(0, len(list) - 1)
    left = []
    right = []
    for i in range(len(list)):
        if list[i] < list[pivot]: # If an entry is smaller than the pivot, it goes in the left list.
            left.append(list[i])
        else: # Otherwise, it goes in the right list.
            right.append(list[i])
    left = quickSort(left) # We then recursively sort these two lists and combine.
    right = quickSort(right)
    return left + right

#
# ~~~~~~~~~~ HEAP SORT ~~~~~~~~~~
#

def heapSort(list):

    # Heap sort is super simple. A (min-)heap is a data structure which, by default, keeps the smallest entry first. So, we turn our list into
    # a heap, and repeatedly add the first element to the list while removing it from the heap. Because heaps are used, this function has a
    # dependency.

    n = len(list)
    result = []
    heapq.heapify(list) # Turn our list into a heap.
    for i in range(n): # Add the first heap element to our list until we run out.
        result.append(heapq.heappop(list))
    return result

#
# ~~~~~~~~~~ BETTER QUICK SORT ~~~~~~~~~~
#

def betterQuickSort(list, depth):

    # The better quick sort algorithm does exactly what quick sort does, but it has a limit to the depth of recursion. If our recursion exceeds
    # this limit, then we use heapsort on the smaller lists. As a result, this function has three dependencies, one for calculating the depth
    # limit, one for the random integer selection of quick sort, and one for the heaps required for heap sort.

    if len(list) <= 1:
        return list
    elif depth > 1 + 2*math.floor(math.log2(len(list))):
        return heapSort(list)
    else:
        pivot = random.randint(0, len(list) - 1)
        left = []
        right = []
        for i in range(len(list)):
            if list[i] < list[pivot]:
                left.append(list[i])
            else:
                right.append(list[i])
        left = betterQuickSort(left, depth + 1)
        right = betterQuickSort(right, depth + 1)
        return left + right

main()