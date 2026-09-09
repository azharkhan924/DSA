# Day 7 — Arrays: Hashing, Matrix Manipulation

---

## 1. Longest Consecutive Sequence

**Problem:** Find the length of the longest consecutive elements sequence. Must run in O(n).

```
Input:  nums = [100, 4, 200, 1, 3, 2]
Output: 4  (sequence: [1, 2, 3, 4])
```

### Approach 1: Sort + Linear Scan

Sort the array, then count consecutive elements.

```java
class Solution {
    public int longestConsecutive(int[] nums) {
        if (nums.length == 1) return 1;
        Arrays.sort(nums);
        int max = 0;
        int seq = 1;

        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] + 1 == nums[i + 1]) seq++;
            else if (nums[i] != nums[i + 1]) seq = 1;  // skip duplicates
            if (max < seq) max = seq;
        }
        return max;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n log n) — due to sorting |
| **Space** | O(1) or O(n) depending on sort |

### Approach 2: HashSet — Start from Sequence Heads (Optimal ✨)

**Key Insight:** Only start counting from the **beginning** of a sequence (where `num - 1` doesn't exist in the set).

```
Set: {100, 4, 200, 1, 3, 2}

num = 100 → 99 not in set → start! → 100 → length 1
num = 4   → 3 in set → skip (not a head)
num = 200 → 199 not in set → start! → 200 → length 1
num = 1   → 0 not in set → start! → 1→2→3→4 → length 4 ✅
num = 3   → 2 in set → skip
num = 2   → 1 in set → skip
```

```java
class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> s = new LinkedHashSet<>();
        for (int i : nums) s.add(i);
        int mx = 0;

        for (int i : s) {
            int num = i;
            int seq = 1;
            if (!s.contains(num - 1)) {           // is this a sequence head?
                while (s.contains(++num)) seq++;   // count the sequence
                mx = Math.max(mx, seq);
            }
        }
        return mx;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — each element is visited at most twice |
| **Space** | O(n) |

> 💡 **Extra Tips:**
> - The `!s.contains(num - 1)` check is the **magic** — it ensures we only start counting from the smallest element of each sequence, avoiding redundant work.
> - **Why O(n)?** Although there's a nested `while`, each element is part of exactly one sequence and is counted exactly once across all iterations.
> - You can use `HashSet` instead of `LinkedHashSet` — `LinkedHashSet` preserves insertion order but isn't necessary here.

---

## 2. Set Matrix Zeroes

**Problem:** If an element is `0`, set its entire row and column to `0`. Do it **in-place**.

### Approach 1: Marker Arrays

Use two arrays to mark which rows and columns need to be zeroed.

```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int row[] = new int[n];  // marks columns to zero
        int col[] = new int[m];  // marks rows to zero

        // Step 1: Find all zeros and mark
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    row[j] = -1;
                    col[i] = -1;
                }
            }
        }

        // Step 2: Zero out marked columns
        for (int i = 0; i < n; i++) {
            if (row[i] == -1) {
                for (int j = 0; j < m; j++) {
                    matrix[j][i] = 0;
                }
            }
        }

        // Step 3: Zero out marked rows
        for (int i = 0; i < m; i++) {
            if (col[i] == -1) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
    }
}
```

### Approach 1b: Same Logic with Helper Functions (Cleaner)

```java
class Solution {
    public void rowZ(int matrix[][], int rowN) {
        for (int i = 0; i < matrix[0].length; i++) {
            matrix[rowN][i] = 0;
        }
    }

    public void colZ(int matrix[][], int colN) {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][colN] = 0;
        }
    }

    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int row[] = new int[n];
        int col[] = new int[m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    row[j] = -1;
                    col[i] = -1;
                }
            }
        }

        for (int i = 0; i < col.length; i++)
            if (col[i] == -1) rowZ(matrix, i);
        for (int i = 0; i < row.length; i++)
            if (row[i] == -1) colZ(matrix, i);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(m × n) |
| **Space** | O(m + n) — for the marker arrays |

> 💡 **Extra Tips:**
> - **O(1) Space Approach:** Use the **first row and first column** of the matrix itself as markers! You'll need an extra variable to track whether the first row/column itself should be zeroed.
> - **Common mistake:** Don't zero elements while scanning — you'll create false zeros. Always scan first, then modify.
> - **Pattern:** This "mark then apply" pattern is used in many matrix problems (Game of Life, etc.).

---

## 3. Rotate Image (Rotate Matrix 90° Clockwise)

**Problem:** Rotate an `n × n` matrix by 90 degrees clockwise, **in-place**.

**Approach:** Two-step process:
1. **Transpose** the matrix (swap `matrix[i][j]` with `matrix[j][i]`)
2. **Reverse** each row

**Visual:**
```
Original:       Transpose:      Reverse Rows:
1  2  3         1  4  7         7  4  1
4  5  6    →    2  5  8    →    8  5  2
7  8  9         3  6  9         9  6  3
                                  ✅ 90° CW
```

```java
class Solution {
    private void reverse(int[] arr) {
        int i = 0;
        int j = arr.length - 1;
        while (j > i) {
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
            i++;
            j--;
        }
    }

    public void rotate(int[][] matrix) {
        int i, j;
        int n = matrix.length;

        // Step 1: Transpose
        for (i = 0; i < n; i++) {
            for (j = i + 1; j < n; j++) {  // j starts at i+1 to avoid double-swapping
                int t = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = t;
            }
        }

        // Step 2: Reverse each row
        for (i = 0; i < n; i++) {
            reverse(matrix[i]);
        }
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n²) |
| **Space** | O(1) — in-place |

> 💡 **Extra Tips:**
> - **Rotation cheat sheet:**
>   - **90° clockwise:** Transpose → Reverse rows
>   - **90° counter-clockwise:** Transpose → Reverse columns (or Reverse rows → Transpose)
>   - **180°:** Reverse rows → Reverse columns
> - **Why `j = i + 1`?** During transpose, starting `j` from `i + 1` ensures we only swap each pair once. Starting from `0` would swap and then swap back.
> - **Common in interviews:** Often asked as a follow-up to spiral matrix traversal.

---

## 📝 Day 7 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Longest Consecutive Sequence | HashSet + Sequence Heads | O(n) | O(n) |
| 2 | Set Matrix Zeroes | Marker Arrays / In-place Marking | O(m×n) | O(m+n) |
| 3 | Rotate Image | Transpose + Reverse | O(n²) | O(1) |
