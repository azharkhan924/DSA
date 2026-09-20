# 🔴 Arrays (Hard) — DSA Sheet

A comprehensive, organized guide to **Hard Array Problems** from the DSA Sheet, complete with visual intuitions, optimal Java solutions, complexity analyses, and interview tips.

---

## 📑 Table of Contents

| # | Problem | Status | Core Pattern |
|:---:|:---|:---:|:---|
| 1 | [Pascal's Triangle (All 3 Variations)](#1-pascals-triangle-leetcode-118) | ✅ Complete | Combinatorics / Row Multiplier |
| 2 | [Majority Element II (> ⌊n/3⌋)](#2-majority-element-ii-n3-times) | ✅ Complete | Extended Boyer-Moore Voting (2 Candidates) |
| 3 | [3 Sum](#3-3-sum-leetcode-15) | ✅ Complete | Sorting + Two Pointers |
| 4 | [4 Sum](#4-4-sum-leetcode-18) | ✅ Complete | Sorting + Two Pointers (2 Fixed Loops) |
| 5 | [Largest Subarray with 0 Sum](#5-largest-subarray-with-0-sum) | ✅ Complete | Prefix Sum + First-Occurrence HashMap |
| 6 | [Count Subarrays with Given XOR K](#6-count-subarrays-with-given-xor-k) | ✅ Complete | Prefix XOR + Frequency HashMap |
| 7 | [Merge Overlapping Subintervals](#7-merge-overlapping-subintervals-leetcode-56) | ✅ Complete | Sort by Start Time + Linear Merge |
| 8 | [Merge Two Sorted Arrays without Extra Space](#8-merge-two-sorted-arrays-without-extra-space) | ✅ Complete | Gap Method (Shell Sort) / Backward Fill |
| 9 | [Find Missing & Repeating Number](#9-find-missing--repeating-number) | ✅ Complete | Math (Sum & Sum of Squares) / XOR |
| 10 | [Count Inversions in an Array](#10-count-inversions-in-an-array) | ✅ Complete | Modified Merge Sort |
| 11 | [Reverse Pairs](#11-reverse-pairs-leetcode-493) | ✅ Complete | Modified Merge Sort (Counting Step) |
| 12 | [Maximum Product Subarray](#12-maximum-product-subarray-leetcode-152) | ✅ Complete | Prefix/Suffix Scan |

---

## 1. Pascal's Triangle (LeetCode 118)

Pascal's Triangle is an iconic triangular array of binomial coefficients:

```
Row 1:         1
Row 2:        1 1
Row 3:       1 2 1
Row 4:      1 3 3 1
Row 5:     1 4 6 4 1
Row 6:   1 5 10 10 5 1
```

In technical interviews, this problem is asked in **3 distinct variations**:

---

### Variation 1: Given Row (R) and Column (C), Find the Element

**Problem:** Given row number `r` and column number `c` (1-indexed), find the value at that specific cell.

```
Input:  r = 5, c = 3
Output: 6  (3rd element of the 5th row)
```

#### 🧠 Mathematical Formula:
Any cell at row `r` and column `c` (1-indexed) is given by:

$$\text{Element} = {}^{r-1}C_{c-1} = \frac{(r - 1)!}{(c - 1)! \times (r - c)!}$$

> **Example:** $r = 5, c = 3 \implies {}^{5-1}C_{3-1} = {}^{4}C_{2} = \frac{4 \times 3}{2 \times 1} = 6$

#### ⚠️ Why NOT calculate full factorials?
Computing factorials with `fact(n)` quickly causes integer overflow:
- $13! = 6,227,020,800$, which exceeds `Integer.MAX_VALUE` ($2.14 \times 10^9$).
- Instead, compute $nCr$ in $O(r)$ time by canceling terms in a single loop:

```java
class Solution {
    // Computes nCr in O(r) time without factorial overflow
    public static long nCr(int n, int r) {
        long res = 1;
        for (int i = 0; i < r; i++) {
            res = res * (n - i);
            res = res / (i + 1);
        }
        return res;
    }

    public static int pascalElement(int r, int c) {
        return (int) nCr(r - 1, c - 1);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(c) — at most `c` loop iterations |
| **Space** | O(1) |

---

### Variation 2: Print the N-th Row of Pascal's Triangle

**Problem:** Given row number `n` (1-indexed), return all elements of that row.

```
Input:  n = 5
Output: [1, 4, 6, 4, 1]
```

#### Transition Formula:
$$\text{nextElement} = \text{currentElement} \times \frac{n - i}{i} \quad (i = 1, 2, \dots, n-1)$$

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public static List<Integer> getNthRow(int n) {
        List<Integer> row = new ArrayList<>();
        long ans = 1;
        row.add((int) ans);

        for (int i = 1; i < n; i++) {
            ans = ans * (n - i);
            ans = ans / i;
            row.add((int) ans);
        }
        return row;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — single pass to generate row of length `n` |
| **Space** | O(1) — excluding output list |

---

### Variation 3: Generate the Full Pascal's Triangle (numRows)

**Problem:** Given `numRows`, return the first `numRows` of Pascal's Triangle. (LeetCode 118)

```
Input:  numRows = 5
Output: [[1],
         [1, 1],
         [1, 2, 1],
         [1, 3, 3, 1],
         [1, 4, 6, 4, 1]]
```

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    private List<Integer> generateRow(int row) {
        List<Integer> ansRow = new ArrayList<>();
        long ans = 1;
        ansRow.add((int) ans);

        for (int col = 1; col < row; col++) {
            ans = ans * (row - col);
            ans = ans / col;
            ansRow.add((int) ans);
        }
        return ansRow;
    }

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();

        for (int r = 1; r <= numRows; r++) {
            triangle.add(generateRow(r));
        }
        return triangle;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n²) — total operations: $1 + 2 + 3 + \dots + n = \frac{n(n+1)}{2}$ |
| **Space** | O(1) — excluding output 2D list |

---

## 2. Majority Element II (> ⌊n/3⌋ times)

**Problem:** Given an integer array `nums` of size `n`, find all elements that appear more than `⌊n / 3⌋` times. (LeetCode 229)

```
Example 1:
Input:  nums = [3, 2, 3]
Output: [3]

Example 2:
Input:  nums = [1, 2]
Output: [1, 2]

Example 3:
Input:  nums = [1]
Output: [1]
```

---

### 🧠 Mathematical Deduction:
- How many elements can strictly exceed $\lfloor n / 3 \rfloor$ occurrences?
- Suppose 3 elements could appear $> \lfloor n / 3 \rfloor$ times. Their combined count would be:
  $$\ge 3 \times \left(\left\lfloor \frac{n}{3} \right\rfloor + 1\right) > n$$
  This would exceed the size of the array, which is impossible!
- **Conclusion:** There can be at most **2 majority elements** (the result list contains 0, 1, or 2 numbers).

---

### Comparison: Majority Element I vs Majority Element II

| Feature | Majority Element I (Medium) | Majority Element II (Hard) |
|---|---|---|
| **Threshold** | $> \lfloor n / 2 \rfloor$ | $> \lfloor n / 3 \rfloor$ |
| **Max Possible Answers** | Exactly 1 | At most 2 (0, 1, or 2) |
| **Candidates Tracked** | 1 candidate (`el`, `count`) | 2 candidates (`el1`, `el2`, `count1`, `count2`) |
| **Cancellation Group** | Pairs of 2 distinct elements | Triplets of 3 distinct elements |
| **Verification Pass** | Optional (if answer guaranteed) | **Mandatory** (answers may not exist) |

---

### Approach 1: Brute Force

For each element, count its occurrences across the entire array.

```java
class Solution {
    public List<Integer> majorityElement(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            if (ans.size() != 0 && nums[i] == ans.get(0)) continue;

            int count = 0;
            for (int j = 0; j < n; j++) {
                if (nums[i] == nums[j]) count++;
            }

            if (count > n / 3) ans.add(nums[i]);
            if (ans.size() == 2) break;
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n²) |
| **Space** | O(1) |

---

### Approach 2: HashMap Frequency Count

Count occurrences using a hash map and collect all keys with frequency $> \lfloor n / 3 \rfloor$.

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {
    public List<Integer> majorityElement(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        int n = nums.length;

        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        for (int key : map.keySet()) {
            if (map.get(key) > n / 3) {
                ans.add(key);
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) |

---

### Approach 3: Extended Boyer-Moore Voting Algorithm (Optimal ✨)

#### Core Intuition (Triplet Cancellation):
Just as we canceled different pairs in Majority Element I, here we cancel **triplets** of 3 distinct elements. If an element appears $> n/3$ times, it cannot be completely eliminated by other elements.

#### Step-by-Step Execution:
1. **Candidate Phase:**
   - Maintain `el1`, `el2` initialized to `Integer.MIN_VALUE` and counts `count1 = 0`, `count2 = 0`.
   - If `count1 == 0` and current element `!= el2`: set `el1 = nums[i]`, `count1 = 1`.
   - Else if `count2 == 0` and current element `!= el1`: set `el2 = nums[i]`, `count2 = 1`.
   - Else if current element `== el1`: `count1++`.
   - Else if current element `== el2`: `count2++`.
   - Else (current element is different from both): cancel one from each candidate → `count1--`, `count2--`.
2. **Verification Phase:**
   - Reset `count1 = 0, count2 = 0`.
   - Traverse the array once more and count actual occurrences of `el1` and `el2`.
   - If `count1 > n / 3`: add `el1` to result.
   - If `count2 > n / 3`: add `el2` to result.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<Integer> majorityElement(int[] nums) {
        int count1 = 0, count2 = 0;
        int el1 = Integer.MIN_VALUE, el2 = Integer.MIN_VALUE;

        // Phase 1: Find potential candidates
        for (int i = 0; i < nums.length; i++) {
            if (count1 == 0 && nums[i] != el2) {
                el1 = nums[i];
                count1 = 1;
            } else if (count2 == 0 && nums[i] != el1) {
                el2 = nums[i];
                count2 = 1;
            } else if (nums[i] == el1) {
                count1++;
            } else if (nums[i] == el2) {
                count2++;
            } else {
                count1--;
                count2--;
            }
        }

        // Phase 2: Verification Pass
        count1 = 0;
        count2 = 0;
        for (int num : nums) {
            if (num == el1) count1++;
            else if (num == el2) count2++;
        }

        // Phase 3: Threshold Check (> n / 3)
        List<Integer> result = new ArrayList<>();
        int threshold = nums.length / 3;

        if (count1 > threshold) result.add(el1);
        if (count2 > threshold) result.add(el2);

        return result;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — exactly two linear passes |
| **Space** | O(1) ✨ — in-place constant variables |

---

## 3. 3 Sum (LeetCode 15)

Given an integer array `nums`, return all the unique triplets `[nums[i], nums[j], nums[k]]` such that:
- `i != j`, `i != k`, and `j != k`
- `nums[i] + nums[j] + nums[k] == 0`
- The solution set **must not contain duplicate triplets**.

```
Input:  nums = [-1, 0, 1, 2, -1, -4]
Output: [[-1, -1, 2], [-1, 0, 1]]
```

---

### The Duplicate Problem
A brute-force solution checking every triplet generates duplicate triplets in different index orders (e.g., `[-1, 0, 1]` vs `[0, 1, -1]`).
- Sorting each triplet before adding to a `Set` costs extra time ($O(N^3 \log 3)$) and memory ($O(N)$ for HashSet).
- The **optimal approach** sorts the entire array first and uses **Two Pointers** with duplicate skipping to produce distinct triplets in $O(1)$ auxiliary space!

---

### Approach 1: Brute Force (3 Nested Loops)
Check all possible triplets with 3 loops, sort each triplet, and add to a `HashSet<List<Integer>>`.

| Complexity | Value |
|------------|-------|
| **Time** | O(n³ × log 3) ≈ O(n³) |
| **Space** | O(2 × no. of unique triplets) |

---

### Approach 2: Better (Hashing with HashSet)

#### Intuition:
From $nums[i] + nums[j] + nums[k] = 0$, we have:
$$\text{nums}[k] = -(\text{nums}[i] + \text{nums}[j])$$

Iterate through pairs $(i, j)$ and maintain a `HashSet` of seen elements between $i$ and $j$. If the required third element is in the set, add the sorted triplet to a `Set<List<Integer>>`.

```java
import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        int n = nums.length;
        Set<List<Integer>> set = new HashSet<>();

        for (int i = 0; i < n; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = i + 1; j < n; j++) {
                int required = -(nums[i] + nums[j]);

                if (seen.contains(required)) {
                    List<Integer> triplet = Arrays.asList(nums[i], nums[j], required);
                    Collections.sort(triplet);
                    set.add(triplet);
                }
                seen.add(nums[j]);
            }
        }
        return new ArrayList<>(set);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n² × log(no. of unique triplets)) |
| **Space** | O(n) + O(2 × no. of unique triplets) |

---

### Approach 3: Optimal (Sorting + Two Pointers) ✨

#### Intuition & Algorithm:
1. **Sort `nums`** in non-decreasing order.
2. Fix `nums[i]` using an outer loop:
   - If `nums[i] > 0`, break immediately (the remaining elements are all positive, sum cannot be $0$).
   - If `i > 0` and `nums[i] == nums[i - 1]`, `continue` to avoid duplicate triplets.
3. Place two pointers: `left = i + 1`, `right = n - 1`.
4. While `left < right`:
   - `sum = nums[i] + nums[left] + nums[right]`
   - If `sum == 0`:
     - Add `[nums[i], nums[left], nums[right]]` to result.
     - Move `left++` and skip duplicate values (`while (left < right && nums[left] == nums[left - 1]) left++`).
     - Move `right--` and skip duplicate values (`while (left < right && nums[right] == nums[right + 1]) right--`).
   - If `sum < 0`: need larger sum $\implies$ `left++`.
   - If `sum > 0`: need smaller sum $\implies$ `right--`.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            if (nums[i] > 0) break;
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (sum == 0) {
                    ans.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    left++;
                    right--;

                    while (left < right && nums[left] == nums[left - 1]) left++;
                    while (left < right && nums[right] == nums[right + 1]) right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n log n + n²) = **O(n²)** |
| **Space** | **O(1)** extra space (excluding space for result list) |

---

## 4. 4 Sum (LeetCode 18)

Given an array `nums` of `n` integers and an integer `target`, return all unique quadruplets `[nums[a], nums[b], nums[c], nums[d]]` such that $nums[a] + nums[b] + nums[c] + nums[d] == target$ with distinct indices $a, b, c, d$.

```
Input:  nums = [1, 0, -1, 0, -2, 2], target = 0
Output: [[-2, -1, 1, 2], [-2, 0, 0, 2], [-1, 0, 0, 1]]
```

---

### ⚠️ Critical Interview Trap: Integer Overflow!
When adding 4 numbers that can each be up to $10^9$ or $-10^9$, their sum can exceed standard 32-bit signed integer limits ($[-2^{31}, 2^{31}-1]$).
Always perform the 4-element sum with `long`:
```java
long sum = (long) nums[i] + nums[j] + nums[k] + nums[l];
```

---

### Approach 1: Brute Force (4 Nested Loops)
Check all four combinations with four loops and insert sorted quadruplets into a `HashSet`.

| Complexity | Value |
|------------|-------|
| **Time** | O(n⁴) |
| **Space** | O(2 × no. of unique quadruplets) |

---

### Approach 2: Better (Hashing with 3 Nested Loops)
Fix three elements ($i, j, k$) and compute the required fourth element:
$$\text{required} = \text{target} - (\text{nums}[i] + \text{nums}[j] + \text{nums}[k])$$
Check against an intermediate `HashSet` of elements seen between $j$ and $k$.

| Complexity | Value |
|------------|-------|
| **Time** | O(n³ × log(no. of unique quadruplets)) |
| **Space** | O(n) + O(2 × no. of unique quadruplets) |

---

### Approach 3: Optimal (Sorting + 2 Fixed Loops + Two Pointers) ✨

#### Intuition & Algorithm:
1. **Sort `nums`** in ascending order.
2. Loop $i$ from $0$ to $n - 1$:
   - Skip duplicates: `if (i > 0 && nums[i] == nums[i - 1]) continue;`
3. Loop $j$ from $i + 1$ to $n - 1$:
   - Skip duplicates: `if (j > i + 1 && nums[j] == nums[j - 1]) continue;`
4. Use two pointers: `k = j + 1`, `l = n - 1`.
5. While `k < l`:
   - `long sum = (long) nums[i] + nums[j] + nums[k] + nums[l];`
   - If `sum == target`:
     - Add `[nums[i], nums[j], nums[k], nums[l]]` to output.
     - Advance `k++` and skip identical duplicates.
     - Decrement `l--` and skip identical duplicates.
   - If `sum < target`: `k++`.
   - If `sum > target`: `l--`.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            // Skip duplicates for 1st pointer
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            for (int j = i + 1; j < n; j++) {
                // Skip duplicates for 2nd pointer
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;

                int k = j + 1;
                int l = n - 1;

                while (k < l) {
                    long sum = (long) nums[i] + nums[j] + nums[k] + nums[l];

                    if (sum == target) {
                        ans.add(Arrays.asList(nums[i], nums[j], nums[k], nums[l]));
                        k++;
                        l--;

                        // Skip duplicates for 3rd and 4th pointers
                        while (k < l && nums[k] == nums[k - 1]) k++;
                        while (k < l && nums[l] == nums[l + 1]) l--;
                    } else if (sum < target) {
                        k++;
                    } else {
                        l--;
                    }
                }
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n log n + n³) = **O(n³)** |
| **Space** | **O(1)** extra space (excluding output list) |

---

### 🧩 Generalization to K-Sum
| Problem | Fixed Loops | Moving Pointers | Time Complexity |
|---------|:-----------:|:---------------:|:---------------:|
| 2 Sum (Sorted) | 0 | 2 (`i`, `j`) | O(n) |
| 3 Sum | 1 (`i`) | 2 (`left`, `right`) | O(n²) |
| 4 Sum | 2 (`i`, `j`) | 2 (`k`, `l`) | O(n³) |
| **K-Sum** | **K - 2** | **2** | **O(n^(K-1))** |

---

## 5. Largest Subarray with 0 Sum

**Problem:** Given an array containing both positive and negative integers, find the length of the longest subarray with sum equal to `0`.

```
Input:  arr = [15, -2, 2, -8, 1, 7, 10, 23]
Output: 5
Explanation: The longest subarray with sum 0 is [-2, 2, -8, 1, 7] (length 5).
```

---

### 🧠 Core Intuition (Prefix Sum Collision)

If prefix sum at index `i` is $S$ and at index `j` is also $S$ ($j > i$), the subarray from index $i+1$ to $j$ must have sum $S - S = 0$.
- To **maximize length** ($j - i$), we only store the **first occurrence** of each prefix sum in the HashMap.
- Pre-seed `map.put(0, -1)` so that subarrays starting from index 0 whose sum is 0 automatically compute length $i - (-1) = i + 1$.

```java
import java.util.HashMap;

class Solution {
    public int maxLen(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1); // Handles cases where prefixSum from index 0 is 0

        int prefixSum = 0;
        int maxLength = 0;

        for (int i = 0; i < arr.length; i++) {
            prefixSum += arr[i];

            if (map.containsKey(prefixSum)) {
                maxLength = Math.max(maxLength, i - map.get(prefixSum));
            } else {
                map.put(prefixSum, i); // Retain earliest index
            }
        }

        return maxLength;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n)** — Single pass with O(1) hash map operations |
| **Space** | **O(n)** — Extra space for prefix sum map |

---

## 6. Count Subarrays with Given XOR K

**Problem:** Given an array of integers `nums` and an integer `k`, return the total number of subarrays whose XOR sum equals `k`.

```
Input:  nums = [4, 2, 2, 6, 4], k = 6
Output: 4
Explanation: Subarrays with XOR = 6 are [4, 2], [4, 2, 2, 6, 4], [2, 2, 6], and [6].
```

---

### 🧠 Core Intuition (Prefix XOR Property)

Let running prefix XOR up to index `i` be $\text{XR}$. If a subarray ending at `i` has XOR $k$, the previous prefix XOR $x$ satisfies:
$$x \oplus k = \text{XR} \implies x = \text{XR} \oplus k$$

- For each element, find how many times $x = \text{XR} \oplus k$ was seen previously in the frequency map.
- Add that frequency to `count`.
- Pre-seed `map.put(0, 1)` to handle cases where $\text{XR} = k$ directly from index 0 ($x = k \oplus k = 0$).

```java
import java.util.HashMap;

class Solution {
    public int subarraysWithXorK(int[] nums, int k) {
        int xor = 0;
        int count = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        for (int i = 0; i < nums.length; i++) {
            xor ^= nums[i];
            int required = xor ^ k;

            if (map.containsKey(required)) {
                count += map.get(required);
            }
            map.put(xor, map.getOrDefault(xor, 0) + 1);
        }

        return count;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n)** — Single pass with O(1) hash map operations |
| **Space** | **O(n)** — Extra space for prefix XOR frequencies |

---

## 7. Merge Overlapping Subintervals (LeetCode 56)

**Problem:** Given an array of `intervals` where `intervals[i] = [start_i, end_i]`, merge all overlapping intervals and return non-overlapping intervals.

```
Input:  intervals = [[1, 3], [2, 6], [8, 10], [15, 18]]
Output: [[1, 6], [8, 10], [15, 18]]
Explanation: [1, 3] and [2, 6] overlap, resulting in [1, 6].
```

---

### 🧠 Core Intuition (Sorting + Linear Scan)

By **sorting** intervals by their start times:
- Any overlapping intervals will become **adjacent**.
- We iterate linearly:
  - If `res` is empty or current `start > last_merged_end`: add as a new interval.
  - If `curr_start <= last_merged_end`: overlap! Extend `last_merged_end = Math.max(last_merged_end, curr_end)`.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) return intervals;

        // Sort by start times
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> res = new ArrayList<>();

        for (int[] curr : intervals) {
            // No overlap -> add new interval
            if (res.isEmpty() || res.get(res.size() - 1)[1] < curr[0]) {
                res.add(curr);
            } 
            // Overlapping -> extend end time
            else {
                res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], curr[1]);
            }
        }

        return res.toArray(new int[res.size()][]);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n log n)** — Sorting dominates; linear scan is O(n) |
| **Space** | **O(n)** — For storing the result list |

---

## 8. Merge Two Sorted Arrays without Extra Space

### Problem Statement
Given two sorted integer arrays `nums1` of size `m` and `nums2` of size `n`, merge them without using any extra auxiliary space ($O(1)$ space) such that:
- `nums1` contains the first `m` smallest elements sorted.
- `nums2` contains the remaining `n` elements sorted.

```
Input:  nums1 = [-5, -2, 4, 5], nums2 = [-3, 1, 8]
Output: nums1 = [-5, -3, -2, 1], nums2 = [4, 5, 8]
```

---

### Approach 1: Two Pointers from Extremes + Sorting (Optimal 1)

#### Intuition
- `nums1` should hold the $m$ smallest elements, while `nums2` holds the $n$ largest elements.
- The largest elements of `nums1` are at the end (`left = m - 1`), and the smallest of `nums2` are at the start (`right = 0`).
- If `nums1[left] > nums2[right]`, swap them, `left--`, `right++`.
- Break when `nums1[left] <= nums2[right]`, then sort both arrays.

```java
import java.util.Arrays;

class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int left = m - 1;
        int right = 0;

        while (left >= 0 && right < n) {
            if (nums1[left] > nums2[right]) {
                int temp = nums1[left];
                nums1[left] = nums2[right];
                nums2[right] = temp;
                left--;
                right++;
            } else {
                break;
            }
        }

        Arrays.sort(nums1);
        Arrays.sort(nums2);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(min(m, n)) + O(m log m) + O(n log n)** |
| **Space** | **O(1)** auxiliary space |

---

### Approach 2: Gap Method / Shell Sort (Optimal 2 — No Library Sort)

#### Intuition
- Treat `nums1` and `nums2` as a single virtual array of length `m + n`.
- Initialize `gap = ceil((m + n) / 2.0)`.
- Compare elements separated by `gap` and swap if out of order.
- Divide `gap` by 2 (ceiling) after each pass until `gap = 1` finishes.

```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int len = m + n;
        int gap = (len / 2) + (len % 2);

        while (gap > 0) {
            int left = 0;
            int right = left + gap;

            while (right < len) {
                // Both in nums1
                if (left < m && right < m) {
                    if (nums1[left] > nums1[right]) {
                        swap(nums1, left, nums1, right);
                    }
                }
                // left in nums1, right in nums2
                else if (left < m && right >= m) {
                    if (nums1[left] > nums2[right - m]) {
                        swap(nums1, left, nums2, right - m);
                    }
                }
                // Both in nums2
                else {
                    if (nums2[left - m] > nums2[right - m]) {
                        swap(nums2, left - m, nums2, right - m);
                    }
                }
                left++;
                right++;
            }

            if (gap == 1) break;
            gap = (gap / 2) + (gap % 2);
        }
    }

    private void swap(int[] a, int i, int[] b, int j) {
        int t = a[i];
        a[i] = b[j];
        b[j] = t;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O((m + n) log(m + n))** |
| **Space** | **O(1)** auxiliary space |

---

### Approach 3: LeetCode 88 (Reverse 3 Pointers)
*When `nums1` already has a buffer of size $m + n$*:

```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1, j = n - 1, k = m + n - 1;

        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
            }
        }

        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(m + n)** |
| **Space** | **O(1)** |

---

## 9. Find Missing & Repeating Number

**Problem:** Given an integer array `nums` of size `n` containing values from `[1, n]`. Each value appears exactly once, except for `A` (appears twice) and `B` (missing). Return `[A, B]`.

```
Input:  nums = [3, 5, 4, 1, 1]
Output: [1, 2]
Explanation: 1 appears twice (repeating), 2 is missing.
```

---

### Approach 1: Brute Force — Count Every Element

For each number from `1` to `n`, count its occurrences in the array.

```java
class Solution {
    public int[] findMissingAndRepeating(int[] nums) {
        int n = nums.length;
        int r = -1, m = -1;

        for (int i = 1; i <= n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (nums[j] == i) count++;
            }
            if (count == 2) r = i;
            else if (count == 0) m = i;

            if (r != -1 && m != -1) break;
        }

        return new int[]{r, m};
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n²)** — For each number 1 to n, scan the array |
| **Space** | **O(1)** |

---

### Approach 2: Hash Array (Frequency Counting)

Use an auxiliary count array of size `n + 1` to track frequencies.

```java
class Solution {
    public int[] findMissingAndRepeating(int[] nums) {
        int n = nums.length;
        int[] hash = new int[n + 1];
        int r = -1, m = -1;

        for (int i = 0; i < n; i++) hash[nums[i]]++;

        for (int i = 1; i <= n; i++) {
            if (hash[i] == 2) r = i;
            else if (hash[i] == 0) m = i;
        }

        return new int[]{r, m};
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n)** |
| **Space** | **O(n)** — Extra hash array |

---

### Approach 3: Optimal — Math (Sum & Sum of Squares) ✨

#### Intuition

Let `R` = repeating, `M` = missing. Using the expected sums vs actual sums:

1. **Sum equation:** `actualSum - Sn = R - M` → `diff`
2. **Sum of squares equation:** `actualSum² - S2n = R² - M² = (R - M)(R + M)` → `diffsqr`
3. **Solve:** `R + M = diffsqr / diff` → `sumPlus`
4. **Result:** `R = (diff + sumPlus) / 2`, `M = R - diff`

> ⚠️ **Important:** Always use `long` to prevent integer overflow when computing `n(n+1)(2n+1)/6` and squared sums.

```java
class Solution {
    public int[] findMissingAndRepeating(int[] nums) {
        long n = nums.length;

        long sumN = (n * (n + 1)) / 2;
        long sum2N = (n * (n + 1) * (2 * n + 1)) / 6;

        long actualSum = 0, actualSum2 = 0;
        for (int i = 0; i < n; i++) {
            actualSum += nums[i];
            actualSum2 += (long) nums[i] * (long) nums[i];
        }

        long diff = actualSum - sumN;          // R - M
        long diffsqr = actualSum2 - sum2N;     // R² - M²
        long sumPlus = diffsqr / diff;         // R + M

        long r = (diff + sumPlus) / 2;
        long m = r - diff;

        return new int[]{(int) r, (int) m};
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n)** — Single pass |
| **Space** | **O(1)** |

---

## 10. Count Inversions in an Array

**Problem:** Given an array `nums`, count the number of inversions: pairs `(i, j)` where `i < j` and `nums[i] > nums[j]`.

```
Input:  nums = [5, 3, 2, 4, 1]
Output: 7
Explanation: Inversions are (5,3), (5,2), (5,4), (5,1), (3,2), (3,1), (4,1)
```

---

### Approach 1: Brute Force — Nested Loop

```java
class Solution {
    public long countInversions(int[] nums) {
        long count = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] > nums[j]) count++;
            }
        }
        return count;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n²)** |
| **Space** | **O(1)** |

---

### Approach 2: Optimal — Modified Merge Sort ✨

#### 🧠 Core Intuition

During the **merge step** of merge sort, when `a[i] > b[j]`, all remaining elements in the left half (`a[i...m1-1]`) also form inversions with `b[j]` (since the left half is sorted). So we add `count += (m1 - i)` and move `j++`.

The counting condition and the merging condition are **identical** here (`a[i] > b[j]`), so we can count directly inside the merge function.

```java
class Solution {
    public long countInversions(int[] nums) {
        return mergeSort(nums, nums.length);
    }

    private long mergeSort(int[] nums, int n) {
        if (n <= 1) return 0;

        int m1 = n / 2;
        int m2 = n - m1;

        int[] a = new int[m1];
        int[] b = new int[m2];

        for (int i = 0; i < m1; i++) a[i] = nums[i];
        for (int i = 0; i < m2; i++) b[i] = nums[m1 + i];

        long count = 0;
        count += mergeSort(a, m1);
        count += mergeSort(b, m2);
        count += merge(nums, a, b, m1, m2);

        return count;
    }

    private long merge(int[] nums, int[] a, int[] b, int m1, int m2) {
        int[] ans = new int[m1 + m2];
        int i = 0, j = 0, k = 0;
        long count = 0;

        while (i < m1 && j < m2) {
            if (a[i] <= b[j]) {
                ans[k++] = a[i++];
            } else {
                // a[i] > b[j] => all elements a[i...m1-1] form inversions with b[j]
                count += (m1 - i);
                ans[k++] = b[j++];
            }
        }

        while (i < m1) ans[k++] = a[i++];
        while (j < m2) ans[k++] = b[j++];

        for (int p = 0; p < ans.length; p++) nums[p] = ans[p];

        return count;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n log n)** — Standard merge sort time |
| **Space** | **O(n)** — Temporary arrays for merging |

---

## 11. Reverse Pairs (LeetCode 493)

**Problem:** Given an integer array `nums`, return the number of **reverse pairs**: pairs `(i, j)` where `i < j` and `nums[i] > 2 * nums[j]`.

```
Input:  nums = [1, 3, 2, 3, 1]
Output: 2
Explanation: Reverse pairs are (3, 1) and (3, 1)
```

---

### 🧠 Key Difference from Count Inversions

| Problem | Counting Condition | Merge Condition | Can Count Inside Merge? |
|---------|-------------------|-----------------|-------------------------|
| Count Inversions | `a[i] > b[j]` | `a[i] > b[j]` | ✅ Yes — Same condition |
| Reverse Pairs | `a[i] > 2 * b[j]` | `a[i] > b[j]` | ❌ No — Different conditions |

Because the counting and merging conditions are **different**, we **must separate** counting into its own pass **before** merging. Otherwise, the merge's pointer movements will cause us to skip valid pairs.

---

### Approach 1: Brute Force — Nested Loop

```java
class Solution {
    public int reversePairs(int[] nums) {
        int count = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((long) nums[i] > 2L * nums[j]) count++;
            }
        }
        return count;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n²)** |
| **Space** | **O(1)** |

---

### Approach 2: Optimal — Modified Merge Sort (Separate Counting) ✨

The algorithm has **two phases** at each merge step:
1. **Count reverse pairs** using a two-pointer pass on the two sorted halves.
2. **Standard merge** to keep the array sorted for future recursion levels.

> ⚠️ **Integer Overflow:** `2 * nums[j]` can overflow `int` when `nums[j]` is near `Integer.MAX_VALUE`. Always use `2L * b[right]`.

```java
class Solution {
    public int reversePairs(int[] nums) {
        return mergeSort(nums, nums.length);
    }

    private int mergeSort(int[] nums, int n) {
        if (n <= 1) return 0;

        int m1 = n / 2;
        int m2 = n - m1;

        int[] a = new int[m1];
        int[] b = new int[m2];

        for (int i = 0; i < m1; i++) a[i] = nums[i];
        for (int i = 0; i < m2; i++) b[i] = nums[m1 + i];

        int count = 0;
        count += mergeSort(a, m1);
        count += mergeSort(b, m2);

        // STEP 1: Count reverse pairs (separate two-pointer pass)
        count += countPairs(a, b, m1, m2);

        // STEP 2: Standard merge (no counting here)
        merge(nums, a, b, m1, m2);

        return count;
    }

    // Two-pointer counting pass: O(m1 + m2)
    private int countPairs(int[] a, int[] b, int m1, int m2) {
        int count = 0;
        int right = 0;

        for (int i = 0; i < m1; i++) {
            while (right < m2 && (long) a[i] > 2L * b[right]) {
                right++;
            }
            count += right;
        }

        return count;
    }

    // Standard merge (no counting logic)
    private void merge(int[] nums, int[] a, int[] b, int m1, int m2) {
        int[] ans = new int[m1 + m2];
        int i = 0, j = 0, k = 0;

        while (i < m1 && j < m2) {
            if (a[i] <= b[j]) ans[k++] = a[i++];
            else ans[k++] = b[j++];
        }

        while (i < m1) ans[k++] = a[i++];
        while (j < m2) ans[k++] = b[j++];

        for (int p = 0; p < ans.length; p++) nums[p] = ans[p];
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n log n)** — O(n) counting + O(n) merging at each of O(log n) levels |
| **Space** | **O(n)** — Temporary arrays for merging |

---

## 12. Maximum Product Subarray (LeetCode 152)

**Problem:** Given an integer array `nums`, find the contiguous subarray with the largest product and return the product.

```
Input:  nums = [2, 3, -2, 4]
Output: 6
Explanation: Subarray [2, 3] has the largest product = 6.
```

---

### 🧠 Core Intuition (Prefix-Suffix Product)

**Key Observations:**
1. **All positive elements** → multiply everything = max product.
2. **Even number of negatives** → multiply everything = max product (negatives cancel out).
3. **Odd number of negatives** → one negative element is a "breaking point". The maximum product is either the prefix product (left of the rightmost negative) or suffix product (right of the leftmost negative).
4. **Zero present** → zero is a breaking point; reset the running product.

By computing both **prefix** (left → right) and **suffix** (right → left) products, we cover all cases. If either product hits 0, reset it to 1.

---

### Approach 1: Brute Force — Nested Loop

```java
class Solution {
    public int maxProduct(int[] nums) {
        int max = Integer.MIN_VALUE;
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            int product = 1;
            for (int j = i; j < n; j++) {
                product *= nums[j];
                max = Math.max(max, product);
            }
        }
        return max;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n²)** |
| **Space** | **O(1)** |

---

### Approach 2: Optimal — Prefix-Suffix Scan ✨

```java
class Solution {
    public int maxProduct(int[] nums) {
        int max = Integer.MIN_VALUE;
        int prefix = 1, suffix = 1;
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            prefix *= nums[i];
            suffix *= nums[n - 1 - i];
            max = Math.max(max, Math.max(prefix, suffix));
            if (prefix == 0) prefix = 1;
            if (suffix == 0) suffix = 1;
        }

        return max;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n)** — Single pass |
| **Space** | **O(1)** |

---

## 📝 Hard Track Summary

| # | Problem | Core Pattern | Time | Space |
|---|---------|--------------|------|-------|
| 1 | Pascal's Triangle (3 Variations) | Combinatorics / Row Multiplier | O(N²) | O(1) extra |
| 2 | Majority Element II (> ⌊n/3⌋) | Extended Boyer-Moore (2 Candidates) | O(n) | O(1) |
| 3 | 3 Sum | Sorting + 1 Fixed Loop + Two Pointers | O(n²) | O(1) extra |
| 4 | 4 Sum | Sorting + 2 Fixed Loops + Two Pointers (with `long` casting) | O(n³) | O(1) extra |
| 5 | Largest Subarray with 0 Sum | Prefix Sum + First-Occurrence HashMap | O(n) | O(n) |
| 6 | Count Subarrays with Given XOR K | Prefix XOR + Frequency HashMap | O(n) | O(n) |
| 7 | Merge Overlapping Subintervals | Sort by Start Time + Linear Merge | O(n log n) | O(n) |
| 8 | Merge Two Sorted Arrays without Extra Space | Gap Method (Shell Sort) / Backward Fill | O((m+n) log(m+n)) | O(1) |
| 9 | Find Missing & Repeating Number | Math (Sum & Sum of Squares) | O(n) | O(1) |
| 10 | Count Inversions in an Array | Modified Merge Sort | O(n log n) | O(n) |
| 11 | Reverse Pairs | Modified Merge Sort (Separate Counting) | O(n log n) | O(n) |
| 12 | Maximum Product Subarray | Prefix-Suffix Product Scan | O(n) | O(1) |
