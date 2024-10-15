import java.util.Scanner;

// Node class for linked list implementation
class Node {
    char data;
    Node next;

    Node(char data) {
        this.data = data;
        this.next = null;
    }
}

// Custom Stack class using linked list
class CharStack {
    private Node top;

    CharStack() {
        this.top = null;
    }

    void push(char data) {
        Node newNode = new Node(data);
        newNode.next = top;
        top = newNode;
    }

    char pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        char data = top.data;
        top = top.next;
        return data;
    }

    boolean isEmpty() {
        return top == null;
    }

    char peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return top.data;
    }
}

// Custom Queue class using linked list
class CharQueue {
    private Node front, rear;

    CharQueue() {
        this.front = this.rear = null;
    }

    void enqueue(char data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
    }

    char dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        char data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        return data;
    }

    boolean isEmpty() {
        return front == null;
    }
}

public class PostfixEvaluator {
    private static int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> -1;
        };
    }

    private static boolean isOperand(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        CharStack stack = new CharStack();
        CharQueue queue = new CharQueue();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (isOperand(c)) {
                queue.enqueue(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    queue.enqueue(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop(); // Discard '('
                } else {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            } else { // Operator
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    queue.enqueue(stack.pop());
                }
                stack.push(c);
            }
        }

        // Pop remaining operators from stack
        while (!stack.isEmpty()) {
            char topOperator = stack.pop();
            if (topOperator == '(') {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            queue.enqueue(topOperator);
        }

        // Build postfix expression from queue
        while (!queue.isEmpty()) {
            postfix.append(queue.dequeue());
        }

        return postfix.toString();
    }


    public static int evaluatePostfix(String postfix) {
        CharStack stack = new CharStack();

        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            if (isOperand(c)) {
                stack.push((char) (c - '0')); // Convert char to int
            } else {
                if (stack.isEmpty()) {
                    throw new IllegalStateException("Not enough operands for operator");
                }
                int operand2 = stack.pop();
                if (stack.isEmpty()) {
                    throw new IllegalStateException("Not enough operands for operator");
                }
                int operand1 = stack.pop();
                switch (c) {
                    case '+':
                        stack.push((char) (operand1 + operand2));
                        break;
                    case '-':
                        stack.push((char) (operand1 - operand2));
                        break;
                    case '*':
                        stack.push((char) (operand1 * operand2));
                        break;
                    case '/':
                        stack.push((char) (operand1 / operand2));
                        break;
                    case '^':
                        stack.push((char) Math.pow(operand1, operand2));
                        break;
                }
            }
        }

        if (stack.isEmpty()) {
            throw new IllegalStateException("Not enough operands for operator");
        }

        return stack.pop();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter infix expression: ");
        String infix = scanner.nextLine();

        // Part 1: Convert infix to postfix
        String postfix = infixToPostfix(infix);
        System.out.println("Postfix expression: " + postfix);

        // Part 2: Evaluate postfix expression
        int result = evaluatePostfix(postfix);
        System.out.println("Result of evaluation: " + result);
    }
}