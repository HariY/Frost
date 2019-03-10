package frost.test.com.frosttest.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import frost.test.com.frosttest.model.Children;
import frost.test.com.frosttest.utils.Constants;
import frost.test.com.frosttest.utils.Utils;

/**
 * This handles the JSON data parsing
 */
public class JsonParser {

    public List<Children>parseResponse(JSONObject dataObj)
    {
        List<Children> dataList = new ArrayList<>();
        try
        {
            JSONObject contentObj = dataObj.getJSONObject(Constants.KEY_CONTENT);
            JSONArray jsonArray = contentObj.getJSONArray(Constants.KEY_INNERDATA);
            for(int i=0; i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                List<Children> resultVideoList =  parseChildern(jsonObject,new ArrayList<Children>());
                dataList.addAll(resultVideoList);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    /*
        This method recursively finds the children which are having the video data.
     */
    public List<Children> parseChildern(JSONObject innerObj, List<Children> resultVideoList)
    {
        Children childrenObj = null;
        try
        {

            String keyType = innerObj.getString(Constants.KEY_TYPE);
            String tag = innerObj.getString(Constants.KEY_TAG);
            childrenObj  =  new Children(keyType,tag);

            //check for video tag
            if(childrenObj.getTagName().equalsIgnoreCase(Constants.KEY_LAZY_VIDEO)){

                JSONArray jsonAttributesArray = innerObj.getJSONArray(Constants.KEY_ATTRIBUTES);
                for(int k=0;k<jsonAttributesArray.length();k++){

                    JSONObject attributesObj = jsonAttributesArray.getJSONObject(k);
                    String key =  attributesObj.getString(Constants.KEY);
                    if(key!=null && key.length()>0 && key.equalsIgnoreCase(Constants.KEY_VIDEO_DATA)){
                        String valueStr =  attributesObj.getString(Constants.KEY_VALUE);
                        valueStr= valueStr.replaceAll(Constants.KEY_QUTATION,"\"");
                        if(Utils.isValidJson(valueStr)){
                            JSONArray valueArry = new JSONArray(valueStr);
                            List<String>videoList = new ArrayList<String>();

                            for(int l =0;l<valueArry.length();l++){

                                JSONObject valueObj = valueArry.getJSONObject(l);
                                JSONObject videoDetailObj =  valueObj.getJSONObject(Constants.KEY_VIDEO_DETAILS);
                                String videoUrl =  videoDetailObj.getString(Constants.KEY_VIDEO_URL);
                                videoList.add(videoUrl);
                            }
                            childrenObj.setVideoList(videoList);
                            resultVideoList.add(childrenObj);
                        }

                    }
                }
            }

            JSONArray jsonChildArray = innerObj.getJSONArray(Constants.KEY_CHILDERN);
            if(jsonChildArray!=null && jsonChildArray.length()>0) {
                for (int i = 0; i < jsonChildArray.length(); i++) {

                    JSONObject jsonObject = jsonChildArray.getJSONObject(i);

                    //calling resursively to identify the childs
                    resultVideoList = parseChildern(jsonObject,resultVideoList);

                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return resultVideoList;
    }

}
