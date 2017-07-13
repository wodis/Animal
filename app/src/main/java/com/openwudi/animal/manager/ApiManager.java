package com.openwudi.animal.manager;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.utils.DeviceUtils;
import com.blankj.utilcode.utils.EmptyUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.exception.AnimalException;
import com.openwudi.animal.exception.RES_STATUS;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.Area;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.MonitorStation;
import com.openwudi.animal.utils.CommonUtil;
import com.openwudi.animal.utils.L;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diwu on 17/7/4.
 */

public class ApiManager {

    private static L logger = L.get(ApiManager.class.getSimpleName());

    //命名空间,作为常量
    private static final String NAME_SPACE = "http://tempuri.org/";
    //相关参数
    private static final String URL = AnimalApplication.INSTANCE.getString(R.string.API_URL);
    private static final String PASSWORD = "D49F1F8D1ACD2380B02F90C0A8059CB6";

    /**
     * 发送SOAP请求
     *
     * @param method
     * @param params
     * @return
     */
    private static String send(String method, Map<String, String> params) {
        HttpTransportSE ht = new HttpTransportSE(URL);
        try {
            ht.debug = true;
            // 使用SOAP1.1协议创建Envelop对象
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            // 实例化SoapObject对象
            SoapObject soapObject = new SoapObject(NAME_SPACE, method);
            soapObject.addProperty("password", PASSWORD);
            logger.d("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━REQ━━");
            logger.d("┃ " + "%s", method);
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (!TextUtils.isEmpty(entry.getValue())) {
                        soapObject.addProperty(entry.getKey(), entry.getValue());
                        logger.d("┃ " + "%s = %s", entry.getKey(), entry.getValue());
                    }
                }
            }
            logger.d("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━REQ━━");
            envelope.bodyOut = soapObject;
            // 设置与.NET提供的webservice保持较好的兼容性
            envelope.dotNet = true;

            // 调用webservice
            ht.call(NAME_SPACE + method, envelope);
            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
                SoapObject bodyIn = (SoapObject) envelope.bodyIn;
                String result = bodyIn.getProperty(method + "Result").toString();
                logger.d("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━RESP━━");
                logger.d("┃ " + "%s", method);
                logger.d("┃ " + "%s", result);
                logger.d("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━RESP━━");
                // 解析服务器响应的SOAP消息
                //出现失败表示异常
                if (!TextUtils.isEmpty(result) && result.contains("失败")) {
                    throw new AnimalException(result, RES_STATUS.RESP_ERROR);
                }
                return result;
            } else {
                throw new AnimalException(RES_STATUS.RESP_ERROR);
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            throw new AnimalException(RES_STATUS.RESP_ERROR);
        }
    }

    /**
     * 根据代码获取字典列表
     *
     * @param encode
     * @return
     */
    public static List<Item> getItemsList(String encode) {
        Map<String, String> params = new HashMap<>(1);
        params.put("encode", encode);
        String result = send("GetItemsList", params);
        List<Item> items = JSON.parseArray(result, Item.class);
        return items;
    }

    /**
     * 根据物种名称获取物种下拉列表
     *
     * @param name
     * @return
     */
    public static List<Animal> getAnimalListByName(String name) {
        Map<String, String> params = new HashMap<>(1);
        params.put("name", name);
        String result = send("GetAnimalListByName", params);
        List<Animal> items = JSON.parseArray(result, Animal.class);
        return items;
    }

    /**
     * 获取物种列表
     *
     * @param keyword
     * @return
     */
    public static List<Animal> getAnimalList(String keyword) {
        Map<String, String> params = new HashMap<>(1);
        params.put("keyword", keyword);
        String result = send("GetAnimalList", params);
        List<Animal> items = JSON.parseArray(result, Animal.class);
        return items;
    }

    public static String getTerminalId() {
        JSONObject object = new JSONObject();
        object.put("F_DeviceModel", DeviceUtils.getModel());
        object.put("F_SerialNumber", CommonUtil.getImei(AnimalApplication.INSTANCE));
        object.put("F_MAC", DeviceUtils.getMacAddress(AnimalApplication.INSTANCE));

        Map<String, String> params = new HashMap<>(1);
        params.put("json", object.toJSONString());

        String result = send("AddTerminal", params);
        if (TextUtils.isEmpty(result)) {
            throw new AnimalException(RES_STATUS.RESP_ERROR);
        }
        return result;
    }

    public static String addUser(String account, String tid, String areaId, String monitor) {
        JSONObject object = new JSONObject();
        object.put("F_Account", account);
        object.put("F_RealName", account);
        object.put("F_NickName", account);
        object.put("F_Terminal_Id", tid);
        object.put("F_Area_Id", areaId);
        object.put("F_MonitorStation_Id", monitor);

        Map<String, String> params = new HashMap<>(1);
        params.put("json", object.toJSONString());
        String uid = send("AddUser", params);
        if (TextUtils.isEmpty(uid)) {
            throw new AnimalException(RES_STATUS.RESP_ERROR);
        }
        return uid;
    }

    public static List<MonitorStation> getMonitorStationList(String encode) {
        Map<String, String> params = new HashMap<>(1);
        params.put("encode", encode);
        String result = send("GetMonitorStationList", params);
        List<MonitorStation> items = JSON.parseArray(result, MonitorStation.class);
        return items;
    }

    public static List<Area> getAreaList(String keyValue) {
        Map<String, String> params = new HashMap<>(1);
        params.put("keyValue", keyValue);
        String result = send("GetAreaList", params);
        List<Area> items = JSON.parseArray(result, Area.class);
        return items;
    }

    public static String addPassword(String uid, String password) {
        JSONObject object = new JSONObject();
        object.put("F_UserId", uid);
        object.put("F_UserPassword", password);
        Map<String, String> params = new HashMap<>(1);
        params.put("json", object.toJSONString());
        String result = send("AddUserLogOn", params);
        return result;
    }

    public static String login(String account, String password) {
        Map<String, String> params = new HashMap<>(1);
        params.put("username", account);
        params.put("userpassword", password);
        String result = send("CheckLogin", params);
        return result;
    }
}
