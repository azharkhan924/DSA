# 🔍 Binary Search — DSA Sheet

A comprehensive, organized guide to **Binary Search** problems from the DSA Sheet, complete with visual intuitions, optimal Java solutions, complexity analyses, and interview tips.

---

## 🧠 Core Concept

Binary Search is a **divide and conquer** algorithm that works on **sorted** data. Instead of checking every element (O(n)), it eliminates half the search space at each step, achieving **O(log n)** time.

### How It Works

```
Search Space: [low ... mid ... high]

1. Calculate mid = low + (high - low) / 2
2. If nums[mid] == target → Found!
3. If nums[mid] < target  → Target is in the RIGHT half → low = mid + 1
4. If nums[mid] > target  → Target is in the LEFT half  → high = mid - 1
5. Repeat until low > high (element not found)
```

### Visual Walkthrough

```
nums = [3, 4, 6, 7, 9, 12, 16, 17],  target = 12

Step 1:  low=0  high=7  mid=3  →  nums[3]=7   < 12  →  low = 4
         [3, 4, 6, 7, 9, 12, 16, 17]
                        ^mid

Step 2:  low=4  high=7  mid=5  →  nums[5]=12  == 12  →  Found at index 5! ✅
         [3, 4, 6, 7, 9, 12, 16, 17]
                           ^mid
```

### Why `low + (high - low) / 2` instead of `(low + high) / 2`?

When `low` and `high` are both large (near `Integer.MAX_VALUE`), their sum overflows. `low + (high - low) / 2` avoids this by computing the difference first.

```
low = 2,000,000,000    high = 2,100,000,000
(low + high) / 2  →  4,100,000,000 / 2  →  INTEGER OVERFLOW! ❌
low + (high - low) / 2  →  2,000,000,000 + 50,000,000  →  2,050,000,000 ✅
```

---

## 📑 Table of Contents

| # | Problem | Status | Core Pattern |
|:---:|:---|:---:|:---|
| 1 | [Binary Search (Iterative)](#1-binary-search-iterative--leetcode-704) | ✅ Complete | Standard BS |
| 2 | [Binary Search (Recursive)](#2-binary-search-recursive) | ✅ Complete | Recursive BS |
| 3 | [Lower Bound](#3-lower-bound) | ✅ Complete | First index ≥ target |
| 4 | [Upper Bound](#4-upper-bound) | ✅ Complete | First index > target |
| 5 | [Search Insert Position](#5-search-insert-position--leetcode-35) | ✅ Complete | Lower Bound variant |
| 6 | [Floor & Ceil in Sorted Array](#6-floor--ceil-in-sorted-array) | ✅ Complete | Modified BS |
| 7 | [First & Last Occurrence](#7-first--last-occurrence--leetcode-34) | ✅ Complete | Lower/Upper Bound |
| 8 | [Count Occurrences in Sorted Array](#8-count-occurrences-in-sorted-array) | ✅ Complete | Last - First + 1 |

---

## 1. Binary Search (Iterative) — LeetCode 704

**Problem:** Given a sorted array `nums` and a `target`, return the index of `target`. If not found, return `-1`.

```
Input:  nums = [-1, 0, 3, 5, 9, 12], target = 9
Output: 4
```

---

### Approach: Standard Binary Search ✨

```java
class Solution {
    public int search(int[] nums, int target) {
        int low = 0, high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** — Halves search space each step |
| **Space** | **O(1)** |

> 💡 **Tip:** The `while (low <= high)` condition ensures we check the last remaining element when `low == high`.

---

## 2. Binary Search (Recursive)

**Problem:** Same as above, but solved recursively.

---

### Approach: Recursive Divide & Conquer

```java
class Solution {
    public int search(int[] nums, int target) {
        return binarySearch(nums, target, 0, nums.length - 1);
    }

    private int binarySearch(int[] nums, int target, int low, int high) {
        if (low > high) return -1; // Base case: not found

        int mid = low + (high - low) / 2;

        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            return binarySearch(nums, target, mid + 1, high);
        } else {
            return binarySearch(nums, target, low, mid - 1);
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(log n)** — Recursive call stack depth |

> 💡 **Interview Tip:** Always mention the space trade-off. Iterative is preferred in production code due to O(1) space and no risk of stack overflow.

---

## 3. Lower Bound

**Problem:** Given a sorted array `nums` and a value `x`, find the **smallest index** `i` such that `nums[i] >= x`. If no such index exists, return `n` (array length).

This is essentially `Arrays.binarySearch()` behavior — it finds the insertion point.

```
Input:  nums = [1, 2, 3, 3, 5, 8, 8, 10, 10, 11], x = 3
Output: 2  (index 2 is the first position where nums[i] >= 3)

Input:  nums = [1, 2, 3, 3, 5, 8, 8, 10, 10, 11], x = 9
Output: 7  (index 7 is the first position where nums[i] >= 9, i.e., 10)
```

---

### 🧠 Core Intuition

- If `nums[mid] >= x`, this could be our answer, but there might be an earlier one → move left: `high = mid - 1` and save `ans = mid`.
- If `nums[mid] < x`, we need a larger value → move right: `low = mid + 1`.

### Approach: Modified Binary Search ✨

```java
class Solution {
    public int lowerBound(int[] nums, int x) {
        int low = 0, high = nums.length - 1;
        int ans = nums.length; // default: no element >= x

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] >= x) {
                ans = mid;       // possible answer
                high = mid - 1;  // look for earlier occurrence
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

---

## 4. Upper Bound

**Problem:** Given a sorted array `nums` and a value `x`, find the **smallest index** `i` such that `nums[i] > x`. If no such index exists, return `n`.

```
Input:  nums = [1, 2, 3, 3, 5, 8, 8, 10, 10, 11], x = 3
Output: 4  (index 4 is the first position where nums[i] > 3, i.e., 5)

Input:  nums = [1, 2, 3, 3, 5, 8, 8, 10, 10, 11], x = 11
Output: 10  (no element > 11, return n)
```

---

### 🧠 Key Difference from Lower Bound

| Bound | Condition | Meaning |
|-------|-----------|---------|
| Lower Bound | `nums[mid] >= x` | First index **≥** x |
| Upper Bound | `nums[mid] > x` | First index **>** x |

The only change is `>=` becomes `>`.

### Approach: Modified Binary Search ✨

```java
class Solution {
    public int upperBound(int[] nums, int x) {
        int low = 0, high = nums.length - 1;
        int ans = nums.length;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] > x) {    // strictly greater
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

---

## 5. Search Insert Position — LeetCode 35

**Problem:** Given a sorted array and a `target`, return the index if found. If not, return the index where it **would be inserted** in order.

```
Input:  nums = [1, 3, 5, 6], target = 5
Output: 2

Input:  nums = [1, 3, 5, 6], target = 2
Output: 1  (would be inserted at index 1)
```

---

### 🧠 Insight

This is **exactly Lower Bound** — find the first index where `nums[i] >= target`.

```java
class Solution {
    public int searchInsert(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int ans = nums.length;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] >= target) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

---

## 6. Floor & Ceil in Sorted Array

**Problem:** Given a sorted array and a value `x`:
- **Floor:** Largest element ≤ x (or -1 if none)
- **Ceil:** Smallest element ≥ x (or -1 if none)

```
Input:  nums = [10, 20, 30, 40, 50], x = 25
Output: Floor = 20, Ceil = 30

Input:  nums = [10, 20, 30, 40, 50], x = 30
Output: Floor = 30, Ceil = 30
```

---

### 🧠 Mapping to Lower/Upper Bound

| Concept | Equivalent |
|---------|------------|
| Ceil | Lower Bound — first index where `nums[i] >= x` |
| Floor | Reverse logic — last index where `nums[i] <= x` |

### Approach: Two Binary Searches ✨

```java
class Solution {
    public int[] floorAndCeil(int[] nums, int x) {
        int floor = findFloor(nums, x);
        int ceil = findCeil(nums, x);
        return new int[]{floor, ceil};
    }

    // Largest element <= x
    private int findFloor(int[] nums, int x) {
        int low = 0, high = nums.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] <= x) {
                ans = nums[mid];  // possible floor
                low = mid + 1;    // look for larger floor
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    // Smallest element >= x (Lower Bound value)
    private int findCeil(int[] nums, int x) {
        int low = 0, high = nums.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] >= x) {
                ans = nums[mid];  // possible ceil
                high = mid - 1;   // look for smaller ceil
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** for each |
| **Space** | **O(1)** |

---

## 7. First & Last Occurrence — LeetCode 34

**Problem:** Given a sorted array with duplicates and a `target`, find the first and last position of `target`. Return `[-1, -1]` if not found.

```
Input:  nums = [5, 7, 7, 8, 8, 10], target = 8
Output: [3, 4]

Input:  nums = [5, 7, 7, 8, 8, 10], target = 6
Output: [-1, -1]
```

---

### 🧠 Core Intuition

- **First Occurrence** = Lower Bound of `target` (first index where `nums[i] >= target`, then verify `nums[i] == target`)
- **Last Occurrence** = Upper Bound of `target` minus 1 (first index where `nums[i] > target`, then go one step back)

Or equivalently, run two binary searches: one that goes left on match (first), one that goes right on match (last).

### Approach: Two Binary Searches ✨

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        int first = findFirst(nums, target);
        if (first == -1) return new int[]{-1, -1};
        int last = findLast(nums, target);
        return new int[]{first, last};
    }

    // Find first occurrence: on match, keep going LEFT
    private int findFirst(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                ans = mid;
                high = mid - 1;  // keep searching left
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    // Find last occurrence: on match, keep going RIGHT
    private int findLast(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                ans = mid;
                low = mid + 1;   // keep searching right
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(2 log n) = O(log n)** |
| **Space** | **O(1)** |

---

## 8. Count Occurrences in Sorted Array

**Problem:** Given a sorted array with duplicates, count how many times `target` appears.

```
Input:  nums = [1, 1, 2, 2, 2, 2, 3], target = 2
Output: 4
```

---

### 🧠 Key Insight

`count = lastOccurrence - firstOccurrence + 1`

Reuse the `findFirst` and `findLast` functions from Problem 7.

### Approach: First & Last Occurrence ✨

```java
class Solution {
    public int countOccurrences(int[] nums, int target) {
        int first = findFirst(nums, target);
        if (first == -1) return 0;
        int last = findLast(nums, target);
        return last - first + 1;
    }

    private int findFirst(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int ans = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                ans = mid;
                high = mid - 1;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    private int findLast(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int ans = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                ans = mid;
                low = mid + 1;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

---

## 📝 Binary Search Track Summary

| # | Problem | Core Pattern | Time | Space |
|---|---------|--------------|------|-------|
| 1 | Binary Search (Iterative) | Standard BS | O(log n) | O(1) |
| 2 | Binary Search (Recursive) | Recursive BS | O(log n) | O(log n) |
| 3 | Lower Bound | First index ≥ x | O(log n) | O(1) |
| 4 | Upper Bound | First index > x | O(log n) | O(1) |
| 5 | Search Insert Position | Lower Bound = Insert Position | O(log n) | O(1) |
| 6 | Floor & Ceil | Largest ≤ x / Smallest ≥ x | O(log n) | O(1) |
| 7 | First & Last Occurrence | Two directional BS | O(log n) | O(1) |
| 8 | Count Occurrences | Last - First + 1 | O(log n) | O(1) |

---

## 🎯 Binary Search Pattern Categories

| Category | When to Use | Problems |
|----------|------------|----------|
| **Standard BS** | Sorted array, find exact target | #1, #2 |
| **Bound Finding** | Find boundary positions (first ≥, first >) | #3, #4, #5, #6 |
| **Occurrence Tracking** | Sorted with duplicates, find range | #7, #8 |
