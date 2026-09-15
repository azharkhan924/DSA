# Day 12 — Arrays (Hard): 0-Sum, XOR K & Merge Intervals

---

## 📑 Table of Contents

1. [Largest Subarray with 0 Sum](#1-largest-subarray-with-0-sum)
   - [Problem Statement](#problem-statement)
   - [Key Intuition](#key-intuition)
   - [Approach 1: Brute Force (O(n²))](#approach-1-brute-force-on)
   - [Approach 2: Optimal (Prefix Sum + First-Occurrence HashMap) (O(n))](#approach-2-optimal-prefix-sum--first-occurrence-hashmap-on)
   - [Dry Run](#dry-run-example)
   - [Complexity Analysis](#complexity-analysis)
2. [Count Subarrays with Given XOR K](#2-count-subarrays-with-given-xor-k)
   - [Problem Statement](#problem-statement-1)
   - [Mathematical Intuition & XOR Properties](#mathematical-intuition--xor-properties)
   - [Approach 1: Brute Force (O(n³))](#approach-1-brute-force-on-1)
   - [Approach 2: Better (O(n²))](#approach-2-better-on)
   - [Approach 3: Optimal (Prefix XOR + Frequency HashMap) (O(n))](#approach-3-optimal-prefix-xor--frequency-hashmap-on)
   - [Dry Run](#dry-run-example-1)
   - [Complexity Analysis](#complexity-analysis-1)
3. [Merge Overlapping Subintervals (LeetCode 56)](#3-merge-overlapping-subintervals-leetcode-56)
   - [Problem Statement](#problem-statement-2)
   - [Visual Intuition & Overlap Conditions](#visual-intuition--overlap-conditions)
   - [Approach 1: Brute Force (O(n log n + 2n))](#approach-1-brute-force-on-log-n--2n)
   - [Approach 2: Optimal (Single-Pass Merge with List) (O(n log n))](#approach-2-optimal-single-pass-merge-with-list-on-log-n)
   - [Dry Run](#dry-run-example-2)
   - [Complexity Analysis](#complexity-analysis-2)
4. [💡 Deep Dive: Length vs. Count in Prefix Hashing](#-deep-dive-length-vs-count-in-prefix-hashing)

---

## 1. Largest Subarray with 0 Sum

### Problem Statement
Given an array `arr` containing both positive and negative integers, find the **length of the longest subarray** whose elements sum to `0`. If no such subarray exists, return `0`.

```
Input:  arr = [15, -2, 2, -8, 1, 7, 10, 23]
Output: 5
Explanation: The longest subarray with sum 0 is [-2, 2, -8, 1, 7] 
             which spans from index 1 to 5 (length = 5 - 1 + 1 = 5).
```

---

### Key Intuition

Suppose the cumulative prefix sum up to index `i` is $S$, and at some later index `j` ($j > i$), the cumulative prefix sum is **also** $S$:

$$\text{PrefixSum}[j] - \text{PrefixSum}[i] = S - S = 0$$

This guarantees that the subarray between index $i+1$ and $j$ must have a sum of **0**!

```
Index:        0    1    2    3    4    5    6    7
Array:       15   -2    2   -8    1    7   10   23
PrefixSum:   15   13   15    7    8   15   25   48
             ▲         ▲              ▲
             |         |              |
           Seen at 0  Seen at 2    Seen at 5
                      len = 2-0=2  len = 5-0=5  <-- MAX!
```

> ⚠️ **Crucial Rule:** To maximize length ($j - i$), whenever a prefix sum repeats, **DO NOT overwrite** its index in the HashMap. Always retain the **earliest (first) occurrence**.

---

### Approach 1: Brute Force (O(n²))

Check all possible subarrays $[i \dots j]$, maintain their running sum, and update `maxLen` whenever the sum is `0`.

```java
class Solution {
    public int maxLen(int[] arr) {
        int maxLen = 0;
        int n = arr.length;

        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                if (sum == 0) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }
        return maxLen;
    }
}
```

| Complexity | Value |
|---|---|
| **Time** | $\mathcal{O}(n^2)$ |
| **Space** | $\mathcal{O}(1)$ |

---

### Approach 2: Optimal (Prefix Sum + First-Occurrence HashMap) (O(n))

#### Handling Subarrays Starting at Index 0:
There are two equally clean ways to handle the edge case where the prefix sum from index 0 itself is 0:
1. **Method A (`m.put(0, -1)`):** Pre-seed the map with sum `0` at virtual index `-1`. When `prefixSum == 0` at index `i`, length becomes $i - (-1) = i + 1$.
2. **Method B (`if (prefixSum == 0)`):** Directly set `maxLen = i + 1`.

#### Optimal Java Implementation:
```java
import java.util.HashMap;

class Solution {
    public int maxLen(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();
        // Seed base case: a prefix sum of 0 exists at index -1
        map.put(0, -1);

        int prefixSum = 0;
        int maxLength = 0;

        for (int i = 0; i < arr.length; i++) {
            prefixSum += arr[i];

            if (map.containsKey(prefixSum)) {
                // Seen before -> Subarray between previous index and i has sum 0
                maxLength = Math.max(maxLength, i - map.get(prefixSum));
            } else {
                // First time seeing this prefixSum -> record first index
                map.put(prefixSum, i);
            }
        }

        return maxLength;
    }
}
```

---

### Dry Run Example

`arr = [15, -2, 2, -8, 1, 7, 10, 23]`

| `i` | `arr[i]` | `prefixSum` | In Map? | Action | `maxLength` | Map State `(sum -> first_idx)` |
|:---:|:---:|:---:|:---:|:---|:---:|:---|
| — | — | 0 | Yes | Initial seed | 0 | `{(0, -1)}` |
| 0 | 15 | 15 | No | Store `(15, 0)` | 0 | `{(0, -1), (15, 0)}` |
| 1 | -2 | 13 | No | Store `(13, 1)` | 0 | `..., (13, 1)` |
| 2 | 2 | 15 | **Yes** | `len = 2 - 0 = 2` | 2 | *(Keep first idx 0)* |
| 3 | -8 | 7 | No | Store `(7, 3)` | 2 | `..., (7, 3)` |
| 4 | 1 | 8 | No | Store `(8, 4)` | 2 | `..., (8, 4)` |
| 5 | 7 | 15 | **Yes** | `len = 5 - 0 = 5` | **5** | *(Keep first idx 0)* |
| 6 | 10 | 25 | No | Store `(25, 6)` | 5 | `..., (25, 6)` |
| 7 | 23 | 48 | No | Store `(48, 7)` | 5 | `..., (48, 7)` |

**Final Answer:** `5` (Subarray: `[-2, 2, -8, 1, 7]`)

---

### Complexity Analysis

| Measure | Complexity | Explanation |
|---|---|---|
| **Time Complexity** | $\mathcal{O}(n)$ | Single pass with $\mathcal{O}(1)$ average HashMap lookups and insertions. |
| **Space Complexity** | $\mathcal{O}(n)$ | At most $n + 1$ unique prefix sums stored in the HashMap. |

---
---

## 2. Count Subarrays with Given XOR K

### Problem Statement
Given an array of integers `nums` and an integer `k`, return the **total number of subarrays** whose bitwise XOR of elements equals `k`.

```
Input:  nums = [4, 2, 2, 6, 4], k = 6
Output: 4
Explanation: The 4 valid subarrays are:
             1. [4, 2]                 --> 4 ^ 2 = 6
             2. [4, 2, 2, 6, 4]        --> 4 ^ 2 ^ 2 ^ 6 ^ 4 = 6
             3. [2, 2, 6]              --> 2 ^ 2 ^ 6 = 6
             4. [6]                    --> 6
```

---

### Mathematical Intuition & XOR Properties

Recall key properties of Bitwise XOR ($\oplus$):
1. **Self-inverse:** $a \oplus a = 0$
2. **Identity:** $a \oplus 0 = a$
3. **Associative & Commutative:** $(a \oplus b) \oplus c = a \oplus (b \oplus c)$

Suppose the prefix XOR up to index `i` is $\text{XR}$.

```
  [----------------- XR -----------------]
  [------ x ------][--------- k ---------]
  0               p p+1                  i
```

If the subarray from $p + 1$ to $i$ has an XOR of $k$, and the prefix XOR up to index $p$ is $x$, then:

$$x \oplus k = \text{XR}$$

XOR both sides by $k$:

$$x \oplus k \oplus k = \text{XR} \oplus k$$
$$x \oplus 0 = \text{XR} \oplus k$$
$$x = \text{XR} \oplus k$$

> 📌 **Takeaway:** For every element at index `i`, we calculate current $\text{XR}$. We need to find how many previous prefixes had an XOR value of $x = \text{XR} \oplus k$. Each such previous prefix gives us a valid subarray!

---

### Approach 1: Brute Force (O(n³))

Compute the XOR for every possible pair $(i, j)$ using a 3rd loop:

```java
class Solution {
    public int subarraysWithXorK(int[] nums, int k) {
        int count = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int xor = 0;
                for (int m = i; m <= j; m++) {
                    xor ^= nums[m];
                }
                if (xor == k) count++;
            }
        }
        return count;
    }
}
```

| Complexity | Value |
|---|---|
| **Time** | $\mathcal{O}(n^3)$ |
| **Space** | $\mathcal{O}(1)$ |

---

### Approach 2: Better (O(n²))

Accumulate XOR directly in the second loop:

```java
class Solution {
    public int subarraysWithXorK(int[] nums, int k) {
        int count = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int xor = 0;
            for (int j = i; j < n; j++) {
                xor ^= nums[j];
                if (xor == k) count++;
            }
        }
        return count;
    }
}
```

| Complexity | Value |
|---|---|
| **Time** | $\mathcal{O}(n^2)$ |
| **Space** | $\mathcal{O}(1)$ |

---

### Approach 3: Optimal (Prefix XOR + Frequency HashMap) (O(n))

#### Algorithm Steps:
1. Initialize `xor = 0` (running prefix XOR) and `count = 0`.
2. Create a `HashMap<Integer, Integer>` to store the frequency of each prefix XOR.
3. Pre-seed `map.put(0, 1)` to account for prefixes that directly equal `k` from index 0.
4. Iterate through `nums`:
   - `xor ^= nums[i]`
   - Calculate required previous prefix: `target = xor ^ k`
   - If `target` is in `map`, add `map.get(target)` to `count`.
   - Update frequency of current `xor` in `map`.
5. Return `count`.

#### Optimal Java Implementation:
```java
import java.util.HashMap;

class Solution {
    public int subarraysWithXorK(int[] nums, int k) {
        int xor = 0;
        int count = 0;
        HashMap<Integer, Integer> map = new HashMap<>();

        // Base case: prefix XOR 0 has appeared 1 time (empty prefix)
        map.put(0, 1);

        for (int i = 0; i < nums.length; i++) {
            // Update running prefix XOR
            xor ^= nums[i];

            // Required prefix XOR: x = xor ^ k
            int required = xor ^ k;

            // Add all occurrences of 'required' to count
            if (map.containsKey(required)) {
                count += map.get(required);
            }

            // Record / increment frequency of current prefix XOR
            map.put(xor, map.getOrDefault(xor, 0) + 1);
        }

        return count;
    }
}
```

---

### Dry Run Example

`nums = [4, 2, 2, 6, 4]`, `k = 6`

| `i` | `nums[i]` | `xor` | `required = xor ^ 6` | `map.get(required)` | `count` | Map State after step |
|:---:|:---:|:---:|:---:|:---:|:---:|:---|
| — | — | 0 | — | — | 0 | `{0: 1}` |
| 0 | 4 | 4 | $4 \oplus 6 = 2$ | 0 (not found) | 0 | `{0: 1, 4: 1}` |
| 1 | 2 | 6 | $6 \oplus 6 = 0$ | **1** | **1** | `{0: 1, 4: 1, 6: 1}` |
| 2 | 2 | 4 | $4 \oplus 6 = 2$ | 0 (not found) | 1 | `{0: 1, 4: 2, 6: 1}` |
| 3 | 6 | 2 | $2 \oplus 6 = 4$ | **2** | **3** | `{0: 1, 4: 2, 6: 1, 2: 1}` |
| 4 | 4 | 6 | $6 \oplus 6 = 0$ | **1** | **4** | `{0: 1, 4: 2, 6: 2, 2: 1}` |

**Total Count:** `4` ✅

---

### Complexity Analysis

| Measure | Complexity | Explanation |
|---|---|---|
| **Time Complexity** | $\mathcal{O}(n)$ | Single pass over $n$ elements with $\mathcal{O}(1)$ average map operations. |
| **Space Complexity** | $\mathcal{O}(n)$ | At most $n + 1$ unique prefix XOR values stored. |

---
---

## 3. Merge Overlapping Subintervals (LeetCode 56)

### Problem Statement
Given an array of `intervals` where `intervals[i] = [start_i, end_i]`, merge all overlapping intervals, and return an array of the **non-overlapping intervals** that cover all the intervals in the input.

```
Input:  intervals = [[1, 3], [2, 6], [8, 10], [15, 18]]
Output: [[1, 6], [8, 10], [15, 18]]
Explanation: Intervals [1, 3] and [2, 6] overlap into [1, 6].
```

---

### Visual Intuition & Overlap Conditions

If we **sort** intervals by their start times:
```
Interval A: [start_A --------- end_A]
Interval B:            [start_B --------- end_B]
Merged:     [start_A -------------------- max(end_A, end_B)]
```

Because the array is sorted by start time (`start_A <= start_B`):
- **Overlap Condition:** If `start_B <= end_A`, then interval $B$ overlaps with interval $A$.
  - Merged Interval: `[start_A, Math.max(end_A, end_B)]`
- **Non-Overlap Condition:** If `start_B > end_A`, no overlap is possible.
  - Interval $A$ is completely finalized, and interval $B$ begins a new interval group.

---

### Approach 1: Brute Force (O(n log n + 2n))

1. Sort intervals by start time.
2. For each interval $i$:
   - If it was already merged in a previous step (i.e. `intervals[i][1] <= last_merged_end`), skip it.
   - Expand `end` by checking subsequent intervals $j$: while `intervals[j][0] <= end`, expand `end = max(end, intervals[j][1])`.
   - Once an interval doesn't overlap, stop and add `[start, end]`.

```java
import java.util.*;

class Solution {
    public int[][] merge(int[][] intervals) {
        int n = intervals.length;
        if (n <= 1) return intervals;

        // Step 1: Sort by start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> ans = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int start = intervals[i][0];
            int end = intervals[i][1];

            // If already merged into previous interval, skip
            if (!ans.isEmpty() && end <= ans.get(ans.size() - 1)[1]) {
                continue;
            }

            // Check following intervals for overlap
            for (int j = i + 1; j < n; j++) {
                if (intervals[j][0] <= end) {
                    end = Math.max(end, intervals[j][1]);
                } else {
                    break;
                }
            }
            ans.add(new int[]{start, end});
        }

        return ans.toArray(new int[ans.size()][]);
    }
}
```

| Complexity | Value |
|---|---|
| **Time** | $\mathcal{O}(n \log n) + \mathcal{O}(2n)$ |
| **Space** | $\mathcal{O}(n)$ (to store merged intervals) |

---

### Approach 2: Optimal (Single-Pass Merge with List) (O(n log n))

Instead of checking forward with an inner loop, maintain a dynamic result list and merge in a **single pass**:

#### Algorithm:
1. Sort `intervals` by start time ascending.
2. Initialize `List<int[]> res = new ArrayList<>()`.
3. Traverse each interval `curr`:
   - If `res` is empty **OR** `curr[0] > res.get(last)[1]`:
     No overlap $\to$ add `curr` to `res`.
   - Else:
     Overlaps $\to$ update `res.get(last)[1] = Math.max(res.get(last)[1], curr[1])`.
4. Convert `res` to `int[][]` and return.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) return intervals;

        // 1. Sort intervals by start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // 2. Collect merged intervals dynamically
        List<int[]> res = new ArrayList<>();

        for (int[] curr : intervals) {
            // Case 1: First interval OR no overlap with latest merged interval
            if (res.isEmpty() || res.get(res.size() - 1)[1] < curr[0]) {
                res.add(curr);
            } 
            // Case 2: Overlapping -> extend end time of latest interval
            else {
                res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], curr[1]);
            }
        }

        // 3. Convert List<int[]> back to 2D array
        return res.toArray(new int[res.size()][]);
    }
}
```

---

### Dry Run Example

`intervals = [[1, 3], [2, 6], [8, 10], [15, 18]]` (Already sorted by start)

| Step | `curr` | Last in `res` | Overlap Check (`curr[0] <= last[1]`) | Action | `res` State |
|:---:|:---:|:---:|:---:|:---|:---|
| 1 | `[1, 3]` | — | — (List empty) | Add `[1, 3]` | `[[1, 3]]` |
| 2 | `[2, 6]` | `[1, 3]` | $2 \le 3$ (Yes) | Update `last[1] = max(3, 6) = 6` | `[[1, 6]]` |
| 3 | `[8, 10]` | `[1, 6]` | $8 \le 6$ (No) | Add `[8, 10]` | `[[1, 6], [8, 10]]` |
| 4 | `[15, 18]` | `[8, 10]` | $15 \le 10$ (No) | Add `[15, 18]` | `[[1, 6], [8, 10], [15, 18]]` |

**Final Result:** `[[1, 6], [8, 10], [15, 18]]` ✅

---

### Complexity Analysis

| Measure | Complexity | Explanation |
|---|---|---|
| **Time Complexity** | $\mathcal{O}(n \log n)$ | Dominated by sorting. The linear scan takes $\mathcal{O}(n)$. |
| **Space Complexity** | $\mathcal{O}(n)$ | To store the output list of merged intervals. |

---
---

## 4. 💡 Deep Dive: Length vs. Count in Prefix Hashing

Both problems leverage the **Prefix Hashing Pattern**, but have an essential difference:

| Feature | Largest Subarray with Sum 0 | Count Subarrays with XOR K |
|:---|:---|:---|
| **Goal** | **Maximum Length** | **Total Frequency / Count** |
| **HashMap Type** | `Map<PrefixSum, FirstIndex>` | `Map<PrefixXOR, FrequencyCount>` |
| **Base Case Seed** | `map.put(0, -1)` | `map.put(0, 1)` |
| **On Collision** | **Do NOT overwrite!** Keep earliest index to maximize $(i - \text{idx})$. | **Increment frequency!** Every past match contributes to count. |
| **Formula** | $\text{len} = i - \text{map.get}(S)$ | $\text{count} += \text{map.get}(\text{XR} \oplus k)$ |
| **Related Problem** | LeetCode 525 (Contiguous Array) | LeetCode 560 (Subarray Sum Equals K) |
