package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;

public class AccountActivity extends AppCompatActivity {
    private Activity that = this;
    private File tmp_photo;
    private Uri imageUri;
    private static final int TAKE_PHOTO = 11;// 拍照
    private static final int LOCAL_CROP = 13;// 本地图库
    private String name = "这里是用户名";;
    private String email = "11111@aks.com";
    private String password = "123456aaa";
    private String introduction = "xxxx";
    private String tmp_filepath = "";
    private String tmp_image = "";
    private String url = Constant.backendUrl+Constant.updateUserUrl;
    private String user_url = Constant.backendUrl+Constant.getUserUrl;

    private TextView curName;
    private TextView curEmail;
    private TextView curPassword;
    private TextView curIntroduction;
    private TextView add_text;

    private ImageView confirmName;
    private ImageView confirmEmail;
    private ImageView confirmPassword;
    private ImageView confirmIntroduction;
    private ImageView user_image;
    private ImageView confirmImage;

    private EditText editNewName;
    private EditText editNewEmail;
    private EditText editNewPassword;
    private EditText editNewIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", 1);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id",IndexActivity.user_id);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(url, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(user_url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();
        if(resu.equals("ok")){
            JSONObject user = (JSONObject)mapType.get("data");
            name = (String) user.get("user_name");
            email = (String) user.get("email");
            password = (String) user.get("password");
            introduction = (String) user.get("introduction");
            tmp_image = user.get("user_image_name").toString();
        }

        // TODO: Get user information to display
        confirmName = findViewById(R.id.confirm_name);
        editNewName = findViewById(R.id.edit_name);
        curName = findViewById(R.id.center_current_name);
        curName.setText("当前用户名：" + name);
        confirmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                name = editNewName.getText().toString();
                // TODO: Connect backend
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image","");
                paramMap.put("introduction","");
                paramMap.put("user_name", name);
                paramMap.put("password", "");
                paramMap.put("email","");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                if(res.equals("ok")){
                    curName.setText("当前用户名：" + name);
                }
                Toast.makeText(that, "更新成功!", Toast.LENGTH_SHORT).show();
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);

            }
        });

        confirmEmail = findViewById(R.id.confirm_email);
        editNewEmail = findViewById(R.id.edit_email);
        curEmail = findViewById(R.id.center_current_email);
        curEmail.setText("当前邮箱：" + email);
        confirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                email = editNewEmail.getText().toString();
                // TODO: Connect backend
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image","");
                paramMap.put("introduction","");
                paramMap.put("user_name", "");
                paramMap.put("password", "");
                paramMap.put("email",email);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                JSONObject obj = new JSONObject(paramMap);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String obj_string = obj.toJSONString();
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                if(res.equals("ok")){
                    curEmail.setText("当前邮箱：" + email);
                }
                Toast.makeText(that, "更新成功!", Toast.LENGTH_SHORT).show();

//                paramMap.put("user_name", account);
//                paramMap.put("password", password);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

        confirmPassword = findViewById(R.id.confirm_password);
        editNewPassword = findViewById(R.id.edit_password);
        curPassword = findViewById(R.id.center_current_password);
        curPassword.setText("当前密码：" + password);
        confirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                password = editNewPassword.getText().toString();
                // TODO: Connect backend
//                paramMap.put("user_name", account);
//                paramMap.put("password", password);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////

                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image","");
                paramMap.put("introduction","");
                paramMap.put("user_name", "");
                paramMap.put("password",password);
                paramMap.put("email","");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                JSONObject obj = new JSONObject(paramMap);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String obj_string = obj.toJSONString();
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                if(res.equals("ok")){
                    curPassword.setText("当前密码：" + password);
                }
                Toast.makeText(that, "更新成功!", Toast.LENGTH_SHORT).show();
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

        confirmIntroduction = findViewById(R.id.confirm_introduction);
        editNewIntroduction = findViewById(R.id.edit_introduction);
        curIntroduction = findViewById(R.id.center_current_introduction);
        curIntroduction.setText("当前介绍：" + introduction);
        confirmIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                introduction = editNewIntroduction.getText().toString();
                // TODO: Connect backend
//                paramMap.put("user_name", account);
//                paramMap.put("password", password);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////

                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image","");
                paramMap.put("introduction",introduction);
                paramMap.put("user_name", "");
                paramMap.put("password","");
                paramMap.put("email","");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                JSONObject obj = new JSONObject(paramMap);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String obj_string = obj.toJSONString();
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                if(res.equals("ok")){
                    curIntroduction.setText("当前介绍：" + introduction);
                }
                Toast.makeText(that, "更新成功!", Toast.LENGTH_SHORT).show();
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);
            }
        });
        user_image = findViewById(R.id.user_image);
        add_text = findViewById(R.id.add_text);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoOrSelectPicture();
            }
        });
        confirmImage = findViewById(R.id.confirm_image);
        confirmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image", FileUtil.file(tmp_filepath));
                paramMap.put("introduction","");
                paramMap.put("user_name", "");
                paramMap.put("password","");
                paramMap.put("email","");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                Toast.makeText(that, "更新成功!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void takePhotoOrSelectPicture() {
        CharSequence[] items = {"拍照","图库"};// 裁剪items选项

        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(AccountActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            // 选择了拍照
                            case 0:
                                // 创建文件保存拍照的图片
                                tmp_filepath = Environment.getExternalStorageDirectory()+"/"+"take_photo_image"+get_time()+".jpg";
                                File takePhotoImage = new File(tmp_filepath);
                                try {
                                    // 文件存在，删除文件
                                    if(takePhotoImage.exists()){
                                        takePhotoImage.delete();
                                    }
                                    // 根据路径名自动的创建一个新的空文件
                                    takePhotoImage.createNewFile();
                                    tmp_photo=takePhotoImage;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    if (ContextCompat.checkSelfPermission(AccountActivity.this,
                                            android.Manifest.permission.CAMERA) != PackageManager
                                            .PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(AccountActivity.this, new
                                                String[]{Manifest.permission.CAMERA }, 1);
                                    } else {
                                        my_take_photo(takePhotoImage);
                                    }
                                }

                                break;
                            // 调用系统图库
                            case 1:

                                // 创建Intent，用于打开手机本地图库选择图片
                                Intent intent1 = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // 启动intent打开本地图库
                                startActivityForResult(intent1,LOCAL_CROP);
                                break;

                        }

                    }
                }).show();
    }
    private void my_take_photo(File takePhotoImage){
        // 获取图片文件的uri对象
//        imageUri = Uri.fromFile(takePhotoImage);
        // 创建Intent，用于启动手机的照相机拍照
        imageUri = FileProvider.getUriForFile(that,"com.example.groupproject.fileProvider",takePhotoImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 指定输出到文件uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        // 启动intent开始拍照
        startActivityForResult(intent, TAKE_PHOTO);

    }
    public  String get_time(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式


        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区


        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间


        String createDate = formatter.format(curDate);   //格式转换
        return createDate;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case TAKE_PHOTO:// 拍照

                if (resultCode == RESULT_OK) {
//                    // 创建intent用于裁剪图片
//                    Intent intent = new Intent("com.android.camera.action.CROP");
//                    // 设置数据为文件uri，类型为图片格式
//                    intent.setDataAndType(imageUri, "image/*");
//                    // 允许裁剪
//                    intent.putExtra("scale", true);
//                    // 指定输出到文件uri中
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    // 启动intent，开始裁剪
//                    startActivityForResult(intent, CROP_PHOTO);
                    try {
                        // 创建BitmapFactory.Options对象
                        System.out.println("????????????????????"+tmp_filepath);
                        BitmapFactory.Options option = new BitmapFactory.Options();
                        // 属性设置，用于压缩bitmap对象
                        option.inSampleSize = 1;
                        option.inPreferredConfig = Bitmap.Config.RGB_565;
                        // 根据文件流解析生成Bitmap对象
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, option);
                        // 展示图片
//                        file_btn.setVisibility(View.INVISIBLE);
                        add_text.setVisibility(View.INVISIBLE);
                        user_image.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case LOCAL_CROP:// 系统图库

                if (resultCode == RESULT_OK) {
//                    // 创建intent用于裁剪图片
//                    Intent intent1 = new Intent("com.android.camera.action.CROP");
//                    // 获取图库所选图片的uri
//                    Uri uri = data.getData();
//                    intent1.setDataAndType(uri, "image/*");
//                    //  设置裁剪图片的宽高
//                    intent1.putExtra("outputX", 300);
//                    intent1.putExtra("outputY", 300);
//                    // 裁剪后返回数据
//                    intent1.putExtra("return-data", true);
//                    // 启动intent，开始裁剪
//                    startActivityForResult(intent1, CROP_PHOTO);
                    try {
                        // 根据返回的data，获取Bitmap对象
                        Uri tmp_uri = data.getData();
                        String path = itemCreateActivity.getimagePathFromUri(AccountActivity.this,tmp_uri);
                        tmp_filepath = path;
                        BitmapFactory.Options option = new BitmapFactory.Options();
//                            // 属性设置，用于压缩bitmap对象
                        option.inSampleSize = 1;
                        option.inPreferredConfig = Bitmap.Config.RGB_565;
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tmp_uri), null, option);
                        // 展示图片
//                        file_btn.setVisibility(View.INVISIBLE);
                        add_text.setVisibility(View.INVISIBLE);
                        user_image.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }
}