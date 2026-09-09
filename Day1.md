# Day 1 — Arrays: Basics & Searching

---

## 1. Second Largest Element

**Problem:** Find the second largest element in an array. Return `-1` if it doesn't exist.

**Approach:** Single pass — track the two largest values simultaneously.

- If `nums[i] > m1` → update `m2 = m1`, then `m1 = nums[i]`
- If `nums[i] < m1` and `nums[i] > m2` → update `m2 = nums[i]`

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

> 💡 **Extra Tip:** This approach handles duplicates correctly. If all elements are the same, `m2` remains `-1`. Be careful — initializing `m2 = -1` only works when elements are non-negative. For general cases, initialize with `Integer.MIN_VALUE` and add a validity flag.

---

## 2. Check if Array is Rotated and Sorted

**Problem:** Given an array `nums`, return `true` if it was originally sorted in non-decreasing order, then rotated some number of positions.

**Approach:** Count the number of "breaks" (where `nums[i] > nums[i+1]` circularly). A sorted-then-rotated array will have **at most 1 break**.

**Dry Run:**
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
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > nums[(i + 1) % nums.length]) c++;
        }
        return c <= 1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

> 💡 **Extra Tip:** The `% nums.length` trick wraps the last element back to the first, making the comparison circular without needing an `if` check. This pattern is useful in many circular array problems (e.g., circular queues, rotation problems).

---

## 3. Remove Duplicates from Sorted Array

**Problem:** Remove duplicates **in-place** from a sorted array. Return the count of unique elements.

```
Input:  nums = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4]
Output: 5, nums = [0, 1, 2, 3, 4, _, _, _, _, _]
```

**Approach:** Two-pointer technique — `j` points to the last unique element. When `nums[i] != nums[j]`, place `nums[i]` at `j+1`.

```java
class Solution {
    public int removeDuplicates(int[] nums) {
        int j = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[j]) {
                nums[j + 1] = nums[i];
                j++;
            }
        }
        return j + 1;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

> 💡 **Extra Tips:**
> - This works because the array is **sorted** — duplicates are always adjacent.
> - The technique is sometimes called the **"slow and fast pointer"** pattern — `j` is slow, `i` is fast.
> - **Follow-up:** LeetCode 80 asks you to allow **at most 2 duplicates**. Modify the condition to `nums[i] != nums[j-1]` and start from `j = 2`.

---

## 📝 Day 1 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Second Largest Element | Single Pass / Two Variables | O(n) | O(1) |
| 2 | Rotated & Sorted Check | Circular Array / Count Breaks | O(n) | O(1) |
| 3 | Remove Duplicates | Two Pointers (Slow & Fast) | O(n) | O(1) |
