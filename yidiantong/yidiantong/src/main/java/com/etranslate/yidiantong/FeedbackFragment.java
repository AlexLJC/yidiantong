package com.etranslate.yidiantong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Alex on 2018/1/28.
 */

public class FeedbackFragment extends Fragment {
    private View view;
    Button submit;
    private TextView title;
    private EditText chattime;
    private EditText chatcontent;
    private RatingBar rate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.feedback_t_fragment, container,false);
        title=(TextView) view.findViewById(R.id.title_text);
        title.setText(R.string.feedback);
        submit=(Button) view.findViewById(R.id.submit);
        chattime=(EditText) view.findViewById(R.id.chattime);
        chatcontent=(EditText) view.findViewById(R.id.chatcontent);
        rate=(RatingBar) view.findViewById(R.id.ratingBar);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                final String mstring;
                mstring="Chat time:"+chattime.getText().toString()+"  /n Content:"+chatcontent.getText().toString()+"   /n Rate:"+rate.getRating();
                Log.d("feedback_test",chattime.getText().toString()+chatcontent.getText().toString()+rate.getRating());

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            String Retstr = WebServiceHelper.GetWebService("http://chatserver20171204045436.azurewebsites.net/",
                                    "sendFeedback", "content", "http://118.25.17.193/messageControl.asmx?wsdl", mstring);
                            Log.d("feedback_test",Retstr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                Intent intent = new Intent(getActivity(), TranslatorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });
        return view;

    }
}
