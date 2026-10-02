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
| 9 | [Search in Rotated Sorted Array (Unique)](#9-search-in-rotated-sorted-array-unique--leetcode-33) | ✅ Complete | Identify sorted half |
| 10 | [Search in Rotated Sorted Array (Duplicates)](#10-search-in-rotated-sorted-array-duplicates--leetcode-81) | ✅ Complete | Handle `nums[low]==nums[mid]==nums[high]` |
| 11 | [Find Minimum in Rotated Sorted Array](#11-find-minimum-in-rotated-sorted-array--leetcode-153) | ✅ Complete | Sorted half has the min candidate |
| 12 | [Number of Rotations](#12-number-of-rotations) | ✅ Complete | Index of minimum |
| 13 | [Single Element in Sorted Array](#13-single-element-in-sorted-array--leetcode-540) | ✅ Complete | Even-Odd index pairing |
| 14 | [Find Peak Element](#14-find-peak-element--leetcode-162) | ✅ Complete | Move towards greater neighbor |
| 15 | [Square Root of N](#15-square-root-of-n) | ✅ Complete | BS on answer space |
| 16 | [Find Nth Root of M](#16-find-nth-root-of-m) | ✅ Complete | BS on answer space |
| 17 | [Koko Eating Bananas](#17-koko-eating-bananas--leetcode-875) | ✅ Complete | BS on answer space |

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

    // findFirst and findLast same as Problem 7
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

---

## 9. Search in Rotated Sorted Array (Unique) — LeetCode 33

**Problem:** Given a sorted array that has been **rotated** at some pivot, search for a `target` in O(log n). All elements are unique.

```
Input:  nums = [4, 5, 6, 7, 0, 1, 2], target = 0
Output: 4

Input:  nums = [4, 5, 6, 7, 0, 1, 2], target = 3
Output: -1
```

---

### 🧠 Core Intuition

In a rotated sorted array, **at least one half** (left or right of `mid`) is **always sorted**. We identify which half is sorted, then check if the target lies within that sorted half.

```
[4, 5, 6, 7, 0, 1, 2]
 L        M        H

Left half [4,5,6,7] is sorted (nums[low] <= nums[mid])
→ Check: is target in [nums[low], nums[mid]]?
  YES → search left (high = mid - 1)
  NO  → search right (low = mid + 1)
```

### Approach: Identify Sorted Half ✨

```java
class Solution {
    public int search(int[] nums, int target) {
        int low = 0, high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) return mid;

            // Left half is sorted
            if (nums[low] <= nums[mid]) {
                if (nums[low] <= target && target < nums[mid]) {
                    high = mid - 1;  // target in left sorted half
                } else {
                    low = mid + 1;   // target in right half
                }
            }
            // Right half is sorted
            else {
                if (nums[mid] < target && target <= nums[high]) {
                    low = mid + 1;   // target in right sorted half
                } else {
                    high = mid - 1;  // target in left half
                }
            }
        }
        return -1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

> ⚠️ **Critical:** The condition is `nums[low] <= nums[mid]` (with `=`) to handle the case when `low == mid` (2-element subarray).

---

## 10. Search in Rotated Sorted Array (Duplicates) — LeetCode 81

**Problem:** Same as Problem 9, but the array **may contain duplicates**. Return `true` if target exists, `false` otherwise.

```
Input:  nums = [2, 5, 6, 0, 0, 1, 2], target = 0
Output: true

Input:  nums = [1, 0, 1, 1, 1], target = 0
Output: true
```

---

### 🧠 The Duplicate Trap

When `nums[low] == nums[mid] == nums[high]`, we **can't determine** which half is sorted.

```
Example: nums = [3, 1, 2, 3, 3, 3, 3], target = 2

low=0, high=6, mid=3
nums[0]=3, nums[3]=3, nums[6]=3  →  Which half is sorted? Can't tell! 🤷
```

**Solution:** When this happens, just shrink both ends: `low++`, `high--`. This degrades to O(n) in the worst case but handles all duplicate scenarios.

### Approach: Handle Ambiguous Case ✨

```java
class Solution {
    public boolean search(int[] nums, int target) {
        int low = 0, high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) return true;

            // ⚠️ Ambiguous case: can't determine sorted half
            if (nums[low] == nums[mid] && nums[mid] == nums[high]) {
                low++;
                high--;
                continue;
            }

            // Left half is sorted
            if (nums[low] <= nums[mid]) {
                if (nums[low] <= target && target < nums[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            // Right half is sorted
            else {
                if (nums[mid] < target && target <= nums[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return false;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** average, **O(n)** worst case (all duplicates) |
| **Space** | **O(1)** |

---

## 11. Find Minimum in Rotated Sorted Array — LeetCode 153

**Problem:** Given a rotated sorted array with unique elements, find the minimum element.

```
Input:  nums = [3, 4, 5, 1, 2]
Output: 1

Input:  nums = [4, 5, 6, 7, 0, 1, 2]
Output: 0
```

---

### 🧠 Core Intuition

The minimum is at the "rotation point." At each step, the **sorted half** gives us a minimum candidate (`nums[low]` of that half). We pick the smaller candidate and then eliminate the sorted half.

```
[4, 5, 6, 7, 0, 1, 2]
 L        M        H

Left half [4,5,6,7] is sorted → min candidate = 4
Eliminate left half → the actual min must be in the right half
```

### Approach: Sorted Half Elimination ✨

```java
class Solution {
    public int findMin(int[] nums) {
        int low = 0, high = nums.length - 1;
        int ans = Integer.MAX_VALUE;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            // Left half is sorted
            if (nums[low] <= nums[mid]) {
                ans = Math.min(ans, nums[low]); // minimum of sorted half
                low = mid + 1;                   // eliminate left half
            }
            // Right half is sorted
            else {
                ans = Math.min(ans, nums[mid]); // minimum of sorted half
                high = mid - 1;                  // eliminate right half
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

## 12. Number of Rotations

**Problem:** Given a rotated sorted array, find how many times it has been rotated. (Equivalently: find the index of the minimum element.)

```
Input:  nums = [4, 5, 6, 7, 0, 1, 2]
Output: 4  (rotated 4 times; minimum is at index 4)

Input:  nums = [3, 4, 5, 1, 2]
Output: 3
```

---

### 🧠 Key Insight

Number of rotations = **index of the minimum element**. Reuse the Find Minimum logic, but track the index instead of the value.

### Approach: Track Minimum Index ✨

```java
class Solution {
    public int findKRotation(int[] nums) {
        int low = 0, high = nums.length - 1;
        int ans = Integer.MAX_VALUE;
        int idx = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[low] <= nums[mid]) {
                if (nums[low] < ans) {
                    ans = nums[low];
                    idx = low;
                }
                low = mid + 1;
            } else {
                if (nums[mid] < ans) {
                    ans = nums[mid];
                    idx = mid;
                }
                high = mid - 1;
            }
        }
        return idx;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

---

## 13. Single Element in Sorted Array — LeetCode 540

**Problem:** Given a sorted array where every element appears exactly **twice** except for one element that appears **once**, find the single element.

```
Input:  nums = [1, 1, 2, 3, 3, 4, 4, 8, 8]
Output: 2

Input:  nums = [3, 3, 7, 7, 10, 11, 11]
Output: 10
```

---

### 🧠 Core Intuition (Even-Odd Index Pairing)

In a valid paired section **before** the single element:
- Pairs start at **even** index: `(even, odd)` → `(0,1)`, `(2,3)`, `(4,5)`, ...

**After** the single element, the pairing shifts:
- Pairs start at **odd** index: `(odd, even)` → `(1,2)`, `(3,4)`, ...

```
Index:  0  1  2  3  4  5  6  7  8
Value: [1, 1, 2, 3, 3, 4, 4, 8, 8]
              ^single
Before single: (1,1) at (0,1) → even-odd ✅
After single:  (3,3) at (3,4), (4,4) at (5,6), (8,8) at (7,8) → odd-even
```

At `mid`:
- If `mid` is **even** and `nums[mid] == nums[mid+1]` → we're on the **left side** (before single) → go right
- If `mid` is **odd** and `nums[mid] == nums[mid-1]` → we're on the **left side** (before single) → go right
- Otherwise → we're on the **right side** (at or after single) → go left

### Approach: Binary Search on Index Parity ✨

```java
class Solution {
    public int singleNonDuplicate(int[] nums) {
        int n = nums.length;

        // Edge cases
        if (n == 1) return nums[0];
        if (nums[0] != nums[1]) return nums[0];
        if (nums[n - 1] != nums[n - 2]) return nums[n - 1];

        int low = 1, high = n - 2;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            // Found the single element
            if (nums[mid] != nums[mid - 1] && nums[mid] != nums[mid + 1]) {
                return nums[mid];
            }

            // We are on the LEFT side of the single element
            // (even,odd) pairing is intact → move right
            if ((mid % 2 == 0 && nums[mid] == nums[mid + 1]) ||
                (mid % 2 == 1 && nums[mid] == nums[mid - 1])) {
                low = mid + 1;
            }
            // We are on the RIGHT side → move left
            else {
                high = mid - 1;
            }
        }
        return -1; // shouldn't reach here
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

> 💡 **Why start at `low=1, high=n-2`?** We handle indices 0 and n-1 as edge cases to avoid out-of-bounds when checking `nums[mid-1]` and `nums[mid+1]`.

---

## 14. Find Peak Element — LeetCode 162

**Problem:** A peak element is an element that is **strictly greater** than its neighbors. Given an array `nums`, find a peak element and return its index. The array may contain multiple peaks — return **any** peak.

`nums[-1] = nums[n] = -∞` (elements outside the array are considered negative infinity).

```
Input:  nums = [1, 2, 3, 1]
Output: 2  (nums[2] = 3 is a peak)

Input:  nums = [1, 2, 1, 3, 5, 6, 4]
Output: 5  (or 1, both are valid peaks)
```

---

### 🧠 Core Intuition

If `nums[mid] < nums[mid + 1]`, there's a **rising slope** to the right → a peak **must exist** on the right side (because eventually the array drops to -∞).

If `nums[mid] < nums[mid - 1]`, the peak is on the left side.

We always move **towards the greater neighbor**.

### Approach: Binary Search on Slope ✨

```java
class Solution {
    public int findPeakElement(int[] nums) {
        int n = nums.length;

        // Edge cases
        if (n == 1) return 0;
        if (nums[0] > nums[1]) return 0;
        if (nums[n - 1] > nums[n - 2]) return n - 1;

        int low = 1, high = n - 2;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) {
                return mid; // Found peak
            } else if (nums[mid] < nums[mid + 1]) {
                low = mid + 1;   // Peak is on the right
            } else {
                high = mid - 1;  // Peak is on the left
            }
        }
        return -1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(log n)** |
| **Space** | **O(1)** |

---

## 15. Square Root of N

**Problem:** Given a non-negative integer `n`, find the integer square root of `n` (i.e., the largest integer `x` such that `x * x <= n`).

```
Input:  n = 36
Output: 6

Input:  n = 28
Output: 5  (5² = 25 ≤ 28, but 6² = 36 > 28)
```

---

### 🧠 Core Intuition (Binary Search on Answer Space)

The answer lies in the range `[1, n]`. For each candidate `mid`, check if `mid * mid <= n`.

- If `mid * mid <= n` → `mid` is a possible answer, try larger: `low = mid + 1`
- If `mid * mid > n` → too large: `high = mid - 1`

### Approach: BS on Answer ✨

```java
class Solution {
    public int mySqrt(int n) {
        if (n == 0) return 0;

        int low = 1, high = n;
        int ans = 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            long sq = (long) mid * mid; // avoid overflow

            if (sq <= n) {
                ans = mid;
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

## 16. Find Nth Root of M

**Problem:** Find the integer `x` such that `x^n = m`. If no such integer exists, return `-1`.

```
Input:  n = 3, m = 27
Output: 3  (3³ = 27)

Input:  n = 4, m = 69
Output: -1  (no integer x where x⁴ = 69)
```

---

### 🧠 Core Intuition

Binary search on `[1, m]`. For each `mid`, compute `mid^n`:
- If `mid^n == m` → found!
- If `mid^n < m` → try larger
- If `mid^n > m` → try smaller

> ⚠️ **Overflow Risk:** `mid^n` can overflow even `long`. Use a safe power function that returns early if it exceeds `m`.

### Approach: BS on Answer with Safe Power ✨

```java
class Solution {
    public int nthRoot(int n, int m) {
        int low = 1, high = m;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = power(mid, n, m);

            if (cmp == 0) {
                return mid;    // mid^n == m
            } else if (cmp == -1) {
                low = mid + 1; // mid^n < m → go right
            } else {
                high = mid - 1; // mid^n > m → go left
            }
        }
        return -1;
    }

    // Returns: -1 if base^exp < target, 0 if equal, 1 if greater
    // Avoids overflow by checking at each multiplication step
    private int power(int base, int exp, int target) {
        long result = 1;
        for (int i = 0; i < exp; i++) {
            result *= base;
            if (result > target) return 1; // early exit to prevent overflow
        }
        if (result == target) return 0;
        return -1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n × log m)** — log m for BS, n for power computation |
| **Space** | **O(1)** |

---

## 17. Koko Eating Bananas — LeetCode 875

**Problem:** Koko has `n` piles of bananas. She can eat at most `k` bananas per hour from a single pile. If a pile has fewer than `k` bananas, she finishes it and waits. Given `h` hours, find the **minimum** eating speed `k` such that she can eat all bananas within `h` hours.

```
Input:  piles = [3, 6, 7, 11], h = 8
Output: 4

Explanation at k=4:
- Pile 3:  ceil(3/4)  = 1 hour
- Pile 6:  ceil(6/4)  = 2 hours
- Pile 7:  ceil(7/4)  = 2 hours
- Pile 11: ceil(11/4) = 3 hours
Total = 8 hours ≤ h ✅
```

---

### 🧠 Core Intuition (Binary Search on Answer Space)

The answer `k` lies in the range `[1, max(piles)]`:
- `k = 1`: slowest possible, takes maximum hours
- `k = max(piles)`: each pile takes exactly 1 hour

For each candidate `k`, compute total hours needed. If `totalHours <= h`, we can try slower (smaller k). Otherwise, we need to eat faster (larger k).

### Approach: BS on Answer ✨

```java
class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int low = 1;
        int high = getMax(piles);
        int ans = high;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            long totalHours = calculateHours(piles, mid);

            if (totalHours <= h) {
                ans = mid;         // possible answer
                high = mid - 1;    // try slower speed
            } else {
                low = mid + 1;     // need to eat faster
            }
        }
        return ans;
    }

    private long calculateHours(int[] piles, int speed) {
        long hours = 0;
        for (int pile : piles) {
            hours += (pile + speed - 1) / speed; // ceil(pile / speed)
        }
        return hours;
    }

    private int getMax(int[] piles) {
        int max = 0;
        for (int pile : piles) {
            max = Math.max(max, pile);
        }
        return max;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | **O(n × log(max(piles)))** — n for computing hours, log(max) for BS |
| **Space** | **O(1)** |

> 💡 **Pattern Recognition:** This is a classic **"Binary Search on Answer Space"** problem. The answer space is a range of values `[min, max]`, and you binary search for the optimal value that satisfies a condition. Other problems with this pattern: Minimum Days to Make M Bouquets, Ship Packages Within D Days, Aggressive Cows, etc.

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
| 9 | Search in Rotated (Unique) | Identify sorted half | O(log n) | O(1) |
| 10 | Search in Rotated (Duplicates) | Handle ambiguous `low==mid==high` | O(log n) avg, O(n) worst | O(1) |
| 11 | Find Min in Rotated | Sorted half elimination | O(log n) | O(1) |
| 12 | Number of Rotations | Index of minimum | O(log n) | O(1) |
| 13 | Single Element in Sorted Array | Even-Odd index parity | O(log n) | O(1) |
| 14 | Find Peak Element | Move towards greater neighbor | O(log n) | O(1) |
| 15 | Square Root of N | BS on answer space | O(log n) | O(1) |
| 16 | Nth Root of M | BS on answer + safe power | O(n log m) | O(1) |
| 17 | Koko Eating Bananas | BS on answer space (minimize k) | O(n log max) | O(1) |

---

## 🎯 Binary Search Pattern Categories

| Category | When to Use | Problems |
|----------|------------|----------|
| **Standard BS** | Sorted array, find exact target | #1, #2 |
| **Bound Finding** | Find boundary positions (first ≥, first >) | #3, #4, #5, #6 |
| **Occurrence Tracking** | Sorted with duplicates, find range | #7, #8 |
| **Rotated Array** | Sorted but rotated, identify sorted half | #9, #10, #11, #12 |
| **Condition-based** | Non-standard search using element properties | #13, #14 |
| **BS on Answer Space** | Answer is in a range, check feasibility | #15, #16, #17 |
