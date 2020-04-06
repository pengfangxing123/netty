package com.netty.datastructure.stack;

import com.google.common.collect.Lists;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Stack;

/**
 * @author 86136
 */
public class Calculator {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        //自定义计算器计算
        //String expression="(199+2)*(6-99)+(18693+1)";
        //int calculate = calculator.calculate(expression);
        //System.out.println("自定义计算器计算值："+calculate);

        //中缀表达式转为 list
        String expression="1+((2+3)*4)-5";
        List<String> list = calculator.toInfixExpressList(expression);
        System.out.println("中缀表达式转为 list："+list);

        //中缀转后缀
        List<String> suffix = calculator.Cent2Suffix(list);
        System.out.println("后缀表达式："+suffix);

        //后缀表达式计算
        //int i = calculator.polanNotation(suffix);
        //System.out.println("后缀表达式计算:"+i);
    }

    /**
     * 逆波兰表达式计算
     * @return
     */
    public int polanNotation(List<String> list){
        Stack<Integer> numStack = new Stack<>();
        list.forEach(p->{
            if(p.matches("\\d+")){
                numStack.push(Integer.parseInt(p));
            }else{
                Integer num1= numStack.pop();
                Integer num2= numStack.pop();

                int cal = this.cal(num1, num2, p.charAt(0));
                numStack.push(cal);
            }
        });
        return numStack.pop();
    }

    /**
     * 中缀表达式转后缀表达式
     * 1）初始化两个栈： 运算符栈s1 和储存中间结果的栈s2 ：
     * 2）从左至右扫描中缀表达式；
     * 3）遇到操作数时， 将其压s2 ：
     * 4）遇到运算符时， 比较其与s1 栈顶运算符的优先级：
     *      1.如果s1 为空， 或栈顶运算符为左括号“ （ ” ， 则直接将此运算符入栈，
     *      2.否则， 若优先级比栈顶运算符的高， 也将运算符压入s1 ；
     *      3.否则， 将s1 栈顶的运算符弹出并压入到s2 中， 再次转到（ 4 ． I) 与s1 中新的栈顶运算符相比较；
     * 5）遇到括号时：
     *      1.如果是左括号“ （ ” ， 则直接压入s1
     *      2.如果是右括号“ ） ” ， 则依次弹出s1 栈顶的运算符， 并压入s2 ， 直到遇到左括号为止， 此时将这一对括号去弃
     * 6）重复步骤2 至5 ， 直到表达式的最右边
     * 7）将s1 中剩余的运算符依次弹出并压入s2
     * 8）依次弹出s2 中的元素并输出， 结果的逆序即为中缀表达式对应的后缀表达式
     * 1+1+1+2*2=>11+1+22*+
     * @return
     */
    public List<String> Cent2Suffix(List<String> list){
        Stack<String> s1=new Stack<>();
        //s2因为没有Pop操作，最后结果还要逆序输出。所以用List替换
        List<String> s2=Lists.newArrayList();

        list.forEach(p->{
            //如果为数字，加入到s2
            if(p.matches("\\d+")){
                s2.add(p);
            }else if ("(".equals(p)){
                s1.push(p);
            }else if (")".equals(p)){
                while (!"(".equals(s1.peek())){
                    s2.add(s1.pop());
                }
                //将( 弹出
                s1.pop();
            }else{
                //4.3
                //因为'（'在运算符优先级中为-1，所以不需要加 &&!"(".equals(s1.peek())这个判断
                while (s1.size()!=0 && priority(s1.peek())>=priority(p)){
                    s2.add(s1.pop());
                }
                s1.push(p);
            }
        });

        while (s1.size()>0){
            s2.add(s1.pop());
        }
        return s2;
    }

    /**
     * 将计算字符串转为list
     * @param s
     * @return
     */
    public List<String> toInfixExpressList(String s){
        List<String> list=Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        char[] chars = s.toCharArray();
        for(int i=0;i<chars.length;i++){
            char c=chars[i];
            if(c<48||c>57){
                //非数字
                list.add(String.valueOf(c));
            }else{
                //数字
                int j=i+1;
                if(j<chars.length){
                    char c1 = chars[j];
                    if (c1<48||c1>57) {
                        list.add(builder+String.valueOf(c));
                        builder.delete(0,builder.length());
                    }else{
                        builder.append(c);
                    }
                }else {
                    builder.append(c);
                    list.add(builder.toString());
                    builder = new StringBuilder();
                }
            }
        }
        return list;
    }

    /**
     * 自定义计算器
     * 带括号(199+2)*(6-99)+(18693+1)
     * @param expression
     * @return
     */
    public int calculate (String expression){
        ArrayStack1<Integer> numStack = new ArrayStack1(10,Integer.class);
        ArrayStack1<Character> operStack = new ArrayStack1(10,Character.class);

        boolean flage=false;
        StringBuilder keepNum = new StringBuilder();
        StringBuilder batBuilder = new StringBuilder();
        for(int i=0;i<expression.length();i++){
            char c = expression.charAt(i);

            //单括号就是把括号内内容取取来计算
            //括号内容处理开始
            if(c=='('){
                flage=true;
                continue;
            }
            if(c==')'){
                flage=false;
                int calculate = calculate(batBuilder.toString());
                batBuilder.delete(0,batBuilder.length());
                numStack.push(calculate);
                continue;
            }
            if(flage){
                batBuilder.append(c);
                continue;
            }
            //括号内容处理结束

            if(this.isOper(c)){
                //运算符
                if(operStack.isEmpty()){
                    operStack.push(c);
                }else{
                    if(this.priority(c)<=this.priority(operStack.peek())){
                        //当前符号优先级小于等于栈顶符号，先运行栈顶的符号运算
                        int num1 = numStack.poll();
                        int num2 = numStack.poll();
                        char oper = operStack.poll();
                        int cal = this.cal(num1, num2, oper);
                        numStack.push(cal);
                        operStack.push(c);
                    }else{
                        operStack.push(c);
                    }
                }
            }else{
                //数字
                keepNum.append(c);
                int cur=i;
                if(++cur<expression.length()){
                    char next = expression.charAt(cur);
                    if(this.isOper(next)){
                        numStack.push(Integer.parseInt(keepNum.toString()));
                        keepNum.delete(0,keepNum.length());
                    }
                }else{
                    //最后一位，直接放入栈
                    numStack.push(Integer.parseInt(keepNum.toString()));
                    keepNum.delete(0,keepNum.length());
                }
            }
        }

        char curOper;
        while (!operStack.isEmpty()) {
            curOper = operStack.poll();
            int num1 = numStack.poll();
            int num2 = numStack.poll();
            int cal = this.cal(num1, num2, curOper);
            numStack.push(cal);
        }
        return numStack.poll();
    }


    /**
     * 定义符号优先级
     * @param oper 这里用Int
     * @return
     */
    public int priority(char oper){
        if(oper=='*' || oper=='/'){
            return 1;
        }else if(oper=='+' || oper=='-'){
            return 0;
        }else {
            return -1;
        }
    }

    /**
     * 定义符号优先级
     * @param oper 这里用Int
     * @return
     */
    public int priority(String oper){
        if("*".equals(oper) || "/".equals(oper)){
            return 1;
        }else if("+".equals(oper) || "-".equals(oper)){
            return 0;
        }else {
            return -1;
        }
    }

    public boolean isOper(char val){
        return val == '*' || val == '/' || val == '+' || val == '-';
    }

    /**
     * 计算
     * @param num1 栈顶元素
     * @param num2
     * @param oper
     * @return
     */
    public int cal(int num1, int num2,char oper){
        System.out.println(num2+"-"+num1+"-"+oper);
        int res=0;
        switch (oper){
            case '+':
                res=num2+num1;
                break;
            case '-':
                res=num2-num1;
                break;
            case '*':
                res=num2*num1;
                break;
            case '/':
                res=num2/num1;
                break;
            default:
                break;
        }
        return res;
    }

}

class ArrayStack1<T>{
    /**
     * 栈大小
     */
    private int maxSize;

    /**
     * 数据存放数组
     */
    private T[] stackArray;

    /**
     * 栈顶
     */
    private int top =-1;

    public ArrayStack1(int maxSize, Class<T> clz) {
        this.maxSize = maxSize;
        this.stackArray = (T[]) Array.newInstance(clz,maxSize);
    }

    public boolean isFull(){
        return top==maxSize-1;
    }

    public boolean isEmpty(){
        return top==-1;
    }

    public void push(T value){
        if(isFull()){
            System.out.println("栈已经满了");
            return ;
        }
        top++;
        stackArray[top]=value;
    }

    public T poll(){
        if(isEmpty()){
            System.out.println("栈是空的");
            throw new RuntimeException("栈是空的");
        }
        T t = stackArray[top];
        top--;
        return t;

    }

    /**
     * 遍历要从栈顶开始
     */
    public void list(){
        if(isEmpty()){
            System.out.println("栈是空的");
            return;
        }

        for(int i=top;i>-1;i--){
            System.out.println(stackArray[i]);
        }
    }

    public T peek() {
        if(isEmpty()){
            System.out.println("栈是空的");
            throw new RuntimeException("栈是空的");
        }
        return stackArray[top];
    }
}
