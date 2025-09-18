
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.TimeUnit;

public class SpinLockDemo {
    // 原子引用：存储持有锁的线程
    private final AtomicReference<Thread> lockHolder = new AtomicReference<>();

    // 获取锁：自旋重试直到成功
    public void lock() {
        Thread current = Thread.currentThread();
        // 若锁未被持有（null），则将当前线程设为持有者（CAS操作）
        while (!lockHolder.compareAndSet(null, current)) {
            // 自旋：空循环，直到获取锁
            System.out.println(current.getName() + " 自旋等待锁...");
        }
        System.out.println(current.getName() + " 获取到锁");
    }

    // 释放锁：将锁持有者设为 null
    public void unlock() {
        Thread current = Thread.currentThread();
        // 只有持有锁的线程能释放锁
        lockHolder.compareAndSet(current, null);
        System.out.println(current.getName() + " 释放锁");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLock = new SpinLockDemo();

        // 线程1获取锁并持有2秒
        new Thread(() -> {
            spinLock.lock();
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLock.unlock();
        }, "线程A").start();

        // 线程2在线程1持有锁时自旋等待
        new Thread(() -> {
            spinLock.lock();
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLock.unlock();
        }, "线程B").start();
    }
}

