# Day 15 — Binary Search: Fundamentals, Bounds & Rotated Arrays

---

## 📑 Table of Contents

1. [Binary Search Fundamentals & Core Architecture](#1-binary-search-fundamentals--core-architecture)
   - [What is Binary Search & Real-Life Analogy](#what-is-binary-search--real-life-analogy)
   - [The Strict Pre-requisite: Monotonicity / Sorted Order](#the-strict-pre-requisite-monotonicity--sorted-order)
   - [⚠️ The Integer Overflow Trap: Why `low + (high - low) / 2` Matters](#-the-integer-overflow-trap-why-low--high---low--2-matters)
   - [Approach 1: Iterative Binary Search (LeetCode 704)](#approach-1-iterative-binary-search-leetcode-704)
   - [Approach 2: Recursive Binary Search](#approach-2-recursive-binary-search)
   - [Complexity & Trade-offs Matrix](#complexity--trade-offs-matrix)
2. [Lower Bound Pattern](#2-lower-bound-pattern)
   - [Definition & Mathematical Semantics](#definition--mathematical-semantics)
   - [Approach 1: Linear Scan ($O(n)$)](#approach-1-linear-scan-on)
   - [Approach 2: Optimal Binary Search ($O(\log n)$)](#approach-2-optimal-binary-search-olog-n)
   - [Step-by-Step Visual Dry Run](#step-by-step-visual-dry-run)
3. [Upper Bound Pattern](#3-upper-bound-pattern)
   - [Definition & Difference from Lower Bound](#definition--difference-from-lower-bound)
   - [Side-by-Side Logic Comparison Matrix](#side-by-side-logic-comparison-matrix)
   - [Approach 1: Linear Scan ($O(n)$)](#approach-1-linear-scan-on-1)
   - [Approach 2: Optimal Binary Search ($O(\log n)$)](#approach-2-optimal-binary-search-olog-n-1)
4. [Search Insert Position (LeetCode 35)](#4-search-insert-position-leetcode-35)
   - [Problem Statement & The "Lower Bound" Realization](#problem-statement--the-lower-bound-realization)
   - [Java Implementation](#java-implementation)
   - [Complexity Analysis](#complexity-analysis)
5. [Floor and Ceil in a Sorted Array](#5-floor-and-ceil-in-a-sorted-array)
   - [Definitions & Intuitive Mapping](#definitions--intuitive-mapping)
   - [Visual Walkthrough](#visual-walkthrough)
   - [Java Implementation](#java-implementation-1)
   - [Complexity Analysis](#complexity-analysis-1)
6. [First and Last Occurrence of Target (LeetCode 34)](#6-first-and-last-occurrence-of-target-leetcode-34)
   - [Problem Statement](#problem-statement)
   - [Approach 1: Lower Bound & Upper Bound Composition with Boundary Check](#approach-1-lower-bound--upper-bound-composition-with-boundary-check)
   - [Approach 2: Pure Binary Search (Separate Left & Right Bias)](#approach-2-pure-binary-search-separate-left--right-bias)
   - [Complexity Analysis](#complexity-analysis-2)
7. [Count Occurrences in a Sorted Array](#7-count-occurrences-in-a-sorted-array)
   - [Formula & Intuition](#formula--intuition)
   - [Java Implementation](#java-implementation-2)
   - [Complexity Analysis](#complexity-analysis-3)
8. [Search in Rotated Sorted Array I — Unique Elements (LeetCode 33)](#8-search-in-rotated-sorted-array-i--unique-elements-leetcode-33)
   - [The "At Least One Half is Sorted" Golden Rule](#the-at-least-one-half-is-sorted-golden-rule)
   - [Decision Tree Flowchart](#decision-tree-flowchart)
   - [Java Implementation](#java-implementation-3)
   - [Step-by-Step Dry Run](#step-by-step-dry-run-1)
   - [Complexity Analysis](#complexity-analysis-4)
9. [Search in Rotated Sorted Array II — With Duplicates (LeetCode 81)](#9-search-in-rotated-sorted-array-ii--with-duplicates-leetcode-81)
   - [The Ambiguity Trap: `nums[low] == nums[mid] == nums[high]`](#the-ambiguity-trap-numslow--numsmid--numshigh)
   - [How to Break the Tie: Boundary Shrinking](#how-to-break-the-tie-boundary-shrinking)
   - [Worst-Case $O(n)$ Degradation Analysis](#worst-case-on-degradation-analysis)
   - [Java Implementation](#java-implementation-4)
   - [Complexity Analysis](#complexity-analysis-5)
10. [📊 Master Comparison & Interview Decision Matrix](#10--master-comparison--interview-decision-matrix)
11. [⚠️ Common Traps & Edge Case Checklist](#11-️-common-traps--edge-case-checklist)

---

## 1. Binary Search Fundamentals & Core Architecture

### What is Binary Search & Real-Life Analogy
Binary Search is an optimal search algorithm designed for **sorted data structures**. It repeatedly divides the search space in half until the desired value is located or the space is exhausted.

> 📖 **The Dictionary Analogy:**
> Think of opening a physical English dictionary to find the word **"Raj"**. You don't read every page from letter **"A"** sequentially ($O(n)$). Instead, you open the book roughly in the middle:
> - If you land on letter **"M"**, you know "R" comes after "M". You instantly discard the entire left half of the book!
> - Next, you open the middle of the remaining right half. If you land on **"T"**, you know "R" comes before "T", so you discard the right half.
> - Within a few flips, you land precisely on "R". This logarithmic reduction is Binary Search!

```
Search Space: [low ................... mid ................... high]
                                        ▲
                                        │
           If target < nums[mid] ───────┴─────── If target > nums[mid]
           Search LEFT (high = mid - 1)          Search RIGHT (low = mid + 1)
```

---

### The Strict Pre-requisite: Monotonicity / Sorted Order
Binary Search **cannot** work on unsorted arrays because eliminating half the elements requires the absolute guarantee that all elements to the left of `mid` are $\le \text{nums}[mid]$ and all elements to the right are $\ge \text{nums}[mid]$.

---

### ⚠️ The Integer Overflow Trap: Why `low + (high - low) / 2` Matters

In elementary programming, the midpoint is often calculated as:
$$\text{mid} = \frac{\text{low} + \text{high}}{2}$$

> [!CAUTION]
> In Java, C++, and C, `int` is a 32-bit signed integer with maximum value $2^{31} - 1 = 2,147,483,647$.
> If an array has a very large search space (e.g., searching within the answer space up to $2 \times 10^9$):
> $$\text{low} = 1.5 \times 10^9, \quad \text{high} = 2.0 \times 10^9$$
> $$\text{low} + \text{high} = 3.5 \times 10^9 > 2.147 \times 10^9 \implies \text{\textbf{32-bit Integer Overflow!}}$$
> The sum wraps around to a **negative number**, causing an immediate `ArrayIndexOutOfBoundsException`.

**The Mathematically Identical, Overflow-Safe Formula:**
$$\text{mid} = \text{low} + \frac{\text{high} - \text{low}}{2}$$

Since `high >= low`, `high - low` is always $\ge 0$ and never exceeds `high`, preventing any overflow.

---

### Approach 1: Iterative Binary Search (LeetCode 704)

#### Detailed Step-by-Step Walkthrough
Given `nums = [3, 4, 6, 7, 8, 12, 16, 17]` ($n = 8$), searching for `target = 13`:

| Step | `low` | `high` | `mid` | `nums[mid]` | Comparison with `13` | Action Taken |
| :---: | :---: | :---: | :---: | :---: | :---: | :--- |
| **1** | `0` | `7` | `3` | `7` | $7 < 13$ | Target in right half $\implies$ `low = mid + 1 = 4` |
| **2** | `4` | `7` | `5` | `12` | $12 < 13$ | Target in right half $\implies$ `low = mid + 1 = 6` |
| **3** | `6` | `7` | `6` | `16` | $16 > 13$ | Target in left half $\implies$ `high = mid - 1 = 5` |
| **4** | `6` | `5` | — | — | `low > high` | Loop terminates! Element not found $\implies$ return `-1` |

#### Java Implementation
```java
class Solution {
    public int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            // Overflow-safe midpoint
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                return mid; // Target found
            } else if (nums[mid] < target) {
                low = mid + 1; // Discard left half
            } else {
                high = mid - 1; // Discard right half
            }
        }

        return -1; // Target not found
    }
}
```

---

### Approach 2: Recursive Binary Search

#### Intuition
Divide and conquer expressed as a recurrence relation:
$$T(n) = T(n/2) + O(1) \implies O(\log n)$$

#### Java Implementation
```java
class Solution {
    public int search(int[] nums, int target) {
        return binarySearch(nums, target, 0, nums.length - 1);
    }

    private int binarySearch(int[] nums, int target, int low, int high) {
        // Base case: search space exhausted
        if (low > high) {
            return -1;
        }

        int mid = low + (high - low) / 2;

        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            return binarySearch(nums, target, mid + 1, high);
        } else {
            return binarySearch(nums, target, low, mid - 1);
        }
    }
}
```

---

### Complexity & Trade-offs Matrix

| Approach | Time Complexity | Auxiliary Space | Call Stack Space | Production Suitability |
| :--- | :---: | :---: | :---: | :--- |
| **Iterative** | $O(\log n)$ | $O(1)$ | $O(1)$ | **Preferred** (Zero memory overhead, immune to stack overflow) |
| **Recursive** | $O(\log n)$ | $O(1)$ | $O(\log n)$ | Educational (Creates recursion frames on the call stack) |

---

## 2. Lower Bound Pattern

### Definition & Mathematical Semantics
> **Lower Bound:** Given a sorted array `arr` and a value `x`, the lower bound is the **smallest index `ind`** such that:
> $$\text{arr}[ind] \ge x$$
> If no element in the array is $\ge x$, the lower bound is defined as the size of the array, $n$.

```
arr = [3, 5, 8, 15, 19], n = 5
       0  1  2   3   4

x = 8   ──> lb = 2   (arr[2] = 8 >= 8)
x = 9   ──> lb = 3   (arr[3] = 15 >= 9)
x = 16  ──> lb = 4   (arr[4] = 19 >= 16)
x = 20  ──> lb = 5   (hypothetical index n = 5, no element >= 20)
```

---

### Approach 1: Linear Scan ($O(n)$)
Check each element sequentially from left to right:
```java
class Solution {
    public int lowerBound(int[] arr, int x) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            if (arr[i] >= x) {
                return i;
            }
        }
        return n;
    }
}
```
- **Time Complexity:** $O(n)$
- **Space Complexity:** $O(1)$

---

### Approach 2: Optimal Binary Search ($O(\log n)$)

#### Intuition & Algorithm
- Maintain `ans = n` as the fallback if no element satisfies the condition.
- At any `mid`:
  - If `arr[mid] >= x`: This is a valid candidate! Save `ans = mid`. But can we find a smaller index to the left? Yes, so look left $\implies$ `high = mid - 1`.
  - If `arr[mid] < x`: `arr[mid]` is strictly smaller than `x`, so it cannot be a candidate, nor can anything to its left. Look right $\implies$ `low = mid + 1`.

#### Java Implementation
```java
class Solution {
    public int lowerBound(int[] arr, int x) {
        int n = arr.length;
        int low = 0;
        int high = n - 1;
        int ans = n; // Default to n if no element >= x

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] >= x) {
                ans = mid;       // Candidate answer found
                high = mid - 1;  // Look for smaller index on the left
            } else {
                low = mid + 1;   // Must search on the right
            }
        }

        return ans;
    }
}
```

> [!NOTE]
> - **C++ STL Equivalent:** `lower_bound(arr.begin(), arr.end(), x) - arr.begin()`
> - **Java Equivalent:** `java.util.Arrays.binarySearch(arr, x)`. If not found, it returns `-(insertion_point + 1)`.

---

### Step-by-Step Visual Dry Run

**Case 1:** `arr = [1, 2, 3, 3, 7, 8, 9, 9, 9, 11]`, $n = 10$, $x = 1$

| Pass | `low` | `high` | `mid` | `arr[mid]` | `arr[mid] >= 1` | `ans` Updated | Next Range |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :--- |
| **1** | `0` | `9` | `4` | `7` | $7 \ge 1$ (True) | `ans = 4` | `high = mid - 1 = 3` |
| **2** | `0` | `3` | `1` | `2` | $2 \ge 1$ (True) | `ans = 1` | `high = mid - 1 = 0` |
| **3** | `0` | `0` | `0` | `1` | $1 \ge 1$ (True) | `ans = 0` | `high = mid - 1 = -1` |
| **End** | `0` | `-1` | — | — | `low > high` | **Final `ans = 0`** | Loop terminated |

**Case 2:** Same array, $x = 9$
- Pass 1: `mid = 4`, `arr[4] = 7 < 9` $\implies$ `low = 5`
- Pass 2: `mid = 7`, `arr[7] = 9 >= 9` $\implies$ `ans = 7`, `high = 6`
- Pass 3: `mid = 5`, `arr[5] = 8 < 9` $\implies$ `low = 6`
- Pass 4: `mid = 6`, `arr[6] = 9 >= 9` $\implies$ `ans = 6`, `high = 5`
- Loop terminates with `ans = 6` (the very first occurrence of 9!).

---

## 3. Upper Bound Pattern

### Definition & Difference from Lower Bound
> **Upper Bound:** Given a sorted array `arr` and a value `x`, the upper bound is the **smallest index `ind`** such that:
> $$\text{arr}[ind] > x \quad (\text{strictly greater})$$
> If no element in the array is $> x$, the upper bound is defined as $n$.

```
arr = [2, 3, 6, 7, 8, 8, 11, 11, 11, 12], n = 10
       0  1  2  3  4  5   6   7   8   9

x = 8   ──> ub = 6   (arr[6] = 11 > 8; first element strictly > 8)
x = 12  ──> ub = 10  (no element > 12; returns n = 10)
x = 1   ──> ub = 0   (arr[0] = 2 > 1)
```

---

### Side-by-Side Logic Comparison Matrix

| Property | Lower Bound | Upper Bound |
| :--- | :--- | :--- |
| **Condition** | `arr[mid] >= x` | `arr[mid] > x` |
| **Meaning** | First element **at least** `x` | First element **strictly greater than** `x` |
| **On Match (`arr[mid] == x`)** | Considered candidate $\implies$ saves `ans` and goes **left** | Not a candidate $\implies$ goes **right** |
| **Duplicate Handling** | Lands on the **first** occurrence of `x` | Lands on the index **immediately after the last** occurrence of `x` |

```
Lower Bound Logic:                    Upper Bound Logic:
if (arr[mid] >= x) {                  if (arr[mid] > x) {
    ans = mid;                            ans = mid;
    high = mid - 1;                       high = mid - 1;
} else {                              } else {
    low = mid + 1;                        low = mid + 1;
}                                     }
```

---

### Approach 1: Linear Scan ($O(n)$)
```java
class Solution {
    public int upperBound(int[] arr, int x) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            if (arr[i] > x) {
                return i;
            }
        }
        return n;
    }
}
```

---

### Approach 2: Optimal Binary Search ($O(\log n)$)

```java
class Solution {
    public int upperBound(int[] arr, int x) {
        int n = arr.length;
        int low = 0;
        int high = n - 1;
        int ans = n;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] > x) {
                ans = mid;       // Candidate found
                high = mid - 1;  // Look for smaller index on the left
            } else {
                low = mid + 1;   // Must search on the right
            }
        }

        return ans;
    }
}
```

- **Time Complexity:** $O(\log n)$
- **Space Complexity:** $O(1)$
- **C++ STL Equivalent:** `upper_bound(arr.begin(), arr.end(), x) - arr.begin()`

---

## 4. Search Insert Position (LeetCode 35)

### Problem Statement & The "Lower Bound" Realization
Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order.

```
Input: nums = [1, 3, 5, 6], target = 5  ──> Output: 2
Input: nums = [1, 3, 5, 6], target = 2  ──> Output: 1  (between 1 and 3)
Input: nums = [1, 3, 5, 6], target = 7  ──> Output: 4  (appended at end)
Input: nums = [1, 3, 5, 6], target = 0  ──> Output: 0  (inserted at beginning)
```

> [!IMPORTANT]
> **Key Realization:**
> Notice where an element is inserted in a sorted array:
> - If `target` is present, it takes its existing index.
> - If `target` is not present, it takes the place of the **first element that is strictly greater than it**.
> In both cases, this is **precisely the Lower Bound**: the smallest index `i` where `nums[i] >= target`!

---

### Java Implementation
```java
class Solution {
    public int searchInsert(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        int ans = nums.length;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] >= target) {
                ans = mid;
                high = mid - 1; // Look for earlier insertion index
            } else {
                low = mid + 1;
            }
        }

        return ans;
    }
}
```

### Complexity Analysis
- **Time Complexity:** $O(\log n)$
- **Space Complexity:** $O(1)$

---

## 5. Floor and Ceil in a Sorted Array

### Definitions & Intuitive Mapping
Given a sorted array `arr` and a query value `x`:
- **Floor:** The largest element in the array that is $\le x$. (Return `-1` if no such element exists).
- **Ceil:** The smallest element in the array that is $\ge x$. (Return `-1` if no such element exists).

```
arr = [10, 20, 30, 40, 50], x = 25
- Floor: 20  (largest value <= 25)
- Ceil:  30  (smallest value >= 25)

x = 5  ──> Floor = -1, Ceil = 10
x = 55 ──> Floor = 50, Ceil = -1
```

### Visual Walkthrough
- **Ceil** is directly calculated using **Lower Bound**:
  - `if (arr[mid] >= x)`: `ans = arr[mid]`, `high = mid - 1` (search left for smaller ceil)
- **Floor** reverses the search direction:
  - `if (arr[mid] <= x)`: `ans = arr[mid]`, `low = mid + 1` (search right for larger floor)

---

### Java Implementation
```java
class Solution {
    public int[] getFloorAndCeil(int[] arr, int x) {
        int floor = findFloor(arr, x);
        int ceil = findCeil(arr, x);
        return new int[]{floor, ceil};
    }

    // Largest element <= x
    private int findFloor(int[] arr, int x) {
        int low = 0;
        int high = arr.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] <= x) {
                ans = arr[mid];  // Valid candidate floor
                low = mid + 1;   // Search right for a larger floor <= x
            } else {
                high = mid - 1;  // Too large, search left
            }
        }

        return ans;
    }

    // Smallest element >= x (Ceil = Lower Bound value)
    private int findCeil(int[] arr, int x) {
        int low = 0;
        int high = arr.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] >= x) {
                ans = arr[mid];  // Valid candidate ceil
                high = mid - 1;  // Search left for a smaller ceil >= x
            } else {
                low = mid + 1;   // Too small, search right
            }
        }

        return ans;
    }
}
```

### Complexity Analysis
- **Time Complexity:** $2 \times O(\log n) = O(\log n)$
- **Space Complexity:** $O(1)$

---

## 6. First and Last Occurrence of Target (LeetCode 34)

### Problem Statement
Given a sorted array of integers `nums` and an integer `target`, find the starting and ending position of the given `target` value. If `target` is not found, return `[-1, -1]`.

```
Input: nums = [5, 7, 7, 8, 8, 10], target = 8  ──> Output: [3, 4]
Input: nums = [5, 7, 7, 8, 8, 10], target = 6  ──> Output: [-1, -1]
Input: nums = [],                 target = 0  ──> Output: [-1, -1]
```

---

### Approach 1: Lower Bound & Upper Bound Composition with Boundary Check

#### The Connection:
- `lowerBound(nums, target)` lands on the **first index** where element $\ge \text{target}$.
- `upperBound(nums, target)` lands on the **first index** where element $> \text{target}$.
- Therefore:
  $$\text{First} = \text{lowerBound}(target)$$
  $$\text{Last} = \text{upperBound}(target) - 1$$

#### ⚠️ The Critical Trap & Validation Condition:
What if `target` is not in the array?
1. If `target` is greater than all elements: `lb == n`.
2. If `target` does not exist, but some larger element exists: `lb < n`, but `nums[lb] != target`!

```
nums = [2, 4, 6, 7, 7, 11, 13], target = 10
lowerBound(10) returns index 5 (nums[5] = 11).
If we don't validate, we will wrongly claim 10 exists at index 5!
```

> [!IMPORTANT]
> **Strict Validation Check:**
> ```java
> if (lb == n || nums[lb] != target) {
>     return new int[]{-1, -1};
> }
> ```

#### Java Implementation
```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        int n = nums.length;
        int lb = lowerBound(nums, target);

        // Validation check
        if (lb == n || nums[lb] != target) {
            return new int[]{-1, -1};
        }

        int ub = upperBound(nums, target);
        return new int[]{lb, ub - 1};
    }

    private int lowerBound(int[] arr, int x) {
        int low = 0, high = arr.length - 1, ans = arr.length;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] >= x) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }

    private int upperBound(int[] arr, int x) {
        int low = 0, high = arr.length - 1, ans = arr.length;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] > x) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
}
```

---

### Approach 2: Pure Binary Search (Separate Left & Right Bias)

Instead of relying on bounds, we can directly modify Binary Search:
- For **First Occurrence**: When `nums[mid] == target`, record `ans = mid` and **keep searching left** (`high = mid - 1`).
- For **Last Occurrence**: When `nums[mid] == target`, record `ans = mid` and **keep searching right** (`low = mid + 1`).

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        int first = findFirst(nums, target);
        if (first == -1) {
            return new int[]{-1, -1};
        }
        int last = findLast(nums, target);
        return new int[]{first, last};
    }

    private int findFirst(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                ans = mid;
                high = mid - 1; // Bias search towards the left
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    private int findLast(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                ans = mid;
                low = mid + 1;  // Bias search towards the right
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }
}
```

### Complexity Analysis
- **Time Complexity:** $2 \times O(\log n) = O(\log n)$
- **Space Complexity:** $O(1)$

---

## 7. Count Occurrences in a Sorted Array

### Formula & Intuition
Given a sorted array `arr` and a target `x`, count how many times `x` appears.

$$\text{Count} = \begin{cases} 0, & \text{if } \text{first} == -1 \\ \text{last} - \text{first} + 1, & \text{otherwise} \end{cases}$$

Alternatively, using bounds:
$$\text{Count} = \text{upperBound}(x) - \text{lowerBound}(x)$$

```
arr = [1, 1, 2, 2, 2, 2, 3], x = 2
First Occurrence = 2
Last Occurrence  = 5
Count = 5 - 2 + 1 = 4
```

---

### Java Implementation
```java
class Solution {
    public int count(int[] arr, int n, int x) {
        int first = findFirst(arr, x);
        if (first == -1) return 0;
        int last = findLast(arr, x);
        return last - first + 1;
    }

    private int findFirst(int[] arr, int x) {
        int low = 0, high = arr.length - 1;
        int ans = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == x) {
                ans = mid;
                high = mid - 1;
            } else if (arr[mid] < x) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    private int findLast(int[] arr, int x) {
        int low = 0, high = arr.length - 1;
        int ans = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == x) {
                ans = mid;
                low = mid + 1;
            } else if (arr[mid] < x) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }
}
```

### Complexity Analysis
- **Time Complexity:** $2 \times O(\log n) = O(\log n)$
- **Space Complexity:** $O(1)$

---

## 8. Search in Rotated Sorted Array I — Unique Elements (LeetCode 33)

### The "At Least One Half is Sorted" Golden Rule

An array originally sorted in ascending order is rotated at an unknown pivot. For example:
`[0, 1, 2, 4, 5, 6, 7]` rotated at pivot index 4 becomes:
`[4, 5, 6, 7, 0, 1, 2]`

```
Visual Profile of Rotated Sorted Array:
Value
  8 │         /
  7 │       /
  6 │     /
  5 │   /               /
  4 │ /               /
  1 │               /
  0 │             /
    └───────────────
      [4, 5, 6, 7]   [0, 1, 2]
       Left Slope     Right Slope
```

> [!IMPORTANT]
> **The Golden Observation:**
> No matter where the pivot index is, when you pick `mid`, **at least one of the two halves (`[low..mid]` or `[mid..high]`) is GUARANTEED to be cleanly sorted!**

---

### Decision Tree Flowchart

```
Is nums[low] <= nums[mid]?
      │
      ├── YES ──> [LEFT HALF IS SORTED]
      │           Is target inside [nums[low] ... nums[mid])?
      │           ├── YES ──> Search Left:  high = mid - 1
      │           └── NO  ──> Search Right: low = mid + 1
      │
      └── NO  ──> [RIGHT HALF IS SORTED]
                  Is target inside (nums[mid] ... nums[high]]?
                  ├── YES ──> Search Right: low = mid + 1
                  └── NO  ──> Search Left:  high = mid - 1
```

---

### Java Implementation
```java
class Solution {
    public int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                return mid; // Target found
            }

            // Step 1: Check if left half is sorted
            if (nums[low] <= nums[mid]) {
                // Check if target lies within sorted left half
                if (nums[low] <= target && target < nums[mid]) {
                    high = mid - 1; // Search left
                } else {
                    low = mid + 1;  // Search right
                }
            }
            // Step 2: Otherwise, right half MUST be sorted
            else {
                // Check if target lies within sorted right half
                if (nums[mid] < target && target <= nums[high]) {
                    low = mid + 1;  // Search right
                } else {
                    high = mid - 1; // Search left
                }
            }
        }

        return -1; // Target not found
    }
}
```

---

### Step-by-Step Dry Run

`nums = [4, 5, 6, 7, 0, 1, 2]`, `target = 0`

| Pass | `low` | `high` | `mid` | `nums[mid]` | Sorted Half Check | Range Check for `target = 0` | Action Taken |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :--- |
| **1** | `0 (4)` | `6 (2)` | `3` | `7` | `nums[0] <= nums[3]` ($4 \le 7$) $\implies$ Left sorted | $4 \le 0 < 7$ is **False** | Target is on right $\implies$ `low = 4` |
| **2** | `4 (0)` | `6 (2)` | `5` | `1` | `nums[4] <= nums[5]` ($0 \le 1$) $\implies$ Left sorted | $0 \le 0 < 1$ is **True** | Target is on left $\implies$ `high = 4` |
| **3** | `4 (0)` | `4 (0)` | `4` | `0` | `nums[4] == 0` | Target matched! | Return index `4` ✅ |

### Complexity Analysis
- **Time Complexity:** $O(\log n)$
- **Space Complexity:** $O(1)$

---

## 9. Search in Rotated Sorted Array II — With Duplicates (LeetCode 81)

### The Ambiguity Trap: `nums[low] == nums[mid] == nums[high]`

When duplicates are introduced, the fundamental check `nums[low] <= nums[mid]` can mislead us into assuming a half is sorted when it is not!

> 🚨 **The Classic Ambiguity Example:**
> Compare these two arrays for `target = 2`:
> 1. `nums = [3, 1, 2, 3, 3, 3, 3]` $\implies$ `low=0 (3)`, `mid=3 (3)`, `high=6 (3)`
> 2. `nums = [3, 3, 3, 3, 1, 2, 3]` $\implies$ `low=0 (3)`, `mid=3 (3)`, `high=6 (3)`
>
> In both cases:
> $$\text{nums}[low] == \text{nums}[mid] == \text{nums}[high] == 3$$
> In Array 1, the target `2` is in the **left half**.
> In Array 2, the target `2` is in the **right half**.
>
> Because all three pointers see `3`, **it is mathematically impossible to know which half is sorted!**

---

### How to Break the Tie: Boundary Shrinking
Since `nums[mid] == 3 != target`, we already know that `mid` is not the answer.
Furthermore, neither `nums[low]` nor `nums[high]` can be our answer (since they are also equal to `nums[mid]`).
Therefore, we can safely shrink the search boundaries by one step from each side without losing any valid target:
```java
low++;
high--;
```

---

### Worst-Case $O(n)$ Degradation Analysis

> [!WARNING]
> If all elements in the array are identical and `target` is different:
> `nums = [3, 3, 3, 3, 3, 3, 3]`, `target = 2`
> In every iteration, the boundary-shrinking branch triggers, shrinking `low` and `high` by 1.
> The loop executes $n/2$ times before terminating.
> - **Average Time Complexity:** $O(\log n)$
> - **Worst-Case Time Complexity:** $O(n)$

---

### Java Implementation
```java
class Solution {
    public boolean search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                return true;
            }

            // Ambiguity Resolution: Shrink boundaries when edges match mid
            if (nums[low] == nums[mid] && nums[mid] == nums[high]) {
                low++;
                high--;
                continue;
            }

            // Left half is sorted
            if (nums[low] <= nums[mid]) {
                if (nums[low] <= target && target < nums[mid]) {
                    high = mid - 1; // Search left
                } else {
                    low = mid + 1;  // Search right
                }
            }
            // Right half is sorted
            else {
                if (nums[mid] < target && target <= nums[high]) {
                    low = mid + 1;  // Search right
                } else {
                    high = mid - 1; // Search left
                }
            }
        }

        return false;
    }
}
```

### Complexity Analysis
- **Time Complexity:** Average $O(\log n)$, Worst Case $O(n)$
- **Space Complexity:** $O(1)$

---

## 10. 📊 Master Comparison & Interview Decision Matrix

| Problem | Array Type | Condition at `mid` | Target Range Update | Time Complexity | Space Complexity |
| :--- | :--- | :--- | :--- | :---: | :---: |
| **Standard BS** | Sorted, distinct | `nums[mid] == target` | `nums[mid] < target ? low = mid + 1 : high = mid - 1` | $O(\log n)$ | $O(1)$ |
| **Lower Bound** | Sorted | `arr[mid] >= x` | Save `ans = mid`, `high = mid - 1` | $O(\log n)$ | $O(1)$ |
| **Upper Bound** | Sorted | `arr[mid] > x` | Save `ans = mid`, `high = mid - 1` | $O(\log n)$ | $O(1)$ |
| **Search Insert** | Sorted, distinct | `nums[mid] >= target` | Exact same logic as Lower Bound | $O(\log n)$ | $O(1)$ |
| **Floor & Ceil** | Sorted | Floor: `arr[mid] <= x`<br>Ceil: `arr[mid] >= x` | Floor: save, `low = mid + 1`<br>Ceil: save, `high = mid - 1` | $O(\log n)$ | $O(1)$ |
| **First & Last** | Sorted with duplicates | First: match $\to$ go left<br>Last: match $\to$ go right | Standard binary search with directional bias | $O(\log n)$ | $O(1)$ |
| **Count Occurrences** | Sorted with duplicates | — | $\text{Last} - \text{First} + 1$ | $O(\log n)$ | $O(1)$ |
| **Rotated Search I** | Rotated, distinct | `nums[low] <= nums[mid]` | Identify sorted half, check boundaries | $O(\log n)$ | $O(1)$ |
| **Rotated Search II** | Rotated with duplicates | `nums[low] == nums[mid] == nums[high]` | Ambiguity: `low++, high--`<br>Else: same as Rotated I | Avg: $O(\log n)$<br>Worst: $O(n)$ | $O(1)$ |

---

## 11. ⚠️ Common Traps & Edge Case Checklist

1. **Integer Overflow:** Always write `mid = low + (high - low) / 2`. Never write `(low + high) / 2`.
2. **Loop Condition (`<=` vs `<`):**
   - Use `while (low <= high)` when searching for a value and updating `low = mid + 1`, `high = mid - 1`. The `<=` guarantees that a search space of size 1 (`low == high`) is checked before terminating.
3. **Missing Validation on Bounds:**
   - Lower Bound returns index `n` if all elements are smaller than `x`. Always verify `lb < n && nums[lb] == x` before reading `nums[lb]` when checking for existence.
4. **Duplicate Ambiguity in Rotated Arrays:**
   - Always place the `if (nums[low] == nums[mid] && nums[mid] == nums[high]) { low++; high--; continue; }` check **immediately after** checking `if (nums[mid] == target)` and **before** checking `nums[low] <= nums[mid]`.
5. **Empty or Single-Element Arrays:**
   - Handle $n = 0$ (instantly returns `-1` or `false` or `[-1, -1]`).
   - For $n = 1$, `low = 0, high = 0`, `mid = 0`: correctly checks the single element on iteration 1.
