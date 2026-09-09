# Day 5 — Arrays: Kadane's Algorithm Extended

---

## 1. Maximum Sum Subarray — Print the Subarray

**Problem:** Find the contiguous subarray with the largest sum **and print its start and end indices**.

This extends Day 4's Kadane's Algorithm to also track **which** subarray gives the maximum sum.

```
Input:  nums = [5, 4, -1, 7, 8]
Output: Sum = 23, Subarray = [0, 4]  (the entire array)
```

**Approach:** Same as Kadane's, but maintain `start`/`end` variables:
- When `sum > maxsum`, record the current window `[start, i]` as the best
- When `sum < 0`, reset and move `start` to `i + 1`

```java
class Demo {
    public static void main(String[] args) {
        int nums[] = {5, 4, -1, 7, 8};
        int sum = 0, maxsum = Integer.MIN_VALUE;
        int start = 0;
        int maxstart = 0, maxend = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (sum > maxsum) {
                maxsum = sum;
                maxstart = start;
                maxend = i;
            }
            if (sum < 0) {
                sum = 0;
                start = i + 1;
            }
        }
        System.out.println("Max Sum: " + maxsum);
        System.out.println("Subarray: [" + maxstart + ", " + maxend + "]");
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

> 💡 **Extra Tips:**
> - The key difference from basic Kadane's: update `maxstart` and `maxend` **when a new max is found**, and update `start` **when the sum resets**.
> - **Edge case:** If all elements are negative, the algorithm correctly picks the single largest (least negative) element.
> - **Interview tip:** Always ask if you need to **return the sum**, **return the subarray**, or **return the indices** — each variant has slightly different tracking logic.

---

### 🔁 Kadane's Algorithm — Full Cheat Sheet

```
Standard Kadane's:
┌──────────────────────────────────────────────────┐
│  sum = 0, maxSum = -∞                            │
│                                                  │
│  for each element:                               │
│    sum += element                                │
│    maxSum = max(maxSum, sum)   ← record FIRST    │
│    if sum < 0: sum = 0        ← reset SECOND     │
│                                                  │
│  return maxSum                                   │
└──────────────────────────────────────────────────┘

With indices:
┌──────────────────────────────────────────────────┐
│  sum = 0, maxSum = -∞                            │
│  start = 0, maxStart = 0, maxEnd = 0             │
│                                                  │
│  for i in range(n):                              │
│    sum += arr[i]                                 │
│    if sum > maxSum:                              │
│      maxSum = sum                                │
│      maxStart = start                            │
│      maxEnd = i                                  │
│    if sum < 0:                                   │
│      sum = 0                                     │
│      start = i + 1                               │
│                                                  │
│  return maxSum, arr[maxStart..maxEnd]             │
└──────────────────────────────────────────────────┘
```

---

## 📝 Day 5 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Max Sum Subarray (with indices) | Kadane's Algorithm (Extended) | O(n) | O(1) |
