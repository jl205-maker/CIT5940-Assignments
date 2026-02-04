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
ArrayList is faster in this case. The ticket tracking system needs to support large amount of add and get operations. While add for both ArrayList and LinkedList is O(1), ArrayList supports faster get operation (O(1)) with its direct indexing, 
whereas get for LinkedList is O(n). On the other hand, to remove from head each time to mimic the queue behavior, using LinkedList is must faster (O(1)), whereas using ArrayList is O(n) because every element needs to be shifted one place to the left.

## When measuring actual runtime, is the LinkedList version Suho wrote, or your ArrayList version faster? Does this change when the list size is small versus when it is very large?
LinkedList
* createShortQueue(ticketQueue)：Average run time was 0.1901 milliseconds.
* createLongQueue(ticketQueue): Average run time was 2.60594 milliseconds.

ArrayList
* createShortQueue(ticketQueue)：Average run time was 0.14166 milliseconds.
* createLongQueue(ticketQueue):

## If you ignore queue creation times, does that affect which ticket processor version is faster?


## Write a paragraph or two in the style of a technical report (think about – how would I write this professionally if I needed to explain my findings to my manager?).
Your report should answer the following questions:
* What did you learn from this experience?
* Which implementation do you suggest should be used? Are there certain situations that might call for the other approach?
* How does the theoretical time complexity compare with your findings?







# Part 4
## What are the Big O and Big Ω times for Javier's algorithm? What are the Big O and Big Ω for space use?



## Write a paragraph or two in the style of a technical report (think about – how would I write this professionally if I needed to explain my findings to my manager?). 
Your report should answer the following questions:
* Which of the two algorithms (yours versus Javier's) is more efficient in time and space (in terms of Big O)
    * What about in actual runtime?
* Which implementation do you suggest should be used? Are there certain situations that might call for the other approach?

