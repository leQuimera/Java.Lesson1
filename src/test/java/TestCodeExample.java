import Annotations.AfterSuite;
import Annotations.BeforeSuite;
import Annotations.Test;

public class TestCodeExample {

    @BeforeSuite
    public static void test1() {
        System.out.println("BeforeSuite");
    }

    @AfterSuite
    public static void test5() {
        System.out.println("AfterSuite");
    }

    @Test(priority = 2)
    public void test2() {
        System.out.println("test2");
    }

    @Test
    public void test3() {
        System.out.println("test3");
    }

    public void test4() {
        System.out.println("test4");
    }

    @Test
    public void test6() {
        System.out.println("test6");
    }

    @Test(priority = 10)
    public void test7() {
        System.out.println("test7");
    }

    @Test(priority = 8)
    public void test8() {
        System.out.println("test8");
    }

    @Test(priority = 4)
    public void test9() {
        System.out.println("test9");
    }

    @Test(priority = 4)
    public void test10() {
        System.out.println("test10");
    }

}
