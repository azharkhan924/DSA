# Day 14 — Arrays (Hard): Inversions, Reverse Pairs, Missing/Repeating & Max Product

---

## 📑 Table of Contents

1. [Find Missing and Repeating Number](#1-find-missing-and-repeating-number)
   - [Problem Statement](#problem-statement)
   - [Approach 1: Brute Force (Nested Loop / Frequency Counting)](#approach-1-brute-force-nested-loop--frequency-counting)
   - [Approach 2: Better (Frequency Array / HashMap)](#approach-2-better-frequency-array--hashmap)
   - [Approach 3: Optimal (Mathematical Equations with Sum & Squares)](#approach-3-optimal-mathematical-equations-with-sum--squares)
   - [Approach 4: Optimal (Bit Manipulation / XOR Buckets)](#approach-4-optimal-bit-manipulation--xor-buckets)
   - [Complexity Analysis](#complexity-analysis)
2. [Count Inversions](#2-count-inversions)
   - [Problem Statement & Definition](#problem-statement--definition)
   - [Approach 1: Brute Force ($O(n^2)$)](#approach-1-brute-force-on2)
   - [Approach 2: Optimal (Divide & Conquer via Modified Merge Sort)](#approach-2-optimal-divide--conquer-via-modified-merge-sort)
   - [Visual Walkthrough & Step-by-Step Counting](#visual-walkthrough--step-by-step-counting)
   - [Java Implementation](#java-implementation)
   - [Complexity Analysis](#complexity-analysis-1)
3. [Reverse Pairs (LeetCode 493)](#3-reverse-pairs-leetcode-493)
   - [Problem Statement & Condition Difference ($nums[i] > 2 \cdot nums[j]$)](#problem-statement--condition-difference-numsi--2-cdot-numsj)
   - [⚠️ Why Counting During Standard Merge Fails](#-why-counting-during-standard-merge-fails)
   - [Approach: Two-Pointer Pass Before Merge](#approach-two-pointer-pass-before-merge)
   - [Java Implementation (with Integer Overflow Protection)](#java-implementation-with-integer-overflow-protection)
   - [Complexity Analysis](#complexity-analysis-2)
4. [Maximum Product Subarray (LeetCode 152)](#4-maximum-product-subarray-leetcode-152)
   - [Problem Statement](#problem-statement-1)
   - [Key Observations: Negatives, Positives, and Zeroes](#key-observations-negatives-positives-and-zeroes)
   - [Approach 1: Prefix and Suffix Product Scan (Kadane-like Intuition)](#approach-1-prefix-and-suffix-product-scan-kadane-like-intuition)
   - [Approach 2: Dynamic Programming (Maintaining Min & Max Products)](#approach-2-dynamic-programming-maintaining-min--max-products)
   - [Java Implementation](#java-implementation-1)
   - [Complexity Analysis](#complexity-analysis-3)
5. [📊 Comparison & Interview Decision Matrix](#5--comparison--interview-decision-matrix)

---

## 1. Find Missing and Repeating Number

### Problem Statement
Given an integer array `nums` of size $n$ containing values from $[1, n]$. Each value appears exactly once, except for:
- One number $A$ which appears **twice** (Repeating number).
- One number $B$ which is **missing**.

Return $[A, B]$ as an array where $A$ is at index 0 and $B$ is at index 1.

```
Input:  nums = [3, 5, 4, 1, 1], n = 5
Output: [1, 2]
Explanation: 1 appears twice (Repeating), 2 is missing from range [1, 5].
```

---

### Approach 1: Brute Force (Nested Loop / Frequency Counting)
Check the count of each number from $1$ to $n$:
```java
class Solution {
    public int[] findMissingAndRepeating(int[] nums) {
        int n = nums.length;
        int repeating = -1, missing = -1;

        for (int i = 1; i <= n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (nums[j] == i) count++;
            }
            if (count == 2) repeating = i;
            else if (count == 0) missing = i;
        }

        return new int[]{repeating, missing};
    }
}
```
- **Time Complexity:** $O(n^2)$
- **Space Complexity:** $O(1)$

---

### Approach 2: Better (Frequency Array / HashMap)
Use a count array of size $n + 1$:
- **Time Complexity:** $O(n)$
- **Space Complexity:** $O(n)$ auxiliary array

---

### Approach 3: Optimal (Mathematical Equations with Sum & Squares)

#### Mathematical Derivation:
Let:
- $S_n = \sum_{i=1}^n i = \frac{n(n+1)}{2}$
- $S_{2n} = \sum_{i=1}^n i^2 = \frac{n(n+1)(2n+1)}{6}$
- $S = \sum \text{nums}[i]$
- $S_2 = \sum \text{nums}[i]^2$

Let $R$ be the repeating number and $M$ be the missing number:
$$\text{Equation 1: } S - S_n = R - M$$
$$\text{Equation 2: } S_2 - S_{2n} = R^2 - M^2 = (R - M)(R + M)$$

Substitute $(R - M)$ from Equation 1 into Equation 2:
$$R + M = \frac{S_2 - S_{2n}}{R - M}$$

Adding the two equations:
$$2R = (R - M) + (R + M) \implies R = \frac{(R - M) + (R + M)}{2}$$
$$M = (R + M) - R$$

#### Java Implementation (Using `long` to Prevent Overflow)
```java
class Solution {
    public int[] findMissingAndRepeating(int[] nums) {
        long n = nums.length;

        // Expected sums
        long sn = (n * (n + 1)) / 2;
        long s2n = (n * (n + 1) * (2 * n + 1)) / 6;

        // Actual sums
        long s = 0, s2 = 0;
        for (int num : nums) {
            s += (long) num;
            s2 += (long) num * (long) num;
        }

        // diff1 = R - M
        long diff1 = s - sn;

        // diff2 = R^2 - M^2 = (R - M)(R + M)
        long diff2 = s2 - s2n;

        // sum1 = R + M
        long sum1 = diff2 / diff1;

        long repeating = (diff1 + sum1) / 2;
        long missing = sum1 - repeating;

        return new int[]{(int) repeating, (int) missing};
    }
}
```
- **Time Complexity:** $O(n)$ single pass
- **Space Complexity:** $O(1)$ auxiliary space

---

### Approach 4: Optimal (Bit Manipulation / XOR Buckets)
- XOR all array elements with numbers $1$ to $n$:
  $$\text{XOR} = R \oplus M$$
- Find the rightmost set bit: `diff_bit = XOR & (-XOR)`
- Split elements and $1..n$ into two buckets based on whether that bit is set.
- One bucket XORs down to $R$, the other to $M$.
- **Time:** $O(n)$, **Space:** $O(1)$, avoids any large integer math.

---

## 2. Count Inversions

### Problem Statement & Definition
Given an array of integers `nums`, count the total number of **inversions**.
An inversion is a pair of indices $(i, j)$ such that:
$$i < j \quad \text{and} \quad \text{nums}[i] > \text{nums}[j]$$

```
Input: nums = [5, 3, 2, 4, 1]
Output: 8
Inversions: (5,3), (5,2), (5,4), (5,1), (3,2), (3,1), (2,1), (4,1)
```

---

### Approach 1: Brute Force ($O(n^2)$)
Check all pairs with nested loops:
- **Time:** $O(n^2)$, **Space:** $O(1)$

---

### Approach 2: Optimal (Divide & Conquer via Modified Merge Sort)

#### Visual Intuition & The "Bulk Counting" Insight
Suppose we have two sorted subarrays:
- Left: `[2, 3, 5, 6]` ($i$ pointer)
- Right: `[1, 2, 4, 7]` ($j$ pointer)

When comparing `nums[i]` and `nums[j]`:
If `nums[i] > nums[j]` (e.g. $2 > 1$):
Because the left array is **already sorted**, if `nums[i]` is greater than `nums[j]`, then **every subsequent element in the left array from index $i$ to the end of the left subarray is also strictly greater than `nums[j]`!**

$$\text{Inversions contributed by } \text{nums}[j] = (\text{mid} - i + 1)$$

```
Left (sorted):  [2,  3,  5,  6]       Right (sorted): [1,  2,  4,  7]
                 ▲                                     ▲
                 i (points to 2)                       j (points to 1)

Since 2 > 1:
All elements from index i to end [2, 3, 5, 6] are > 1!
Count += (4 - 0) = 4 inversions in O(1) time!
```

---

### Java Implementation
```java
class Solution {
    public long countInversions(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }

    private long mergeSort(int[] nums, int low, int high) {
        long count = 0;
        if (low >= high) return count;

        int mid = low + (high - low) / 2;
        count += mergeSort(nums, low, mid);
        count += mergeSort(nums, mid + 1, high);
        count += merge(nums, low, mid, high);

        return count;
    }

    private long merge(int[] nums, int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int left = low;
        int right = mid + 1;
        int k = 0;
        long count = 0;

        while (left <= mid && right <= high) {
            if (nums[left] <= nums[right]) {
                temp[k++] = nums[left++];
            } else {
                // All remaining elements in left half form inversions with nums[right]
                count += (mid - left + 1);
                temp[k++] = nums[right++];
            }
        }

        while (left <= mid) temp[k++] = nums[left++];
        while (right <= high) temp[k++] = nums[right++];

        for (int i = 0; i < temp.length; i++) {
            nums[low + i] = temp[i];
        }

        return count;
    }
}
```

### Complexity Analysis
- **Time Complexity:** $O(n \log n)$ — Standard merge sort division tree with linear merge.
- **Space Complexity:** $O(n)$ — Auxiliary array for merging.

---

## 3. Reverse Pairs (LeetCode 493)

### Problem Statement & Condition Difference ($nums[i] > 2 \cdot nums[j]$)
Given an integer array `nums`, return the number of **reverse pairs**.
A reverse pair is a pair $(i, j)$ such that:
$$0 \le i < j < n \quad \text{and} \quad \text{nums}[i] > 2 \cdot \text{nums}[j]$$

```
Input: nums = [1, 3, 2, 3, 1]
Output: 2
Explanation: (1, 4) -> nums[1] = 3 > 2 * nums[4] = 2
             (3, 4) -> nums[3] = 3 > 2 * nums[4] = 2
```

---

### ⚠️ Why Counting During Standard Merge Fails
In Count Inversions, the condition was `nums[i] > nums[j]`. That condition directly aligned with the merging step (`nums[left] <= nums[right]`).
In Reverse Pairs, the condition is `nums[i] > 2 * nums[j]`.
If we try to count pairs during the merge, advancing the pointers based on the $2 \times$ condition breaks the sorting merge, causing miscounts.

> [!IMPORTANT]
> **Separation of Concerns:**
> 1. First, **count reverse pairs** between the two sorted halves using a dedicated two-pointer scan ($O(n)$).
> 2. Then, perform standard merge sort to keep the combined array sorted ($O(n)$).

---

### Java Implementation (with Integer Overflow Protection)
```java
class Solution {
    public int reversePairs(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }

    private int mergeSort(int[] nums, int low, int high) {
        if (low >= high) return 0;

        int mid = low + (high - low) / 2;
        int count = 0;
        count += mergeSort(nums, low, mid);
        count += mergeSort(nums, mid + 1, high);
        count += countPairs(nums, low, mid, high);
        merge(nums, low, mid, high);

        return count;
    }

    // Step 1: Count pairs before merging
    private int countPairs(int[] nums, int low, int mid, int high) {
        int count = 0;
        int right = mid + 1;

        for (int i = low; i <= mid; i++) {
            // Use 2L to prevent 32-bit integer overflow when nums[right] is near Integer.MAX_VALUE
            while (right <= high && (long) nums[i] > 2L * nums[right]) {
                right++;
            }
            count += (right - (mid + 1));
        }

        return count;
    }

    // Step 2: Standard merge
    private void merge(int[] nums, int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int left = low, right = mid + 1, k = 0;

        while (left <= mid && right <= high) {
            if (nums[left] <= nums[right]) {
                temp[k++] = nums[left++];
            } else {
                temp[k++] = nums[right++];
            }
        }

        while (left <= mid) temp[k++] = nums[left++];
        while (right <= high) temp[k++] = nums[right++];

        for (int i = 0; i < temp.length; i++) {
            nums[low + i] = temp[i];
        }
    }
}
```

### Complexity Analysis
- **Time Complexity:** $O(2n \log n) = O(n \log n)$ — Two passes over the halves per merge level.
- **Space Complexity:** $O(n)$ — Auxiliary array for merge.

---

## 4. Maximum Product Subarray (LeetCode 152)

### Problem Statement
Given an integer array `nums`, find a contiguous non-empty subarray that has the largest product, and return that product.

```
Input:  nums = [2, 3, -2, 4]
Output: 6   (subarray [2, 3])

Input:  nums = [-2, 0, -1]
Output: 0   (subarray [0])
```

---

### Key Observations: Negatives, Positives, and Zeroes

1. **All Positive Numbers:** Product keeps growing; maximum is product of the entire array.
2. **Even Count of Negative Numbers:** Negative $\times$ Negative = Positive! The entire product is positive.
3. **Odd Count of Negative Numbers:** One negative number will break the product into negative. Removing either the prefix up to that negative number or the suffix from that negative number leaves an even count of negatives, yielding the maximum product!
4. **Zero Encountered:** A zero resets the contiguous product. It acts as a boundary splitting the array into independent subarrays. Whenever prefix or suffix product becomes 0, reset it to 1!

```
Array: [-2,  3,  4, -1, -2,  1,  5, -3]
Odd negative numbers (3 negatives: -2, -1, -3)

Best product will either be:
- Starting from index 0 ending before the last negative (Prefix Scan)
- Starting after the first negative ending at index n-1 (Suffix Scan)
```

---

### Approach 1: Prefix and Suffix Product Scan (Optimal 1)

```java
class Solution {
    public int maxProduct(int[] nums) {
        int n = nums.length;
        int maxProduct = Integer.MIN_VALUE;
        int prefix = 1;
        int suffix = 1;

        for (int i = 0; i < n; i++) {
            // Reset to 1 if previously 0
            if (prefix == 0) prefix = 1;
            if (suffix == 0) suffix = 1;

            prefix *= nums[i];
            suffix *= nums[n - 1 - i];

            maxProduct = Math.max(maxProduct, Math.max(prefix, suffix));
        }

        return maxProduct;
    }
}
```

---

### Approach 2: Dynamic Programming (Maintaining Min & Max Products)

Because multiplying by a negative number turns a minimum product into a maximum product and vice-versa:
```java
class Solution {
    public int maxProduct(int[] nums) {
        int maxProd = nums[0];
        int minProd = nums[0];
        int result = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int current = nums[i];

            if (current < 0) {
                // Swap max and min when encountering negative
                int temp = maxProd;
                maxProd = minProd;
                minProd = temp;
            }

            maxProd = Math.max(current, maxProd * current);
            minProd = Math.min(current, minProd * current);

            result = Math.max(result, maxProd);
        }

        return result;
    }
}
```

### Complexity Analysis
- **Time Complexity:** $O(n)$ single pass
- **Space Complexity:** $O(1)$ constant auxiliary space

---

## 5. 📊 Comparison & Interview Decision Matrix

| Problem | Core Algorithmic Pattern | Time Complexity | Space Complexity | Critical Gotcha |
| :--- | :--- | :---: | :---: | :--- |
| **Missing & Repeating** | Math ($S, S^2$) / XOR Buckets | $O(n)$ | $O(1)$ | Use `long` to avoid 32-bit integer overflow during $n^2$ calculations |
| **Count Inversions** | Modified Merge Sort | $O(n \log n)$ | $O(n)$ | Add `mid - left + 1` when left element > right element |
| **Reverse Pairs** | Merge Sort with 2-Pointer counting pass | $O(n \log n)$ | $O(n)$ | Must count **before** merge; cast to `2L * nums[right]` for overflow |
| **Max Product Subarray** | Prefix & Suffix Scan / Min-Max DP | $O(n)$ | $O(1)$ | Reset prefix/suffix to 1 after zeroes; odd negatives handled by bidirectional scan |
