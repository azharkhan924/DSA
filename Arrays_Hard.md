# 🔴 Arrays (Hard) — DSA Sheet

A comprehensive, organized guide to **Hard Array Problems** from the DSA Sheet, complete with visual intuitions, optimal Java solutions, complexity analyses, and interview tips.

---

## 📑 Table of Contents

| # | Problem | Status | Core Pattern |
|:---:|:---|:---:|:---|
| 1 | [Pascal's Triangle (All 3 Variations)](#1-pascals-triangle-leetcode-118) | ✅ Complete | Combinatorics / Row Multiplier |
| 2 | Majority Element II (> ⌊n/3⌋) | 📌 Track | Extended Boyer-Moore Voting (2 Candidates) |
| 3 | 3 Sum | 📌 Track | Sorting + Two Pointers |
| 4 | 4 Sum | 📌 Track | Sorting + Two Pointers (2 Fixed Pointers) |
| 5 | Largest Subarray with 0 Sum | 📌 Track | Prefix Sum + First-Occurrence HashMap |
| 6 | Count Subarrays with Given XOR K | 📌 Track | Prefix XOR + Frequency HashMap |
| 7 | Merge Overlapping Subintervals | 📌 Track | Sort by Start Time + Linear Merge |
| 8 | Merge Two Sorted Arrays without Extra Space | 📌 Track | Gap Method (Shell Sort) / Backward Fill |
| 9 | Find Missing & Repeating Number | 📌 Track | Math (Sum & Sum of Squares) / XOR |
| 10 | Count Inversions in an Array | 📌 Track | Modified Merge Sort |
| 11 | Reverse Pairs | 📌 Track | Modified Merge Sort (Counting Step) |
| 12 | Maximum Product Subarray | 📌 Track | Prefix/Suffix Scan or Min-Max Kadane |

---

## 1. Pascal's Triangle (LeetCode 118)

Pascal's Triangle is an iconic triangular array of binomial coefficients:

```
Row 1:         1
Row 2:        1 1
Row 3:       1 2 1
Row 4:      1 3 3 1
Row 5:     1 4 6 4 1
Row 6:   1 5 10 10 5 1
```

In technical interviews, this problem is asked in **3 distinct variations**:

---

### Variation 1: Given Row (R) and Column (C), Find the Element

**Problem:** Given row number `r` and column number `c` (1-indexed), find the value at that specific cell.

```
Input:  r = 5, c = 3
Output: 6  (3rd element of the 5th row)
```

#### 🧠 Mathematical Formula:
Any cell at row `r` and column `c` (1-indexed) is given by:

$$\text{Element} = {}^{r-1}C_{c-1} = \frac{(r - 1)!}{(c - 1)! \times (r - c)!}$$

> **Example:** $r = 5, c = 3 \implies {}^{5-1}C_{3-1} = {}^{4}C_{2} = \frac{4 \times 3}{2 \times 1} = 6$

#### ⚠️ Why NOT calculate full factorials?
Computing factorials with `fact(n)` quickly causes integer overflow:
- $13! = 6,227,020,800$, which exceeds `Integer.MAX_VALUE` ($2.14 \times 10^9$).
- Instead, compute $nCr$ in $O(r)$ time by canceling terms in a single loop:

```java
class Solution {
    // Computes nCr in O(r) time without factorial overflow
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
| **Time** | O(c) — at most `c` loop iterations |
| **Space** | O(1) |

---

### Variation 2: Print the N-th Row of Pascal's Triangle

**Problem:** Given row number `n` (1-indexed), return all elements of that row.

```
Input:  n = 5
Output: [1, 4, 6, 4, 1]
```

#### Naive Approach ($O(n^2)$ time):
Calculate `nCr(n - 1, c - 1)` for each column from $1$ to $n$.

#### Optimal Observation ($O(n)$ time ✨):
Look closely at row $n = 5$:
- Col 1: $1$
- Col 2: $\frac{4}{1} = 4 = \text{Col } 1 \times \frac{5 - 1}{1}$
- Col 3: $\frac{4 \times 3}{1 \times 2} = 6 = \text{Col } 2 \times \frac{5 - 2}{2}$
- Col 4: $\frac{4 \times 3 \times 2}{1 \times 2 \times 3} = 4 = \text{Col } 3 \times \frac{5 - 3}{3}$
- Col 5: $\frac{4 \times 3 \times 2 \times 1}{1 \times 2 \times 3 \times 4} = 1 = \text{Col } 4 \times \frac{5 - 4}{4}$

#### Transition Formula:
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

### Variation 3: Generate the Full Pascal's Triangle (numRows)

**Problem:** Given `numRows`, return the first `numRows` of Pascal's Triangle. (LeetCode 118)

```
Input:  numRows = 5
Output: [[1],
         [1, 1],
         [1, 2, 1],
         [1, 3, 3, 1],
         [1, 4, 6, 4, 1]]
```

#### Approach: Row-by-Row Generation (Optimal ✨)

For every row $r$ from $1$ to `numRows`, generate that row in $O(r)$ using the formula from Variation 2, and add it to our final list.

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    // Helper to generate a single row in O(row) time
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
| **Time** | O(n²) — total operations: $1 + 2 + 3 + \dots + n = \frac{n(n+1)}{2}$ |
| **Space** | O(1) — excluding output 2D list |

---

### 💡 Extra Tips & Interview Facts

> 1. **Index Clarification:** Always confirm with the interviewer if $R$ and $C$ are **0-indexed** or **1-indexed**:
>    - 1-indexed: ${}^{R-1}C_{C-1}$
>    - 0-indexed: ${}^{R}C_{C}$
> 2. **Symmetry:** Each row is palindromic: ${}^{n}C_{r} = {}^{n}C_{n-r}$.
> 3. **Row Sum Property:** The sum of all elements in the $n$-th row (1-indexed) is $2^{n-1}$.
> 4. **DP Alternative:** In dynamic programming, `triangle[i][j] = triangle[i - 1][j - 1] + triangle[i - 1][j]`. The combinatorics approach used above is faster and requires no parent-row lookups.

---

## 📝 Summary: Pascal's Triangle Variations

| Variation | Question | Best Approach | Time | Space |
|---|---|---|---|---|
| **Var 1** | Value at $(R, C)$ | $nCr(R-1, C-1)$ without factorials | O(C) | O(1) |
| **Var 2** | Entire $N$-th Row | Multiply & divide: `ans * (n - i) / i` | O(N) | O(1) |
| **Var 3** | Full Triangle (`numRows`) | Generate each row via Var 2 helper | O(N²) | O(1) extra |
