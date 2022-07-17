package com.alhalel.topten.leetcode;

public class MinCostClimbingStairs {

    public static void main(String[] args) {
        System.out.println(minCostClimbingStairs(new int[]{10, 15, 20})); // 15
        System.out.println(minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1})); // 6
        //System.out.println(minCostClimbingStairs(new int[]{4, 5, 3, 1, 1, 100, 1, 1, 100, 1})); // 10
        System.out.println(minCostClimbingStairs(new int[]{0, 1, 1, 0})); // 1
        System.out.println(minCostClimbingStairs(new int[]{0, 1, 2, 2})); // 2
    }

    public static int minCostClimbingStairs(int[] cost) {
        for (int i = 2; i < cost.length; i ++) {
            cost[i] += Math.min(cost[i - 1], cost[i - 2]);
        }
        return Math.min(cost[cost.length - 1], cost[cost.length - 2]);
    }

}
