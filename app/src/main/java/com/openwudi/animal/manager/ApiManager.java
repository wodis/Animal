package com.openwudi.animal.manager;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.openwudi.animal.R;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.exception.AnimalException;
import com.openwudi.animal.exception.RES_STATUS;
import com.openwudi.animal.model.Item;

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
    //命名空间,作为常量
    private static final String NAME_SPACE = "http://tempuri.org/";
    //相关参数
    private static final String URL = AnimalApplication.INSTANCE.getString(R.string.API_URL);
    private static final String PASSWORD = "D49F1F8D1ACD2380B02F90C0A8059CB6";

    /**
     * 发送SOAP请求
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
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (!TextUtils.isEmpty(entry.getValue())) {
                        soapObject.addProperty(entry.getKey(), entry.getValue());
                    }
                }
            }
            envelope.bodyOut = soapObject;
            // 设置与.NET提供的webservice保持较好的兼容性
            envelope.dotNet = true;

            // 调用webservice
            ht.call(NAME_SPACE + method, envelope);
            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
                SoapObject result = (SoapObject) envelope.bodyIn;
                String re = result.getProperty(method + "Result").toString();
                System.out.println(result);
                System.out.println(re);
                // 解析服务器响应的SOAP消息
                return re;
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
     * @param encode
     * @return
     */
    public static List<Item> getItemsList(String encode){
        Map<String, String> params = new HashMap<>(1);
        params.put("encode", encode);
        String result = send("GetItemsList", params);
        List<Item> items = JSON.parseArray(result, Item.class);
        return items;
    }
}
