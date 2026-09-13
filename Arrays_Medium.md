# 🟡 Arrays (Medium) — DSA Sheet

A comprehensive, organized guide to all **14 Medium Array Problems** from the DSA Sheet, complete with visual intuitions, optimal Java solutions, complexity analyses, and interview tips.

---

## 📑 Table of Contents

1. [Two Sum](#1-two-sum)
2. [Sort Colors / Dutch National Flag (0s, 1s, 2s)](#2-sort-colors--dutch-national-flag-0s-1s-2s)
3. [Majority Element (> n/2 times)](#3-majority-element--n2-times)
4. [Maximum Subarray Sum (Kadane's Algorithm)](#4-maximum-subarray-sum-kadanes-algorithm)
5. [Print Subarray with Maximum Subarray Sum](#5-print-subarray-with-maximum-subarray-sum)
6. [Best Time to Buy and Sell Stock](#6-best-time-to-buy-and-sell-stock)
7. [Rearrange Array Elements by Sign](#7-rearrange-array-elements-by-sign)
8. [Next Permutation](#8-next-permutation)
9. [Leaders in an Array](#9-leaders-in-an-array)
10. [Longest Consecutive Sequence](#10-longest-consecutive-sequence)
11. [Set Matrix Zeroes](#11-set-matrix-zeroes)
12. [Rotate Matrix by 90° (Rotate Image)](#12-rotate-matrix-by-90-rotate-image)
13. [Spiral Matrix Traversal](#13-spiral-matrix-traversal)
14. [Count Subarrays with Given Sum (Subarray Sum Equals K)](#14-count-subarrays-with-given-sum-subarray-sum-equals-k)

---

## 1. Two Sum

**Problem:** Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.

```
Input:  nums = [2, 7, 11, 15], target = 9
Output: [0, 1]
Explanation: nums[0] + nums[1] = 2 + 7 = 9
```

### Approach: HashMap Complement Lookup (Optimal ✨)

As you traverse, store `(nums[i] → index)` in a map. For each element, check if its complement `target - nums[i]` already exists in the map.

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) |

> 💡 **Tip:** If the array is already sorted, use **Two Pointers** from both ends for O(1) space.

---

## 2. Sort Colors / Dutch National Flag (0s, 1s, 2s)

**Problem:** Given an array with `n` objects colored red (`0`), white (`1`), or blue (`2`), sort them **in-place** in a single pass.

```
Input:  nums = [2, 0, 2, 1, 1, 0]
Output: [0, 0, 1, 1, 2, 2]
```

### Approach: Dutch National Flag Algorithm (Single Pass ✨)

Use three pointers: `low`, `mid`, `high`.

#### Invariant:
- `[0 ... low - 1]` → all `0`s
- `[low ... mid - 1]` → all `1`s
- `[mid ... high]` → unknown / unprocessed
- `[high + 1 ... n - 1]` → all `2`s

| Value of `nums[mid]` | Action |
|:---:|---|
| `0` | Swap `nums[low]` and `nums[mid]`; increment `low++`, `mid++` |
| `1` | Just increment `mid++` |
| `2` | Swap `nums[mid]` and `nums[high]`; decrement `high--` (*don't move `mid`!*) |

```java
class Solution {
    private void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    public void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;

        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low++, mid++);
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                swap(nums, mid, high--);
            }
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — single pass |
| **Space** | O(1) — in-place |

> 💡 **Why not increment `mid` when swapping with `high`?**
> The element coming from `high` was previously unknown (could be 0, 1, or 2). We must evaluate it at `mid` in the next iteration.

---

## 3. Majority Element (> n/2 times)

**Problem:** Find the element that appears more than `⌊n / 2⌋` times in the array.

```
Input:  nums = [2, 2, 1, 1, 1, 2, 2]
Output: 2
```

### Approach: Boyer-Moore Voting Algorithm (Optimal ✨)

The majority element occurs more than all other elements combined. If we pair up and cancel out different elements, the majority element will always be the last candidate standing.

```java
class Solution {
    public int majorityElement(int[] nums) {
        int candidate = nums[0];
        int count = 0;

        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
            if (num == candidate) {
                count++;
            } else {
                count--;
            }
        }
        return candidate;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) ✨ |

> 💡 **Verification Pass:** If the problem does not guarantee a majority element exists, perform a second pass to verify `count > n / 2`.
> **Follow-up:** *Majority Element II* (> ⌊n/3⌋) uses **two candidates and two counters**.

---

## 4. Maximum Subarray Sum (Kadane's Algorithm)

**Problem:** Find the contiguous subarray (containing at least one number) which has the largest sum.

```
Input:  nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
Output: 6
Explanation: Subarray [4, -1, 2, 1] has the largest sum = 6.
```

### Approach: Kadane's Algorithm

1. Keep a running `sum`.
2. Record `maxSum = max(maxSum, sum)` at each step.
3. If `sum < 0`, reset `sum = 0` (a negative prefix only reduces future subarray sums).

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int sum = 0;
        int maxSum = Integer.MIN_VALUE;

        for (int num : nums) {
            sum += num;
            maxSum = Math.max(maxSum, sum);
            if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

> ⚠️ **Important:** Update `maxSum` **before** resetting `sum = 0` so arrays with all negative numbers return the least negative number correctly.

---

## 5. Print Subarray with Maximum Subarray Sum

**Problem:** Find the subarray with the maximum sum and return / print its boundary indices `[start, end]`.

```
Input:  nums = [5, 4, -1, 7, 8]
Output: Sum = 23, Subarray = [0, 4]
```

### Approach: Kadane's Extended with Pointer Tracking

- Update `maxStart = start` and `maxEnd = i` whenever a new `maxSum` is discovered.
- When `sum < 0`, reset `sum = 0` and update candidate `start = i + 1`.

```java
class Solution {
    public static void printMaxSubarray(int[] nums) {
        int sum = 0, maxSum = Integer.MIN_VALUE;
        int start = 0;
        int maxStart = 0, maxEnd = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];

            if (sum > maxSum) {
                maxSum = sum;
                maxStart = start;
                maxEnd = i;
            }

            if (sum < 0) {
                sum = 0;
                start = i + 1;
            }
        }

        System.out.println("Max Sum: " + maxSum);
        System.out.println("Subarray Indices: [" + maxStart + ", " + maxEnd + "]");
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

---

### 🔁 Kadane's Algorithm Cheat Sheet

```
Standard (Sum Only):                  With Subarray Tracking:
┌────────────────────────────────┐   ┌────────────────────────────────┐
│ sum = 0, maxSum = -∞           │   │ sum = 0, maxSum = -∞           │
│ for each element:              │   │ start = 0, maxStart = maxEnd = 0
│   sum += element               │   │ for i = 0 to n - 1:            │
│   maxSum = max(maxSum, sum)    │   │   sum += nums[i]               │
│   if sum < 0: sum = 0          │   │   if sum > maxSum:             │
│ return maxSum                  │   │     maxSum = sum               │
└────────────────────────────────┘   │     maxStart = start, maxEnd = i
                                     │   if sum < 0:                  │
                                     │     sum = 0, start = i + 1     │
                                     │ return [maxStart, maxEnd]      │
                                     └────────────────────────────────┘
```

---

## 6. Best Time to Buy and Sell Stock

**Problem:** Choose a single day to buy one stock and choose a different day in the future to sell that stock to maximize profit.

```
Input:  prices = [7, 1, 5, 3, 6, 4]
Output: 5 (buy on day 2 at 1, sell on day 5 at 6 -> profit = 6 - 1 = 5)
```

### Approach: Greedy Minimum Tracking (Optimal ✨)

Traverse left to right, maintaining `minPrice` seen so far. At each day, potential profit is `prices[i] - minPrice`.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice = prices[0];
        int maxProfit = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < minPrice) {
                minPrice = prices[i];
            } else {
                maxProfit = Math.max(maxProfit, prices[i] - minPrice);
            }
        }
        return maxProfit;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

---

## 7. Rearrange Array Elements by Sign

**Problem:** Rearrange equal numbers of positive and negative integers in alternating order: positives at even indices (`0, 2, 4...`), negatives at odd indices (`1, 3, 5...`). Preserve relative order.

```
Input:  nums = [3, 1, -2, -5, 2, -4]
Output: [3, -2, 1, -5, 2, -4]
```

### Approach: Single Pass Index Jumping (+2)

Initialize `posIndex = 0` and `negIndex = 1`. Place elements into a new result array in a single traversal.

```java
class Solution {
    public int[] rearrangeArray(int[] nums) {
        int[] result = new int[nums.length];
        int posIndex = 0;
        int negIndex = 1;

        for (int num : nums) {
            if (num > 0) {
                result[posIndex] = num;
                posIndex += 2;
            } else {
                result[negIndex] = num;
                negIndex += 2;
            }
        }
        return result;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — single pass |
| **Space** | O(n) — for result array |

---

## 8. Next Permutation

**Problem:** Rearrange numbers into the lexicographically next greater permutation. If no greater permutation exists, rearrange into the lowest possible order (sorted ascending). Must be done **in-place**.

```
Input:  nums = [1, 2, 3]  → Output: [1, 3, 2]
Input:  nums = [3, 2, 1]  → Output: [1, 2, 3]
Input:  nums = [1, 1, 5]  → Output: [1, 5, 1]
```

### Visual Algorithm & Intuition:

Consider `nums = [2, 1, 5, 4, 3, 0, 0]`

1. **Find the Breakpoint:** Scan from right to left to find the first element `nums[i] < nums[i + 1]`.
   - Here, `nums[1] = 1 < 5`. Breakpoint index is `i = 1`.
   - Elements to the right `[5, 4, 3, 0, 0]` are in decreasing order (maximum permutation for that suffix).
2. **Handle Edge Case:** If no breakpoint exists (entire array is descending, e.g., `[3, 2, 1]`), reverse the whole array and return.
3. **Find Successor to Swap:** Scan from right to left to find the first element `nums[j] > nums[i]`.
   - Smallest element greater than `1` from right is `3` at `j = 4`.
   - Swap `nums[1]` and `nums[4]`: array becomes `[2, 3, 5, 4, 1, 0, 0]`.
4. **Reverse the Suffix:** Suffix from `i + 1` to `n - 1` is still in descending order. Reverse it to make it ascending (the smallest possible order).
   - `[5, 4, 1, 0, 0]` reversed becomes `[0, 0, 1, 4, 5]`.
   - Final result: `[2, 3, 0, 0, 1, 4, 5]` ✅

```java
class Solution {
    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int t = nums[start];
            nums[start++] = nums[end];
            nums[end--] = t;
        }
    }

    private void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    public void nextPermutation(int[] nums) {
        int n = nums.length;
        int i = n - 2;

        // Step 1: Find first decreasing element from right
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }

        // Step 2 & 3: If breakpoint exists, find element just larger and swap
        if (i >= 0) {
            int j = n - 1;
            while (nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }

        // Step 4: Reverse the suffix from i + 1 to end
        reverse(nums, i + 1, n - 1);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — at most 3 linear scans |
| **Space** | O(1) — in-place |

---

## 9. Leaders in an Array

**Problem:** An element is a **leader** if it is strictly greater than or equal to all elements to its right. The rightmost element is always a leader.

```
Input:  nums = [16, 17, 4, 3, 5, 2]
Output: [17, 5, 2]
```

### Approach: Right-to-Left Traversal

Traverse from right to left, keeping track of `maxRight`. If current element `>= maxRight`, it is a leader.

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Solution {
    public List<Integer> leaders(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        int n = nums.length;
        int maxRight = nums[n - 1];
        ans.add(maxRight);

        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] >= maxRight) {
                maxRight = nums[i];
                ans.add(nums[i]);
            }
        }

        Collections.reverse(ans);
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) — for returning the result |

---

## 10. Longest Consecutive Sequence

**Problem:** Given an unsorted array of integers `nums`, return the length of the longest consecutive elements sequence in **O(n) time**.

```
Input:  nums = [100, 4, 200, 1, 3, 2]
Output: 4 (sequence is [1, 2, 3, 4])
```

### Approach: HashSet — Sequence Heads Only (Optimal ✨)

1. Put all numbers into a `HashSet`.
2. Only start counting a sequence if `num - 1` does NOT exist in the set (i.e. `num` is the **head** of a sequence).
3. If `num` is a head, count forward: `num + 1`, `num + 2`...

```java
import java.util.HashSet;
import java.util.Set;

class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) set.add(num);

        int longest = 0;

        for (int num : set) {
            // Check if num is the start of a sequence
            if (!set.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;

                while (set.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }

                longest = Math.max(longest, currentStreak);
            }
        }
        return longest;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — each number is visited at most twice |
| **Space** | O(n) — for the hash set |

> 💡 **Why is it O(n)?** The `while` loop only runs for sequence heads. Across all numbers, each element is part of exactly one sequence and checked at most twice.

---

## 11. Set Matrix Zeroes

**Problem:** Given an `m × n` matrix, if an element is `0`, set its entire row and column to `0` **in-place**.

```
Input:  [[1, 1, 1],
         [1, 0, 1],
         [1, 1, 1]]

Output: [[1, 0, 1],
         [0, 0, 0],
         [1, 0, 1]]
```

### Optimal In-Place Approach (O(1) Space ✨)

Use the **first row and first column** of the matrix itself as the marker arrays. Use an extra variable `col0` to track whether the first column needs to be zeroed.

```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int col0 = 1;

        // Step 1: Mark rows and columns in first row & column
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) col0 = 0;
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        // Step 2: Fill inner matrix from bottom-right up
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 1; j--) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
            if (col0 == 0) {
                matrix[i][0] = 0;
            }
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(m × n) |
| **Space** | O(1) — in-place |

---

## 12. Rotate Matrix by 90° (Rotate Image)

**Problem:** Rotate an `n × n` 2D matrix clockwise by 90 degrees **in-place**.

```
Original:       Transpose:      Reverse Each Row:
1  2  3         1  4  7         7  4  1
4  5  6    →    2  5  8    →    8  5  2
7  8  9         3  6  9         9  6  3
                                  ✅ 90° Clockwise
```

### Approach: Transpose + Reverse Rows

```java
class Solution {
    public void rotate(int[][] matrix) {
        int n = matrix.length;

        // Step 1: Transpose (swap matrix[i][j] with matrix[j][i])
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            int left = 0, right = n - 1;
            while (left < right) {
                int temp = matrix[i][left];
                matrix[i][left++] = matrix[i][right];
                matrix[i][right--] = temp;
            }
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n²) |
| **Space** | O(1) — in-place |

---

## 13. Spiral Matrix Traversal

**Problem:** Return all elements of an `m × n` matrix in **spiral order**.

```
Input:  matrix = [[1, 2, 3],
                  [4, 5, 6],
                  [7, 8, 9]]
Output: [1, 2, 3, 6, 9, 8, 7, 4, 5]
```

### Approach: Four Boundary Pointers

Maintain `top`, `bottom`, `left`, `right` boundaries and shrink them layer by layer:

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            // 1. Traverse top row (left to right)
            for (int i = left; i <= right; i++) ans.add(matrix[top][i]);
            top++;

            // 2. Traverse right column (top to bottom)
            for (int i = top; i <= bottom; i++) ans.add(matrix[i][right]);
            right--;

            // Check boundaries before bottom and left traversals
            if (top <= bottom && left <= right) {
                // 3. Traverse bottom row (right to left)
                for (int i = right; i >= left; i--) ans.add(matrix[bottom][i]);
                bottom--;

                // 4. Traverse left column (bottom to top)
                for (int i = bottom; i >= top; i--) ans.add(matrix[i][left]);
                left++;
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(m × n) |
| **Space** | O(1) — excluding output list |

> 💡 **Tip:** The `if (top <= bottom && left <= right)` check prevents re-traversing already visited rows/cols in non-square matrices.

---

## 14. Count Subarrays with Given Sum (Subarray Sum Equals K)

**Problem:** Given an array of integers `nums` and an integer `k`, return the total number of subarrays whose sum equals `k`. (LeetCode 560)

```
Input:  nums = [1, 1, 1], k = 2
Output: 2
```

### 🧠 Core Intuition: Prefix Sum + Frequency Map

$$\text{If running sum is } x, \text{ and a subarray ends at } i \text{ with sum } k \implies \text{Prefix before it must have sum } (x - k)$$

> **Why Two Pointers / Sliding Window Fails:**
> Because `nums` can contain **negative numbers**, the running sum does not change monotonically. Pointers cannot be moved greedily.

### Day 3 vs Day 14 Comparison:

| Feature | Day 3: Longest Subarray Sum K | Day 14: Subarray Sum Equals K |
|---|---|---|
| **Goal** | Maximum **length** | Total **count** of subarrays |
| **Map Value** | First Index (`putIfAbsent`) | Occurrence **Frequency** (`count`) |
| **Action** | `maxLen = max(maxLen, i - map.get(x-k))` | `count += map.get(x - k)` |

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int prefixSum = 0;
        Map<Integer, Integer> map = new HashMap<>();

        // Base case: prefix sum of 0 appears 1 time (empty prefix)
        map.put(0, 1);

        for (int num : nums) {
            prefixSum += num;

            // Check if (prefixSum - k) exists in map
            if (map.containsKey(prefixSum - k)) {
                count += map.get(prefixSum - k);
            }

            // Update frequency of prefixSum in map
            map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
        }

        return count;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) |

> 💡 **Why check before inserting?**
> When $k = 0$, inserting `prefixSum` into the map first would allow it to match itself, falsely counting an empty subarray of length 0. Always check first, then insert.

---

## 📝 Medium Track Summary

| # | Problem | Core Pattern | Time | Space |
|---|---------|--------------|------|-------|
| 1 | Two Sum | HashMap Complement Lookup | O(n) | O(n) |
| 2 | Sort Colors (0s, 1s, 2s) | Dutch National Flag (3 Pointers) | O(n) | O(1) |
| 3 | Majority Element (> n/2) | Boyer-Moore Voting | O(n) | O(1) |
| 4 | Maximum Subarray Sum | Kadane's Algorithm | O(n) | O(1) |
| 5 | Print Max Sum Subarray | Kadane's Extended (Start/End) | O(n) | O(1) |
| 6 | Buy & Sell Stock | Track Running Minimum | O(n) | O(1) |
| 7 | Rearrange by Sign | Index Jumping (+2) | O(n) | O(n) |
| 8 | Next Permutation | Breakpoint → Swap → Suffix Reverse | O(n) | O(1) |
| 9 | Leaders in an Array | Right-to-Left Scan | O(n) | O(n) |
| 10 | Longest Consecutive Sequence | HashSet + Sequence Heads | O(n) | O(n) |
| 11 | Set Matrix Zeroes | In-place Row/Col Markers | O(m×n) | O(1) |
| 12 | Rotate Matrix 90° | Transpose + Reverse Rows | O(n²) | O(1) |
| 13 | Spiral Matrix | 4-Pointer Boundary Shrinking | O(m×n) | O(1) |
| 14 | Count Subarrays with Sum K | Prefix Sum + Frequency HashMap | O(n) | O(n) |
