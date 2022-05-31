package com.example.groupproject;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;


public class itemCreateActivity extends AppCompatActivity {
    private static final int TAKE_PHOTO = 11;// 拍照
    private static final int LOCAL_CROP = 13;// 本地图库
    private static final int TAKE_VIDEO = 14;
    private static final int LOCAL_VIDEO = 15;
    private File tmp_photo;
    private EditText title;
    private EditText content;
    private int global_type = 0;

    private Uri imageUri;
    private Uri VideoUri;
    String total_msg = "";
    Activity that = this;
    String tmp_title = "unset";
    String tmp_content = "unset";
    int tmp_type = 0;//默认为纯文本
    String tmp_loc = "unset";
    String tmp_filepath = "unset";
    ArrayList<item_unfinished> item_list = new ArrayList<>();
   ImageView file_btn;
    Button save_btn;
    TextView add_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_create);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        file_btn = findViewById(R.id.add_btn);
        file_btn.setVisibility(View.VISIBLE);
        add_text = findViewById(R.id.add_text);
        add_text.setVisibility(View.VISIBLE);
        readSDcard();
        if(total_msg!=""){
            HashMap mapType = JSON.parseObject(total_msg,HashMap.class);
//            String tmp_str=mapType.get("save_list").toString();
            System.out.println(mapType.get("save_list").toString());
            JSONArray tmp_list = (JSONArray) mapType.get("save_list");
            JSONObject tmp =  (JSONObject) tmp_list.get(0);
            title = findViewById(R.id.title_input);
            title.setText(tmp.get("title").toString());
            content = findViewById(R.id.content_input);
            content.setText(tmp.get("content").toString());
            global_type= (int)tmp.get("type");

            String tmp_file = tmp.get("filename").toString();
            if(!tmp_file.equals("unset")){
                tmp_filepath = tmp_file;
                if(global_type ==1){
                    File file = new File(tmp_filepath);
                    if(file.exists()){
                        Bitmap bm = BitmapFactory.decodeFile(tmp_filepath);
                        file_btn.setImageBitmap(bm);
                    }

                }else if(global_type ==3){
                    Bitmap bm = getVideoThumb(tmp_filepath);
//                    file_btn.setVisibility(View.INVISIBLE);
                    add_text.setVisibility(View.INVISIBLE);
                    file_btn.setImageBitmap(bm);
                }else{

                }
            }


        }
        Spinner type_spin = findViewById(R.id.item_types);
        type_spin.setSelection(global_type);
        type_spin.setOnItemSelectedListener(new MySelectedListener());

//        mygeo_Thread my = new mygeo_Thread();
//        my.run();
//        System.out.println(total_msg);


        file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tmp_type == 0){

                }else if(tmp_type == 1){//图片
                    takePhotoOrSelectPicture();
                }else if(tmp_type == 2){//语音

                }else{//视频
                    takeVideoOrSelect();
                }
            }
        });
        EditText title = findViewById(R.id.title_input);
        EditText content = findViewById(R.id.content_input);
        Button send_btn = findViewById(R.id.send_button);
        send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(title.length()==0){
                    Toast.makeText(that,"标题不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    tmp_title = title.getText().toString();
                }
                if(content.length()==0){
                    Toast.makeText(that,"内容不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    tmp_content = content.getText().toString();
                }
                if(tmp_type !=0){
                    if(tmp_filepath.length()==0){
                        Toast.makeText(that,"上传文件不能为空",Toast.LENGTH_SHORT).show();
                    }
                }
                if(tmp_loc.length()==0){

                }
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",2);
                paramMap.put("title",tmp_title);
                paramMap.put("content",tmp_content);
                paramMap.put("type",tmp_type);
                paramMap.put("location",tmp_loc);
                paramMap.put("file", FileUtil.file(tmp_filepath));
                String result1= HttpUtil.post("http://183.172.179.184:8765/item/send/", paramMap);
                System.out.println(result1);
            }
        });
        save_btn = findViewById(R.id.save_button);
        save_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(title.length()==0){
                    tmp_title = "unset";
                }else {
                    tmp_title = title.getText().toString();
                }
                if(content.length()==0){
                    tmp_content = "unset";
                }else {
                    tmp_content = content.getText().toString();
                }
                if(tmp_type !=0){
                    if(tmp_filepath.length()==0){
                        tmp_filepath = "unset";
                    }
                }
                if(tmp_loc.length()==0){
                    tmp_loc = "unset";
                }
                item_unfinished it = new item_unfinished();
                it.setTitle(tmp_title);
                it.setContent(tmp_content);
                it.setType(tmp_type);
                it.setLoc(tmp_loc);
                it.setFilename(tmp_filepath);
//                String str = JSON.toJSONString(it);
                item_list.add(it);
                item_unfinished_list save_list = new item_unfinished_list();
                save_list.setlist(item_list);

                String str = JSON.toJSONString(save_list);
                writeSDcard(str);
                System.out.println(str);
            }
        });
    }
    class mygeo_Thread implements Runnable{
        @Override
        public void run() {
            LocationManager loactionmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(false);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = loactionmanager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(itemCreateActivity.this, new
                        String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 1);
                ActivityCompat.requestPermissions(itemCreateActivity.this, new
                        String[]{Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
            }
            Location location = loactionmanager.getLastKnownLocation(provider);

            double longitude = location.getLongitude();//经度
            double latitude = location.getLatitude();
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            String longti = nf.format(longitude);
            String lati = nf.format(latitude);
            String geo_str_o = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
            String geo_str_t = lati+","+longti+"&language=zh-CN&sensor=false";//37.422,-122.084&language=zh-CN&sensor=false
            String geo_str = geo_str_o+geo_str_t;
            System.out.println(geo_str);
            try{
                String geo_res = HttpUtil.get(geo_str);
                System.out.println(geo_res);
            } catch (Exception e) {e.printStackTrace();}

//            GeocodeAddress my_geo = new GeocodeAddress();
//            my_geo.onPostExecute(my_geo.doInBackground(location));
        }
    }

    public class MySelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0){

            }else if(position == 1){

            }else if(position ==2){

            }else if(position ==3){

            }
            tmp_type = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private void readSDcard(){
        try {
            // 推断是否存在SD卡
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的文件夹
                String sdDire = getExternalFilesDir(null).getPath();
                File testFile = new File(sdDire,"test.txt");
                FileInputStream inStream = new FileInputStream(testFile);
                InputStreamReader reader = new InputStreamReader(inStream, "GBK");
                BufferedReader bReader =  new BufferedReader(reader);
                StringBuffer stringBuffer = new StringBuffer("");
                String str;
                while ((str = bReader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                total_msg =  stringBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void writeSDcard(String str) {
        try {
            // 推断是否存在SD卡
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的文件夹
                String sdDire = getExternalFilesDir(null).getPath();
                File testFile = new File(sdDire,"test.txt");
                FileOutputStream outFileStream = new FileOutputStream(testFile);
                outFileStream.write(str.getBytes());
                outFileStream.close();
                System.out.println("test.txt ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GeocodeAddress extends AsyncTask<Location, Void, String>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Location... params) {
            // TODO Auto-generated method stub
            if(params[0]!=null)
            {
                Geocoder geocoder=new Geocoder(itemCreateActivity.this);
                try {
                    List<Address> address=geocoder.getFromLocation(params[0].getLatitude(), params[0].getLongitude(), 1);
                    tmp_loc="";
                    if(address.size()>0)
                    {
                        tmp_loc+="经度："+String.valueOf(address.get(0).getLongitude()*1E6)+"\n";
                        tmp_loc+="纬度："+String.valueOf(address.get(0).getLatitude()*1E6)+"\n";
                        tmp_loc+="国家："+address.get(0).getCountryName()+"\n";
                        tmp_loc+="省："+address.get(0).getAdminArea()+"\n";
                        tmp_loc+="城市："+address.get(0).getLocality()+"\n";
                        tmp_loc+="名称："+address.get(0).getAddressLine(1)+"\n";
                        tmp_loc+="街道："+address.get(0).getAddressLine(0);
                        return tmp_loc;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(result!=null&& !result.equals(""))
            {
                System.out.println(result);
            }
        }
    }
    public class item_unfinished_list{
        public ArrayList<item_unfinished> save_list = new ArrayList<>();
        public void setlist(ArrayList<item_unfinished> it_list){
            save_list = it_list;
        }
    }

    private void takeVideoOrSelect(){
        CharSequence[] items = {"拍照","图库"};// 裁剪items选项

        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(itemCreateActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            // 选择了拍照
                            case 0:
                                // 创建文件保存拍照的图片
                                File takeVideo = new File(Environment.getExternalStorageDirectory(), "take_video.mp4");
                                try {
                                    // 文件存在，删除文件
                                    if(takeVideo.exists()){
                                        takeVideo.delete();
                                    }
                                    // 根据路径名自动的创建一个新的空文件
                                    takeVideo.createNewFile();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                try{
                                    VideoUri = Uri.fromFile(takeVideo);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,VideoUri);
                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                                startActivityForResult(intent,TAKE_VIDEO);

                                break;
                            // 调用系统图库
                            case 1:

                                // 创建Intent，用于打开手机本地图库选择图片
                                Intent intent1 = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                // 启动intent打开本地图库
                                startActivityForResult(intent1,LOCAL_VIDEO);
                                break;

                        }

                    }
                }).show();
    }

    private void takePhotoOrSelectPicture() {
        CharSequence[] items = {"拍照","图库"};// 裁剪items选项

        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(itemCreateActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            // 选择了拍照
                            case 0:
                                // 创建文件保存拍照的图片
                                File takePhotoImage = new File(Environment.getExternalStorageDirectory(), "take_photo_image.jpg");
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
                                    if (ContextCompat.checkSelfPermission(itemCreateActivity.this,
                                            Manifest.permission.CAMERA) != PackageManager
                                            .PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(itemCreateActivity.this, new
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
        imageUri = Uri.fromFile(takePhotoImage);
        // 创建Intent，用于启动手机的照相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定输出到文件uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        // 启动intent开始拍照
        startActivityForResult(intent, TAKE_PHOTO);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    my_take_photo(tmp_photo);
                } else {
                    Toast.makeText(this, "You denied the pemission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    /**
     * 调用startActivityForResult方法启动一个intent后，
     * 可以在该方法中拿到返回的数据
     */
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
                        String path = getimagePathFromUri(itemCreateActivity.this,imageUri);
                        tmp_filepath = path;
                        BitmapFactory.Options option = new BitmapFactory.Options();
                        // 属性设置，用于压缩bitmap对象
                        option.inSampleSize = 1;
                        option.inPreferredConfig = Bitmap.Config.RGB_565;
                        // 根据文件流解析生成Bitmap对象
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, option);
                        // 展示图片
//                        file_btn.setVisibility(View.INVISIBLE);
                        add_text.setVisibility(View.INVISIBLE);
                        file_btn.setImageBitmap(bitmap);
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
                        String path = getimagePathFromUri(itemCreateActivity.this,tmp_uri);
                        tmp_filepath = path;
                        BitmapFactory.Options option = new BitmapFactory.Options();
//                            // 属性设置，用于压缩bitmap对象
                        option.inSampleSize = 1;
                        option.inPreferredConfig = Bitmap.Config.RGB_565;
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tmp_uri), null, option);
                        // 展示图片
//                        file_btn.setVisibility(View.INVISIBLE);
                        add_text.setVisibility(View.INVISIBLE);
                        file_btn.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_VIDEO:// 视频
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Video saved to:\n" +

                            data.getData(), Toast.LENGTH_LONG).show();
                    String path = getFilePathFromUri(itemCreateActivity.this,data.getData());
                    tmp_filepath =path;
                    Bitmap bm = getVideoThumb(path);
//                    file_btn.setVisibility(View.INVISIBLE);
                    add_text.setVisibility(View.INVISIBLE);
                    file_btn.setImageBitmap(bm);

                }
                break;

            case LOCAL_VIDEO:// 视频
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Video saved to:\n" +

                            data.getData(), Toast.LENGTH_LONG).show();
                    String path = getFilePathFromUri(itemCreateActivity.this,data.getData());
                    System.out.println(path);
                    tmp_filepath =path;
                    Bitmap bm = getVideoThumb(path);

//                    file_btn.setVisibility(View.INVISIBLE);
                    add_text.setVisibility(View.INVISIBLE);
                    file_btn.setImageBitmap(bm);
                }
                break;
            }
        }
        public static Bitmap getVideoThumb(String path){
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(path);
            return media.getFrameAtTime();
        }
        public static String getFilePathFromUri(Context context, Uri uri) {
            if (null == uri) return null;
            final String scheme = uri.getScheme();
            String data = null;
            if (scheme == null)
                data = uri.getPath();
            else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                data = uri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Video.VideoColumns
                        .DATA}, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Video.VideoColumns
                                .DATA);
                        if (index > -1) {
                            data = cursor.getString(index);
                        }
                    }
                    cursor.close();
                }
            }
            return data;
        }
        public static String getimagePathFromUri(Context context, Uri uri) {
            if (null == uri) return null;
            final String scheme = uri.getScheme();
            String data = null;
            if (scheme == null)
                data = uri.getPath();
            else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                data = uri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns
                        .DATA}, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (index > -1) {
                            data = cursor.getString(index);
                        }
                    }
                    cursor.close();
                }
            }
            return data;
        }

    }


