# Day 8 — Arrays: Matrix Traversal

---

## 1. Spiral Matrix

**Problem:** Given an `m × n` matrix, return all elements in **spiral order**.

```
Input:  matrix = [[1, 2, 3],
                  [4, 5, 6],
                  [7, 8, 9]]

Output: [1, 2, 3, 6, 9, 8, 7, 4, 5]
```

**Visual:**
```
→  →  →
         ↓
←  ←  ←
↓
→
```

**Approach:** Use four boundary pointers and shrink them after each traversal:

| Pointer | Meaning | Moves |
|---------|---------|-------|
| `a` (top) | Top row boundary | → left to right |
| `b` (right) | Right column boundary | ↓ top to bottom |
| `c` (bottom) | Bottom row boundary | ← right to left |
| `d` (left) | Left column boundary | ↑ bottom to top |

**Step-by-step:**
1. Traverse **top row** (`a`, from `d` to `b`) → then `a++`
2. Traverse **right column** (`b`, from `a` to `c`) → then `b--`
3. Traverse **bottom row** (`c`, from `b` to `d`) → then `c--`
4. Traverse **left column** (`d`, from `c` to `a`) → then `d++`
5. Repeat until boundaries cross

```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        int m = matrix.length - 1, n = matrix[0].length - 1;
        int a = 0, b = n;   // top, right
        int c = m, d = 0;   // bottom, left
        List<Integer> ans = new ArrayList<>();

        while (a <= c && b >= d) {
            // → Traverse top row
            for (int i = d; i <= b; i++) ans.add(matrix[a][i]);
            a++;

            // ↓ Traverse right column
            for (int i = a; i <= c; i++) ans.add(matrix[i][b]);
            b--;

            // Check boundaries before bottom and left traversal
            if (a <= c && b >= d) {
                // ← Traverse bottom row
                for (int i = b; i >= d; i--) ans.add(matrix[c][i]);
                c--;

                // ↑ Traverse left column
                for (int i = c; i >= a; i--) ans.add(matrix[i][d]);
                d++;
            }
        }
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(m × n) — visits every element exactly once |
| **Space** | O(1) — excluding the output list |

> 💡 **Extra Tips:**
> - The **inner boundary check** (`if (a <= c && b >= d)`) before the bottom and left traversals prevents **duplicate traversal** in non-square matrices (e.g., single row or single column remaining).
> - **Naming convention:** Using `top`, `right`, `bottom`, `left` instead of `a`, `b`, `c`, `d` makes the code more readable in interviews:
>   ```java
>   int top = 0, bottom = m - 1;
>   int left = 0, right = n - 1;
>   ```
> - **Related problems:**
>   - *Spiral Matrix II* — fill a matrix in spiral order (same logic, but write instead of read)
>   - *Spiral Matrix III* — start from a point and spiral outward
> - **Pattern:** Boundary shrinking is a common technique for layer-by-layer matrix problems (e.g., rotating rings of a matrix).

---

## 📝 Day 8 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Spiral Matrix | Four-Pointer Boundary Shrinking | O(m×n) | O(1) |
