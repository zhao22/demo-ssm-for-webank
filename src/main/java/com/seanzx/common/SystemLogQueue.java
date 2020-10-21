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

    private static final BlockingDeque<HttpRequestPO> requestQueue = new LinkedBlockingDeque<>();

    private static final BlockingDeque<DatabaseOperatePO> databaseOperateQueue = new LinkedBlockingDeque<>();

    private final ExecutorService requestPool = singleThreadExecutor();

    private final ExecutorService dataBaseOperatePool = singleThreadExecutor();

    public static void put(HttpRequestPO httpRequestPO) {
        requestQueue.offer(httpRequestPO);
    }

    public static void put(DatabaseOperatePO databaseOperatePO) {
        databaseOperateQueue.offer(databaseOperatePO);
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
                    HttpRequestPO httpRequestPO = requestQueue.take();
                    httpRequestService.add(httpRequestPO);
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
                    DatabaseOperatePO databaseOperatePO = databaseOperateQueue.take();
                    databaseOperateService.add(databaseOperatePO);
                } catch (Exception e) {
                    logger.info("数据库操作数据保存异常", e);
                }
            }
        });
    }

    private ExecutorService singleThreadExecutor() {
        return new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    }
}
