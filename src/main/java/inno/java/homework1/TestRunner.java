package inno.java.homework1;

import inno.java.homework1.Annotations.AfterSuite;
import inno.java.homework1.Annotations.BeforeSuite;
import inno.java.homework1.Annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


public class TestRunner {

    public static void main(String[] args) throws Exception {
        runTests(TestCodeExample.class);
    }


    private static void runTests(Class testClass) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {

        Object testInstance = testClass.getDeclaredConstructor().newInstance();
        Method[] methods = testClass.getMethods();

        List<Method> beforeTestFunctions = new ArrayList<>();
        List<Method> afterTestFunctions = new ArrayList<>();
        HashMap<Method, Integer> testFunctions = new HashMap<>();

        for(Method mth: methods){
                if(mth.isAnnotationPresent(Test.class)){
                    if(mth.getAnnotation(Test.class).priority() <= 10 && mth.getAnnotation(Test.class).priority() > 0)
                        testFunctions.put(mth, mth.getAnnotation(Test.class).priority());
                    else throw new IllegalArgumentException("Приоритет тестов должен находиться в интервале от 1 до 10");
                }
                if(mth.isAnnotationPresent(AfterSuite.class) && Modifier.isStatic(mth.getModifiers())) afterTestFunctions.add(mth);
                if(mth.isAnnotationPresent(BeforeSuite.class) && Modifier.isStatic(mth.getModifiers())) beforeTestFunctions.add(mth);
        }

        runSuitedAnnotation(beforeTestFunctions);
        runTestsAnnotation(testFunctions, testInstance);
        runSuitedAnnotation(afterTestFunctions);
    }

    private static void runTestsAnnotation(HashMap<Method, Integer> tests, Object testInstance) {
        tests.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(t -> {
                    try {
                        System.out.println("Выполняется кейс с приоритетом " + t.getValue());
                        t.getKey().invoke(testInstance);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void runSuitedAnnotation(List<Method> annotationList) throws InvocationTargetException, IllegalAccessException {
        if(!annotationList.isEmpty()) {
            if (annotationList.size() == 1) {
                annotationList.get(0).invoke(null);
            } else
                throw new IllegalArgumentException("Аннотаций больше двух " + annotationList.get(0).getName());
        }
    }

}
