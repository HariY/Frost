package frost.test.com.frosttest.service;

public interface RequestCallBack {

    void onRequestSuccessFull(ServerReqestTypes serverReqestType, Object result);

    void onRequestFailure(ServerReqestTypes serverReqestType, String errorMsg);


}
