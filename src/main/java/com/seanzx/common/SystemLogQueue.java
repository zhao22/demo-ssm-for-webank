package com.seanzx.common;

import com.seanzx.po.DatabaseOperatePO;
import com.seanzx.po.HttpRequestPO;
import com.seanzx.service.DatabaseOperateService;
import com.seanzx.service.HttpRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 系统日志保存队列
 * 利用单线程池和阻塞队列异步保存 Http 请求日志， 数据库操作日志
 * @author zhaoxin
 * @date 2020/10/21
 */
@Component
public class SystemLogQueue {

    private static final Logger logger = LoggerFactory.getLogger(SystemLogQueue.class);

    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired
    private DatabaseOperateService databaseOperateService;

    private static final BlockingDeque<HttpRequestPO> REQUEST_QUEUE = new LinkedBlockingDeque<>();

    private static final BlockingDeque<DatabaseOperatePO> DATABASE_OPERATE_QUEUE = new LinkedBlockingDeque<>();

    private final ExecutorService requestPool = singleThreadExecutor("thread-save-http-request");

    private final ExecutorService dataBaseOperatePool = singleThreadExecutor("thread-save-database-operate");

    public static void put(HttpRequestPO httpRequest) {
        REQUEST_QUEUE.offer(httpRequest);
    }

    public static void put(DatabaseOperatePO databaseOperate) {
        DATABASE_OPERATE_QUEUE.offer(databaseOperate);
    }

    @PostConstruct
    private void startExecutors()  {
        startRequestExecutors();
        startDatabaseOperateExecutors();
    }

    private void startRequestExecutors() {
        requestPool.execute(() -> {
            logger.info("http请求记录线程启动成功");
            while(true) {
                try {
                    // 如果暂时没有请求，将会阻塞于此
                    HttpRequestPO httpRequest = REQUEST_QUEUE.take();
                    httpRequestService.add(httpRequest);
                } catch (Exception e) {
                    logger.info("http请求数据保存异常", e);
                }
            }
        });
    }

    private void startDatabaseOperateExecutors() {
        dataBaseOperatePool.execute(() -> {
            logger.info("数据库操作记录线程启动成功");
            while(true) {
                try {
                    // 如果暂时没有请求，将会阻塞于此
                    DatabaseOperatePO databaseOperate = DATABASE_OPERATE_QUEUE.take();
                    databaseOperateService.add(databaseOperate);
                } catch (Exception e) {
                    logger.info("数据库操作数据保存异常", e);
                }
            }
        });
    }

    private ExecutorService singleThreadExecutor(String threadName) {
        return new ThreadPoolExecutor(1, 1, 5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                r -> {
                    Thread t = new Thread(r);
                    t.setName(threadName);
                    return t;
                });
    }
}
