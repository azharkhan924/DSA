# Day 13 — Arrays (Hard): Merge Two Sorted Arrays Without Extra Space

---

## 📑 Table of Contents

1. [Problem Understanding & Variations](#1-problem-understanding--variations)
   - [Problem Statement](#problem-statement)
   - [Two Classic Variations (LeetCode 88 vs. GFG / Striver)](#two-classic-variations-leetcode-88-vs-gfg--striver)
2. [⚠️ Common Trap: Why Naive Two-Pointers from the Left Fails](#2-️-common-trap-why-naive-two-pointers-from-the-left-fails)
3. [Approach 1: Brute Force with Extra Space](#3-approach-1-brute-force-with-extra-space)
   - [Intuition & Algorithm](#intuition--algorithm)
   - [Java Implementation](#java-implementation)
   - [Complexity Analysis](#complexity-analysis)
4. [Approach 2: Two Pointers from Extremes + Sorting (Optimal 1)](#4-approach-2-two-pointers-from-extremes--sorting-optimal-1)
   - [Key Intuition](#key-intuition)
   - [Hinglish Explanation](#hinglish-explanation)
   - [Step-by-Step Dry Run](#step-by-step-dry-run)
   - [Java Implementation](#java-implementation-1)
   - [Complexity Analysis](#complexity-analysis-1)
5. [Approach 3: Gap Method / Shell Sort Intuition (Optimal 2)](#5-approach-3-gap-method--shell-sort-intuition-optimal-2)
   - [The Shell Sort / Gap Concept](#the-shell-sort--gap-concept)
   - [Why Gap Method Works & Ceiling Formula](#why-gap-method-works--ceiling-formula)
   - [Step-by-Step Gap Walkthrough](#step-by-step-gap-walkthrough)
   - [Java Implementation](#java-implementation-2)
   - [Complexity Analysis](#complexity-analysis-2)
6. [Approach 4: LeetCode 88 In-Place Reverse Fill (3 Pointers from End)](#6-approach-4-leetcode-88-in-place-reverse-fill-3-pointers-from-end)
   - [Visual Intuition](#visual-intuition)
   - [Java Implementation](#java-implementation-3)
   - [Complexity Analysis](#complexity-analysis-3)
7. [📊 Comparison & Interview Decision Matrix](#7--comparison--interview-decision-matrix)

---

## 1. Problem Understanding & Variations

### Problem Statement
Given two sorted integer arrays `nums1` of size `m` and `nums2` of size `n`, merge them such that:
- `nums1` contains the first `m` smallest elements in sorted order.
- `nums2` contains the remaining `n` elements in sorted order.
- **Strict Constraint:** Do it in **$O(1)$ auxiliary space** (without allocating another array).

```
Input:
nums1 = [-5, -2, 4, 5], m = 4
nums2 = [-3, 1, 8],      n = 3

Output:
nums1 = [-5, -3, -2, 1]
nums2 = [4, 5, 8]

Combined View: [-5, -3, -2, 1, 4, 5, 8]
```

---

### Two Classic Variations (LeetCode 88 vs. GFG / Striver)

In technical interviews, you must immediately clarify which variation the interviewer wants:

| Feature | Variation A: GFG / Striver / Coding Ninjas | Variation B: LeetCode 88 |
| :--- | :--- | :--- |
| **Array Structure** | Two completely separate arrays: `nums1` of size $m$, `nums2` of size $n$. | `nums1` has size $m + n$ (with $n$ zeroes buffered at the end). |
| **Return / Output** | Both `nums1` and `nums2` are rearranged in-place. | All merged elements must be in `nums1`. |
| **Optimal Algorithm** | **Gap Method (Shell Sort)** or **Extremes Swap + Sort** ($O(1)$ space). | **3 Pointers from the End (Reverse Fill)** ($O(m+n)$ time, $O(1)$ space). |

---

## 2. ⚠️ Common Trap: Why Naive Two-Pointers from the Left Fails

A very tempting first intuition is:
> *"Start `i = 0` on `nums1` and `j = 0` on `nums2`. If `nums1[i] <= nums2[j]`, do `i++`. Else swap `nums1[i]` with `nums2[j]` and do `j++`."*

### Why this breaks:
```
nums1 = [-5, -2, 4, 5], nums2 = [-3, 1, 8]
```
1. `i = 0 (-5) <= j = 0 (-3)` $\rightarrow$ `i++` ($i=1$).
2. `nums1[1] (-2) > nums2[0] (-3)` $\rightarrow$ Swap them:
   - `nums1 = [-5, -3, 4, 5]`
   - `nums2 = [-2, 1, 8]`
   - Advance `j++` ($j=1$).
3. **The Disaster:** `-2` is now at `nums2[0]`. Because `j` only increments, `nums2[0]` is **never visited again**!
4. Later, `nums1` has larger elements (`1`, `5`), but `-2` can never move into `nums1`.
5. Final result will incorrectly have `-2` in `nums2` instead of `nums1`.

> [!CAUTION]
> Once an element is placed into `nums2[j]` via swapping, the sorted property of `nums2` is ruined. Pointers moving only forward cannot guarantee that smaller elements swapped into earlier indices will ever reach `nums1`.

---

## 3. Approach 1: Brute Force with Extra Space

### Intuition & Algorithm
Use a temporary array `ans` of size $m + n$. Run standard merge algorithm (like in Merge Sort), then copy elements back to `nums1` and `nums2`.

```
nums1: [-5, -2,  4,  5] ──┐
                          ├──> [ans: -5, -3, -2, 1, 4, 5, 8] ──> Copy back
nums2: [ -3,  1,  8 ] ────┘
```

### Java Implementation
```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] ans = new int[m + n];
        int i = 0, j = 0, k = 0;

        // Merge two sorted arrays into ans
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                ans[k++] = nums1[i++];
            } else {
                ans[k++] = nums2[j++];
            }
        }

        while (i < m) ans[k++] = nums1[i++];
        while (j < n) ans[k++] = nums2[j++];

        // Copy back: first m to nums1, remaining n to nums2
        for (int p = 0; p < m; p++) {
            nums1[p] = ans[p];
        }
        for (int p = 0; p < n; p++) {
            nums2[p] = ans[m + p];
        }
    }
}
```

### Complexity Analysis
- **Time Complexity:** $O(m + n)$ — Merging takes linear time, copying back takes $O(m + n)$.
- **Space Complexity:** $O(m + n)$ — Requires allocating an auxiliary array of size $m + n$.

---

## 4. Approach 2: Two Pointers from Extremes + Sorting (Optimal 1)

### Key Intuition

At the end of the merge:
- `nums1` should hold the **$m$ smallest elements**.
- `nums2` should hold the **$n$ largest elements**.

Where are the largest candidates in `nums1`? **At the end (`left = m - 1`)**.  
Where are the smallest candidates in `nums2`? **At the start (`right = 0`)**.

If `nums1[left] > nums2[right]`, they are on the wrong sides!
- Swap them.
- Decrement `left--` (check next largest in `nums1`).
- Increment `right++` (check next smallest in `nums2`).

If at any point `nums1[left] <= nums2[right]`, **stop early**! Since both arrays were sorted, all elements to the left of `left` are even smaller, and all elements to the right of `right` are even larger.

```
nums1: [ -5,  -2,   4,   5 ]         nums2: [ -3,   1,   8 ]
                        ▲                           ▲
                        │                           │
                      left (starts at m-1)        right (starts at 0)
```

> [!NOTE]
> **In simple terms (Hinglish):**
> `nums1` mein sabse bade elements piche baithe hain (`left = m-1`), aur `nums2` mein sabse chhote elements aage baithe hain (`right = 0`).  
> Agar `nums1` ka koi bada element `nums2` ke chhote element se bhi bada nikal gaya, toh wo galat array mein hai! Dono ko aapas mein **swap** kar do.  
> Jab tak `nums1[left] > nums2[right]` hai tab tak swap karte raho. Jaise hi condition false hui, **break** kar jao kyunki aage sab already sahi side par hain. Aakhiri step mein bas dono arrays ko sort kar do!

---

### Step-by-Step Dry Run

**Input:** `nums1 = [-5, -2, 4, 5]`, `nums2 = [-3, 1, 8]`

| Iteration | `left` (idx, val) | `right` (idx, val) | Condition (`nums1[left] > nums2[right]`) | Action | Array State |
| :---: | :---: | :---: | :---: | :---: | :--- |
| **Start** | `3` (`5`) | `0` (`-3`) | `5 > -3` ✅ | Swap, `left--`, `right++` | `nums1: [-5, -2, 4, -3]`<br>`nums2: [5, 1, 8]` |
| **2** | `2` (`4`) | `1` (`1`) | `4 > 1` ✅ | Swap, `left--`, `right++` | `nums1: [-5, -2, 1, -3]`<br>`nums2: [5, 4, 8]` |
| **3** | `1` (`-2`) | `2` (`8`) | `-2 > 8` ❌ | **Break loop** | — |

**Final Sort:**
- `Arrays.sort(nums1)` $\rightarrow$ `[-5, -3, -2, 1]`
- `Arrays.sort(nums2)` $\rightarrow$ `[4, 5, 8]`
- ✅ All smallest $m$ in `nums1`, all largest $n$ in `nums2`!

---

### Java Implementation
```java
import java.util.Arrays;

class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int left = m - 1;
        int right = 0;

        // Swap out-of-place elements between the two arrays
        while (left >= 0 && right < n) {
            if (nums1[left] > nums2[right]) {
                int temp = nums1[left];
                nums1[left] = nums2[right];
                nums2[right] = temp;

                left--;
                right++;
            } else {
                // Since arrays were sorted initially, no more swaps needed
                break;
            }
        }

        // Re-sort both arrays to restore sorted order within each
        Arrays.sort(nums1);
        Arrays.sort(nums2);
    }
}
```

### Complexity Analysis
- **Time Complexity:** $O(\min(m, n)) + O(m \log m) + O(n \log n)$
  - Swapping loop runs at most $\min(m, n)$ times.
  - Sorting takes $O(m \log m)$ and $O(n \log n)$.
- **Space Complexity:** $O(1)$ auxiliary space.

---

## 5. Approach 3: Gap Method / Shell Sort Intuition (Optimal 2)

### The Shell Sort / Gap Concept
Is it possible to merge without calling a built-in sort (`Arrays.sort`) at the end while still keeping $O(1)$ space?  
**Yes! By using the Gap Method (derived from Donald Shell's Shell Sort, 1959).**

Instead of comparing adjacent elements, we compare elements separated by a fixed **gap**:

$$\text{Initial Gap} = \left\lceil \frac{m + n}{2} \right\rceil = \left( \frac{m + n}{2} \right) + (m + n) \pmod 2$$

In each iteration:
1. Place `left = 0` and `right = left + gap`.
2. Compare elements at `left` and `right`. If `left > right`, swap them.
3. Advance both `left++` and `right++` until `right` reaches $m + n$.
4. Reduce gap: $\text{gap} = \lceil \text{gap} / 2 \rceil$.
5. Repeat until pass with `gap = 1` finishes.

```
Virtual Combined Array of size (m + n):
Index:    0    1    2    3    |    4    5    6
Array:   [-5, -2,   4,   5]   |  [-3,   1,   8]
          ▲                   |         ▲
        left                  |       right = left + gap
```

### Virtual Index Mapping
Since the elements are split across two arrays:
- If `index < m`: Element is `nums1[index]`.
- If `index >= m`: Element is `nums2[index - m]`.

Three comparison cases exist:
1. **Both in `nums1`:** `left < m && right < m` $\rightarrow$ compare `nums1[left]` & `nums1[right]`
2. **`left` in `nums1`, `right` in `nums2`:** `left < m && right >= m` $\rightarrow$ compare `nums1[left]` & `nums2[right - m]`
3. **Both in `nums2`:** `left >= m && right >= m` $\rightarrow$ compare `nums2[left - m]` & `nums2[right - m]`

---

### Step-by-Step Gap Walkthrough

**Input:** `nums1 = [-5, -2, 4, 5] (m=4)`, `nums2 = [-3, 1, 8] (n=3)`, $m + n = 7$

#### Pass 1: $\text{gap} = \lceil 7 / 2 \rceil = 4$
- `left = 0, right = 4`: compare `nums1[0] (-5)` & `nums2[0] (-3)` $\rightarrow -5 \le -3$, no swap.
- `left = 1, right = 5`: compare `nums1[1] (-2)` & `nums2[1] (1)` $\rightarrow -2 \le 1$, no swap.
- `left = 2, right = 6`: compare `nums1[2] (4)` & `nums2[2] (8)` $\rightarrow 4 \le 8$, no swap.

#### Pass 2: $\text{gap} = \lceil 4 / 2 \rceil = 2$
- `left = 0, right = 2`: compare `nums1[0] (-5)` & `nums1[2] (4)` $\rightarrow$ no swap.
- `left = 1, right = 3`: compare `nums1[1] (-2)` & `nums1[3] (5)` $\rightarrow$ no swap.
- `left = 2, right = 4`: compare `nums1[2] (4)` & `nums2[0] (-3)` $\rightarrow 4 > -3$! **Swap!**  
  `nums1[2] = -3`, `nums2[0] = 4`.
- `left = 3, right = 5`: compare `nums1[3] (5)` & `nums2[1] (1)` $\rightarrow 5 > 1$! **Swap!**  
  `nums1[3] = 1`, `nums2[1] = 5`.
- `left = 4, right = 6`: compare `nums2[0] (4)` & `nums2[2] (8)` $\rightarrow$ no swap.

State after gap 2: `nums1 = [-5, -2, -3, 1]`, `nums2 = [4, 5, 8]`

#### Pass 3: $\text{gap} = \lceil 2 / 2 \rceil = 1$
- `left = 1, right = 2`: compare `nums1[1] (-2)` & `nums1[2] (-3)` $\rightarrow -2 > -3$! **Swap!**  
  `nums1 = [-5, -3, -2, 1]`.
- All other pairs $\le$.

Pass with `gap = 1` finishes $\rightarrow$ Array is **100% sorted without any sorting library!**

---

### Java Implementation
```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int len = m + n;
        // gap = ceil(len / 2.0)
        int gap = (len / 2) + (len % 2);

        while (gap > 0) {
            int left = 0;
            int right = left + gap;

            while (right < len) {
                // Case 1: Both pointers in nums1
                if (left < m && right < m) {
                    if (nums1[left] > nums1[right]) {
                        swap(nums1, left, nums1, right);
                    }
                }
                // Case 2: left in nums1, right in nums2
                else if (left < m && right >= m) {
                    if (nums1[left] > nums2[right - m]) {
                        swap(nums1, left, nums2, right - m);
                    }
                }
                // Case 3: Both pointers in nums2
                else {
                    if (nums2[left - m] > nums2[right - m]) {
                        swap(nums2, left - m, nums2, right - m);
                    }
                }
                left++;
                right++;
            }

            // If we just finished the pass with gap = 1, we are done
            if (gap == 1) break;
            gap = (gap / 2) + (gap % 2);
        }
    }

    private void swap(int[] arr1, int i, int[] arr2, int j) {
        int temp = arr1[i];
        arr1[i] = arr2[j];
        arr2[j] = temp;
    }
}
```

### Complexity Analysis
- **Time Complexity:** $O((m + n) \log(m + n))$
  - Number of gap reductions: $\log_2(m + n)$.
  - Each pass scans the array in $O(m + n)$.
- **Space Complexity:** $O(1)$ — Truly in-place, zero extra memory.

---

## 6. Approach 4: LeetCode 88 In-Place Reverse Fill (3 Pointers from End)

### Visual Intuition
On LeetCode, `nums1` is given with size $m + n$:
```
nums1 = [1, 2, 3, 0, 0, 0], m = 3
nums2 = [2, 5, 6],         n = 3
```

Because the empty space is at the **back** of `nums1`, we can fill the largest elements starting from the end (`k = m + n - 1`) backwards!

```
nums1: [  1,   2,   3,   0,   0,   0  ]
                    ▲                  ▲
                    │                  │
                  i = m-1          k = m+n-1

nums2: [  2,   5,   6  ]
                    ▲
                    │
                  j = n-1
```

By placing the largest elements from `nums1[k]` down to `nums1[0]`, we **never overwrite** any unvisited elements in `nums1`!

---

### Java Implementation
```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;       // Pointer to end of valid elements in nums1
        int j = n - 1;       // Pointer to end of nums2
        int k = m + n - 1;   // Pointer to back of nums1 buffer

        // Compare from behind and place the larger element at nums1[k]
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
            }
        }

        // If elements remain in nums2, copy them over
        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
        // Note: If i >= 0, they are already in their correct places!
    }
}
```

### Complexity Analysis
- **Time Complexity:** $O(m + n)$ — Each element is examined and placed once.
- **Space Complexity:** $O(1)$ — No extra auxiliary space.

---

## 7. 📊 Comparison & Interview Decision Matrix

| Approach | Problem Type | Time Complexity | Auxiliary Space | Best Used When |
| :--- | :--- | :---: | :---: | :--- |
| **Approach 1: Extra Array** | Any | $O(m + n)$ | $O(m + n)$ | Clarifying brute force in early interview stage. |
| **Approach 2: Extremes Swap + Sort** | Two Separate Arrays (GFG) | $O(\min(m, n)) + O(m \log m + n \log n)$ | $O(1)$ | Interviewer allows internal sorting; easiest to write cleanly. |
| **Approach 3: Gap Method (Shell Sort)** | Two Separate Arrays (GFG) | $O((m + n) \log(m + n))$ | $O(1)$ | Interviewer strictly forbids built-in sort; demonstrates advanced algorithmic knowledge. |
| **Approach 4: Reverse 3-Pointers** | LeetCode 88 (`nums1` has buffer) | $O(m + n)$ | $O(1)$ | LeetCode 88 standard; optimal linear time solution. |

---

### 💡 Interview Pro-Tips
1. **Always ask:** *"Does `nums1` have enough buffer at the end to hold `nums2`, or are they two completely separate arrays?"*
2. If `nums1` has buffer $\rightarrow$ **Approach 4 (Reverse Fill)** is the cleanest and fastest ($O(m+n)$).
3. If they are separate arrays $\rightarrow$ Discuss **Approach 2** first (intuitive swap + sort), then drop the **Gap Method (Approach 3)** to show you know how Shell sort principles solve this in $O(1)$ space without library sorts!
