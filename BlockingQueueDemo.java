import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueDemo {
    // 容量为 5 的阻塞队列
    private static final LinkedBlockingQueue<Integer> QUEUE = new LinkedBlockingQueue<>(5);

    // 生产者：向队列存数据
    static class Producer implements Runnable {
        @Override
        public void run() {
            int count = 0;
            while (true) {
                try {
                    count++;
                    QUEUE.put(count); // 队列满时自动阻塞
                    System.out.println(Thread.currentThread().getName() + " 生产：" + count + "，队列size：" + QUEUE.size());
                    TimeUnit.SECONDS.sleep(1); // 模拟生产耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 消费者：从队列取数据
    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Integer data = QUEUE.take(); // 队列空时自动阻塞
                    System.out.println(Thread.currentThread().getName() + " 消费：" + data + "，队列size：" + QUEUE.size());
                    TimeUnit.SECONDS.sleep(2); // 模拟消费耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        // 启动 1 个生产者、2 个消费者
        new Thread(new Producer(), "生产者A").start();
        new Thread(new Consumer(), "消费者X").start();
        new Thread(new Consumer(), "消费者Y").start();
    }
}
	
