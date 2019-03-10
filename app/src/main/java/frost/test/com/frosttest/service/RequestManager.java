package frost.test.com.frosttest.service;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * RequestManager handles API request through pool
 */
public class RequestManager
{

    private static RequestManager requestManager;
    private ExecutorService executorService;

    private RequestManager()
    {
        executorService = Executors.newFixedThreadPool(4);
    }

    public static RequestManager getInstance() {
        if (requestManager == null) {
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    public void addRequest(BaseRequest request){
        Future future =  executorService.submit(request);
    }

    public void stop()
    {
        try
        {
            executorService.shutdownNow();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
