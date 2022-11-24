package top.mnsx.netty.c2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/14 16:41
 * @Description:
 */
@Slf4j
public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        // 提交任务
        Future<Integer> future = threadPool.submit(() -> {
            log.debug("执行计算");
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 50;
        });
        // 主线程通过future来获取结果
        log.debug("等待结果");
        log.debug("执行结果: {}", future.get());
    }
}
