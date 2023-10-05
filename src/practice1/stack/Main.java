package practice1.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        List<Integer> stack = new ArrayList<>();
        Random random = new Random();

        for(int i=0; i< 10; i++) {
            stack.add(random.nextInt());
        }

        Solution sol = new Solution(stack);
        sol.push(4);
        sol.begin();
        sol.push(7);
        sol.begin();
        sol.push(2);
        assert(sol.rollback());
        assert (sol.top() == 7);
        sol.begin();
        sol.push(10);
        assert (sol.commit());

    }

    private static class Solution {
        private List<Integer> stack;
        private boolean isInprogress = false;

        public Solution(List<Integer> stack) {
            this.stack = stack;
        }

        public void push(Integer value) {
            stack.add(value);
        }

        public void pop() {
            if(!stack.isEmpty()) {
                stack.remove(stack.size() - 1);
            }
        }

        public Integer top() {
           if (stack.isEmpty()) {
               return 0;
           }
           return stack.get(0);
        }

        public void begin() {
            if (isInprogress) {
                return;
            }
            isInprogress = true;
        }

        public boolean rollback() {
            return false;
        }

        public boolean commit() {
            return false;
        }
    }

}
