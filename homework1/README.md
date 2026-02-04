## Please enter your personal info here:
Name: Jiani Liu

PennKey (e.g., taliem): liu32


# Part 1:
## Are Alicia and Lloyd both wrong, or perhaps both right? Is only one of them correct? Why?
It depends on what type of list is used.

If the passed in lst is ArrayList: Lloyd is correct.

Snippet A: overall O(1)

    size(): O(1)
    get(i): O(1) because direct indexing

Snippet B: overall O(N)

    size(): O(1)
    get(0): O(1)
    remove(0): O(N) because all following elements must shift left
    get(size()-1): O(1)
    remove(size()-1): 
    
If the passed in lst is LinkedList: Alicia is correct.

Snippet A: overall O(N)

    size(): O(1)
    get(i): O(N) must walk pointer to the i-th node

Snippet B: overall O(1)

    size(): O(1)
    get(0): O(1)
    remove(0): O(1) because LinkedList maintains a pointer to the last element
    get(size()-1): O(1): O(1)
    remove(size()-1):  O(1)

# Part 2:
## What are the Big O and Big Ω times for snippets C and D?
Suppose an m * n grid.

Snippet C (exist when target is found):

    O(m*n): target does not exist or appears at the very last cell (m-1, n-1);
    Ω(1): target is at cell [0, 0].

Snippet D (no early exist):

    O(m*n): loop through all cells even after target is found;
    Ω(m*n): loop through all cells even after target is found.

## When measuring actual runtime, does one of the snippets run faster than the other? In what situations? Why do you think this is the case?
getGridOne(): target at cell [0, 0]

    findFirstInstanceOne(): Run time was 0.0024 milliseconds.
    findFirstInstanceTwo(): Run time was 9.4926 milliseconds.

getGridTwo(): target at cell [3999, 3999]

    findFirstInstanceOne(): Run time was 10.5992 milliseconds.
    findFirstInstanceTwo(): Run time was 8.9226 milliseconds.

## What else do you notice about the reported runtime? Is it 100% consistent every time you run it?
Snippet D runs faster on grid two than on grid one even if it scans through the entire grid for both iterations. When Snippet C and D both scan through the entire grid to eventually find the target, Snippet D runs slightly faster. This indicates that even with the same input size, the runtime is not 100% consistent each time.

# Part 3:
## Before you make any changes, explain whether you think a LinkedList or an ArrayList makes more sense in this instance. Which do you think will be faster? Why?
LinkedList is faster in this case. For a “queue” where you repeatedly remove the front element, LinkedList.remove(0) is O(1), 
while ArrayList.remove(0) is O(N) because all later elements shift left.
## When measuring actual runtime, is the LinkedList version Suho wrote, or your ArrayList version faster? Does this change when the list size is small versus when it is very large?
LinkedList

    createShortQueue(ticketQueue)：Average run time was 0.1901 milliseconds.
    createLongQueue(ticketQueue): Average run time was 2.60594 milliseconds.

ArrayList

    createShortQueue(ticketQueue)：Average run time was 0.14166 milliseconds.
    createLongQueue(ticketQueue):Average run time was 12.98852 milliseconds.

The measured runtime shows ArrayList can be slightly faster for small queues due to lower overhead and better cache locality, even though remove(0) is O(N). However, as the queue size grows, 
the repeated shifting makes the ArrayList approach much slower overall (total O(N²)), while LinkedList stays O(N) because each remove(0) is O(1). Therefore the performance gap becomes visible only at large input sizes.

## If you ignore queue creation times, does that affect which ticket processor version is faster?
Ignoring queue creation time does not change the Big-O conclusion that LinkedList is faster. Creating a queue is O(N) for both ArrayList and LinkedList (adding n elements to the queue). Therefore, the main difference is between
the processing time. Remove(0) is O(1) for LinkedList, so it is O(N) for LinkedList to process N tickets. Remove(0) is O(N) for ArrayList due to each element shifting right, so it is O(N^2) to process N tickets. Therefore, LinkedList
is still faster, especially for larger queues.

ArrayList LIFO (stack) Implementation (O(1) removal) for comparison

    createShortQueue(ticketQueue)：Average run time was 0.16295 milliseconds.
    createLongQueue(ticketQueue):Average run time was 2.24414 milliseconds.

## Write a paragraph or two in the style of a technical report (think about – how would I write this professionally if I needed to explain my findings to my manager?).
Your report should answer the following questions:
* What did you learn from this experience?


* Which implementation do you suggest should be used? Are there certain situations that might call for the other approach?
* How does the theoretical time complexity compare with your findings?

Selecting the appropriate data structure depends on how it is implemented and how the system is used in the specific context. While ArrayList can be efficient for small
workloads because it has less overhead, repeated removal from it incurs O(N) per operation, leading to poor scalability. In contrast, LinkedList has larger overhead (when creating pointers and nodes), but
it supports O(1) removal from the head and is better for large, queue-like tasks where tickets are processed in FIFO order. If we do not process tickets in that order, ArrayList can achieve similar runtime 
performance where ticket at the end is processed first (LIFO, stack-like structure). Theoretical time complexity aligns with the findings when input size is large, but it is less obvious when input size is small.


# Part 4
## What are the Big O and Big Ω times for Javier's algorithm? What are the Big O and Big Ω for space use?

Time Complexity

    N operations: populating the queue
    N - 1 operations: repeatedly merging the first two arrays
      1: get first
      1: get first
      a + b -> log(N): each merge
      1: add combined to queue
    1: return queue.remove(0)

Overall Time Complexity = N + (N - 1)(1 + 1 + log(N) + 1) + 1 = O(N*log(N)) = Ω(N*log(N)) therefore Θ(N*log(N))

    
Space Complexity: O(N) and Ω(N) therefore Θ(N) because it uses a LinkedList queue to store the sorted array.

## Write a paragraph or two in the style of a technical report (think about – how would I write this professionally if I needed to explain my findings to my manager?). 
Your report should answer the following questions:
* Which of the two algorithms (yours versus Javier's) is more efficient in time and space (in terms of Big O)
    * What about in actual runtime?
* Which implementation do you suggest should be used? Are there certain situations that might call for the other approach?

JavierMergeSort

    Average run time was 0.26755 milliseconds.

MergeSort

    Average run time was 0.10918 milliseconds.

Both algorithms are O(N*LogN) in time and O(N) in space. MergeSort has shorter actual runtime then JavierMergeSort. While
theoretically both algorithms have the same performance, the experiment showed that recursive approach offers better real-world
performance. 