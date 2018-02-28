package com.etranslate.yidiantong;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONStringer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Alex on 2018/1/25.
 */

public class SettingFragment extends Fragment {
    private View view;
    private ImageView img_sel;
    private Uri imageUri;
    private TextView name_show;
    private TextView passwd;
    /**request Code 从相册选择照片并裁切**/
    private final static int SELECT_PIC=123;
    /**request Code 从相册选择照片不裁切**/
    private final static int SELECT_ORIGINAL_PIC=126;
    /**request Code 拍取照片并裁切**/
    private final static int TAKE_PIC=124;
    /**request Code 拍取照片不裁切**/
    private final static int TAKE_ORIGINAL_PIC=127;
    /**request Code 裁切照片**/
    private final static int CROP_PIC=125;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_fragment, container,false);
        img_sel=(ImageView) view.findViewById(R.id.userImg);
        name_show=(TextView) view.findViewById(R.id.name_show);
        passwd=(TextView) view.findViewById(R.id.passwd);
        name_show.setText(Data.lastname);
        ArrayList uids = new ArrayList<>();
        uids.add(Data.userid);
        Data.Icon=Data.mIMKit.getContactService().getContactProfileInfo(Data.userid, Data.AppKey).getAvatarPath();
        if(Data.picture!=null)
        {
            img_sel.setImageBitmap(Data.picture);
        }
        else if(Data.Icon!=null) {
            Log.d("img_test_imgurl",Data.Icon);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        final Bitmap t=returnBitMap(Data.Icon);
                        Data.picture=t;
                        view.post(new Runnable() {

                            @Override
                            public void run() {
                                img_sel.setImageBitmap(t);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        img_sel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generatedmethod stub
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_PICK);//Pick an item fromthe data
                        intent.setType("image/*");//从所有图片中进行选择
                        startActivityForResult(intent, SELECT_ORIGINAL_PIC);
                    }
                }

        );
        name_show.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText text = new EditText(view.getContext());
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("修改名字")
                                .setView(text)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        // 获取输入框的内容
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {

                                                    JSONStringer myString;
                                                    myString = new JSONStringer().object();
                                                    myString.key("phone");
                                                    myString.value(Data.userid.toString());
                                                    myString.key("passwd");
                                                    myString.value(Data.passwd.toString());
                                                    myString.key("firstname");
                                                    myString.value(text.getText().toString());
                                                    myString.key("lastname");
                                                    myString.value(text.getText().toString());
                                                    myString.key("gender");
                                                    myString.value("null");

                                                    // myString.key("action");
                                                    //  myString.value("register");
                                                    myString.endObject();
                                                    String l = myString.toString();
                                                    Log.d("myString", myString.toString());
                                                    String Retstr = WebServiceHelper.GetWebService("http://chatserver20171204045436.azurewebsites.net/",
                                                            "Update", "askStr", "http://118.25.17.193/messageControl.asmx?wsdl", myString.toString());
                                                    Log.d("Update Profile tag", Retstr);
                                                    Data.lastname=text.getText().toString()            ;
                                                    dialog.dismiss();
                                                } catch (Exception e) {

                                                }
                                            }
                                        }).start();

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }

        );
        passwd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText text = new EditText(view.getContext());
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("修改密码")
                                .setView(text)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        // 获取输入框的内容
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {

                                                    JSONStringer myString;
                                                    myString = new JSONStringer().object();
                                                    myString.key("phone");
                                                    myString.value(Data.userid.toString());
                                                    myString.key("passwd");
                                                    myString.value(text.getText().toString());
                                                    myString.key("firstname");
                                                    myString.value(Data.firstname);
                                                    myString.key("lastname");
                                                    myString.value(Data.firstname);
                                                    myString.key("gender");
                                                    myString.value("null");

                                                    // myString.key("action");
                                                    //  myString.value("register");
                                                    myString.endObject();
                                                    String l = myString.toString();
                                                    Log.d("myString", myString.toString());
                                                    String Retstr = WebServiceHelper.GetWebService("http://chatserver20171204045436.azurewebsites.net/",
                                                            "Update", "askStr", "http://118.25.17.193/messageControl.asmx?wsdl", myString.toString());
                                                    Log.d("Update Profile tag", Retstr);
                                                    Data.lastname=text.getText().toString()            ;
                                                    dialog.dismiss();
                                                } catch (Exception e) {

                                                }
                                            }
                                        }).start();

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }

        );
        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case SELECT_ORIGINAL_PIC:
                if (resultCode==getActivity().RESULT_OK) {//从相册选择照片不裁切
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor =getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Log.d("img_test",picturePath);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);//You can use this bitmap if need full image to further use
                        final Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,  600 ,600, true);//this bitmap2 you can use only for display
                        img_sel.setImageBitmap(bitmap2); //trying full image
                        final String[] parameter=new String[2];
                        final String[] action=new String[2];
                        parameter[0]="type";
                        parameter[1]="userid";
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        action[0]="jpg";
                        action[1]=Data.userid;
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try  {
                                    String Retstr = WebServiceHelper.GetWebService("http://chatserver20171204045436.azurewebsites.net/",
                                            "upLoadPhoto", parameter, "http://118.25.17.193/messageControl.asmx?wsdl",action, baos.toByteArray());
                                    Data.picture=bitmap2;
                                    Data.Icon=Data.mIMKit.getContactService().getContactProfileInfo(Data.userid, Data.AppKey).getAvatarPath();
                                    final Bitmap t=returnBitMap(Data.Icon);
                                    view.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            img_sel.setImageBitmap(t);
                                        }
                                    });
                                    Log.d("img_test",Retstr);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();

                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        Log.d("img_test",e.toString()+"erroerroerro");
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            bitmap2 = Bitmap.createScaledBitmap(bitmap,  600 ,600, true);//this bitmap2 you can use only for display
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap2;
    }

}
