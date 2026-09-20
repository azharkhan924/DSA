# 🎯 Array Patterns — Recognition & Strategy Guide

A cheatsheet to help you **instantly recognize** which algorithm to apply based on problem keywords, constraints, and structure. Master these patterns and you'll never be stuck on an array problem again.

---

## 📑 Quick Pattern Lookup

| # | Pattern | Trigger Keywords | Go-To Algorithm |
|:---:|:---|:---|:---|
| 1 | [Find Pair/Triplet with Target Sum](#1-find-pairtriplet-with-target-sum) | "two numbers", "sum equals", "pair" | Two Pointers / HashMap |
| 2 | [Count/Find Subarrays with Property](#2-countfind-subarrays-with-property) | "subarray sum", "subarray XOR", "contiguous" | Prefix Sum/XOR + HashMap |
| 3 | [Maximum/Minimum Subarray](#3-maximumminimum-subarray) | "maximum subarray", "largest sum", "largest product" | Kadane's / Prefix-Suffix |
| 4 | [Find Majority/Most Frequent](#4-find-majoritymost-frequent) | "majority", "more than n/2", "more than n/3" | Boyer-Moore Voting |
| 5 | [Missing/Duplicate in 1-to-N](#5-missingduplicate-in-1-to-n) | "missing number", "repeating", "1 to n" | Math / XOR / Index Marking |
| 6 | [Count Ordered Pairs (i,j)](#6-count-ordered-pairs-ij) | "count pairs", "inversions", "i < j and nums[i] > f(nums[j])" | Modified Merge Sort |
| 7 | [In-Place Rearrangement](#7-in-place-rearrangement) | "in-place", "without extra space", "rearrange", "sort 0s 1s 2s" | Two Pointers / DNF |
| 8 | [Merge Sorted Data](#8-merge-sorted-data) | "merge sorted", "two sorted arrays", "without extra space" | Two Pointers / Gap Method |
| 9 | [Intervals & Ranges](#9-intervals--ranges) | "overlapping", "merge intervals", "non-overlapping" | Sort + Linear Sweep |
| 10 | [Matrix Operations](#10-matrix-operations) | "rotate matrix", "spiral", "set zeroes", "2D array" | Transpose / Boundary Pointers |
| 11 | [Sliding Window](#11-sliding-window) | "consecutive", "window of size k", "longest subarray (positives)" | Expand/Shrink Window |
| 12 | [Permutation/Ordering](#12-permutationordering) | "next permutation", "lexicographically", "next greater arrangement" | Breakpoint + Swap + Reverse |

---

## 1. Find Pair/Triplet with Target Sum

### 🔍 How to Identify
- Problem says: "find two/three/four numbers that sum to target"
- Asks for indices or values

### 🧠 Decision Tree
```
Want indices (not sorted)?
  └─ YES → HashMap (store value → index, lookup complement)
  └─ NO / Array is sorted → Sort + Two Pointers

How many numbers?
  └─ 2 → Direct Two Pointers or HashMap
  └─ 3 → Fix 1 outer loop + Two Pointers  → O(n²)
  └─ 4 → Fix 2 outer loops + Two Pointers → O(n³)
```

### ⚡ Quick Template
```java
// Two Sum (unsorted) → HashMap
Map<Integer, Integer> map = new HashMap<>();
for (int i = 0; i < n; i++) {
    if (map.containsKey(target - nums[i])) return new int[]{map.get(target - nums[i]), i};
    map.put(nums[i], i);
}

// 3 Sum (sorted) → Fix i + Two Pointers
Arrays.sort(nums);
for (int i = 0; i < n - 2; i++) {
    int left = i + 1, right = n - 1;
    while (left < right) { /* two pointer logic */ }
}
```

### 📋 Problems
| Problem | Approach |
|---------|----------|
| Two Sum | HashMap complement |
| 3 Sum | Sort + Fix 1 + Two Pointers |
| 4 Sum | Sort + Fix 2 + Two Pointers |

---

## 2. Count/Find Subarrays with Property

### 🔍 How to Identify
- "count subarrays with sum/XOR equal to K"
- "longest subarray with sum K"
- "subarray with sum 0"

### 🧠 Decision Tree
```
What is the property?
  ├─ Sum = K → Prefix Sum + HashMap
  ├─ XOR = K → Prefix XOR + HashMap
  └─ Sum = 0 → Prefix Sum + HashMap (K = 0)

What to find?
  ├─ COUNT of subarrays → Frequency HashMap (store count)
  └─ LONGEST subarray  → First-occurrence HashMap (store first index)
```

### ⚡ Key Insight
If `prefix[j] - prefix[i] = K`, then subarray `(i, j]` has sum K.
- To **count**: HashMap stores `(prefixSum → count of occurrences)`. Add `count += map.get(prefixSum - K)`.
- To find **longest**: HashMap stores `(prefixSum → first index)`. Maximize `j - map.get(prefixSum - K)`.

### 📋 Problems
| Problem | HashMap Stores |
|---------|---------------|
| Count Subarrays with Sum K | Prefix sum → frequency |
| Count Subarrays with XOR K | Prefix XOR → frequency |
| Longest Subarray with Sum K | Prefix sum → first index |
| Largest Subarray with 0 Sum | Prefix sum → first index (K=0) |

---

## 3. Maximum/Minimum Subarray

### 🔍 How to Identify
- "maximum subarray sum"
- "maximum product subarray"
- "largest contiguous subarray"

### 🧠 Decision Tree
```
What to maximize?
  ├─ SUM → Kadane's Algorithm
  │    └─ Track currentSum, reset to 0 when negative
  └─ PRODUCT → Prefix-Suffix Product
       └─ Compute prefix (L→R) and suffix (R→L)
       └─ Reset to 1 when hitting 0
       └─ Handles negatives: odd negatives create a "breaking point"
```

### ⚡ Why Kadane's Doesn't Work for Product
Kadane's works for sum because `negative + positive` can become positive later. But for product, `negative × negative = positive`, so we can't simply discard negative products. The prefix-suffix approach handles this naturally.

### 📋 Problems
| Problem | Algorithm |
|---------|-----------|
| Maximum Subarray Sum | Kadane's |
| Maximum Product Subarray | Prefix-Suffix Product |

---

## 4. Find Majority/Most Frequent

### 🔍 How to Identify
- "appears more than n/2 times"
- "appears more than n/3 times"
- "majority element"

### 🧠 Decision Tree
```
Threshold?
  ├─ > n/2 → 1 candidate → Boyer-Moore (single)
  └─ > n/3 → At most 2 candidates → Boyer-Moore (extended)

⚠️ ALWAYS verify candidates with a second pass!
Boyer-Moore finds candidates, not guaranteed majority elements.
```

### ⚡ Key Insight
At most `⌊n/k⌋` elements can appear more than `n/k` times. So:
- `> n/2` → at most **1** majority element
- `> n/3` → at most **2** majority elements

### 📋 Problems
| Problem | Candidates |
|---------|-----------|
| Majority Element (> n/2) | 1 candidate |
| Majority Element II (> n/3) | 2 candidates |

---

## 5. Missing/Duplicate in 1-to-N

### 🔍 How to Identify
- "array of size n, values from 1 to n"
- "one number missing", "one number repeated"
- "find missing and repeating"

### 🧠 Decision Tree
```
What's missing/duplicate?
  ├─ Only missing → SUM formula: missing = expectedSum - actualSum
  │                  or XOR: XOR all elements ^ XOR(1..n)
  ├─ Only duplicate → Same approaches
  └─ Both missing AND repeating → 
       ├─ Hash array (O(n) space)
       └─ Math: Sum + Sum of Squares (O(1) space) ✨
```

### ⚡ Why Math Works
Two unknowns (R, M) need two equations:
1. `actualSum - expectedSum = R - M`
2. `actualSumSq - expectedSumSq = R² - M²`

### 📋 Problems
| Problem | Best Approach |
|---------|--------------|
| Find Missing Number | XOR or Sum formula |
| Single Number (appears once) | XOR all elements |
| Find Missing & Repeating | Math (Sum + Sum of Squares) |

---

## 6. Count Ordered Pairs (i,j)

### 🔍 How to Identify
- "count pairs (i, j) where i < j"
- "count inversions" (`nums[i] > nums[j]`)
- "reverse pairs" (`nums[i] > 2 * nums[j]`)
- Any ordering constraint between pair elements

### 🧠 Decision Tree
```
Is the counting condition THE SAME as the sorting/merging condition?
  ├─ YES (e.g., a[i] > b[j]) → Count INSIDE merge function
  └─ NO  (e.g., a[i] > 2*b[j]) → SEPARATE counting pass BEFORE merge

⚠️ This is the #1 trap! Getting this wrong gives wrong answers.
```

### ⚡ Why Merge Sort Works
When merging two sorted halves, if `a[i] > b[j]`, then **all** elements `a[i], a[i+1], ..., a[m1-1]` also satisfy the condition (sorted property). So we count `m1 - i` pairs in O(1) instead of checking each one.

### ⚡ Overflow Trap
For reverse pairs: `2 * nums[j]` can overflow `int`. Always cast: `(long) a[i] > 2L * b[j]`.

### 📋 Problems
| Problem | Count Where? | Condition |
|---------|-------------|-----------|
| Count Inversions | Inside merge | `a[i] > b[j]` |
| Reverse Pairs | Separate pass | `a[i] > 2 * b[j]` |

---

## 7. In-Place Rearrangement

### 🔍 How to Identify
- "rearrange in-place"
- "without extra space"
- "sort 0s, 1s, 2s"
- "separate positives and negatives"

### 🧠 Decision Tree
```
How many categories?
  ├─ 2 categories → Two Pointers (reader-writer)
  │    └─ Move zeros, remove duplicates
  └─ 3 categories → Dutch National Flag (3 pointers: low, mid, high)
       └─ Sort colors
```

### 📋 Problems
| Problem | Technique |
|---------|-----------|
| Move Zeros to End | Reader-writer two pointers |
| Remove Duplicates | Reader-writer two pointers |
| Sort Colors (0, 1, 2) | Dutch National Flag |
| Rearrange by Sign | Two-pointer alternating placement |

---

## 8. Merge Sorted Data

### 🔍 How to Identify
- "merge two sorted arrays"
- "without extra space"
- "in-place merge"

### 🧠 Decision Tree
```
Extra space allowed?
  ├─ YES → Standard merge with auxiliary array: O(m+n) time, O(m+n) space
  └─ NO  → 
       ├─ Two Pointers from Extremes + Sort: O(min(m,n) + sorting)
       └─ Gap Method (Shell Sort): O((m+n) log(m+n)), O(1) space ✨
```

### 📋 Problems
| Problem | Technique |
|---------|-----------|
| Merge Two Sorted Arrays (with buffer) | 3 pointers from end |
| Merge Two Sorted Arrays (no extra space) | Gap method |

---

## 9. Intervals & Ranges

### 🔍 How to Identify
- "merge overlapping intervals"
- "non-overlapping intervals"
- Arrays of `[start, end]` pairs

### 🧠 Strategy
1. **Sort by start time**
2. **Linear sweep**: compare current interval with the last merged one
   - No overlap (`curr.start > last.end`) → add new interval
   - Overlap → extend `last.end = max(last.end, curr.end)`

### 📋 Problems
| Problem | Technique |
|---------|-----------|
| Merge Overlapping Subintervals | Sort + Linear merge |

---

## 10. Matrix Operations

### 🔍 How to Identify
- "rotate matrix 90°"
- "spiral order"
- "set matrix zeroes"

### 🧠 Strategy Map
| Operation | Algorithm |
|-----------|-----------|
| Rotate 90° CW | Transpose + Reverse each row |
| Rotate 90° CCW | Transpose + Reverse each column |
| Spiral Traversal | 4 boundaries: top, bottom, left, right |
| Set Zeroes | Use 1st row & 1st col as markers |

### 📋 Problems
| Problem | Technique |
|---------|-----------|
| Rotate Image | Transpose + Reverse rows |
| Spiral Matrix | Boundary pointers |
| Set Matrix Zeroes | First row/col markers |

---

## 11. Sliding Window

### 🔍 How to Identify
- "consecutive elements"
- "window of size k"
- "longest subarray with positive numbers only"

### 🧠 Decision Tree
```
Fixed or Variable window?
  ├─ Fixed size K → Expand to K, then slide (add right, remove left)
  └─ Variable size → 
       ├─ Expand right until condition met
       └─ Shrink left until condition broken
       
⚠️ Only works reliably with POSITIVE numbers for sum problems!
For arrays with negatives, use Prefix Sum + HashMap instead.
```

### 📋 Problems
| Problem | Window Type |
|---------|------------|
| Maximum Consecutive Ones | Variable (count 1s, reset on 0) |
| Longest Subarray with Sum K (positives) | Variable (expand/shrink) |

---

## 12. Permutation/Ordering

### 🔍 How to Identify
- "next permutation"
- "lexicographically next"
- "next greater arrangement"

### 🧠 Strategy
3-step algorithm:
1. Find **breakpoint** (rightmost `i` where `nums[i] < nums[i+1]`)
2. Find **swap partner** (rightmost `j > i` where `nums[j] > nums[i]`)
3. **Reverse** the suffix after breakpoint

### 📋 Problems
| Problem | Technique |
|---------|-----------|
| Next Permutation | Breakpoint + Swap + Reverse |

---

## 🗺️ Master Decision Flowchart

```
START: Read the problem carefully
  │
  ├─ "Find pair/triplet with sum"          → Pattern 1 (Two Pointers / HashMap)
  ├─ "Count/Longest subarray with sum/XOR" → Pattern 2 (Prefix Sum + HashMap)
  ├─ "Maximum subarray sum/product"        → Pattern 3 (Kadane's / Prefix-Suffix)
  ├─ "Majority element"                    → Pattern 4 (Boyer-Moore Voting)
  ├─ "Missing/Repeating in 1-to-N"         → Pattern 5 (Math / XOR)
  ├─ "Count pairs with ordering"           → Pattern 6 (Modified Merge Sort)
  ├─ "Rearrange in-place"                  → Pattern 7 (Two Pointers / DNF)
  ├─ "Merge sorted arrays"                 → Pattern 8 (Gap Method)
  ├─ "Overlapping intervals"               → Pattern 9 (Sort + Sweep)
  ├─ "2D matrix operation"                 → Pattern 10 (Matrix Manipulation)
  ├─ "Sliding window / consecutive"        → Pattern 11 (Sliding Window)
  └─ "Next permutation"                    → Pattern 12 (Breakpoint Algorithm)
```

---

## 🎯 Common Traps & Edge Cases

| Trap | Where It Appears | How to Avoid |
|------|-----------------|--------------|
| **Integer Overflow** | 4Sum, Missing & Repeating, Reverse Pairs | Cast to `long` before multiplication |
| **Counting ≠ Merging condition** | Reverse Pairs vs Count Inversions | Separate counting pass when conditions differ |
| **Prefix Sum with negatives** | Longest Subarray Sum K | Can't use sliding window; use HashMap |
| **Off-by-one in boundary** | Spiral Matrix, Gap Method | Carefully track `top/bottom/left/right` |
| **Boyer-Moore needs verification** | Majority Element I & II | Always verify candidates in second pass |
| **0 resets product, not sum** | Max Product Subarray | Reset prefix/suffix to 1 (not 0) on zero |
| **Duplicate skipping** | 3Sum, 4Sum | Skip same values for `i`, `left`, `right` to avoid duplicate triplets |
