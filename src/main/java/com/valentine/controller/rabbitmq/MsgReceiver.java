package com.valentine.controller.rabbitmq;

import com.valentine.common.CommonConstants;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

@Component
@RabbitListener(queues = CommonConstants.RabbitQueue.QUEUE_SPRING_CLOUD_CONFIG)
public class MsgReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 构建唯一会话Id
    private static String getSessionId() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }

    @RabbitHandler
    public void process(String content) {
        HttpClient httpClient = new DefaultHttpClient();

        String url = "http://localhost:9090/bus/refresh";

        HttpPost post = new HttpPost(url);
        // 构造消息头
        post.setHeader("Content-type", "application/json; charset=utf-8");
        post.setHeader("Connection", "Close");
        String sessionId = getSessionId();
        post.setHeader("SessionId", sessionId);

        // 构建消息实体
        StringEntity entity = new StringEntity(content, Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        // 发送Json格式的数据请求
        entity.setContentType("application/json");
        post.setEntity(entity);

        HttpResponse response;
        try {
            response = httpClient.execute(post);
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                logger.info("请求出错: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();

            logger.info("接收处理队列A当中的消息： " + content);
        } finally {
            try {
                post.releaseConnection();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
