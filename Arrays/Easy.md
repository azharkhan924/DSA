# 🟢 Arrays (Easy) — DSA Sheet

A comprehensive, organized guide to all **14 Easy Array Problems** from the DSA Sheet, complete with visual intuitions, optimal Java solutions, complexity analyses, and interview tips.

---

## 📑 Table of Contents

1. [Largest Element in an Array](#1-largest-element-in-an-array)
2. [Second Largest Element](#2-second-largest-element)
3. [Check if Array is Sorted and Rotated](#3-check-if-array-is-sorted-and-rotated)
4. [Remove Duplicates from Sorted Array](#4-remove-duplicates-from-sorted-array)
5. [Left Rotate Array by One](#5-left-rotate-array-by-one)
6. [Rotate Array by K Places (Left & Right)](#6-rotate-array-by-k-places-left--right)
7. [Move Zeros to End](#7-move-zeros-to-end)
8. [Linear Search](#8-linear-search)
9. [Union of Two Sorted Arrays](#9-union-of-two-sorted-arrays)
10. [Find Missing Number](#10-find-missing-number)
11. [Maximum Consecutive Ones](#11-maximum-consecutive-ones)
12. [Single Number (Appears Once, Others Twice)](#12-single-number-appears-once-others-twice)
13. [Longest Subarray with Given Sum K (Positives Only)](#13-longest-subarray-with-given-sum-k-positives-only)
14. [Longest Subarray with Sum K (Positives & Negatives)](#14-longest-subarray-with-sum-k-positives--negatives)

---

## 1. Largest Element in an Array

**Problem:** Given an array `nums`, find the largest element in the array.

```
Input:  nums = [2, 5, 1, 3, 0]
Output: 5
```

### Approach: Linear Scan (Optimal ✨)

Traverse the array once, maintaining a variable `max` initialized to the first element.

```java
class Solution {
    public int largest(int[] nums) {
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
        }
        return max;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — single pass |
| **Space** | O(1) |

> 💡 **Tip:** Brute force by sorting (`Arrays.sort(nums)`) takes O(n log n) time. A single linear scan is optimal at O(n).

---

## 2. Second Largest Element

**Problem:** Find the second largest distinct element in an array. Return `-1` if it does not exist.

```
Input:  nums = [12, 35, 1, 10, 34, 1]
Output: 34
```

### Approach: Single Pass (Two Variables)

Track the largest (`m1`) and second largest (`m2`) simultaneously in a single iteration:
- If `nums[i] > m1`: update `m2 = m1`, then `m1 = nums[i]`
- Else if `nums[i] < m1` and `nums[i] > m2`: update `m2 = nums[i]`

```java
class Solution {
    public int secondLargestElement(int[] nums) {
        int m1 = nums[0];
        int m2 = -1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > m1) {
                m2 = m1;
                m1 = nums[i];
            } else if (nums[i] < m1 && nums[i] > m2) {
                m2 = nums[i];
            }
        }
        return m2;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — single pass |
| **Space** | O(1) |

> 💡 **Extra Tip:** Initializing `m2 = -1` assumes non-negative integers. For negative numbers, initialize with `Integer.MIN_VALUE` and verify that a valid second largest exists before returning.

---

## 3. Check if Array is Sorted and Rotated

**Problem:** Return `true` if the array was originally sorted in non-decreasing order, then rotated some number of positions (including 0 positions).

```
Input:  nums = [3, 4, 5, 1, 2]
Output: true (originally [1, 2, 3, 4, 5], rotated 3 positions)
```

### Approach: Count Circular Decreases (Breaks)

A sorted and rotated array can have **at most 1 break** where `nums[i] > nums[(i + 1) % n]`.
- Wrapping around with `(i + 1) % n` compares the last element with the first element circularly.
- If breaks `count <= 1`, the array is valid; if `count > 1`, return `false`.

```
nums = [3, 4, 5, 1, 2]
nums[0]=3 > nums[1]=4 ? No
nums[1]=4 > nums[2]=5 ? No
nums[2]=5 > nums[3]=1 ? Yes ✅  → c = 1
nums[3]=1 > nums[4]=2 ? No
nums[4]=2 > nums[0]=3 ? No       (circular check)
c = 1 ≤ 1 → true ✅
```

```java
class Solution {
    public boolean check(int[] nums) {
        int c = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] > nums[(i + 1) % n]) c++;
        }
        return c <= 1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

---

## 4. Remove Duplicates from Sorted Array

**Problem:** Remove duplicates **in-place** from a sorted array such that each unique element appears once. Return the count of unique elements.

```
Input:  nums = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4]
Output: 5, nums = [0, 1, 2, 3, 4, _, _, _, _, _]
```

### Approach: Two Pointers (Slow & Fast)

Use `j` (slow pointer) to track the index of the last placed unique element:
- `i` (fast pointer) scans through the array from `1` to `n-1`.
- When `nums[i] != nums[j]`, increment `j` and place `nums[i]` at `nums[j]`.

```java
class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int j = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[j]) {
                nums[++j] = nums[i];
            }
        }
        return j + 1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) — in-place |

> 💡 **Follow-up (LeetCode 80):** If at most **2 duplicates** are allowed, change condition to `nums[i] != nums[j - 1]` starting from `j = 2`.

---

## 5. Left Rotate Array by One

**Problem:** Given an array `nums`, rotate the array to the left by one position.

```
Input:  nums = [1, 2, 3, 4, 5]
Output: [2, 3, 4, 5, 1]
```

### Approach: Store First & Shift

1. Store the first element `temp = nums[0]`.
2. Shift all elements from index `1` to `n-1` one position left: `nums[i - 1] = nums[i]`.
3. Place `temp` at the end: `nums[n - 1] = temp`.

```java
class Solution {
    public void rotateByOne(int[] nums) {
        int n = nums.length;
        if (n <= 1) return;
        int temp = nums[0];
        for (int i = 1; i < n; i++) {
            nums[i - 1] = nums[i];
        }
        nums[n - 1] = temp;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

---

## 6. Rotate Array by K Places (Left & Right)

**Problem:** Rotate the array by `k` steps.
- **Right Rotation (LeetCode 189):** Elements shift to the right, tail wraps to head.
- **Left Rotation (Standard / GFG):** Elements shift to the left, head wraps to tail.

### Approach: Reversal Algorithm (Three Reverses ✨)

Always reduce `k = k % n` first.

#### Case A: Right Rotate by K
1. Reverse first `n - k` elements: `[0 ... n-k-1]`
2. Reverse last `k` elements: `[n-k ... n-1]`
3. Reverse the entire array: `[0 ... n-1]`

```
Original:       [1, 2, 3, 4, 5, 6, 7], k = 3
Step 1 (0→3):   [4, 3, 2, 1, 5, 6, 7]
Step 2 (4→6):   [4, 3, 2, 1, 7, 6, 5]
Step 3 (0→6):   [5, 6, 7, 1, 2, 3, 4] ✅
```

```java
class Solution {
    private void reverse(int[] nums, int i, int j) {
        while (i < j) {
            int t = nums[i];
            nums[i] = nums[j];
            nums[j] = t;
            i++;
            j--;
        }
    }

    public void rotateRight(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        reverse(nums, 0, n - k - 1);
        reverse(nums, n - k, n - 1);
        reverse(nums, 0, n - 1);
    }

    public void rotateLeft(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        reverse(nums, 0, k - 1);
        reverse(nums, k, n - 1);
        reverse(nums, 0, n - 1);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) — in-place |

---

## 7. Move Zeros to End

**Problem:** Move all `0`s to the end while maintaining the relative order of non-zero elements in-place.

```
Input:  nums = [0, 1, 0, 3, 12]
Output: [1, 3, 12, 0, 0]
```

### Approach: Partition / Two Pointers (Snowball ✨)

Pointer `s` marks the "write head" where the next non-zero should be placed.
Pointer `e` scans through the array. Whenever `nums[e] != 0`, swap `nums[s]` with `nums[e]` and increment `s`.

```java
class Solution {
    public void moveZeroes(int[] nums) {
        int s = 0;
        for (int e = 0; e < nums.length; e++) {
            if (nums[e] != 0) {
                int temp = nums[e];
                nums[e] = nums[s];
                nums[s] = temp;
                s++;
            }
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

---

## 8. Linear Search

**Problem:** Given an array `nums` and an integer `k`, return the index of the first occurrence of `k`, or `-1` if not present.

```
Input:  nums = [1, 2, 3, 4, 5], k = 3
Output: 2
```

### Approach: Direct Iteration

```java
class Solution {
    public int searchInSorted(int nums[], int k) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == k) return i;
        }
        return -1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

---

## 9. Union of Two Sorted Arrays

**Problem:** Return all distinct elements present in either of two sorted arrays, in sorted order.

```
Input:  nums1 = [1, 2, 3, 4, 5], nums2 = [1, 2, 7]
Output: [1, 2, 3, 4, 5, 7]
```

### Approach: Two Pointers (Merge Step without Extra Set ✨)

Compare elements at pointers `i` and `j`. Before adding an element into `ans`, verify it does not match `ans.get(ans.size() - 1)` to prevent duplicates.

```java
import java.util.ArrayList;

class Solution {
    public static ArrayList<Integer> findUnion(int a[], int b[]) {
        int i = 0, j = 0;
        ArrayList<Integer> ans = new ArrayList<>();

        while (i < a.length && j < b.length) {
            int val;
            if (a[i] < b[j]) val = a[i++];
            else if (a[i] > b[j]) val = b[j++];
            else {
                val = a[i];
                i++;
                j++;
            }

            if (ans.isEmpty() || ans.get(ans.size() - 1) != val) {
                ans.add(val);
            }
        }

        while (i < a.length) {
            if (ans.isEmpty() || ans.get(ans.size() - 1) != a[i]) ans.add(a[i]);
            i++;
        }

        while (j < b.length) {
            if (ans.isEmpty() || ans.get(ans.size() - 1) != b[j]) ans.add(b[j]);
            j++;
        }

        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(m + n) |
| **Space** | O(m + n) — for the output list |

---

## 10. Find Missing Number

**Problem:** Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return the only number missing from the range.

```
Input:  nums = [3, 0, 1]  (n = 3, range [0, 3])
Output: 2
```

### Approach 1: Sum Formula
$$\text{Expected Sum} = \frac{n \times (n + 1)}{2}$$
$$\text{Missing} = \text{Expected Sum} - \sum \text{nums}[i]$$

### Approach 2: XOR Trick (Optimal ✨ — Avoids Integer Overflow)
XOR all numbers in range `[0, n]` and XOR all elements in `nums`. Since $x \oplus x = 0$ and $x \oplus 0 = x$, duplicates cancel out, leaving only the missing number.

```java
class Solution {
    public int missingNumber(int[] nums) {
        int n = nums.length;
        int xor = 0;

        // XOR all numbers from 0 to n
        for (int i = 0; i <= n; i++) {
            xor ^= i;
        }

        // XOR all array elements
        for (int num : nums) {
            xor ^= num;
        }

        return xor;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

---

## 11. Maximum Consecutive Ones

**Problem:** Given a binary array `nums`, return the maximum number of consecutive `1`s in the array.

```
Input:  nums = [1, 1, 0, 1, 1, 1]
Output: 3
```

### Approach: Running Counter

Maintain a running `count`:
- Increment when `nums[i] == 1` and update `maxCount = max(maxCount, count)`.
- Reset `count = 0` when `nums[i] == 0`.

```java
class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int count = 0;
        int maxCount = 0;

        for (int num : nums) {
            if (num == 1) {
                count++;
                maxCount = Math.max(maxCount, count);
            } else {
                count = 0;
            }
        }
        return maxCount;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

---

## 12. Single Number (Appears Once, Others Twice)

**Problem:** Given a non-empty array of integers where every element appears twice except for one, find that single one.

```
Input:  nums = [4, 1, 2, 1, 2]
Output: 4
```

### Approach: Bitwise XOR

Properties of XOR:
1. $a \oplus a = 0$ (identical numbers cancel)
2. $a \oplus 0 = a$
3. XOR is associative and commutative

```java
class Solution {
    public int singleNumber(int[] nums) {
        int ans = 0;
        for (int x : nums) {
            ans ^= x;
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

> 💡 **Follow-ups:**
> - *Single Number II* (every element appears 3 times, one appears once) → use bit-level counting.
> - *Single Number III* (two unique elements appear once) → XOR all, partition into two groups using the rightmost set bit.

---

## 13. Longest Subarray with Given Sum K (Positives Only)

**Problem:** Given an array containing only **positive integers** and an integer `k`, find the length of the longest subarray with sum equal to `k`.

```
Input:  nums = [1, 2, 3, 1, 1, 1, 1], k = 3
Output: 3 (subarray [1, 1, 1])
```

### Approach: Sliding Window / Two Pointers (Optimal ✨)

Because all numbers are positive, the sum is **monotonic**:
- Expanding right pointer `r` **strictly increases** the sum.
- Shrinking left pointer `l` **strictly decreases** the sum.

```java
class Solution {
    public int longestSubarray(int[] arr, int k) {
        int l = 0, sum = 0, maxLen = 0;

        for (int r = 0; r < arr.length; r++) {
            sum += arr[r];

            // Shrink window if sum exceeds k
            while (l <= r && sum > k) {
                sum -= arr[l++];
            }

            if (sum == k) {
                maxLen = Math.max(maxLen, r - l + 1);
            }
        }
        return maxLen;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — each element is added and removed at most once |
| **Space** | O(1) |

> ⚠️ **Important:** This sliding window approach **fails** if the array contains zeros or negative numbers because monotonicity breaks. For general arrays, use the HashMap approach below.

---

## 14. Longest Subarray with Sum K (Positives & Negatives)

**Problem:** Given an array containing positives, negatives, and zeros, find the length of the longest subarray with sum equal to `k`.

```
Input:  nums = [10, 5, 2, 7, 1, -10], k = 15
Output: 6 (entire array: 10 + 5 + 2 + 7 + 1 - 10 = 15)
```

### Approach: Prefix Sum + First-Occurrence HashMap

#### Key Mathematical Insight:
$$\text{If } \text{prefixSum}[i] - \text{prefixSum}[j] = k \implies \text{Sum of subarray } (j, i] = k$$
$$\implies \text{Required previous sum} = \text{prefixSum}[i] - k$$

To maximize length $(i - j)$, we want $j$ to be as small (earliest) as possible. Therefore, store only the **first occurrence** of each prefix sum (`putIfAbsent`).

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int longestSubarray(int[] arr, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        int sum = 0, maxLen = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];

            // Subarray starting from index 0
            if (sum == k) {
                maxLen = i + 1;
            }

            // Subarray starting after map.get(sum - k)
            if (map.containsKey(sum - k)) {
                maxLen = Math.max(maxLen, i - map.get(sum - k));
            }

            // Only store the FIRST occurrence to maximize length
            map.putIfAbsent(sum, i);
        }
        return maxLen;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) — for hash map |

> 💡 **Why `putIfAbsent`?**
> Overwriting the index would give a later index, resulting in a **shorter** subarray. Keeping the earliest index guarantees the **longest** subarray.

---

## 📝 Easy Track Summary

| # | Problem | Core Pattern | Time | Space |
|---|---------|--------------|------|-------|
| 1 | Largest Element | Linear Scan | O(n) | O(1) |
| 2 | Second Largest Element | Single Pass (2 Trackers) | O(n) | O(1) |
| 3 | Check Sorted & Rotated | Count Circular Breaks (`% n`) | O(n) | O(1) |
| 4 | Remove Duplicates | Two Pointers (Slow & Fast) | O(n) | O(1) |
| 5 | Left Rotate by One | Shift & Wrap First Element | O(n) | O(1) |
| 6 | Rotate Array by K | Reversal Algorithm (3 Reverses) | O(n) | O(1) |
| 7 | Move Zeros to End | Partition / Snowball Pointer | O(n) | O(1) |
| 8 | Linear Search | Sequential Scan | O(n) | O(1) |
| 9 | Union of Sorted Arrays | Merge Two Pointers | O(m+n) | O(m+n) |
| 10 | Find Missing Number | XOR Cancellation / Sum Formula | O(n) | O(1) |
| 11 | Max Consecutive Ones | Running Counter | O(n) | O(1) |
| 12 | Single Number | Bitwise XOR | O(n) | O(1) |
| 13 | Longest Subarray Sum K (Positives) | Sliding Window (Two Pointers) | O(n) | O(1) |
| 14 | Longest Subarray Sum K (General) | Prefix Sum + First-Occurrence Map | O(n) | O(n) |
