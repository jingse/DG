package com.example.guihuan.chatwifitest.chat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.Var;
import com.example.guihuan.chatwifitest.emoji.Emoji;
import com.example.guihuan.chatwifitest.emoji.EmojiUtil;
import com.example.guihuan.chatwifitest.emoji.FaceFragment;
import com.example.guihuan.chatwifitest.jsip_ua.impl.DeviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.guihuan.chatwifitest.Var.myName;
import static com.example.guihuan.chatwifitest.Var.serverSip;


public class ChatActivity extends FragmentActivity implements FaceFragment.OnEmojiClickListener {

    private List<ChatMsg> chatMsgList = new ArrayList<>();
    private ListView chatMsgListView;
    private ChatMsgAdapter adapter;

    private EditText inputText;
    private ImageButton send;
    private ImageButton recordSound;
    private ImageButton showPicture;
    private ImageButton showCamera;
    private ImageButton showVideo;
    private ImageButton showFile;
    private ImageButton showEmoji;

    private ImageButton backToMain;
    private FrameLayout container;


    private Boolean isShowEmoji;


    private int friendImageId;
    private String friendName;
    private String latestMsg;
    private String latestMsgTime;
    private String targetAddress;
    private List<String> recentMsgList;//接受从list传过来的消息信息


    //自定义变量
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;//图片路径


    private Handler chatHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                // 在线消息
                case Var.Message:

                    String data = String.valueOf(msg.obj);
                    Log.d("TAG", "消息: data is " + data);


                    String temp[] = data.split("&");
                    String type = temp[0];
                    String from = temp[1];
                    String to = temp[2];
                    String msgContent = temp[3];
                    String curTime = temp[4];

                    if (type.equals("1")) { //私聊信息

                        handlePrivateMsg(from, msgContent, curTime);
                    } else { //群聊信息

                        Toast.makeText(ChatActivity.this, "收到群聊消息", Toast.LENGTH_SHORT).show();
                        handlePublicMsg(from, msgContent, curTime);
                    }
                    break;

                /*case 9: //定位聊天信息为最后一条
                    chatMsgListView.setSelection(chatMsgList.size()); // 将ListView定位到最后一行
                    break;*/

                //离线消息
                case Var.DownlineMessage:
                    String ddata = (String)msg.obj;
                    String dtemp[] = ddata.split("&");
                    String dtype = dtemp[0];
                    String dfrom = dtemp[1];
                    String dtime = dtemp[2];
                    String dcontent = dtemp[3];

                    if (dtype.equals("1")) { //消息
                        handleOfflineMsg(dfrom, dtime, dcontent);
                        //dealPublic(transferMsg.getFrom(),transferMsg.getContent());
                    } else { //好友申请
                        //dealPrivate(transferMsg.getFrom(),transferMsg.getContent());
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序
        setContentView(R.layout.activity_chatting);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.chat_title_bar); // 注意顺序


        DeviceImpl.getInstance().setChatHandler(chatHandler); // 将handler向下传


        Intent intent = this.getIntent();
        friendName = intent.getExtras().getString("chatting_friend_name");
        friendImageId = intent.getExtras().getInt("chatting_friend_head");

        // 将标题栏的用户名设为正在聊天的人的用户名
        TextView chat_friend_name = findViewById(R.id.chat_title_name);
        chat_friend_name.setText(friendName);

        initChatMsgs();

        adapter = new ChatMsgAdapter(ChatActivity.this, R.layout.chat_msg_item, chatMsgList);
        chatMsgListView = findViewById(R.id.msg_list_view);
        chatMsgListView.setAdapter(adapter);


        backToMain = findViewById(R.id.chatBackToMain);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        inputText.setMovementMethod(LinkMovementMethod.getInstance());

        recordSound = findViewById(R.id.recordSound);
        showCamera = findViewById(R.id.showCamera);
        showPicture = findViewById(R.id.showPicture);
        showVideo = findViewById(R.id.showVideo);
        showFile = findViewById(R.id.showFile);
        showEmoji = findViewById(R.id.showEmoji);

        container = findViewById(R.id.Container);
        container.setVisibility(View.GONE);

        isShowEmoji = false;


        inputText.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    container.setVisibility(View.GONE);
                    //1.得到InputMethodManager对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(ChatActivity.this.INPUT_METHOD_SERVICE);
                    //2.调用showSoftInput方法显示软键盘，其中view为聚焦的view组件
                    imm.showSoftInput(inputText, InputMethodManager.SHOW_FORCED);
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });


        send.setOnClickListener(new ListView.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    ChatMsg chatMsg = new ChatMsg(content, ChatMsg.TYPE_SENT, myName, friendImageId, false);
                    chatMsgList.add(chatMsg);
                    adapter.notifyDataSetChanged();
                    container.setVisibility(View.GONE);
                    chatMsgListView.setSelection(chatMsgList.size());

                    /*用handler异步刷新listview*/
                    Message message1 = new Message();
                    message1.what = Var.Message;
                    chatHandler.sendMessage(message1);
                    inputText.setText(""); // 清空输入框中的内容


                    if(friendName.equals("群")) {
                        String message = "2&" + myName + "&" + friendName + "&" + content;
                        DeviceImpl.getInstance().SendMessage(serverSip, message, "MESSAGE");
                    }else{
                        String message = "1&" + myName + "&" + friendName + "&" + content;
                        DeviceImpl.getInstance().SendMessage(serverSip, message, "MESSAGE");
                    }


                }
            }
        });


        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "back", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                Bundle bundle =new Bundle();
                bundle.putString("friendName", friendName);
                bundle.putString("latestMsg",latestMsg);
                bundle.putString("latestMsgTime", latestMsgTime);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);

                finish();
            }
        });


        recordSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "sound", Toast.LENGTH_SHORT).show();
            }
        });


        showCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "camera", Toast.LENGTH_SHORT).show();

                // 创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT < 24) {
                    //将File对象转换为Uri并启动照相程序
                    imageUri = Uri.fromFile(outputImage);
                } else {
                    imageUri = FileProvider.getUriForFile(ChatActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
                }
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//照相
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);////指定图片输出地址
                startActivityForResult(intent, TAKE_PHOTO);//启动照相
            }
        });


        showPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "picture", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });


        showVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "video", Toast.LENGTH_SHORT).show();
            }
        });


        showFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "file", Toast.LENGTH_SHORT).show();
            }
        });


        showEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "emoji", Toast.LENGTH_SHORT).show();

                if (!isShowEmoji) {
                    container.setVisibility(View.VISIBLE);
                    isShowEmoji = true;
                    /*隐藏键盘*/
                    InputMethodManager imm = (InputMethodManager) getSystemService(ChatActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                } else {
                    container.setVisibility(View.GONE);
                    isShowEmoji = false;
                }
                 /*用handler异步刷新listview*/
                Message message1 = new Message();
                message1.what = 9;
//                chatHandler.sendMessage(message1);

            }
        });


        FaceFragment faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.Container, faceFragment).commit();
    }



    private void initChatMsgs() {
        switch (friendName){
            case "1":
                ChatMsg chatMsg1 = new ChatMsg("Hello guy.", ChatMsg.TYPE_RECEIVED, friendName, friendImageId, false);
                chatMsgList.add(chatMsg1);
                ChatMsg chatMsg2 = new ChatMsg("Hello. Who is that?", ChatMsg.TYPE_SENT, friendName, friendImageId, false);
                chatMsgList.add(chatMsg2);
                ChatMsg chatMsg3 = new ChatMsg("This is Tom. Nice talking to you. ", ChatMsg.TYPE_RECEIVED, friendName, friendImageId, false);
                chatMsgList.add(chatMsg3);
                break;
            case "2":
                ChatMsg chatMsg2_1 = new ChatMsg("你好.", ChatMsg.TYPE_RECEIVED, friendName, friendImageId, false);
                chatMsgList.add(chatMsg2_1);
                ChatMsg chatMsg2_2 = new ChatMsg("你好", ChatMsg.TYPE_SENT, friendName, friendImageId, false);
                chatMsgList.add(chatMsg2_2);
                break;
            case "3":
                ChatMsg chatMsg3_1 = new ChatMsg("哈哈.", ChatMsg.TYPE_RECEIVED, friendName, friendImageId, false);
                chatMsgList.add(chatMsg3_1);
                ChatMsg chatMsg3_2 = new ChatMsg("再见", ChatMsg.TYPE_SENT, friendName, friendImageId, false);
                chatMsgList.add(chatMsg3_2);
                break;
            case "4":
                ChatMsg chatMsg4_1 = new ChatMsg("我是4，你是？", ChatMsg.TYPE_RECEIVED, friendName, friendImageId, false);
                chatMsgList.add(chatMsg4_1);
                ChatMsg chatMsg4_2 = new ChatMsg("不告诉你", ChatMsg.TYPE_SENT, friendName, friendImageId, false);
                chatMsgList.add(chatMsg4_2);
                break;
            case "群":
                ChatMsg chatMsg5_1 = new ChatMsg("大家好", ChatMsg.TYPE_RECEIVED, friendName, R.drawable.headgroupmember, true);
                chatMsgList.add(chatMsg5_1);
                ChatMsg chatMsg5_2 = new ChatMsg("你好", ChatMsg.TYPE_SENT, friendName, R.drawable.headgroupmember, true);
                chatMsgList.add(chatMsg5_2);
                break;
            default:
                break;
        }

    }


    void handlePrivateMsg(String from, String content, String time){
        if(from.equals(friendName)){
            //展示私聊信息
            updateMsgUI(content, ChatMsg.TYPE_RECEIVED, friendImageId);
        }
        else{
            Toast.makeText(ChatActivity.this, content, Toast.LENGTH_SHORT).show();
            //DeviceImpl.getInstance().getReCallMsgList().add(from + "#502750694#" + content);
        }
    }


    void handlePublicMsg(String from, String content, String time){
        if(friendName.equals("群")){
            //展示群聊信息
            if(!from.equals(myName))
                updateMsgUI(content, ChatMsg.TYPE_RECEIVED, R.drawable.headgroupmember);
        }
        else{
            Toast.makeText(ChatActivity.this, content, Toast.LENGTH_SHORT).show();
            //DeviceImpl.getInstance().getReCallMsgList().add(from + "#502750694#" + content);
        }
    }


    void handleOfflineMsg(String from, String time, String content) {


    }





    //打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    //获得本机的允许
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //将拍摄的照片显示出来
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //图片解析成Bitmap对象
                        // 将拍摄的照片显示出来
                        //inputText.append(Html.fromHtml("<imgsrc='" + imageUri + "'/>", imageGetter, new MTagHandler(this)));
                        //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //picture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //picture.setImageBitmap(bitmap);

            //inputText.append(Html.fromHtml("<imgsrc='" + imageUri + "'/>", imageGetter, new MTagHandler(this)));


        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    Html.ImageGetter imageGetter = new Html.ImageGetter() {

        @Override
        public Drawable getDrawable(String source) {
            int id = Integer.parseInt(source);
            Drawable drawable = ChatActivity.this.getResources().getDrawable(id);
            //drawable.setBounds(int left, int top, int right, int bottom);
            return drawable;
        }
    };



    private void displayTextView() {
        try {
            EmojiUtil.handlerEmojiText(inputText, inputText.getText().toString(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onEmojiDelete() {
        String text = inputText.getText().toString();
        if (text.isEmpty())
            return;

        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                inputText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                displayTextView();
                return;
            }
            inputText.getText().delete(index, text.length());
            displayTextView();
            return;
        }
    }


    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            int index = inputText.getSelectionStart();
            Editable editable = inputText.getEditableText();

            if (index < 0) {
                editable.append(emoji.getContent());

            } else {
                editable.insert(index, emoji.getContent());
            }
            displayTextView();
        }

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(ChatActivity.this);
                return false;
            }

        });
    }



    void updateMsgUI(String message, int type, int friendImageId){
        ChatMsg msg = new ChatMsg(message, type, friendName, friendImageId, false);
        chatMsgList.add(msg);
        adapter.notifyDataSetChanged();                   // 当有新消息时，刷新ListView中的显示
        chatMsgListView.setSelection(chatMsgList.size()); // 将ListView定位到最后一行
    }




}



