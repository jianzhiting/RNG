package com.jane.rng;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Gossip extends AppCompatActivity {
    private Timer timer;
    private Handler mHandler;
    private int cnt = (int)(Math.random()*4);
    private int decide = 0;
    private boolean flag = false;
    private LinearLayout myLayout;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private HashMap<String, Integer> imgMatch = new HashMap<String, Integer>();
    private Resources resources;
    Queue q = new Queue();

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gossip);

        myLayout = ( LinearLayout ) findViewById ( R.id.quotation) ;

        final TextView gossipTitle = findViewById(R.id.gossipTitle);
        resources = getBaseContext().getResources();
        final Drawable drawable[] = new Drawable[]{
                resources.getDrawable(R.drawable.gossip1),
                resources.getDrawable(R.drawable.gossip2),
                resources.getDrawable(R.drawable.gossip3),
                resources.getDrawable(R.drawable.gossip4)
        };

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    gossipTitle.setBackground(drawable[cnt]);
                    cnt = (cnt + 1)%4;
                    super.handleMessage(msg);
                }
                else if(msg.what == 2){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map = list.get(msg.arg1);
                    if(msg.arg2 == 0){
                        q.EnQueue(addLeft(map.get("name"), map.get("quotation")));
                    }
                    else
                        q.EnQueue(addRight(map.get("name"), map.get("quotation")));
                    if(decide > 4){
                        RelativeLayout v = q.DeQueue();
                        if ( null != v ) {
                            ViewGroup parent = ( ViewGroup ) v.getParent();
                            parent.removeView(v);
                        }
                    }
                }
            }
        };

        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage(1);
                mHandler.sendMessage(msg);
                if (flag)
                    cancel();
            }
        },0,15000);

        imgMatch.put("xiaohu", R.mipmap.xiaohu);
        imgMatch.put("karsa", R.mipmap.karsa);
        imgMatch.put("AmazingJ", R.mipmap.amazing);
        imgMatch.put("Zz1tai", R.mipmap.zitai);
        imgMatch.put("uzi", R.mipmap.uzi);
        imgMatch.put("letme", R.mipmap.letme);
        imgMatch.put("ming", R.mipmap.ming);
        imgMatch.put("langx", R.mipmap.langx);

        String str1[] = new String[]{
                "xiaohu",
                "赢，才能打破质疑：赢，就是最好的证明！至于为什么是虎大将军，让我来告诉你。",
                "好烦啊我现在有点害羞不想露出我的虎头了٩(//̀Д/́/)۶ ",
                "我唱歌是不是听多了挺有感觉的？",
                "哈哈"
        };
        String str2[] = new String[]{
                "karsa",
                "打分...50分吧...1000分满分",
                "语言已经不能形容你的帅了，但数字可以，1/10"
        };
        String str3[] = new String[]{
                "AmazingJ",
                "？我suo的奏是剖痛发啊！淦！！٩(//̀Д/́/)۶"
        };
        String str4[] = new String[]{
                "Zz1tai",
                "想了想，有个事情小弟得给大家报备一下",
                "我梦想打职业 跟RYL丶Able一队",
                "一看狼行跟我们就不是一个年代的 用最多的是卡密尔！我们那年代的必全是茂凯！比如芙兰朵露和扣肉碗！",
                "但是我们永远不亏"
        };
        String str5[] = new String[]{
                "uzi",
                "没有像往年那样带着那么大的压力去打每一场比赛，我觉得今年就是大家真正能够放轻松一点去享受那个舞台。",
                "想来个SKT",
                "就是要这样！！这样疯狂干你！！就因为你是RNG的上单！！！",
                "弹幕都让我奖励一把亚索了！！",
                "这就是自豪哥的魅腻",
                "史森明！！！快来保护我！！快来养我呀！！"
        };
        String str6[] = new String[]{
                "letme",
                "喂？？？在吗！！！说话啊！！鼓励我啊！！！！(╯‵□′)╯︵┻━┻",
                "我收不到反馈，手一直在抖"
        };
        String str7[] = new String[]{
                "ming",
                "嘻嘻嘻嘻嘻嘻",
                "没有信心的话 我要怎么打",
                "我喜欢林允儿",
                "为无愧满身金甲而战",
                "没事没事，小狗，我在看你我在看你"
        };
        String str8[] = new String[]{
                "langx",
                "AJ是RNG全上单的希望",
                "一百 一百 一百倍美颜！兄弟们！",
                "嗯",
                "我表现的一般般吧，都是队友打的好"
        };
        AddList(str1);
        AddList(str2);
        AddList(str3);
        AddList(str4);
        AddList(str5);
        AddList(str6);
        AddList(str7);
        AddList(str8);

        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage(2);
                msg.arg1 = (int) (Math.random()*list.size());
                msg.arg2 = decide%2;
                decide++;
                mHandler.sendMessage(msg);
                if (flag)
                    cancel();
            }
        },1000,3000);
    }

    private RelativeLayout addLeft(String setname, String setinfo) {
        View hiddenView = getLayoutInflater().inflate( R.layout.add_left_view, myLayout, false ) ;
        TextView name= hiddenView.findViewById(R.id.name);
        TextView info= hiddenView.findViewById(R.id.info);
        ImageView head = hiddenView.findViewById(R.id.l_head);
        name.setText(setname);
        info.setText(setinfo);
        head.setImageDrawable(resources.getDrawable(imgMatch.get(setname)));
        myLayout.addView ( hiddenView ) ;
        return hiddenView.findViewById(R.id.parent_view);
    }
    private RelativeLayout addRight(String setname, String setinfo) {
        View hiddenView = getLayoutInflater().inflate( R.layout.add_right_view, myLayout, false ) ;
        TextView name= hiddenView.findViewById(R.id.name);
        TextView info= hiddenView.findViewById(R.id.info);
        ImageView head = hiddenView.findViewById(R.id.r_head);
        name.setText(setname);
        info.setText(setinfo);
        head.setImageDrawable(resources.getDrawable(imgMatch.get(setname)));
        myLayout.addView ( hiddenView ) ;
        return hiddenView.findViewById(R.id.parent_view);
    }

    private void AddList(String[] str) {
        HashMap<String, String> map;
        String name = str[0];
        for (int i=1; i<str.length; i++){
            map = new HashMap<String, String>();
            map.put("name", name);
            map.put("quotation", str[i]);
            list.add(map);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.funcmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        if(item.getItemId() == R.id.real_time_news){
            it = new Intent(Gossip.this, RealTimeNews.class);
            startActivity(it);
            flag = true;
            this.finish();
        }
        else if(item.getItemId() == R.id.hidden_gems){
            it = new Intent(Gossip.this, HiddenGems.class);
            startActivity(it);
            flag = true;
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
