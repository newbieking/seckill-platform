public class TicketSeller {
    // 总票数（共享资源）
    private int ticketCount = 100;

    // 售票方法（加锁保证原子性）
    public synchronized void sellTicket() {
        if (ticketCount > 0) {
            System.out.println(Thread.currentThread().getName() + " 售出第 " + ticketCount + " 张票");
            ticketCount--;
        }
    }

    public static void main(String[] args) {
        TicketSeller seller = new TicketSeller();
        // 模拟 3 个售票窗口（线程）
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
// 典型的“用微小的锁开销，换取巨大的CPU空转浪费减少”，投入产出比很高。 
                   synchronized (seller) { // 二次加锁判断，避免最后一张票竞争
                        if (seller.ticketCount <= 0) break;
                        seller.sellTicket();
                    }
                    // 模拟售票间隔
                    try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }, "窗口" + (i + 1)).start();
        }
    }
}

