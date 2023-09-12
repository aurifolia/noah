package org.example.user.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/7
 **/
public class Test001 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread() + "  11111");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread() + "  22222");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread() + "  2222222222222222222");
        });
        TimeUnit.SECONDS.sleep(1);
        voidCompletableFuture.cancel(true);
        System.out.println("555555555555");
        TimeUnit.SECONDS.sleep(2);
    }
}
