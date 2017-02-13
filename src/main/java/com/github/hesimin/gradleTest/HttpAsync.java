package com.github.hesimin.gradleTest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author hesimin 2017-02-13
 */
public class HttpAsync {
    public static void main(String[] args) throws InterruptedException, IOException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();

        final CountDownLatch latch = new CountDownLatch(1);
        final HttpGet request = new HttpGet("https://www.baidu.com/");

        client.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                System.out.println("completed :"+httpResponse.getStatusLine());
                try {
                    String result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                    //System.out.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            }

            @Override
            public void failed(Exception e) {
                System.out.println("failed :"+e.getMessage());
                latch.countDown();
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled ");
                latch.countDown();
            }
        });

        System.out.println("async execute ");
        latch.await();
        System.out.println("http end");
        client.close();
    }
}
