package shop.mtcoding.blog;

public class test {
    public static void main(String[] args) {
        int a = 8;
        int b = a/3;
        int c = a%3;
        System.out.println(b);
        System.out.println(c);
        if(a%3==0){
            System.out.println(b);
        }else if(a%3!=0){
            System.out.println(b+1);
        }
    }
}
