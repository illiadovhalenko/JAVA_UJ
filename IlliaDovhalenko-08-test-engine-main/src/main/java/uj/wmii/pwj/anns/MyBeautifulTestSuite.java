package uj.wmii.pwj.anns;

public class MyBeautifulTestSuite {

    @MyTest
    public void testSoemthing() {
        System.out.println("I'm testing something!");
    }

    @MyTest(params = {"a param", "b param", "c param. Long, long C param."})
    public void testWithParam(String param) {
        System.out.printf("I was invoked with parameter: %s\n", param);
    }

    public void notATest() {
        System.out.println("I'm not a test.");
    }

    @MyTest
    public void imFailue() {
        System.out.println("I AM EVIL.");
        throw new NullPointerException();
    }

    @MyTest(params = {"10", "11"}, expectedResult = "21")
    public int addTest(int x, int y){
        return x+y;
    }
    @MyTest(params = {"10", "11"}, expectedResult = "22")
    public int addTestFail(int x, int y){
        return x+y;
    }

    @MyTest(params = {"one", "two"}, expectedResult = "onetwo")
    public String addTestString(String a, String b){
        return a + b;
    }

    @MyTest(params = {"one", "two"}, expectedResult = "onetwoD")
    public String addTestStringFail(String a, String b){
        return a + b;
    }
    @MyTest(params = {"one", "32"}, expectedResult = "one32")
    public String addDifferentType(String a, int b){
        return a + b;
    }
}
