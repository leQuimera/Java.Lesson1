import Annotations.AfterSuite;
import Annotations.BeforeSuite;
import Annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestRunner {

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("[ERROR] Invalid command line: one and one only filename expected");
            System.exit(1);
        }
        runTests(Class.forName(args[0]));
    }


    private static void runTests(Class testClass) throws InvocationTargetException, IllegalAccessException {

        Method[] methods = testClass.getMethods();

        List<Method> beforeTestFunction = new ArrayList<>();
        List<Method> afterTestFunction = new ArrayList<>();
        HashMap<Method, Integer> testFunction = new HashMap<Method, Integer>();

        for(Method mth: methods){
            if(mth.getAnnotations().length <= 1) {
                if(mth.isAnnotationPresent(Test.class)) testFunction.put(mth, mth.getAnnotation(Test.class).priority());
                if(mth.isAnnotationPresent(AfterSuite.class) && Modifier.isStatic(mth.getModifiers())) afterTestFunction.add(mth);
                if(mth.isAnnotationPresent(BeforeSuite.class) && Modifier.isStatic(mth.getModifiers())) beforeTestFunction.add(mth);
            }
            else {
                System.out.println("Слишком много аннотаций для метода " + mth.getName());
            }
        }

        runBeforeTestAnnotation(beforeTestFunction);

        testFunction.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(t -> System.out.println("Тест с рейтом " + t.getValue() + " - Название " + t.getKey().getName()));

        runAfterTestAnnotation(afterTestFunction);
    }

    private static void runBeforeTestAnnotation(List<Method> bfrTest) throws InvocationTargetException, IllegalAccessException {
        if(!bfrTest.isEmpty()) {
            if (bfrTest.size() == 1) {
                bfrTest.get(0).invoke(null);
            } else
                System.out.println("Аннотаций типа beforeSuite больше двух " + bfrTest.size());
        }
    }

    private static void runAfterTestAnnotation(List<Method> afrTest) throws InvocationTargetException, IllegalAccessException {
        if(!afrTest.isEmpty()) {
            if (afrTest.size() == 1) {
                afrTest.get(0).invoke(null);
            } else
                System.out.println("Аннотаций типа afterSuite больше двух " + afrTest.size());
        }
    }


}
