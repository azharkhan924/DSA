# 📘 Array Algorithms — Complete Reference

A comprehensive guide to every algorithm and technique used across all **40 Array problems** (Easy, Medium, Hard) in the DSA Sheet.

---

## 📑 Table of Contents

| # | Algorithm | Difficulty Used In | Key Problems |
|:---:|:---|:---:|:---|
| 1 | [Linear Scan / Greedy](#1-linear-scan--greedy) | 🟢🟡🔴 | Largest Element, Buy & Sell Stock, Leaders |
| 2 | [Two Pointers](#2-two-pointers) | 🟢🟡🔴 | Remove Duplicates, Move Zeros, 3Sum, 4Sum |
| 3 | [Reversal Technique](#3-reversal-technique) | 🟢 | Rotate Array by K Places |
| 4 | [Hashing / HashMap](#4-hashing--hashmap) | 🟢🟡🔴 | Two Sum, Longest Consecutive Sequence, Missing Number |
| 5 | [Prefix Sum / Prefix XOR](#5-prefix-sum--prefix-xor) | 🟢🟡🔴 | Subarray Sum K, XOR K, Longest Subarray Sum K |
| 6 | [Kadane's Algorithm](#6-kadanes-algorithm) | 🟡 | Maximum Subarray Sum |
| 7 | [Dutch National Flag (3-Way Partition)](#7-dutch-national-flag-3-way-partition) | 🟡 | Sort Colors (0s, 1s, 2s) |
| 8 | [Boyer-Moore Voting](#8-boyer-moore-voting) | 🟡🔴 | Majority Element (n/2), Majority Element II (n/3) |
| 9 | [Next Permutation Algorithm](#9-next-permutation-algorithm) | 🟡 | Next Permutation |
| 10 | [Matrix Manipulation](#10-matrix-manipulation) | 🟡 | Set Matrix Zeroes, Rotate Image, Spiral Traversal |
| 11 | [Sorting + Sweep](#11-sorting--sweep) | 🔴 | Merge Overlapping Intervals |
| 12 | [Gap Method (Shell Sort)](#12-gap-method-shell-sort) | 🔴 | Merge Two Sorted Arrays without Extra Space |
| 13 | [Merge Sort (Modified)](#13-merge-sort-modified) | 🔴 | Count Inversions, Reverse Pairs |
| 14 | [Math (Sum & Sum of Squares)](#14-math-sum--sum-of-squares) | 🟢🔴 | Find Missing Number, Missing & Repeating |
| 15 | [Prefix-Suffix Product](#15-prefix-suffix-product) | 🔴 | Maximum Product Subarray |
| 16 | [XOR Bit Manipulation](#16-xor-bit-manipulation) | 🟢 | Single Number, Missing Number |
| 17 | [Combinatorics (nCr)](#17-combinatorics-ncr) | 🔴 | Pascal's Triangle |

---

## 1. Linear Scan / Greedy

### What It Is
Traverse the array once (or twice), maintaining running variables (max, min, count, etc.) and making locally optimal decisions.

### When to Use It
- Find max/min element
- Track a running state (consecutive ones, leaders from right)
- Problems where a single pass with state tracking suffices

### Template Code
```java
int result = initialValue;
for (int i = 0; i < n; i++) {
    // Update result based on current element
    result = Math.max(result, nums[i]);  // or any logic
}
return result;
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Largest Element | Track `max` in single pass |
| Second Largest Element | Track `max` and `secondMax` |
| Maximum Consecutive Ones | Count consecutive 1s, reset on 0 |
| Leaders in an Array | Scan right-to-left, track `maxFromRight` |
| Best Time to Buy & Sell Stock | Track `minPrice` so far, maximize `price - minPrice` |

---

## 2. Two Pointers

### What It Is
Use two index variables (pointers) that move towards each other or in the same direction, narrowing the search space.

### When to Use It
- **Sorted array + find pair with target sum** → opposite ends
- **In-place removal / partitioning** → same direction (reader-writer pattern)
- **k-Sum problems** → fix outer loops, two pointers on inner

### Template Code (Opposite Direction)
```java
int left = 0, right = n - 1;
while (left < right) {
    int sum = nums[left] + nums[right];
    if (sum == target) { /* found */ }
    else if (sum < target) left++;
    else right--;
}
```

### Template Code (Same Direction — Reader-Writer)
```java
int writer = 0;
for (int reader = 0; reader < n; reader++) {
    if (shouldKeep(nums[reader])) {
        nums[writer++] = nums[reader];
    }
}
// writer = new length
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Remove Duplicates from Sorted Array | Reader-writer: skip duplicates |
| Move Zeros to End | Reader-writer: move non-zeros forward |
| 3 Sum | Fix `i`, two pointers on remaining |
| 4 Sum | Fix `i` and `j`, two pointers on remaining |
| Union of Two Sorted Arrays | Two pointers merge two sorted inputs |
| Merge Two Sorted Arrays | Gap method / backward fill |

---

## 3. Reversal Technique

### What It Is
Reverse portions of an array to achieve rotation or rearrangement without extra space.

### When to Use It
- Rotate array by K positions (left or right)

### Template Code (Rotate Right by K)
```java
// Rotate right by k positions
k = k % n;
reverse(nums, 0, n - 1);     // Reverse entire array
reverse(nums, 0, k - 1);     // Reverse first k elements
reverse(nums, k, n - 1);     // Reverse remaining elements
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Rotate Array by K Places | Three reversals: whole → first k → last n-k |

---

## 4. Hashing / HashMap

### What It Is
Use a HashMap or HashSet for O(1) average-time lookups, enabling single-pass solutions that would otherwise require nested loops.

### When to Use It
- **Find complement / pair** → HashMap stores seen values
- **Frequency counting** → HashMap counts occurrences
- **Detect first occurrence** → HashMap stores first index
- **Check membership** → HashSet for O(1) contains

### Template Code (Complement Lookup)
```java
Map<Integer, Integer> map = new HashMap<>();
for (int i = 0; i < n; i++) {
    int complement = target - nums[i];
    if (map.containsKey(complement)) {
        // Found pair
    }
    map.put(nums[i], i);
}
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Two Sum | HashMap: `value → index`, lookup complement |
| Longest Consecutive Sequence | HashSet for O(1) membership, find sequence starts |
| Find Missing & Repeating | Hash array: frequency counting |
| Longest Subarray Sum K | Prefix sum + first-occurrence HashMap |
| Largest Subarray with 0 Sum | Prefix sum + first-occurrence HashMap |
| Count Subarrays with Given XOR K | Prefix XOR + frequency HashMap |

---

## 5. Prefix Sum / Prefix XOR

### What It Is
Precompute cumulative sums (or XORs) so that any subarray sum can be computed in O(1): `sum(i, j) = prefix[j] - prefix[i-1]`.

### When to Use It
- **Subarray sum equals K** → prefix sum + HashMap
- **Subarray XOR equals K** → prefix XOR + HashMap
- **Longest subarray with sum K** → prefix sum + first-occurrence HashMap

### Template Code (Count Subarrays with Sum K)
```java
Map<Integer, Integer> map = new HashMap<>();
map.put(0, 1); // empty prefix
int prefixSum = 0, count = 0;

for (int num : nums) {
    prefixSum += num;
    int need = prefixSum - k;
    if (map.containsKey(need)) {
        count += map.get(need);
    }
    map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
}
return count;
```

### Key Insight
If `prefixSum[j] - prefixSum[i] = K`, then the subarray `(i, j]` has sum K. We use a HashMap to efficiently find how many previous prefix sums equal `prefixSum[j] - K`.

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Longest Subarray with Sum K (Positives) | Prefix sum + sliding window |
| Longest Subarray with Sum K (Pos & Neg) | Prefix sum + first-occurrence HashMap |
| Largest Subarray with 0 Sum | Prefix sum + first-occurrence HashMap (K=0) |
| Count Subarrays with Sum K | Prefix sum + frequency HashMap |
| Count Subarrays with XOR K | Prefix XOR + frequency HashMap |

---

## 6. Kadane's Algorithm

### What It Is
A dynamic programming approach that finds the maximum subarray sum in O(n) by maintaining the maximum sum ending at each position.

### When to Use It
- **Maximum subarray sum** (contiguous)
- Any problem reducible to "best ending here" optimization

### Template Code
```java
int maxSum = nums[0];
int currentSum = 0;

for (int i = 0; i < n; i++) {
    currentSum += nums[i];
    maxSum = Math.max(maxSum, currentSum);
    if (currentSum < 0) currentSum = 0; // Reset — negative prefix hurts future sums
}
return maxSum;
```

### Extension: Print the Subarray
Track `start` and `end` indices. Update `start` when resetting `currentSum`, update `end` when updating `maxSum`.

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Maximum Subarray Sum | Standard Kadane's |
| Print Subarray with Max Sum | Kadane's + index tracking |

---

## 7. Dutch National Flag (3-Way Partition)

### What It Is
Partition an array into three sections using three pointers (`low`, `mid`, `high`), each section containing one category of elements.

### When to Use It
- Sort array containing only 3 distinct values (0, 1, 2)
- Any 3-way partitioning problem

### Template Code
```java
int low = 0, mid = 0, high = n - 1;

while (mid <= high) {
    if (nums[mid] == 0) {
        swap(nums, low, mid);
        low++; mid++;
    } else if (nums[mid] == 1) {
        mid++;
    } else { // nums[mid] == 2
        swap(nums, mid, high);
        high--;
        // Don't increment mid — swapped element needs checking
    }
}
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Sort Colors (0s, 1s, 2s) | 3-way partition: 0s left, 1s middle, 2s right |

---

## 8. Boyer-Moore Voting

### What It Is
Find the majority element(s) in O(n) time, O(1) space by maintaining candidate(s) and a count that increments/decrements as elements match/differ.

### When to Use It
- **Majority element (> n/2)** → 1 candidate
- **Majority element (> n/3)** → 2 candidates (at most 2 elements can appear > n/3 times)

### Template Code (n/2 Majority)
```java
int candidate = 0, count = 0;
for (int num : nums) {
    if (count == 0) candidate = num;
    count += (num == candidate) ? 1 : -1;
}
// Verify: count occurrences of candidate
```

### Template Code (n/3 Majority — Extended)
```java
int c1 = 0, c2 = 0, cnt1 = 0, cnt2 = 0;
for (int num : nums) {
    if (num == c1) cnt1++;
    else if (num == c2) cnt2++;
    else if (cnt1 == 0) { c1 = num; cnt1 = 1; }
    else if (cnt2 == 0) { c2 = num; cnt2 = 1; }
    else { cnt1--; cnt2--; }
}
// Verify both candidates against threshold n/3
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Majority Element (> n/2) | Single candidate voting |
| Majority Element II (> n/3) | Two candidates voting |

---

## 9. Next Permutation Algorithm

### What It Is
A specific 3-step algorithm to generate the next lexicographically greater permutation of an array in-place.

### Steps
1. **Find the breakpoint**: Scan right-to-left for the first `i` where `nums[i] < nums[i+1]`
2. **Find the swap**: Scan right-to-left for the first `j` where `nums[j] > nums[i]`, then swap
3. **Reverse**: Reverse the suffix `nums[i+1...n-1]` to get the smallest next permutation

### Template Code
```java
// Step 1: Find breakpoint
int bp = -1;
for (int i = n - 2; i >= 0; i--) {
    if (nums[i] < nums[i + 1]) { bp = i; break; }
}
if (bp == -1) { reverse(nums, 0, n - 1); return; }

// Step 2: Find swap partner
for (int j = n - 1; j > bp; j--) {
    if (nums[j] > nums[bp]) { swap(nums, bp, j); break; }
}

// Step 3: Reverse suffix
reverse(nums, bp + 1, n - 1);
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Next Permutation | Direct application |

---

## 10. Matrix Manipulation

### What It Is
Techniques for operating on 2D arrays: transpose, rotation, spiral traversal, and in-place marking.

### Techniques

| Technique | How |
|-----------|-----|
| **Transpose** | Swap `matrix[i][j]` ↔ `matrix[j][i]` for `i < j` |
| **Rotate 90° CW** | Transpose + Reverse each row |
| **Spiral Traversal** | 4 boundary pointers: `top`, `bottom`, `left`, `right` |
| **In-place Marking** | Use first row/column as markers (Set Matrix Zeroes) |

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Set Matrix Zeroes | First row/col as markers → O(1) space |
| Rotate Image (90°) | Transpose + Reverse rows |
| Spiral Matrix | Shrinking boundary traversal |

---

## 11. Sorting + Sweep

### What It Is
Sort the input by a key (e.g., start time), then perform a linear sweep to merge or process adjacent elements.

### When to Use It
- Merge overlapping intervals
- Any problem where sorting reveals adjacency relationships

### Template Code (Merge Intervals)
```java
Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
List<int[]> result = new ArrayList<>();

for (int[] curr : intervals) {
    if (result.isEmpty() || result.get(result.size() - 1)[1] < curr[0]) {
        result.add(curr);
    } else {
        result.get(result.size() - 1)[1] = Math.max(result.get(result.size() - 1)[1], curr[1]);
    }
}
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Merge Overlapping Subintervals | Sort by start, linear merge |

---

## 12. Gap Method (Shell Sort)

### What It Is
Treat two separate sorted arrays as a single virtual array. Compare and swap elements separated by a `gap`, then halve the gap repeatedly until `gap = 1`.

### When to Use It
- Merge two sorted arrays **without extra space**

### Template Code
```java
int len = m + n;
int gap = (len / 2) + (len % 2); // ceil(len / 2)

while (gap > 0) {
    int left = 0, right = left + gap;
    while (right < len) {
        // Get values from virtual array (maps to nums1 or nums2)
        // Swap if out of order
        left++; right++;
    }
    if (gap == 1) break;
    gap = (gap / 2) + (gap % 2);
}
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Merge Two Sorted Arrays without Extra Space | Gap method on virtual concatenated array |

---

## 13. Merge Sort (Modified)

### What It Is
Standard merge sort, but with an additional counting step during the merge phase to count specific pair conditions.

### When to Use It
- **Count inversions** (`nums[i] > nums[j]` where `i < j`)
- **Count reverse pairs** (`nums[i] > 2 * nums[j]` where `i < j`)
- Any problem asking for ordered pair counts that can leverage sorted halves

### ⚠️ Critical Design Decision

| Condition | Same as merge condition? | Where to count? |
|-----------|--------------------------|-----------------|
| `a[i] > b[j]` (inversions) | ✅ Yes | Inside merge function |
| `a[i] > 2*b[j]` (reverse pairs) | ❌ No | **Separate** counting pass BEFORE merge |

### Template Code (Count Inversions — Count Inside Merge)
```java
private long merge(int[] nums, int[] a, int[] b, int m1, int m2) {
    int[] ans = new int[m1 + m2];
    int i = 0, j = 0, k = 0;
    long count = 0;

    while (i < m1 && j < m2) {
        if (a[i] <= b[j]) ans[k++] = a[i++];
        else {
            count += (m1 - i); // All remaining left elements form inversions
            ans[k++] = b[j++];
        }
    }
    // ... copy remaining + copy back to nums
    return count;
}
```

### Template Code (Reverse Pairs — Separate Counting)
```java
// STEP 1: Count pairs (separate two-pointer pass)
count += countPairs(a, b, m1, m2);
// STEP 2: Standard merge (no counting)
merge(nums, a, b, m1, m2);

private int countPairs(int[] a, int[] b, int m1, int m2) {
    int count = 0, right = 0;
    for (int i = 0; i < m1; i++) {
        while (right < m2 && (long) a[i] > 2L * b[right]) right++;
        count += right;
    }
    return count;
}
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Count Inversions | Count during merge (`a[i] > b[j]`) |
| Reverse Pairs | Separate count before merge (`a[i] > 2*b[j]`) |

---

## 14. Math (Sum & Sum of Squares)

### What It Is
Use mathematical formulas for expected sums to deduce missing/repeating numbers without extra space.

### Key Formulas
| Formula | Expression |
|---------|-----------|
| Sum of 1 to n | `n * (n + 1) / 2` |
| Sum of squares of 1 to n | `n * (n + 1) * (2n + 1) / 6` |

### When to Use It
- Find missing number in 1 to n
- Find missing **and** repeating number

### Template Code (Missing & Repeating)
```java
long diff = actualSum - expectedSum;          // R - M
long diffsqr = actualSumSq - expectedSumSq;  // R² - M²
long sumPlus = diffsqr / diff;                // R + M

long R = (diff + sumPlus) / 2;
long M = R - diff;
```

> ⚠️ Always use `long` to prevent integer overflow!

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Find Missing Number | `expectedSum - actualSum = missing` |
| Find Missing & Repeating | Two equations, two unknowns |

---

## 15. Prefix-Suffix Product

### What It Is
Compute products from both ends of the array simultaneously. Reset to 1 when hitting zero.

### When to Use It
- Maximum product subarray (handles negatives and zeros)

### Template Code
```java
int prefix = 1, suffix = 1;
int max = Integer.MIN_VALUE;

for (int i = 0; i < n; i++) {
    prefix *= nums[i];
    suffix *= nums[n - 1 - i];
    max = Math.max(max, Math.max(prefix, suffix));
    if (prefix == 0) prefix = 1;
    if (suffix == 0) suffix = 1;
}
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Maximum Product Subarray | Prefix (L→R) and Suffix (R→L) products |

---

## 16. XOR Bit Manipulation

### What It Is
XOR has unique properties: `a ^ a = 0`, `a ^ 0 = a`. XORing all elements cancels out pairs.

### When to Use It
- Find the single number (all others appear twice)
- Find missing number (XOR with 1 to n)

### Template Code
```java
int xor = 0;
for (int num : nums) xor ^= num;
// xor now holds the unique element (if all others appeared twice)
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Single Number | XOR all → pairs cancel, single remains |
| Find Missing Number | XOR array elements with XOR of 1 to n |

---

## 17. Combinatorics (nCr)

### What It Is
Use the mathematical property that Pascal's Triangle element at row `r`, column `c` is `nCr(r, c) = r! / (c! * (r-c)!)`. Computed efficiently using the row multiplier method.

### When to Use It
- Pascal's Triangle (all 3 variations)
- Computing specific binomial coefficients

### Template Code (Row Multiplier)
```java
// Compute nCr(n, r) efficiently
long result = 1;
for (int i = 0; i < r; i++) {
    result = result * (n - i) / (i + 1);
}
```

### Problems That Use It
| Problem | How It's Used |
|---------|--------------|
| Pascal's Triangle | Generate rows using row multiplier |

---

## 📊 Algorithm Complexity Summary

| Algorithm | Best Time | Space | In-Place? |
|-----------|----------|-------|-----------|
| Linear Scan | O(n) | O(1) | ✅ |
| Two Pointers | O(n) | O(1) | ✅ |
| Reversal | O(n) | O(1) | ✅ |
| HashMap | O(n) | O(n) | ❌ |
| Prefix Sum | O(n) | O(n) | ❌ |
| Kadane's | O(n) | O(1) | ✅ |
| Dutch National Flag | O(n) | O(1) | ✅ |
| Boyer-Moore Voting | O(n) | O(1) | ✅ |
| Sorting + Sweep | O(n log n) | O(n) | ❌ |
| Gap Method | O(n log n) | O(1) | ✅ |
| Modified Merge Sort | O(n log n) | O(n) | ❌ |
| Math (Sum/Squares) | O(n) | O(1) | ✅ |
| Prefix-Suffix Product | O(n) | O(1) | ✅ |
| XOR | O(n) | O(1) | ✅ |
