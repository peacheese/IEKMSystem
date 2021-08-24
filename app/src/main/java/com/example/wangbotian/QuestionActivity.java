package com.example.wangbotian;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.alibaba.fastjson.*;


import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Random;

import cn.jiguang.imui.messages.*;

public class QuestionActivity  extends AppCompatActivity implements View.OnClickListener{

    MessageList messageList;
    MsgListAdapter adapter;
    Button sendButton;
    TextInputEditText sendText;
    final String[] courses = new String[] {"chinese", "english", "math", "physics", "chemistry", "biology", "geo", "politics"};
    final String[] notFound = new String[]{"暂时没有理解您的问题，麻烦您再详细描述一下",
            "很抱歉，暂时还不太理解您的问题，请您再详细描述一下", "这个问题我暂时还不太理解，麻烦再详细描述一下您的问题"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);
        sendText = findViewById(R.id.send_text);

        messageList = findViewById(R.id.msg_list);
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        adapter = new MsgListAdapter<>("0", holdersConfig , null);
        messageList.setAdapter(adapter);
        adapter.addToStart(new Message("请在下方输入需要询问的问题", 0), true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_button:
                String question = sendText.getText().toString();
                if (!question.equals("")) {
                    adapter.addToStart(new Message(question, 1), true);
                    sendText.setText("");
                    replyQuestion(question);
                }

        }
    }

    private void replyQuestion(String question){
        for (String course : courses){
            String result = OpenEducation.answerQuestion(course,question);
            if (result.indexOf("此问题没有找到答案") <= 0 && result.indexOf("[]") <= 0){
                JSONObject resultJson = JSON.parseObject(result);
                JSONArray dataArray = JSON.parseArray(resultJson.getString("data"));
                JSONObject dataJson = dataArray.getJSONObject(0);
                result = dataJson.getString("value");
                adapter.addToStart(new Message(result, 2), true);
                return;
            }
        }
        adapter.addToStart(new Message(notFound[(int)(Math.floor(Math.random()*3))], 2), true);
        return ;
    }

}