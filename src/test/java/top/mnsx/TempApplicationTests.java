package top.mnsx;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class TempApplicationTests {

    @Test
    void contextLoads() {
    }

    // 1
    public static class HiddenIterator {
        private final Set<Integer> set = new HashSet<>();
        private Set<Integer> safeSet = Collections.synchronizedSet(set);

        public synchronized void add(Integer i) {
            safeSet.add(i);
        }

        public synchronized void remove(Integer i) {
            safeSet.remove(i);
        }

        public void addTenThings() {
            Random r = new Random();
            for (int i = 0; i < 10; ++i) {
                add(r.nextInt());
                System.out.println(safeSet);
            }
        }
    }

    /*public static void main(String[] args) {
        HiddenIterator hiddenIterator = new HiddenIterator();
        new Thread(hiddenIterator::addTenThings).start();

        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            hiddenIterator.remove(hiddenIterator.safeSet.iterator().next());
            System.out.println("do it");
            System.out.println(hiddenIterator.safeSet);
        }).start();
    }*/

    // 2

    public static void main(String[] args) throws InterruptedException {
//        List<Integer> safeList = Collections.synchronizedList(new ArrayList<>());
        ArrayList<Integer> safeList = new ArrayList<>();

        System.out.println(System.currentTimeMillis());
        Random r = new Random();
        for (int i = 0; i < 100; ++i) {
            safeList.add(r.nextInt());
        }
        System.out.println(System.currentTimeMillis());

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Integer integer : safeList) {
            System.out.println(integer);
            Thread.sleep(100);
        }

        new Thread(() -> {
            safeList.add(3);
            System.out.println("do it");
            System.out.println(safeList);
        }).start();
    }

}
