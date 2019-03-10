package frost.test.com.frosttest.service;

import android.os.Handler;

import org.json.JSONObject;

import java.util.List;
import frost.test.com.frosttest.model.Children;
import frost.test.com.frosttest.parser.JsonParser;
import frost.test.com.frosttest.utils.Constants;

/**
 * This class handle Search API calls
 */
public class VideoSearchRequest extends BaseRequest{

    private String contentId;
    private Handler handler;
    private RequestCallBack callBackListener;

    public VideoSearchRequest(String contentId, RequestCallBack callBackListener)
    {
        this.contentId = contentId;
        this.callBackListener = callBackListener;
        this.handler = new Handler();
    }

    private List<Children> getSearchdata(String contentId)
    {

        List<Children> dataList =null;
        try
        {
            StringBuilder url = new StringBuilder(Constants.HOST_URL);
            url =url.append(Constants.PAGE_CONTENT);
            url =url.append(contentId);
            RequestEntity requestEnity = new RequestEntity(url.toString(), RequestEntity.RequestTypes.GET);
            Object obj =  doGetServerRequest(requestEnity);
            if(obj instanceof JSONObject){
                JSONObject resultObj = (JSONObject)obj;
                JsonParser jsonParser = new JsonParser();
                dataList = jsonParser.parseResponse(resultObj);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return dataList;
    }

    @Override
    public void run() {
        if(!Thread.currentThread().isInterrupted()) {

            final List<Children> dataList = getSearchdata(this.contentId);

            if(!Thread.currentThread().isInterrupted())
            {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (dataList != null && dataList.size() > 0) {
                            callBackListener.onRequestSuccessFull(ServerReqestTypes.VIDEO_SEARCH, dataList);
                        } else {
                            callBackListener.onRequestFailure(ServerReqestTypes.VIDEO_SEARCH, Constants.API_ERROR_MSG);
                        }
                    }
                });
            }
        }
    }
}
