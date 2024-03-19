package uj.wmii.pwj.exec;

import java.util.*;
import java.util.concurrent.*;

public class MyExecService implements ExecutorService {
    private boolean isShutdown=false;
    private boolean isTerminated=false;
    private final BlockingQueue<Runnable> tasks= new LinkedBlockingQueue<>();
    private final Thread thread= new Thread(this::workerLoop);
    private void workerLoop() {
        while (!isShutdown || !tasks.isEmpty()) {
            Runnable task = tasks.poll();
            if (task != null) {
                task.run();
            }
        }
        isTerminated = true;
    }

    static MyExecService newInstance() {
        MyExecService myExecService = new MyExecService();
        myExecService.thread.start();;
        return myExecService;
    }

    @Override
    public void shutdown() {
            isShutdown=true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        thread.interrupt();
        isShutdown=true;
        List<Runnable> unfinishedTasks = new ArrayList<>(tasks);
        tasks.clear();
        return unfinishedTasks;
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        return isTerminated;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long time = System.currentTimeMillis() + unit.toMillis(timeout);

        while(!isTerminated && !isShutdown && System.currentTimeMillis()<time){
            Thread.sleep(10);
        }
        return isTerminated();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (!isShutdown && !isTerminated){
            FutureTask<T> futureTask = new FutureTask<>(task);
            execute(futureTask);
            return futureTask;
        }
        throw new RejectedExecutionException();
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if(!isShutdown && !isTerminated){
            Callable<T> callable=Executors.callable(task, result);
            return submit(callable);
        }
        throw new RejectedExecutionException();
    }

    @Override
    public Future<?> submit(Runnable task) {
        return submit(task, null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        List<Future<T>> result = new ArrayList<>();
        for(var task : tasks){
            result.add(submit(task));
        }
        return result;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        List<Future<T>> result = new ArrayList<>();
        long breakTime = System.currentTimeMillis()+unit.toMillis(timeout);
        for(var task : tasks){
            if(System.currentTimeMillis()>breakTime) {
                break;
            }
            Future<T> future = submit(task);
            result.add(future);
            try {
                T value = future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
//        while(System.currentTimeMillis()<breakTime){
//            Thread.sleep(10);
//        }
        //thread.interrupt();
        //tasks.clear();
        //thread.start();
        return result;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        for (var task : tasks) {
            T result = submit(task).get();
            if (result != null) {
                return result;
            }
        }
//        List<Future<T>> listOfFutures=invokeAll(tasks);
//        for(var future: listOfFutures){
//            T result = future.get();
//            if(result!=null){
//                return result;
//            }
//        }
        throw new RejectedExecutionException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long breakTime =System.currentTimeMillis()+ unit.toMillis(timeout);
        for (var task : tasks) {
            T result = submit(task).get();
            if(System.currentTimeMillis()>breakTime) {
                break;
            }
            if (result != null) {
                return result;
            }
        }
        throw new RejectedExecutionException();
    }

    @Override
    public void execute(Runnable command) {
        tasks.add(command);
    }
}
