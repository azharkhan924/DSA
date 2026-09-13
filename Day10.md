# Day 10 — Arrays (Hard): Pascal's Triangle & Majority Element II

---

## 1. Pascal's Triangle (LeetCode 118)

Pascal's Triangle is a triangular array of numbers where each number is the sum of the two numbers directly above it:

```
Row 1:        1
Row 2:       1 1
Row 3:      1 2 1
Row 4:     1 3 3 1
Row 5:    1 4 6 4 1
Row 6:  1 5 10 10 5 1
```

There are **3 distinct interview variations** based on Pascal's Triangle:
1. **Variation 1:** Given row `r` and column `c`, find the element at position `(r, c)`.
2. **Variation 2:** Given row number `n`, print the entire `n`-th row.
3. **Variation 3:** Given `numRows`, generate and return the entire Pascal's Triangle.

---

### Variation 1: Find the Element at Row `r` and Column `c`

**Problem:** Given row `r` and column `c` (1-indexed), find the value at that position.

```
Input:  r = 5, c = 3
Output: 6  (3rd element of 5th row)
```

#### Mathematical Formula:
$$\text{Element} = {}^{r-1}C_{c-1} = \frac{(r - 1)!}{(c - 1)! \times (r - c)!}$$

> **Example:** $r = 5, c = 3 \implies {}^{4}C_{2} = \frac{4 \times 3}{2 \times 1} = 6$

#### Pitfall with Factorials:
Do **NOT** compute factorials directly using `fact(n)` — $13!$ exceeds `Integer.MAX_VALUE`. Compute $nCr$ in $O(r)$ time by canceling terms in a single loop:

```java
class Solution {
    // Calculates nCr in O(r) time without overflow
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
| **Time** | O(c) |
| **Space** | O(1) |

---

### Variation 2: Print the N-th Row of Pascal's Triangle

**Problem:** Given row number `n` (1-indexed), return all elements of the `n`-th row.

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
| **Time** | O(n) |
| **Space** | O(1) — excluding output list |

---

### Variation 3: Generate the Entire Pascal's Triangle (numRows)

**Problem:** Given an integer `numRows`, return the first `numRows` of Pascal's Triangle. (LeetCode 118)

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
| **Time** | O(n²) |
| **Space** | O(1) — excluding output 2D list |

---

## 2. Majority Element II (LeetCode 229)

**Problem:** Given an integer array of size `n`, find all elements that appear more than `⌊n / 3⌋` times.

```
Example 1:
Input:  nums = [3, 2, 3]
Output: [3]  (3 appears 2 times > 3/3 = 1)

Example 2:
Input:  nums = [1, 2]
Output: [1, 2]  (both appear 1 time > 2/3 = 0)
```

### 🧠 Core Mathematical Insight:
- How many elements can appear **more than $\lfloor n / 3 \rfloor$ times**?
- Suppose 3 elements could appear $> n/3$ times:
  $$3 \times \left(\left\lfloor\frac{n}{3}\right\rfloor + 1\right) > n$$
  This would exceed the total number of elements in the array!
- Therefore, there can be **at most 2 majority elements** (the answer list size is always between `0` and `2`).

---

### Approach 1: Brute Force

Iterate through every element and count its occurrences using a nested loop.

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
| **Space** | O(n) — for hash map |

---

### Approach 3: Extended Boyer-Moore Voting Algorithm (Optimal ✨)

This generalizes the Boyer-Moore algorithm from Day 4 (where we canceled pairs of different elements). Here, we cancel **triplets** of 3 distinct elements:

1. **Phase 1 (Find Candidates):**
   - Maintain 2 candidates (`el1`, `el2`) and 2 counters (`count1`, `count2`).
   - If `count1 == 0` and `nums[i] != el2`: pick `nums[i]` as `el1`, `count1 = 1`.
   - Else if `count2 == 0` and `nums[i] != el1`: pick `nums[i]` as `el2`, `count2 = 1`.
   - Else if `nums[i] == el1`: `count1++`.
   - Else if `nums[i] == el2`: `count2++`.
   - Else: triplet cancellation → decrement both `count1--` and `count2--`.

2. **Phase 2 (Mandatory Verification Pass):**
   - In Majority Element I ($> n/2$), a majority element is guaranteed to exist.
   - Here, majority elements are **not guaranteed** (e.g. `[1, 2, 3]` has 0 majority elements).
   - We **must** do a second pass to count actual frequencies of `el1` and `el2`.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<Integer> majorityElement(int[] nums) {
        int count1 = 0, count2 = 0;
        int el1 = Integer.MIN_VALUE, el2 = Integer.MIN_VALUE;

        // Phase 1: Boyer-Moore Voting (Find 2 potential candidates)
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

        // Phase 2: Verification Pass (Count actual frequencies)
        count1 = 0;
        count2 = 0;
        for (int num : nums) {
            if (num == el1) count1++;
            else if (num == el2) count2++;
        }

        // Phase 3: Add only if frequency > n / 3
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
| **Time** | O(n) — exactly two passes |
| **Space** | O(1) ✨ — only constant variables |

> 💡 **Common Pitfall:**
> Never check `if (el1 != 0)` to validate candidates — `0` is a completely valid array element (e.g. `nums = [0]`). Always use a second frequency counting pass!

---

## 📝 Day 10 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Pascal's Triangle (3 Variations) | Combinatorics / Row Multiplier | O(N²) | O(1) extra |
| 2 | Majority Element II (> ⌊n/3⌋) | Extended Boyer-Moore Voting (2 Candidates) | O(n) | O(1) |
