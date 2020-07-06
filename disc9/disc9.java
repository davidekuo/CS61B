// Question 2

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}

public static boolean isBSTGood(TreeNode T) {
    return isBSTHelper(T, Integer.MIN_VALUE, Integer.MAX_VALUE);
}

public static boolean isBSTHelper(TreeNode T, int lowerBound, int upperBound) {
    if (T == null)
        return true;

    if (T.val < lowerBound || T.val > upperBound)
        return false;

    return isBSTHelper(T.left, lowerBound, T.val) && isBSTHelper(T.right, T.val, upperBound)

    /*
        Value for each node must stay within bounds set by its upstream nodes
        For root node (which has no upstream nodes), bounds are Integer.MIN_VALUE and Integer.MAX_VALUE
        Given a valid node A with lower bound LB and upper bound UB,
            A.left will inherit A's lower bound LB but have a NEW upper bound of A.val
                because in a valid BST, everything to the left of a node must have a value less than the value of that node
            A.right will have NEW lower bound of A.val and inherit A's upper bound UB
                because in a valid BST, everything to the right of a node must have a value greater than the value of that node
    */
}

