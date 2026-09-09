# Day 3 — Arrays: Hashing, XOR & Prefix Sum

---

## 1. Union of Two Sorted Arrays

**Problem:** Find the union of two sorted arrays (all distinct elements from both, in sorted order).

```
Input:  nums1 = [1, 2, 3, 4, 5], nums2 = [1, 2, 7]
Output: [1, 2, 3, 4, 5, 7]
```

### Approach 1: TreeSet (Simple but not optimal)

Use a `TreeSet` to automatically handle duplicates and sorting.

```java
class Solution {
    public int[] unionArray(int[] nums1, int[] nums2) {
        Set<Integer> s = new TreeSet<>();
        for (int i : nums1) s.add(i);
        for (int i : nums2) s.add(i);
        int ans[] = new int[s.size()];
        int j = 0;
        for (int i : s) ans[j++] = i;
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O((m+n) log(m+n)) — TreeSet insertion is O(log n) |
| **Space** | O(m+n) |

### Approach 2: LinkedHashSet + Two Pointers (Merge-like)

Merge two sorted arrays (like merge sort's merge step) using a `LinkedHashSet` to avoid duplicates.

```java
class Solution {
    public int[] unionArray(int[] nums1, int[] nums2) {
        Set<Integer> s = new LinkedHashSet<>();
        int i = 0, j = 0;

        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] > nums2[j]) s.add(nums2[j++]);
            else s.add(nums1[i++]);
        }
        while (i < nums1.length) s.add(nums1[i++]);
        while (j < nums2.length) s.add(nums2[j++]);
        return s.stream().mapToInt(Integer::intValue).toArray();
    }
}
```

### Approach 3: ArrayList + Two Pointers (Optimal ✨)

No extra data structures for deduplication — just check the last inserted element.

```java
class Solution {
    public static ArrayList<Integer> findUnion(int a[], int b[]) {
        int i = 0, j = 0;
        ArrayList<Integer> ans = new ArrayList<>();

        while (i < a.length && j < b.length) {
            int val;

            if (a[i] < b[j])
                val = a[i++];
            else if (a[i] > b[j])
                val = b[j++];
            else {
                val = a[i];
                i++;
                j++;
            }

            if (ans.isEmpty() || ans.get(ans.size() - 1) != val)
                ans.add(val);
        }

        while (i < a.length) {
            if (ans.isEmpty() || ans.get(ans.size() - 1) != a[i])
                ans.add(a[i]);
            i++;
        }

        while (j < b.length) {
            if (ans.isEmpty() || ans.get(ans.size() - 1) != b[j])
                ans.add(b[j]);
            j++;
        }

        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(m+n) |
| **Space** | O(m+n) for the result only |

> 💡 **Extra Tips:**
> - Approach 3 is the **best for interviews** — O(m+n) with no extra data structures.
> - The merge-style pattern comes from **Merge Sort's merge step** — master that and you can solve many two-sorted-array problems.
> - For **Intersection** (not union), only add when `a[i] == b[j]`.

---

## 2. Single Number (XOR)

**Problem:** Every element appears **twice** except one. Find the single one.

**Approach:** XOR all elements. Since `a ^ a = 0` and `a ^ 0 = a`, all pairs cancel out, leaving the unique element.

```
Example: [4, 1, 2, 1, 2]
4 ^ 1 = 5
5 ^ 2 = 7
7 ^ 1 = 6
6 ^ 2 = 4  ← answer!
```

```java
class Solution {
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res ^= nums[i];
        }
        return res;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

> 💡 **Extra Tips — XOR Properties to Remember:**
> - `a ^ a = 0` (self-cancellation)
> - `a ^ 0 = a` (identity)
> - XOR is **commutative** and **associative** — order doesn't matter
> - **Follow-ups:**
>   - *Single Number II* (every element 3 times, one appears once) → use bit counting
>   - *Single Number III* (two unique elements) → XOR all, then split by a set bit

---

## 3. Longest Subarray with Sum K

### General Case (with negatives): Prefix Sum + HashMap

**Key Insight:**
```
If prefixSum[i] − prefixSum[j] == k
→ the subarray from j+1 to i has sum k
```

**Algorithm:**
1. Traverse the array, maintaining a running `sum` (prefix sum)
2. If `sum == k`, the entire subarray `[0..i]` is a candidate
3. If `sum - k` exists in the hashmap, we found a subarray of length `i - map[sum-k]`
4. Store `sum → i` in the map (only first occurrence — for **longest** subarray)

```java
class Solution {
    public int longestSubarray(int[] arr, int k) {
        HashMap<Integer, Integer> m = new HashMap<>();
        int sum = 0, mxlen = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (sum == k) mxlen = i + 1;

            if (m.containsKey(sum - k)) {
                mxlen = Math.max(mxlen, i - m.get(sum - k));
            }
            m.putIfAbsent(sum, i);  // only store FIRST occurrence
        }
        return mxlen;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) — for the hashmap |

> 💡 **Why `putIfAbsent`?** We want the **longest** subarray, so we need the **earliest** index where a prefix sum occurred. If we overwrote it, we'd get shorter subarrays.

---

### Positive Numbers Only: Sliding Window / Two Pointers (Optimal)

When all numbers are positive, the prefix sum is **strictly increasing**, so we can use a shrinking window.

**Solution 1: Prefix Sum Array + Two Pointers**

```java
class Solution {
    static ArrayList<Integer> subarraySum(int[] arr, int target) {
        int pref[] = new int[arr.length];
        pref[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            pref[i] += pref[i - 1] + arr[i];
        }
        int l = 0, r = 0, sum = 0;
        ArrayList<Integer> ans = new ArrayList<>();
        while (l < arr.length && r < arr.length) {
            if (pref[r] == target) {
                ans.add(l + 1);
                ans.add(r + 1);
                return ans;
            }
            sum = Math.abs(pref[l] - pref[r]);
            if (sum < target) r++;
            else if (sum == target) {
                ans.add(l + 2);
                ans.add(r + 1);
                return ans;
            } else l++;
        }
        ans.add(-1);
        return ans;
    }
}
```

**Solution 2: Sliding Window (Cleaner ✨)**

```java
class Solution {
    public int longestSubarray(int[] arr, int k) {
        int l = 0, sum = 0, maxLen = 0;

        for (int r = 0; r < arr.length; r++) {
            sum += arr[r];

            while (sum > k) {
                sum -= arr[l++];  // shrink window from left
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
| **Time** | O(n) — each element is added/removed at most once |
| **Space** | O(1) |

> 💡 **Extra Tips:**
> - The sliding window approach **only works for positive numbers** because adding an element always increases the sum and removing always decreases it.
> - If the array contains **zeros or negatives**, the monotonic property breaks → use the HashMap approach.
> - **Pattern:** Sliding Window is your go-to for subarray problems with **positive integers** and a target sum.

---

## 📝 Day 3 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Union of Two Sorted Arrays | Merge (Two Pointers) | O(m+n) | O(m+n) |
| 2 | Single Number | XOR / Bit Manipulation | O(n) | O(1) |
| 3 | Longest Subarray Sum K (general) | Prefix Sum + HashMap | O(n) | O(n) |
| 3 | Longest Subarray Sum K (positive) | Sliding Window | O(n) | O(1) |
