# Day 6 — Arrays: Greedy & Rearrangement

---

## 1. Best Time to Buy and Sell Stock

**Problem:** Given daily stock prices, find the maximum profit from a single buy-sell transaction.

### Approach 1: Traverse Backward

Track the highest selling price seen so far (from the right). For each day, compute profit.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int profit = 0;
        int sell = prices[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            if (prices[i] > sell) sell = prices[i];
            else {
                profit = Math.max(profit, sell - prices[i]);
            }
        }
        return profit;
    }
}
```

### Approach 2: Traverse Forward (Cleaner ✨)

Track the minimum buying price seen so far (from the left). For each day, compute profit.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int buy = prices[0], profit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < buy) buy = prices[i];
            else {
                profit = Math.max(profit, prices[i] - buy);
            }
        }
        return profit;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(1) |

> 💡 **Extra Tips:**
> - Both approaches are essentially the same idea — one scans left-to-right tracking `minBuy`, the other right-to-left tracking `maxSell`. The forward approach is more intuitive.
> - **Key insight:** You want to buy at the **lowest point before** the **highest selling point**.
> - **Follow-ups:**
>   - *Buy & Sell Stock II* — unlimited transactions → sum all positive differences
>   - *Buy & Sell Stock III* — at most 2 transactions → use DP with states
>   - *Buy & Sell Stock with Cooldown* — must wait 1 day after selling → state machine DP

---

## 2. Rearrange Array Elements by Sign

**Problem:** Rearrange so that positive and negative elements alternate. Positives at even indices, negatives at odd indices. Preserve relative order.

```
Input:  nums = [3, 1, -2, -5, 2, -4]
Output: [3, -2, 1, -5, 2, -4]
```

### Approach 1: Separate into Two Lists

```java
class Solution {
    public int[] rearrangeArray(int[] nums) {
        List<Integer> pos = new ArrayList<>();
        List<Integer> neg = new ArrayList<>();

        for (int i : nums) {
            if (i >= 0) pos.add(i);
            else neg.add(i);
        }

        int p = 0, n = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i % 2 == 0) nums[i] = pos.get(p++);
            else nums[i] = neg.get(n++);
        }
        return nums;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) |

### Approach 2: Single-Pass with Result Array (Optimal ✨)

Use two index pointers — `posIndex` starts at 0 (even), `negIndex` starts at 1 (odd). Both jump by 2.

```java
class Solution {
    public int[] rearrangeArray(int[] nums) {
        int posIndex = 0;
        int negIndex = 1;
        int result[] = new int[nums.length];

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                result[posIndex] = nums[i];
                posIndex += 2;
            } else {
                result[negIndex] = nums[i];
                negIndex += 2;
            }
        }
        return result;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) — single pass |
| **Space** | O(n) — for result array |

> 💡 **Extra Tips:**
> - Approach 2 is cleaner because it processes everything in **one pass** — no need to split and merge.
> - This problem **guarantees** equal numbers of positives and negatives. If not guaranteed, you'd need to handle remaining elements separately.
> - **Pattern:** When placing elements at specific index patterns (even/odd), use **index counters that jump by a fixed step**.

---

## 3. Leaders in an Array

**Problem:** An element is a **leader** if it is greater than or equal to all elements to its right. The rightmost element is always a leader.

**Approach:** Traverse from **right to left**, tracking the current maximum.

```java
class Solution {
    public List<Integer> leaders(int[] nums) {
        List<Integer> ans = new ArrayList<>();

        int mx = nums[nums.length - 1];
        ans.add(mx);

        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] >= mx) {
                mx = nums[i];
                ans.add(nums[i]);
            }
        }

        Collections.reverse(ans);
        return ans;
    }
}
```

| Complexity | Value |
|------------|-------|
| **Time** | O(n) |
| **Space** | O(n) — for the result (O(1) extra) |

> 💡 **Extra Tips:**
> - Traversing **right-to-left** is the key trick — it lets you know the max of all elements to the right in O(1).
> - The reverse at the end is needed because we collected leaders from right to left.
> - **Similar pattern:** Next Greater Element, Stock Span — many problems benefit from **backward traversal** or using a **monotonic stack**.

---

## 📝 Day 6 Summary

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Buy & Sell Stock | Greedy (Track Min/Max) | O(n) | O(1) |
| 2 | Rearrange by Sign | Index Jumping / Partitioning | O(n) | O(n) |
| 3 | Leaders in Array | Right-to-Left Traversal | O(n) | O(n) |
