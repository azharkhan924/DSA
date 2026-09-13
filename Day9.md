# Day 9 — Arrays: Prefix Sum & Hash Map

---

## 1. Subarray Sum Equals K (LeetCode 560)

**Problem:** Given an array of integers `nums` and an integer `k`, return the total number of subarrays whose sum equals to `k`.

A **subarray** is a contiguous non-empty sequence of elements within an array.

```
Example 1:
Input: nums = [1, 1, 1], k = 2
Output: 2
Explanation: Subarrays with sum 2 are nums[0..1] and nums[1..2].

Example 2:
Input: nums = [1, 2, 3], k = 3
Output: 2
Explanation: Subarrays with sum 3 are nums[0..1] and nums[2..2].
```

---

### ⚠️ Why Sliding Window (Two Pointers) Fails Here

A very common question when solving this problem: *“Can we use a two-pointer sliding window?”*

> **No!** Sliding window only works when all numbers are **non-negative** (strictly positive or positive with zeros).
> - When elements are positive, expanding the window **always increases** the sum, and shrinking **always decreases** it (monotonic property).
> - But here, `nums` can contain **negative numbers** (`-1000 <= nums[i] <= 1000`).
> - Adding a negative number *decreases* the sum, and shrinking can *increase* or *decrease* the sum unpredictably.
> - Therefore, we cannot greedily move pointers. We must use **Prefix Sum + HashMap**.

---

### 🧠 The Core Intuition (Prefix Sum)

Imagine you are computing the running prefix sum as you walk from left to right:

```
Index:       0     1     2     3     4
Elements:  [ 1,    2,    3,   -3,    1 ]
PrefixSum:   1     3     6     3     4  (total sum so far = x = 4)
             |                 |
             `--- sum = x-k ---'--- sum = k ---'
```

#### The Formula:
If the total prefix sum up to current index $i$ is **$x$** (i.e. `prefixSum`), and a subarray ending at $i$ has sum **$k$**, then the subarray before it must have had sum:

$$\text{Remaining Prefix} = x - k$$

> **In simple terms (Hinglish):**
> Agar shuru se ab tak ka total sum **$x$** hai, aur hamein ek aisa ending part chahiye jiska sum **$k$** ho, toh iska matlab shuru se lekar us ending part se pehle tak ka sum pakka **$x - k$** hona chahiye!
>
> Agar hume pehle kabhi **$x - k$** sum mila tha, toh us point se lekar abhi tak ka jo part bacha, uska sum guaranteed **$k$** hoga.

---

### 🔍 Difference Between Day 3 vs Day 9

| Feature | Day 3: Longest Subarray with Sum K | Day 9: Subarray Sum Equals K (LC 560) |
| :--- | :--- | :--- |
| **Goal** | Find maximum **length** of subarray | Count **total number** of valid subarrays |
| **HashMap Value** | Stores **First Index** (`sum → earliest index`) | Stores **Frequency** (`sum → count of occurrences`) |
| **Map Update** | `map.putIfAbsent(sum, i)` (don't overwrite!) | `map.put(sum, map.getOrDefault(sum, 0) + 1)` |
| **When target found** | `maxLen = max(maxLen, i - map.get(sum - k))` | `count += map.get(sum - k)` |

---

### Approach 1: Brute Force — Check All Subarrays

Generate all possible subarrays using nested loops and check if their sum equals `k`.

```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += nums[j];
                if (sum == k) {
                    count++;
                }
            }
        }
        return count;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n²) — nested loops check every contiguous pair `(i, j)` |
| **Space** | O(1) — no extra memory used |

*Note: For $n = 2 \times 10^4$, $O(n^2) = 4 \times 10^8$ operations, which results in **Time Limit Exceeded (TLE)** on LeetCode.*

---

### Approach 2: Prefix Sum + Frequency HashMap (Optimal ✨)

#### Step-by-Step Algorithm:
1. Initialize `count = 0` and `prefixSum = 0`.
2. Create a HashMap `prefixSumCount` where:
   - **Key:** Prefix sum value
   - **Value:** Number of times this prefix sum has occurred so far
3. **Important Base Case:** Add `prefixSumCount.put(0, 1)`:
   - A prefix sum of `0` has occurred once before reading any elements (an empty prefix).
   - If `prefixSum == k`, then `prefixSum - k == 0`. Looking up `0` will correctly count this subarray starting from index `0`.
4. Traverse through each element `num` in `nums`:
   - Add `num` to `prefixSum`.
   - Calculate `remove = prefixSum - k`.
   - If `remove` exists in `prefixSumCount`, add its frequency to `count`:
     `count += prefixSumCount.get(remove);`
   - Update the frequency of `prefixSum` in the map:
     `prefixSumCount.put(prefixSum, prefixSumCount.getOrDefault(prefixSum, 0) + 1);`
5. Return `count`.

#### Complete Java Code:

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int prefixSum = 0;
        
        // Map to store: prefixSum -> frequency of that prefixSum
        Map<Integer, Integer> prefixSumCount = new HashMap<>();
        
        // Base case: prefix sum 0 has appeared 1 time (empty subarray)
        prefixSumCount.put(0, 1);
        
        for (int num : nums) {
            prefixSum += num;
            
            // Required previous sum that would leave a subarray of sum k
            int remove = prefixSum - k;
            
            // If (prefixSum - k) was seen before, add all its occurrences to count
            if (prefixSumCount.containsKey(remove)) {
                count += prefixSumCount.get(remove);
            }
            
            // Store / update the frequency of current prefixSum
            prefixSumCount.put(prefixSum, prefixSumCount.getOrDefault(prefixSum, 0) + 1);
        }
        
        return count;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — single pass through the array, with O(1) average hash map lookups |
| **Space** | O(n) — hash map stores up to n distinct prefix sums |

---

### 📊 Step-by-Step Dry Run Example

Let's dry run on: `nums = [1, 2, 3, -3, 1, 1, 1]`, `k = 3`

Initial state: `prefixSum = 0`, `count = 0`, `map = {0: 1}`

| Index `i` | `num` | `prefixSum` | `remove = prefixSum - k` | `remove` in map? | `count` updated | Map after step |
|:---:|:---:|:---:|:---:|:---:|:---:|:---|
| — | — | 0 | — | — | 0 | `{0: 1}` |
| 0 | 1 | 1 | 1 - 3 = -2 | No | 0 | `{0: 1, 1: 1}` |
| 1 | 2 | 3 | 3 - 3 = 0 | Yes (freq = 1) | 0 + 1 = **1** (subarray `[1, 2]`) | `{0: 1, 1: 1, 3: 1}` |
| 2 | 3 | 6 | 6 - 3 = 3 | Yes (freq = 1) | 1 + 1 = **2** (subarray `[3]`) | `{0: 1, 1: 1, 3: 1, 6: 1}` |
| 3 | -3 | 3 | 3 - 3 = 0 | Yes (freq = 1) | 2 + 1 = **3** (subarray `[1, 2, 3, -3]`) | `{0: 1, 1: 1, 3: 2, 6: 1}` |
| 4 | 1 | 4 | 4 - 3 = 1 | Yes (freq = 1) | 3 + 1 = **4** (subarray `[2, 3, -3, 1]`) | `{0: 1, 1: 1, 3: 2, 6: 1, 4: 1}` |
| 5 | 1 | 5 | 5 - 3 = 2 | No | 4 | `{..., 5: 1}` |
| 6 | 1 | 6 | 6 - 3 = 3 | Yes (freq = 2!) | 4 + 2 = **6** (subarrays `[-3,1,1,1]` & `[1,1,1]`) | `{..., 6: 2}` |

Total subarrays found = **6**.

---

### 💡 Extra Tips & Common Pitfalls

> 1. **Why `map.put(0, 1)` is required:**
>    - Suppose `nums = [3]`, `k = 3`.
>    - At index 0, `prefixSum = 3`, `remove = 3 - 3 = 0`.
>    - If `{0: 1}` wasn't in the map, `remove = 0` wouldn't be found, and we'd miss the subarray `[3]`.
>    - Putting `(0, 1)` accounts for valid subarrays that start from index 0!

> 2. **Why check `remove` BEFORE inserting current `prefixSum` into the map:**
>    - If $k = 0$, e.g., `nums = [1]`, `k = 0`.
>    - `prefixSum = 1`. If we inserted `1` into the map first, then `remove = 1 - 0 = 1` would find itself, falsely counting an empty subarray of length 0!
>    - Always check first, then update the map.

> 3. **Common Bug from Rough Notes (Day9.txt):**
>    ```java
>    // ❌ INCORRECT:
>    if (prefixSumCount.containsKey(remove)) {
>        count += prefixSumCount(prefixSum, prefixSumCount.put(...)); // Syntax error & map updated only inside if!
>    }
>    
>    // ✅ CORRECT:
>    if (prefixSumCount.containsKey(remove)) {
>        count += prefixSumCount.get(remove);
>    }
>    prefixSumCount.put(prefixSum, prefixSumCount.getOrDefault(prefixSum, 0) + 1);
>    ```
>    Remember: The map must be updated **every single iteration**, regardless of whether `remove` was found or not.

---

## 📝 Day 9 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Subarray Sum Equals K (LC 560) | Prefix Sum + Frequency HashMap | O(n) | O(n) |
