# Day 4 — Arrays: Hashing, Sorting & Classic Algorithms

---

## 1. Two Sum

**Problem:** Find two indices whose elements add up to `target`.

```
Input:  nums = [2, 7, 11, 15], target = 9
Output: [0, 1]
Explanation: nums[0] + nums[1] = 2 + 7 = 9
```

**Approach:** Use a HashMap to store `(target - nums[i]) → i`. For each element, check if it already exists as a key (meaning its complement was seen before).

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> m = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (m.containsKey(nums[i]))
                return new int[]{m.get(nums[i]), i};
            else {
                m.put(target - nums[i], i);
            }
        }
        return new int[]{-1};
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) |

> 💡 **Extra Tips:**
> - This is arguably the most famous LeetCode problem — **nail it in interviews**.
> - If the array is **sorted**, use **Two Pointers** instead (O(1) space): start from both ends, shrink based on sum comparison.
> - **Variant:** If asked to return the values (not indices), and duplicates exist, be careful with overwriting in the map.

---

## 2. Sort Colors (Dutch National Flag Problem)

**Problem:** Sort an array containing only `0`, `1`, and `2` — in-place, single pass.

```
Input:  nums = [2, 0, 2, 1, 1, 0]
Output: [0, 0, 1, 1, 2, 2]
```

### Approach 1: Counting

Count occurrences of each value, then overwrite.

```java
class Solution {
    public void sortColors(int[] nums) {
        int count0 = 0, count1 = 0, count2 = 0;
        for (int i : nums) {
            if (i == 0) count0++;
            else if (i == 1) count1++;
            else if (i == 2) count2++;
        }
        for (int i = 0; i < nums.length; i++) {
            if (i < count0) nums[i] = 0;
            else if (i < count0 + count1) nums[i] = 1;
            else nums[i] = 2;
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — two passes |
| **Space** | O(1) |

### Approach 2: Dutch National Flag Algorithm (Single Pass ✨)

Use **three pointers**: `low`, `mid`, `high`

**Invariant:**
- `[0 ... low-1]` → all 0s
- `[low ... mid-1]` → all 1s
- `[mid ... high]` → unsorted / unknown
- `[high+1 ... n-1]` → all 2s

**Rules:**
| `nums[mid]` | Action |
|-------------|--------|
| `0` | Swap `nums[low] ↔ nums[mid]`, increment both `low++`, `mid++` |
| `1` | Just `mid++` |
| `2` | Swap `nums[mid] ↔ nums[high]`, decrement `high--` (don't move `mid`!) |

```java
class Solution {
    private void swap(int nums[], int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    public void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (high >= mid) {
            if (nums[mid] == 0) swap(nums, low++, mid++);
            else if (nums[mid] == 1) mid++;
            else swap(nums, mid, high--);
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — **single pass** |
| **Space** | O(1) |

> 💡 **Extra Tips:**
> - **Why don't we increment `mid` when swapping with `high`?** Because the swapped element from `high` is unknown — it could be 0, 1, or 2. We need to check it again.
> - **Why is it safe to increment `mid` when swapping with `low`?** Because `low ≤ mid`, so the element at `low` has already been processed — it's either 0 or 1.
> - This algorithm is named after the **Dutch flag** which has three horizontal bands of color.

---

## 3. Majority Element

**Problem:** Find the element that appears more than `⌊n/2⌋` times.

### Approach 1: HashMap Frequency Count

```java
class Solution {
    public int majorityElement(int[] nums) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i : nums) {
            m.put(i, m.getOrDefault(i, 0) + 1);
            if (m.get(i) > nums.length / 2) return i;
        }
        return -1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) |

### Approach 2: Boyer-Moore Voting Algorithm (Optimal ✨)

**Intuition:** The majority element appears **more than all other elements combined**. So if we "cancel out" one majority vote with one minority vote, the majority element will **always survive**.

**How it works:**
1. Maintain a `candidate` and a `count`
2. If `count == 0`, pick the current element as the new candidate
3. If current == candidate → `count++`
4. Otherwise → `count--` (cancellation)

```java
class Solution {
    public int majorityElement(int[] nums) {
        int element = nums[0], count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (count == 0) element = nums[i];
            if (element == nums[i]) count++;
            else count--;
        }
        return element;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) ✨ |

> 💡 **Extra Tips:**
> - If majority element is **not guaranteed** to exist, you need a **second pass** to verify that the candidate actually has count > n/2.
> - **Follow-up:** *Majority Element II* (LeetCode 229) — find all elements appearing more than `⌊n/3⌋` times. You need **two candidates** (at most 2 such elements can exist).

---

## 4. Maximum Subarray (Kadane's Algorithm)

**Problem:** Find the contiguous subarray with the largest sum.

```
Input:  nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
Output: 6
Explanation: The subarray [4, -1, 2, 1] has the largest sum = 6
```

**Approach — Kadane's Algorithm:**
- Keep a running `sum`
- Track `maxsum` seen so far
- If `sum` drops below 0, reset to 0 (starting a new subarray is better than carrying negative sum)

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int sum = 0, maxsum = Integer.MIN_VALUE;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            maxsum = Math.max(maxsum, sum);
            if (sum <= 0) sum = 0;
        }
        return maxsum;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

> 💡 **Extra Tips:**
> - **Why reset to 0?** A negative prefix sum will only drag down any future subarray. It's better to start fresh.
> - **Important:** Update `maxsum` **before** resetting `sum`. This handles the case where all elements are negative.
> - **Follow-ups:**
>   - Print the actual subarray → see Day 5
>   - Circular subarray → use Kadane's twice (max subarray + min subarray, answer = max(kadane, totalSum - minSubarray))
> - **Alternative perspective:** This is a special case of **dynamic programming** where `dp[i] = max(nums[i], dp[i-1] + nums[i])`.

---

## 📝 Day 4 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Two Sum | HashMap (Complement Lookup) | O(n) | O(n) |
| 2 | Sort Colors | Dutch National Flag (3-way Partition) | O(n) | O(1) |
| 3 | Majority Element | Boyer-Moore Voting | O(n) | O(1) |
| 4 | Maximum Subarray | Kadane's Algorithm | O(n) | O(1) |
