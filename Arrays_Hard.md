# 🔴 Arrays (Hard) — DSA Sheet

A comprehensive, organized guide to **Hard Array Problems** from the DSA Sheet, complete with visual intuitions, optimal Java solutions, complexity analyses, and interview tips.

---

## 📑 Table of Contents

| # | Problem | Status | Core Pattern |
|:---:|:---|:---:|:---|
| 1 | [Pascal's Triangle (All 3 Variations)](#1-pascals-triangle-leetcode-118) | ✅ Complete | Combinatorics / Row Multiplier |
| 2 | [Majority Element II (> ⌊n/3⌋)](#2-majority-element-ii-n3-times) | ✅ Complete | Extended Boyer-Moore Voting (2 Candidates) |
| 3 | 3 Sum | 📌 Track | Sorting + Two Pointers |
| 4 | 4 Sum | 📌 Track | Sorting + Two Pointers (2 Fixed Pointers) |
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

## 📝 Hard Track Summary

| # | Problem | Core Pattern | Time | Space |
|---|---------|--------------|------|-------|
| 1 | Pascal's Triangle (3 Variations) | Combinatorics / Row Multiplier | O(N²) | O(1) extra |
| 2 | Majority Element II (> ⌊n/3⌋) | Extended Boyer-Moore (2 Candidates) | O(n) | O(1) |
