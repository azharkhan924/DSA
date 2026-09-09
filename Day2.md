# Day 2 — Arrays: In-place Manipulation

---

## 1. Rotate Array

**Problem:** Rotate the array to the right by `k` steps.

```
Input:  nums = [1, 2, 3, 4, 5, 6, 7], k = 3
Output: [5, 6, 7, 1, 2, 3, 4]
```

**Approach: Three Reverses** 🔄

The key insight is that rotation can be achieved with three reverse operations:
1. Reverse `[0 ... n-k-1]` → reverse the first part
2. Reverse `[n-k ... n-1]` → reverse the second part
3. Reverse `[0 ... n-1]` → reverse the entire array

**Visual Walkthrough:**
```
Original:        [1, 2, 3, 4, 5, 6, 7]    k = 3

Step 1 (0→3):    [4, 3, 2, 1, 5, 6, 7]    reverse first n-k elements
Step 2 (4→6):    [4, 3, 2, 1, 7, 6, 5]    reverse last k elements
Step 3 (0→6):    [5, 6, 7, 1, 2, 3, 4]    reverse entire array ✅
```

```java
class Solution {
    private void reverse(int[] nums, int i, int j) {
        while (j > i) {
            int t = nums[i];
            nums[i] = nums[j];
            nums[j] = t;
            i++;
            j--;
        }
    }

    public void rotate(int[] nums, int k) {
        k = k % nums.length;  // handle k > n
        reverse(nums, 0, nums.length - k - 1);
        reverse(nums, nums.length - k, nums.length - 1);
        reverse(nums, 0, nums.length - 1);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) — in-place |

> 💡 **Extra Tips:**
> - Always do `k = k % n` first! If `k >= n`, rotating by `n` gives back the original array.
> - **Why this works:** Reversing twice undoes itself, but with a boundary split, it effectively "rotates" the split point to the beginning.
> - **Alternative approach:** Use a temporary array of size `k`, but that costs O(k) extra space.

---

## 2. Move Zeros

**Problem:** Move all `0`s to the end while maintaining the relative order of non-zero elements. Do this **in-place**.

```
Input:  nums = [0, 1, 0, 3, 12]
Output: [1, 3, 12, 0, 0]
```

### Solution 1: Explicit Zero & Digit Pointers

**Approach:** Use two pointers — `zero` finds the first zero, `digit` finds the next non-zero after it, then swap.

```java
class Solution {
    public void moveZeroes(int[] nums) {
        int digit = 0, zero = 0;
        while (zero < nums.length) {
            while (zero < nums.length && nums[zero] != 0) zero++;
            digit = zero;
            while (digit < nums.length && nums[digit] == 0) digit++;

            if (digit >= nums.length || zero >= nums.length) break;
            int t = nums[digit];
            nums[digit] = nums[zero];
            nums[zero] = t;
        }
    }
}
```

### Solution 2: Snowball / Overwrite Approach (Cleaner ✨)

**Approach:** Use `s` (slow) to track where the next non-zero should go. Every non-zero element gets swapped to position `s`.

```java
class Solution {
    public void moveZeroes(int[] nums) {
        int s = 0;
        int e = 0;
        while (e < nums.length) {
            if (nums[e] != 0) {
                int temp = nums[e];
                nums[e] = nums[s];
                nums[s] = temp;
                s++;
            }
            e++;
        }
    }
}
```

| Complexity | Solution 1 | Solution 2 |
|------------|-----------|-----------|
| **Time** | O(n) | O(n) |
| **Space** | O(1) | O(1) |

> 💡 **Extra Tips:**
> - Solution 2 is preferred in interviews — it's cleaner and uses the same pattern as **Remove Duplicates** and **Remove Element**.
> - The `s` pointer acts as a "write head" — everything before `s` is finalized (non-zero), everything from `s` onward still needs processing.
> - **Pattern recognition:** This "partition" technique is the same idea behind QuickSort's partition step.

---

## 📝 Day 2 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Rotate Array | Three Reverses | O(n) | O(1) |
| 2 | Move Zeros | Two Pointers / Partition | O(n) | O(1) |
