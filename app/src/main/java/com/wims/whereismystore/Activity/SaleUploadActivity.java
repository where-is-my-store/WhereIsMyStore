package com.wims.whereismystore.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.wims.whereismystore.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SaleUploadActivity extends AppCompatActivity {
    private static final int FROM_ALBUM = 1;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private String strIndustry=null;
    private ArrayList<Uri> imageList;
    private ArrayList<Uri> currentImageList=new ArrayList();
    private int currentImageIndex=0;
    private Button addressButton;
    private ImageButton select_images_btn;
    private ImageView[] image=new ImageView[10];
    private Spinner indSpin;

    private ArrayAdapter<CharSequence> industryGroupSpin;
    private EditText address_editText;
    private String BNumTotal;
    private EditText price;
    private EditText title;
    private EditText contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_upload);

        image[0]=findViewById(R.id.upload_image1);image[1]=findViewById(R.id.upload_image2);
        image[2]=findViewById(R.id.upload_image3);image[3]=findViewById(R.id.upload_image4);
        image[4]=findViewById(R.id.upload_image5);image[5]=findViewById(R.id.upload_image6);
        image[6]=findViewById(R.id.upload_image7);image[7]=findViewById(R.id.upload_image8);
        image[8]=findViewById(R.id.upload_image9);image[9]=findViewById(R.id.upload_image10);

        price=findViewById(R.id.upload_price_editText);
        title=findViewById(R.id.upload_title_editText);
        contents=findViewById(R.id.upload_contents_editText);

        address_editText=findViewById(R.id.upload_address_EditText);
        //취소 버튼 클릭 시 업로드 취소
        Button cancel_btn=findViewById(R.id.upload_cancle_button);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //스피너 업종별 선택
        indSpin=findViewById(R.id.industryName);

        industryGroupSpin=ArrayAdapter.createFromResource(this,R.array.industryGroup,R.layout.support_simple_spinner_dropdown_item);
        industryGroupSpin.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        indSpin.setAdapter(industryGroupSpin);
        indSpin.setSelection(1);
        indSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strIndustry=industryGroupSpin.getItem(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //주소 검색
        addressButton=findViewById(R.id.upload_address_button);
        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SaleUploadActivity.this, WebViewActivity.class);
                startActivityForResult(i,SEARCH_ADDRESS_ACTIVITY);
            }
        });
        //사업자 번호 합치기
        EditText BNum1=findViewById(R.id.upload_BNum_first_editText);
        EditText BNum2=findViewById(R.id.upload_BNum_middle_editText);
        EditText BNum3=findViewById(R.id.upload_BNum_last_editText);

        BNumTotal=BNum1.getText().toString()+BNum2.getText().toString()+BNum3.getText().toString();


        //업로드 할 이미지 선택
        select_images_btn=findViewById(R.id.upload_images_button);
        select_images_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPermission();
                if(currentImageIndex<10)
                    selectPhotoDialog();
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(SaleUploadActivity.this);
                    builder.setTitle("알림").setMessage("사진을 10개까지만 선택이 가능합니다.ㅜㅜ");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
            }
        });
        //완료 버튼 클릭 시 업로드
        Button complete_btn=findViewById(R.id.upload_complete_button);
        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BNumTotal.length()!=10||currentImageIndex==0||address_editText.getText().length()==0||price.getText().length()==0||title.getText().length()==0||contents.getText().length()==0){
                    AlertDialog.Builder builder=new AlertDialog.Builder(SaleUploadActivity.this);
                    builder.setTitle("알림").setMessage("빈 공간이 있습니다.\n모두 작성해주세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }else{

                }
            }
        });
        //이미지 선택 시 이미지 삭제(1~10)
        image[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(0);
            }
        });
        image[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(1);
            }
        });
        image[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(2);
            }
        });
        image[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(3);
            }
        });
        image[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(4);
            }
        });
        image[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(5);
            }
        });
        image[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(6);
            }
        });
        image[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(7);
            }
        });
        image[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(8);
            }
        });
        image[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageViewResource(9);
            }
        });

    }
    //이미지뷰 클릭 시 삭제
    private void deleteImageViewResource(int index){
        Log.d("index",""+currentImageIndex);
        if(currentImageIndex==0){
            currentImageList.clear();
            image[currentImageIndex].setImageResource(0);
            currentImageIndex=0;
        }else {
            currentImageList.remove(index);
            image[currentImageIndex].setImageResource(0);
            currentImageIndex--;
            for (int i = index; i <= currentImageIndex; i++) {
                setImage(i);
            }
        }
    }
    //사진 버튼 클릭 시 앨범 선택 다이어로그
    private void selectPhotoDialog(){
        AlertDialog.Builder alt_bld=new AlertDialog.Builder(SaleUploadActivity.this);
        alt_bld.setTitle("사진 업로드").setCancelable(false).setNeutralButton("앨범선택", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("upload", "다이얼로그 > 앨범선택 선택");
                //앨범에서 선택
                selectAlbum();
            }
        }).setNegativeButton("취소 ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("upload", "다이얼로그 > 취소 선택");
                //취소 클릭, dialog 닫기
                dialog.cancel();
            }
        });
        AlertDialog alert=alt_bld.create();
        alert.show();
    }

    public void selectAlbum(){
        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("upload", "onActivityResult CALL");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FROM_ALBUM){
            if(resultCode== Activity.RESULT_OK){
                if(currentImageIndex<10) {
                    imageList = new ArrayList();
                    int imageListIndex = currentImageIndex;

                    // 멀티 선택을 지원하지 않는 기기에서는 getClipdata()가 없음 => getData()로 접근해야 함
                    if (data.getClipData() == null) {
                        Log.i("upload", "1. single choice" + String.valueOf(data.getData()));
                        imageList.add(data.getData());
                        imageListIndex++;
                    } else {
                        ClipData clipData = data.getClipData();
                        Log.i("upload", "clipdata " + String.valueOf(clipData.getItemCount()));
                        if (clipData.getItemCount() > 10) {
                            Toast.makeText(SaleUploadActivity.this, "사진은 10개까지 선택가능 합니다.\n다시 선택해 주세요", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //멀티 선택에서 하나만 선택했을 경우
                        else if (clipData.getItemCount() == 1) {
                            Uri dataStr = clipData.getItemAt(0).getUri();
                            Log.i("upload", "2. clipdata choice " + String.valueOf(clipData.getItemAt(0).getUri()));
                            Log.i("upload", "2. single choice " + clipData.getItemAt(0).getUri().getPath());
                            imageList.add(dataStr);
                            imageListIndex++;
                        } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                if (imageListIndex == 10) {
                                    Toast.makeText(SaleUploadActivity.this, "사진은 10개까지 선택가능 합니다.\n다시 선택해 주세요", Toast.LENGTH_LONG).show();
                                    return;
                                } else {
                                    Log.i("upload", "3. single choice " + String.valueOf(clipData.getItemAt(i).getUri()));
                                    imageList.add(clipData.getItemAt(i).getUri());
                                    imageListIndex++;
                                }
                            }
                        }
                    }
                    showSelectedImage(imageList);
                }else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(SaleUploadActivity.this);
                    builder.setTitle("알림").setMessage("사진을 10개까지만 선택이 가능합니다.ㅜㅜ");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
            }else{
                Toast.makeText(SaleUploadActivity.this, "사진 선택을 취소하였습니다", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==SEARCH_ADDRESS_ACTIVITY){
            if(resultCode==RESULT_OK){
                String address_text=data.getExtras().getString("data");
                if(data!=null){
                    address_editText.setText(address_text);
                }
            }
        }
    }
    private void showSelectedImage(ArrayList<Uri> imageList){
        currentImageIndex=imageList.size()-1;
        for(int i=0; i<imageList.size();i++){
            currentImageList.add(imageList.get(i));
            setImage(i);
        }
    }
    private void setImage(int index){
        Picasso.get()
                .load((Uri)currentImageList.get(index))
                .into(image[index]);
    }

    //카메라, 사진첩 접근 퍼미션
    private void setPermission(){
        PermissionListener permissionListener =new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(SaleUploadActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(SaleUploadActivity.this, "Permission Denied: "+deniedPermissions.get(0), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("사진 업로드를 위해서는 사진 접근 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한]에서 권한을 허용할 수 있습니다.")
                .setPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .check();
    }
    private void writePost(){
        
    }

}