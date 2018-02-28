package com.etranslate.yidiantong;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
/**
 * Created by Alex on 2018/1/10.
 */
public class WebServiceHelper {
    public static String GetWebService(String nameSpace, String methodName, String parameter,
                                       String url, String action) {

        SoapObject soapObj = new SoapObject(nameSpace, methodName);
        soapObj.addProperty(parameter, action);
        //soapObj.addProperty(parameter2, action2);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.bodyOut = soapObj;
        envelope.dotNet = true;

        HttpTransportSE transport = new HttpTransportSE(url);

        try {
            /**
             * 连接网络获取数据
             */
            transport.call(nameSpace + methodName, envelope);
            SoapObject soapReault = (SoapObject) envelope.bodyIn;
            //SoapFault error = (SoapFault)envelope.bodyIn;

            String result = soapReault.getProperty(0).toString();
            return result;

        } catch (SoapFault e) {

            return e.getMessage()+"4";

        } catch (ConnectException e) {

            /**
             * 无网络连接
             */
            return "ConnectException";

        } catch (IOException e) {

            return e.getMessage()+"3";

        } catch (XmlPullParserException e) {

            return e.getMessage()+"2";

        } catch (Exception e) {

            return e.getMessage()+"1";

        }
    }
    public static String GetWebService(String nameSpace, String methodName, String[] parameter,
                                       String url, String[] action,byte[] photo) {

        SoapObject soapObj = new SoapObject(nameSpace, methodName);
        for(int i=0;i<parameter.length;i++) {
            soapObj.addProperty(parameter[i], action[i]);
        }
        soapObj.addProperty("b",photo);
        //soapObj.addProperty(parameter2, action2);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        new MarshalBase64().register(envelope); // serialization
        envelope.bodyOut = soapObj;
        envelope.dotNet = true;

        HttpTransportSE transport = new HttpTransportSE(url);

        try {
            /**
             * 连接网络获取数据
             */
            transport.call(nameSpace + methodName, envelope);
            SoapObject soapReault = (SoapObject) envelope.bodyIn;
            //SoapFault error = (SoapFault)envelope.bodyIn;

            String result = soapReault.getProperty(0).toString();
            return result;

        } catch (SoapFault e) {

            return e.getMessage()+"4";

        } catch (ConnectException e) {

            /**
             * 无网络连接
             */
            return "ConnectException";

        } catch (IOException e) {

            return e.getMessage()+"3";

        } catch (XmlPullParserException e) {

            return e.getMessage()+"2";

        } catch (Exception e) {

            return e.getMessage()+"1"+e.toString()+e.getCause();

        }
    }
}
