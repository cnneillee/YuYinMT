package com.uesct.neil.yuyinmt;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.uesct.neil.yuyinmt.adapter.ChattingMsgsAdapter;
import com.uesct.neil.yuyinmt.bean.ChattingMsgItem;
import com.uesct.neil.yuyinmt.bean.VoiceBean;
import com.uesct.neil.yuyinmt.exception.CommonException;
import com.uesct.neil.yuyinmt.until.ChattingMachine;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Neil on 2016/2/25.
 */
public class ChattingActivity extends Activity {
    private View mRootView;
    private MyAsyncTask task;
    private EditText etWords;

    private ChattingMachine machineMT = new ChattingMachine(this);
    private ListView lvMsg;
    private ChattingMsgsAdapter adapter;
    private List<ChattingMsgItem> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=56cd8ae5");

        datas = new ArrayList<>();
        //初始化View(ListView、adapter)
        initViews();

    }

    private void initViews() {
        lvMsg = (ListView) findViewById(R.id.id_lvChattingMsgs);
        etWords = (EditText) findViewById(R.id.id_etWords);
        etWords.setVisibility(View.GONE);
        adapter = new ChattingMsgsAdapter(this, R.layout.item_chatting_msg, datas);
        lvMsg.setAdapter(adapter);
        lvMsg.setClickable(false);
    }

    public void chatting(View view) {
        final StringBuffer totalResultContent = new StringBuffer();
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new InitListener() {
            @Override
            public void onInit(int i) {
                System.out.println("hfkjhsjkhfks000000000000000");
            }
        });
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解结果
        mDialog.setParameter("asr_sch", "1");
        mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                Toast.makeText(ChattingActivity.this, "successful", Toast.LENGTH_SHORT).show();
                String result = recognizerResult.getResultString();
                Log.e("NEIL", "result--->" + result);
                Log.e("NEIL", "isLast--->" + isLast);
                String resultContent = parseResult(result);
                if (isLast) {
                    totalResultContent.append(resultContent);
                    //此次语音识别的最终文字内容
                    Toast.makeText(ChattingActivity.this,
                            totalResultContent.toString(), Toast.LENGTH_LONG).show();
                    Log.e("RESULT_OF_THIS_TIME", totalResultContent.toString());

                    //将识别后的文字添加至Adapter
                    datas.add(new ChattingMsgItem(totalResultContent.toString(),
                            ChattingMsgItem.ChattingMsgType.Me));
                    adapter.notifyDataSetChanged();
                    //回答语音

                    task = new MyAsyncTask();
                    task.execute(totalResultContent.toString());
//                        ChattingMsgItem item = machineMT.sendMessage(totalResultContent.toString());
//                        item.setPicMTId(R.drawable.bubble_received);
//                        datas.add(item);
//                        adapter.notifyDataSetChanged();

                } else {
                    totalResultContent.append(resultContent);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                Toast.makeText(ChattingActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 使用Gson解析服务器端返回的识别数据的json
     */
    private String parseResult(String json) {
        Gson gson = new Gson();
        VoiceBean voiceBean = gson.fromJson(json, VoiceBean.class);
        StringBuffer sb = new StringBuffer();
        ArrayList<VoiceBean.WS> wss = voiceBean.ws;
        for (int i = 0; i < wss.size(); i++) {
            String w = wss.get(i).cw.get(0).w;
            System.out.println(i + "content:" + wss.get(i).cw.get(0).w + ",cws.size():" + wss.size());
            sb.append(w);
        }
        Log.e("ChattingActivity", sb.toString());
        return sb.toString();
    }

    class MyAsyncTask extends AsyncTask<String, Void, ChattingMsgItem> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ChattingMsgItem chattingMsgItem) {
            super.onPostExecute(chattingMsgItem);
//            chattingMsgItem.setPicMTId(R.mipmap.ic_launcher);
            datas.add(chattingMsgItem);
            adapter.notifyDataSetChanged();
            reading(chattingMsgItem.getContent());
        }

        @Override
        protected ChattingMsgItem doInBackground(String... params) {
            try {
                return machineMT.sendMessage(params[0]);
            } catch (CommonException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 语音朗读
     *
     * @param content
     */
    public void reading(String content) {
        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录13.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        //合成监听器
        SynthesizerListener mSynListener = new SynthesizerListener() {
            //会话结束回调接口，没有错误时，error为null
            public void onCompleted(SpeechError error) {
            }

            //缓冲进度回调
            //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            }

            //开始播放
            public void onSpeakBegin() {
            }

            //暂停播放
            public void onSpeakPaused() {
            }

            //播放进度回调
            //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
            }

            //恢复播放回调接口
            public void onSpeakResumed() {
            }

            //会话事件回调接口
            public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            }
        };
        //3.开始合成
        mTts.startSpeaking(content, mSynListener);
    }
}
