# Day 11 — Arrays (Hard): 3 Sum & 4 Sum

---

## 1. 3 Sum (LeetCode 15)

Given an integer array `nums`, return all the triplets `[nums[i], nums[j], nums[k]]` such that:
- `i != j`, `i != k`, and `j != k` (distinct indices)
- `nums[i] + nums[j] + nums[k] == 0`
- The solution set **must not contain duplicate triplets**.

```
Input:  nums = [-1, 0, 1, 2, -1, -4]
Output: [[-1, -1, 2], [-1, 0, 1]]
Explanation:
nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0
nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0
nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0
The distinct triplets are [-1, 0, 1] and [-1, -1, 2].
```

---

### The Core Challenge: Handling Duplicates
Multiple combinations of indices can produce the exact same numbers in different orders (e.g. `[-1, 0, 1]` and `[0, 1, -1]`).
- A naive approach requires sorting each triplet before inserting into a `Set` to filter duplicates.
- The **optimal approach** sorts the array upfront and skips duplicate adjacent values using two pointers, eliminating the need for extra hash sets entirely!

---

### Approach 1: Brute Force (3 Nested Loops)

Generate all possible triplets using three nested loops. Sort each triplet before inserting into a `Set<List<Integer>>` to enforce uniqueness.

```java
import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        int n = nums.length;
        Set<List<Integer>> set = new HashSet<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        List<Integer> triplet = Arrays.asList(nums[i], nums[j], nums[k]);
                        Collections.sort(triplet);
                        set.add(triplet);
                    }
                }
            }
        }
        return new ArrayList<>(set);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n³ × log 3) ≈ O(n³) |
| **Space** | O(2 × no. of unique triplets) — for HashSet and return list |

---

### Approach 2: Better (Hashing with HashSet)

#### Intuition:
We need $nums[i] + nums[j] + nums[k] = 0$, which means:
$$\text{nums}[k] = -(\text{nums}[i] + \text{nums}[j])$$

Instead of running a third loop for `k`, keep track of elements seen between index `i` and `j` in a `HashSet`. If the required third element exists in the set, we found a valid triplet!

```java
import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        int n = nums.length;
        Set<List<Integer>> set = new HashSet<>();

        for (int i = 0; i < n; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = i + 1; j < n; j++) {
                int required = -(nums[i] + nums[j]);

                // If third element was seen between i and j
                if (seen.contains(required)) {
                    List<Integer> triplet = Arrays.asList(nums[i], nums[j], required);
                    Collections.sort(triplet);
                    set.add(triplet);
                }
                seen.add(nums[j]);
            }
        }
        return new ArrayList<>(set);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n² × log(no. of unique triplets)) |
| **Space** | O(n) + O(2 × no. of unique triplets) |

---

### Approach 3: Optimal (Sorting + Two Pointers) ✨

#### Intuition:
1. **Sort the array** in ascending order.
2. Fix the first number `nums[i]` using an outer loop:
   - If `i > 0` and `nums[i] == nums[i - 1]`, **skip it** to avoid duplicate triplets.
   - If `nums[i] > 0`, **break early** — because subsequent elements are all positive, their sum can never be $0$.
3. Set two pointers for the remaining range:
   - `left = i + 1`
   - `right = n - 1`
4. While `left < right`:
   - `sum = nums[i] + nums[left] + nums[right]`
   - If `sum == 0`:
     - Record `[nums[i], nums[left], nums[right]]`.
     - Move `left++` and skip all identical adjacent values (`while (left < right && nums[left] == nums[left - 1]) left++`).
     - Move `right--` and skip all identical adjacent values (`while (left < right && nums[right] == nums[right + 1]) right--`).
   - If `sum < 0`: we need a larger sum $\implies$ `left++`.
   - If `sum > 0`: we need a smaller sum $\implies$ `right--`.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            // Early exit: smallest element is > 0, sum cannot be 0
            if (nums[i] > 0) break;

            // Skip duplicate fixed elements
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (sum == 0) {
                    ans.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    left++;
                    right--;

                    // Skip duplicate left values
                    while (left < right && nums[left] == nums[left - 1]) left++;
                    // Skip duplicate right values
                    while (left < right && nums[right] == nums[right + 1]) right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n log n + n²) = **O(n²)** |
| **Space** | **O(1)** extra space (excluding space used to store the output) |

> 💡 **Key Insight:**
> Sorting transforms duplicate detection from an expensive hash-set operation ($O(N \log K)$ with string/list hashing) to simple pointer skips ($O(1)$) during linear traversal!

---

## 2. 4 Sum (LeetCode 18)

Given an array `nums` of `n` integers and an integer `target`, return an array of all **unique quadruplets** `[nums[a], nums[b], nums[c], nums[d]]` such that:
- `0 <= a, b, c, d < n`
- `a, b, c, d` are **distinct** indices.
- `nums[a] + nums[b] + nums[c] + nums[d] == target`

```
Input:  nums = [1, 0, -1, 0, -2, 2], target = 0
Output: [[-2, -1, 1, 2], [-2, 0, 0, 2], [-1, 0, 0, 1]]
```

---

### ⚠️ Critical Interview Trap: Integer Overflow!
In 4 Sum, numbers can be up to $10^9$ or $-10^9$. Summing four elements can reach $\pm 4 \times 10^9$, which exceeds 32-bit `Integer.MAX_VALUE` ($2.14 \times 10^9$).
Always cast numbers to `long` before addition:
```java
long sum = (long) nums[i] + nums[j] + nums[k] + nums[l];
```
Failing to do this causes silent overflow and fails on LeetCode edge test cases!

---

### Approach 1: Brute Force (4 Nested Loops)

Use four nested loops to check all quadruplet combinations. Store sorted quadruplets in a `Set<List<Integer>>` to filter duplicates.

| Complexity | Value |
|------------|-------|
| **Time** | O(n⁴) |
| **Space** | O(2 × no. of unique quadruplets) |

---

### Approach 2: Better (Hashing with 3 Nested Loops)

#### Intuition:
Similar to 3 Sum, fix three elements ($i, j, k$) and compute the required fourth element:
$$\text{required} = \text{target} - (\text{nums}[i] + \text{nums}[j] + \text{nums}[k])$$

Use a `HashSet` containing numbers visited between index `j` and `k`.

```java
import java.util.*;

class Solution {
    public List<List<Integer>> fourSum(int[] arr, int target) {
        int n = arr.length;
        Set<List<Integer>> set = new HashSet<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Set<Long> seen = new HashSet<>();

                for (int k = j + 1; k < n; k++) {
                    // Prevent overflow by doing calculation in long
                    long required = (long) target - arr[i] - arr[j] - arr[k];

                    if (seen.contains(required)) {
                        List<Integer> temp = Arrays.asList(arr[i], arr[j], arr[k], (int) required);
                        Collections.sort(temp);
                        set.add(temp);
                    }
                    seen.add((long) arr[k]);
                }
            }
        }
        return new ArrayList<>(set);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n³ × log(no. of unique quadruplets)) |
| **Space** | O(n) + O(2 × no. of unique quadruplets) |

---

### Approach 3: Optimal (Sorting + 2 Fixed Loops + Two Pointers) ✨

#### Intuition:
Extend the 3 Sum two-pointer approach by adding one more outer loop:
1. **Sort the array**.
2. **Loop 1 (`i`):** Fix the first element from $0$ to $n-1$.
   - Skip duplicate values: `if (i > 0 && nums[i] == nums[i - 1]) continue;`
3. **Loop 2 (`j`):** Fix the second element from $i+1$ to $n-1$.
   - Skip duplicate values: `if (j > i + 1 && nums[j] == nums[j - 1]) continue;`
4. **Two Pointers (`k` and `l`):**
   - Initialize `k = j + 1`, `l = n - 1`.
   - Compute `sum = (long) nums[i] + nums[j] + nums[k] + nums[l]`.
   - If `sum == target`:
     - Add `[nums[i], nums[j], nums[k], nums[l]]` to output.
     - Move `k++` and skip identical duplicates.
     - Move `l--` and skip identical duplicates.
   - If `sum < target`: move `k++` (need larger sum).
   - If `sum > target`: move `l--` (need smaller sum).

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            // Skip duplicates for the 1st element
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            for (int j = i + 1; j < n; j++) {
                // Skip duplicates for the 2nd element
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;

                int k = j + 1;
                int l = n - 1;

                while (k < l) {
                    long sum = (long) nums[i] + nums[j] + nums[k] + nums[l];

                    if (sum == target) {
                        ans.add(Arrays.asList(nums[i], nums[j], nums[k], nums[l]));

                        k++;
                        l--;

                        // Skip duplicate values for 3rd and 4th pointers
                        while (k < l && nums[k] == nums[k - 1]) k++;
                        while (k < l && nums[l] == nums[l + 1]) l--;
                    } else if (sum < target) {
                        k++;
                    } else {
                        l--;
                    }
                }
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n log n + n³) = **O(n³)** |
| **Space** | **O(1)** extra space (excluding output list) |

---

### 🧩 Generalizing to K-Sum
Notice the universal recurrence pattern from 2 Sum to 3 Sum to 4 Sum:
- **2 Sum (sorted):** Two Pointers $\implies O(n)$
- **3 Sum:** 1 Fixed Loop + Two Pointers $\implies O(n^2)$
- **4 Sum:** 2 Fixed Loops + Two Pointers $\implies O(n^3)$
- **K-Sum:** $(K - 2)$ Fixed Loops + Two Pointers $\implies O(n^{K-1})$

---

## 📝 Day 11 Summary

| # | Problem | Core Pattern | Time | Space |
|---|---------|--------------|------|-------|
| 1 | 3 Sum | Sorting + 1 Fixed Loop + Two Pointers | O(n²) | O(1) extra |
| 2 | 4 Sum | Sorting + 2 Fixed Loops + Two Pointers (with `long` casting) | O(n³) | O(1) extra |
