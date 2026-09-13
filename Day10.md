# Day 10 — Arrays (Hard): Pascal's Triangle

---

## 1. Pascal's Triangle (LeetCode 118)

Pascal's Triangle is a triangular array of numbers where each number is the sum of the two numbers directly above it:

```
Row 1:        1
Row 2:       1 1
Row 3:      1 2 1
Row 4:     1 3 3 1
Row 5:    1 4 6 4 1
Row 6:  1 5 10 10 5 1
```

There are **3 distinct interview variations** based on Pascal's Triangle:
1. **Variation 1:** Given row `r` and column `c`, find the element at position `(r, c)`.
2. **Variation 2:** Given row number `n`, print the entire `n`-th row.
3. **Variation 3:** Given `numRows`, generate and return the entire Pascal's Triangle.

---

### Variation 1: Find the Element at Row `r` and Column `c`

**Problem:** Given row `r` and column `c` (1-indexed), find the value at that position.

```
Input:  r = 5, c = 3
Output: 6  (3rd element of 5th row)
```

#### Mathematical Formula:
The element at row `r` and column `c` is given by the combination formula:

$$\text{Element} = {}^{r-1}C_{c-1} = \frac{(r - 1)!}{(c - 1)! \times (r - c)!}$$

> **Example:** $r = 5, c = 3 \implies {}^{4}C_{2} = \frac{4 \times 3}{2 \times 1} = 6$

#### Pitfall with Factorials:
Do **NOT** compute factorials directly using `fact(n)`!
- $13!$ exceeds `Integer.MAX_VALUE` ($2 \times 10^9$) and will cause integer overflow.
- Instead, compute $nCr$ iteratively in $O(r)$ time by canceling terms on the fly.

```java
class Solution {
    // Calculates nCr in O(r) time without overflow
    public static long nCr(int n, int r) {
        long res = 1;
        for (int i = 0; i < r; i++) {
            res = res * (n - i);
            res = res / (i + 1);
        }
        return res;
    }

    public static int pascalElement(int r, int c) {
        return (int) nCr(r - 1, c - 1);
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(c) — at most `c` operations |
| **Space** | O(1) |

---

### Variation 2: Print the N-th Row of Pascal's Triangle

**Problem:** Given row number `n` (1-indexed), return all elements of the `n`-th row.

```
Input:  n = 5
Output: [1, 4, 6, 4, 1]
```

#### Approach 1: Brute Force (Calculate nCr for every element)
Call `nCr(n - 1, c - 1)` for each column $c \in [1, n]$.
- **Time Complexity:** $O(n \times r) \approx O(n^2)$

#### Approach 2: Compute Next Element from Previous in O(1) (Optimal ✨)

Observe row $n = 5$:
- Col 1: $1$
- Col 2: $\frac{4}{1} = 4$
- Col 3: $\frac{4 \times 3}{1 \times 2} = 6 = \text{Col } 2 \times \frac{5 - 2}{2}$
- Col 4: $\frac{4 \times 3 \times 2}{1 \times 2 \times 3} = 4 = \text{Col } 3 \times \frac{5 - 3}{3}$
- Col 5: $\frac{4 \times 3 \times 2 \times 1}{1 \times 2 \times 3 \times 4} = 1 = \text{Col } 4 \times \frac{5 - 4}{4}$

#### General Formula:
$$\text{nextElement} = \text{currentElement} \times \frac{n - i}{i} \quad (i = 1, 2, \dots, n-1)$$

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public static List<Integer> getNthRow(int n) {
        List<Integer> row = new ArrayList<>();
        long ans = 1;
        row.add((int) ans);

        for (int i = 1; i < n; i++) {
            ans = ans * (n - i);
            ans = ans / i;
            row.add((int) ans);
        }
        return row;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — single pass to generate row of length `n` |
| **Space** | O(1) — excluding output list |

---

### Variation 3: Generate the Entire Pascal's Triangle (numRows)

**Problem:** Given an integer `numRows`, return the first `numRows` of Pascal's Triangle. (LeetCode 118)

```
Input:  numRows = 5
Output: [[1],
         [1, 1],
         [1, 2, 1],
         [1, 3, 3, 1],
         [1, 4, 6, 4, 1]]
```

#### Approach: Generate Row-by-Row using the O(n) Row Helper (Optimal ✨)

For each row from `1` to `numRows`, generate the row using our $O(row)$ method and append to the result.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    private List<Integer> generateRow(int row) {
        List<Integer> ansRow = new ArrayList<>();
        long ans = 1;
        ansRow.add((int) ans);

        for (int col = 1; col < row; col++) {
            ans = ans * (row - col);
            ans = ans / col;
            ansRow.add((int) ans);
        }
        return ansRow;
    }

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();

        for (int r = 1; r <= numRows; r++) {
            triangle.add(generateRow(r));
        }
        return triangle;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n²) — 1 + 2 + 3 + ... + n = n(n+1)/2 operations total |
| **Space** | O(1) — excluding output 2D list |

---

### 💡 Extra Tips & Interview Summary

> - **0-Indexed vs 1-Indexed:** In interviews, always clarify if row and column are 0-indexed or 1-indexed.
>   - If 1-indexed: formula is ${}^{r-1}C_{c-1}$.
>   - If 0-indexed: formula is ${}^{r}C_{c}$.
> - **Symmetry Property:** Pascal's triangle rows are palindromic: ${}^{n}C_{r} = {}^{n}C_{n-r}$.
> - **Sum of Row:** The sum of elements in row $n$ (1-indexed) is $2^{n-1}$.

---

## 📝 Day 10 Summary

| # | Variation | Method | Time | Space |
|---|-----------|--------|------|-------|
| 1 | Element at (R, C) | Combinations: `(R-1)C(C-1)` | O(C) | O(1) |
| 2 | N-th Row | Running multiplier: `ans * (n - i) / i` | O(N) | O(1) |
| 3 | Full Triangle | Generate each row using `generateRow()` | O(N²) | O(1) extra |
