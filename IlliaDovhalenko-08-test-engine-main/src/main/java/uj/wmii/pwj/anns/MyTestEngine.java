package uj.wmii.pwj.anns;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyTestEngine {

    private final String className;
    private static void asciiiArt(){
        System.out.println("""
                                
                .-.   .-..-.  .-.    .----..-. .-..----. .----. .----. .----..----..----..----. .----.     .---. .----. .----..---. .----..-. .-. .---. .-..-. .-..----.
                |  `.'  | \\ \\/ /    { {__  | { } || {}  }| {}  }| {}  }| {_  | {_  | {_  | {}  }| {}  }   {_   _}| {_  { {__ {_   _}| {_  |  `| |/   __}| ||  `| || {_ \s
                | |\\ /| |  }  {     .-._} }| {_} || .--' | .--' | .--' | {__ | {__ | {__ | .-. \\| .-. \\     | |  | {__ .-._} } | |  | {__ | |\\  |\\  {_ }| || |\\  || {__\s
                `-' ` `-'  `--'     `----' `-----'`-'    `-'    `-'    `----'`----'`----'`-' `-'`-' `-'     `-'  `----'`----'  `-'  `----'`-' `-' `---' `-'`-' `-'`----'
                                
                """);
    }
    public static void main(String[] args) {
        asciiiArt();
        if (args.length < 1) {
            System.out.println("Please specify test class name");
            System.exit(-1);
        }
        String className = args[0].trim();
        System.out.printf("Testing class: %s\n", className);
        MyTestEngine engine = new MyTestEngine(className);
        engine.runTests();
    }

    public MyTestEngine(String className) {
        this.className = className;
    }

    public void runTests() {
        final Object unit = getObject(className);
        List<Method> testMethods = getTestMethods(unit);
        int successCount = 0;
        int failCount = 0;
        int errorCount = 0;
        for (Method m: testMethods) {
            TestResult result = launchSingleMethod(m, unit);
            if (result == TestResult.SUCCESS) successCount++;
            else if (result == TestResult.FAIL) failCount++;
            else errorCount++;
        }
        System.out.printf("\nEngine launched %d tests.\n", testMethods.size());
        System.out.printf("%d of them passed, %d failed(%d with error)\n", successCount, failCount+errorCount, errorCount);
    }

    private TestResult launchSingleMethod(Method method, Object unit) {
        try {
            System.out.println("\nTesting method: "+ method.getName());
            String[] params = method.getAnnotation(MyTest.class).params();
            String expectedResult = method.getAnnotation(MyTest.class).expectedResult();
            if (params.length == 0) {
                Object methodResult = method.invoke(unit);
                return resultCheck(methodResult, expectedResult, method);
            } else {
                var Types = method.getParameterTypes();
                Object[] paramsConv = new Object[Types.length];
                try {
                    paramsConv = convertParams(Types, params);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                try {
                    Object methodResult = method.invoke(unit, paramsConv);
                    return resultCheck(methodResult, expectedResult, method);
                }catch (Exception e){
                    System.out.println("Tested method: "+ method.getName()+". Error.");
                    return TestResult.ERROR;
                }
            }
        } catch (Exception e) {
            System.out.println("Tested method: "+ method.getName()+". Error.");
            return TestResult.ERROR;
        }
    }
    private static Object[] convertParams(Class<?>[] Types, String[] params) throws Exception {
        Object[] paramsConv = new Object[Types.length];
        for(int i = 0; i < Types.length; i++){
            System.out.println(Types[i].getName());
            switch (Types[i].getName()){
                case "int"-> paramsConv[i]=Integer.parseInt(params[i]);
                case "boolean"-> paramsConv[i]=Boolean.parseBoolean(params[i]);
                case "double"-> paramsConv[i]=Double.parseDouble(params[i]);
                case "java.lang.String"->paramsConv[i]=params[i];
                case "char"->paramsConv[i]=params[i].charAt(0);
                default -> throw new Exception("Incorrect type of parameter");
            }
        }
        return paramsConv;
    }
    private static List<Method> getTestMethods(Object unit) {
        Method[] methods = unit.getClass().getDeclaredMethods();
        return Arrays.stream(methods).filter(
                m -> m.getAnnotation(MyTest.class) != null).collect(Collectors.toList());
    }
    public TestResult resultCheck(Object methodResult, String result, Method method){
        if(methodResult == null && (result.equals("null") || result.isEmpty())){
            System.out.println("Tested method: "+ method.getName()+". Success.");
            return TestResult.SUCCESS;
        }
        if(methodResult.toString().equals(result)){
            System.out.println("Tested method: "+ method.getName()+". Success.");
            return TestResult.SUCCESS;
        }else{
            System.out.println("Tested method: "+ method.getName()+". Fail. Expected: "+ result+", but was: "+ methodResult);
            return TestResult.FAIL;
        }
    }
    private static Object getObject(String className) {
        try {
            Class<?> unitClass = Class.forName(className);
            return unitClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return new Object();
        }
    }
}
