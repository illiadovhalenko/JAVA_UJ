package uj.wmii.pwj.exec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.converter.ConvertWith;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExecServiceTest {

    @Test
    void testExecute() {
        MyExecService s = MyExecService.newInstance();
        TestRunnable r = new TestRunnable();
        s.execute(r);
        doSleep(10);
        assertTrue(r.wasRun);
    }

    @Test
    void testScheduleRunnable() {
        MyExecService s = MyExecService.newInstance();
        TestRunnable r = new TestRunnable();
        s.submit(r);
        doSleep(10);
        assertTrue(r.wasRun);
    }

    @Test
    void testScheduleRunnableWithResult() throws Exception {
        MyExecService s = MyExecService.newInstance();
        TestRunnable r = new TestRunnable();
        Object expected = new Object();
        Future<Object> f = s.submit(r, expected);
        doSleep(10);
        assertTrue(r.wasRun);
        assertTrue(f.isDone());
        assertEquals(expected, f.get());
    }

    @Test
    void testScheduleCallable() throws Exception {
        MyExecService s = MyExecService.newInstance();
        StringCallable c = new StringCallable("X", 10);
        Future<String> f = s.submit(c);
        doSleep(20);
        assertTrue(f.isDone());
        assertEquals("X", f.get());
    }

    @Test
    void testShutdown() {
        ExecutorService s = MyExecService.newInstance();
        s.execute(new TestRunnable());
        doSleep(10);
        s.shutdown();
        assertThrows(
            RejectedExecutionException.class,
            () -> s.submit(new TestRunnable()));
    }

    @Test
    void testShutdown2(){
        ExecutorService s = MyExecService.newInstance();
        int numberOfTasks=5;
        for(int i=1; i<=numberOfTasks; i++){
            s.submit(new StringCallable("Task "+ i, i*100));
        }
        doSleep(200);
        s.shutdown();
        doSleep(1000);
        var notCompletedTasks = s.shutdownNow();
        assertTrue(notCompletedTasks.isEmpty());
    }

    @Test
    void testShutdownNow() {
        ExecutorService s = MyExecService.newInstance();
        int numberOfTasks=5;
        for(int i=1; i<=numberOfTasks; i++){
            s.submit(new StringCallable("Task "+ i, i*100));
        }
        doSleep(200);
        var notCompletedTasks = s.shutdownNow();
        System.out.println(notCompletedTasks.size());
        assertTrue(s.isShutdown());
        assertFalse(notCompletedTasks.isEmpty());
    }
    @Test
    void isTerminated(){
        ExecutorService s = MyExecService.newInstance();
        int numberOfTasks=2;
        for(int i=1; i<=numberOfTasks; i++){
            s.submit(new StringCallable("Task "+ i, i*10));
        }
        doSleep(1000);
        s.shutdown();
        doSleep(100);
        assertTrue(s.isTerminated());
    }
    @Test
    void testInvokeAll()throws ExecutionException, InterruptedException {
        MyExecService execService = MyExecService.newInstance();
        int numberOfTasks = 10;
        List<Callable<String>> tasks = new ArrayList<>();
        for(int i=1; i<=numberOfTasks; i++){
            tasks.add(new StringCallable("Task "+ i, i*10));
        }
        List<Future<String>> result = execService.invokeAll(tasks);
        assertNotNull(result);
        assertEquals(result.size(), tasks.size());
        boolean bool = true;
        Thread.sleep(20);
//        for(int i=1; i<=numberOfTasks; i++){
//            assertTrue(result.get(i-1).isDone());
//            String s =result.get(i-1).get();
//            if (!s.equals("Task " + i)) {
//                bool = false;
//                break;
//            }
//        }
        assertTrue(!result.isEmpty());
    }
    @Test
    void testInvokeAllTimeOut() throws InterruptedException {
        MyExecService execService = MyExecService.newInstance();
        int numberOfTasks = 10;
        List<Callable<String>> tasks = new ArrayList<>();
        for(int i=1; i<=numberOfTasks; i++){
            tasks.add(new StringCallable("Task "+ i, i*100));
        }
        List<Future<String>> result = execService.invokeAll(tasks, 1000,TimeUnit.MILLISECONDS);
        doSleep(1000);
        execService.shutdownNow();
        assertTrue(execService.isShutdown());
        doSleep(10);
        assertTrue(execService.isTerminated());
        assertFalse(result.isEmpty());
    }

    @Test
    void testInvokeAnyTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        MyExecService execService = MyExecService.newInstance();
        int numberOfTasks = 10;
        List<Callable<String>> tasks = new ArrayList<>();
        for(int i=1; i<=numberOfTasks; i++){
            tasks.add(new StringCallable("Task "+ i, i*100));
        }
        String result = execService.invokeAny(tasks, 150, TimeUnit.MILLISECONDS);
        //doSleep(1);
        var notCompletedTask = execService.shutdownNow();
        assertTrue(execService.isShutdown());
        doSleep(10);
        assertTrue(execService.isTerminated());
        assertNotNull(result);
        System.out.println(result);
        boolean bool = false;
        for(int i=1; i<=numberOfTasks; i++){
            if (result.equals("Task " + i)) {
                bool = true;
                break;
            }
        }
        assertTrue(bool);
    }
    @Test
    void testInvokeAny() throws ExecutionException, InterruptedException {
        MyExecService execService = MyExecService.newInstance();
        int numberOfTasks = 10;
        List<Callable<String>> tasks = new ArrayList<>();
        for(int i=1; i<=numberOfTasks; i++){
            tasks.add(new StringCallable("Task "+ i, i*50));
        }
        String result = execService.invokeAny(tasks);
        assertNotNull(result);
        boolean bool = false;
        for(int i=1; i<=numberOfTasks; i++){
            if (result.equals("Task " + i)) {
                bool = true;
                break;
            }
        }
        assertTrue(bool);
    }
    static void doSleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

class StringCallable implements Callable<String> {

    private final String result;
    private final int milis;

    StringCallable(String result, int milis) {
        this.result = result;
        this.milis = milis;
    }

    @Override
    public String call() throws Exception {
        ExecServiceTest.doSleep(milis);
        return result;
    }
}
class TestRunnable implements Runnable {

    boolean wasRun;
    @Override
    public void run() {
        wasRun = true;
    }
}
