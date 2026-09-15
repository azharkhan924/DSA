# Day 12 — Arrays (Hard): Largest Subarray with 0 Sum & Subarrays with XOR K

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
3. [💡 Deep Dive: Length vs. Count in Prefix Hashing](#-deep-dive-length-vs-count-in-prefix-hashing)

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

## 💡 Deep Dive: Length vs. Count in Prefix Hashing

Both problems leverage the **Prefix Hashing Pattern**, but have an essential difference:

| Feature | Largest Subarray with Sum 0 | Count Subarrays with XOR K |
|:---|:---|:---|
| **Goal** | **Maximum Length** | **Total Frequency / Count** |
| **HashMap Type** | `Map<PrefixSum, FirstIndex>` | `Map<PrefixXOR, FrequencyCount>` |
| **Base Case Seed** | `map.put(0, -1)` | `map.put(0, 1)` |
| **On Collision** | **Do NOT overwrite!** Keep earliest index to maximize $(i - \text{idx})$. | **Increment frequency!** Every past match contributes to count. |
| **Formula** | $\text{len} = i - \text{map.get}(S)$ | $\text{count} += \text{map.get}(\text{XR} \oplus k)$ |
| **Related Problem** | LeetCode 525 (Contiguous Array) | LeetCode 560 (Subarray Sum Equals K) |
