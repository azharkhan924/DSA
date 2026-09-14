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
| 5 | Largest Subarray with 0 Sum | 📌 Track | Prefix Sum + First-Occurrence HashMap |
| 6 | Count Subarrays with Given XOR K | 📌 Track | Prefix XOR + Frequency HashMap |
| 7 | Merge Overlapping Subintervals | 📌 Track | Sort by Start Time + Linear Merge |
| 8 | Merge Two Sorted Arrays without Extra Space | 📌 Track | Gap Method (Shell Sort) / Backward Fill |
| 9 | Find Missing & Repeating Number | 📌 Track | Math (Sum & Sum of Squares) / XOR |
| 10 | Count Inversions in an Array | 📌 Track | Modified Merge Sort |
| 11 | Reverse Pairs | 📌 Track | Modified Merge Sort (Counting Step) |
| 12 | Maximum Product Subarray | 📌 Track | Prefix/Suffix Scan or Min-Max Kadane |

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

## 📝 Hard Track Summary

| # | Problem | Core Pattern | Time | Space |
|---|---------|--------------|------|-------|
| 1 | Pascal's Triangle (3 Variations) | Combinatorics / Row Multiplier | O(N²) | O(1) extra |
| 2 | Majority Element II (> ⌊n/3⌋) | Extended Boyer-Moore (2 Candidates) | O(n) | O(1) |
| 3 | 3 Sum | Sorting + 1 Fixed Loop + Two Pointers | O(n²) | O(1) extra |
| 4 | 4 Sum | Sorting + 2 Fixed Loops + Two Pointers (with `long` casting) | O(n³) | O(1) extra |
