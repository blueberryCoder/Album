package com.bluberry.album.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bluberry.album.R;
import com.bluberry.album.base.BaseMvpActivity;
import com.bluberry.album.entity.Image;
import com.bluberry.album.presentor.AlbumPresentor;
import com.bluberry.album.view.adapter.AlbumGridViewAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 主页面
 * 多选相册
 */
public class AlbumActivity extends BaseMvpActivity<IAlbumView,AlbumPresentor<IAlbumView>> implements IAlbumView{

    public static final String IMAGES_PATH_CODE ="IMAGES";

    private GridView mGridView ;
    private AlbumGridViewAdapter mAdapter;
    private ProgressDialog mProgressDialog ;
    private Button btnSelected;
    private Button btnPreview;
    private PopupWindow mPopWindow;

    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView() ;
        mPresentor.queryImagesOfAlbum(this);
    }

    private void initView() {
        mGridView = (GridView)findViewById(R.id.main_gridview);
        btnSelected = (Button)findViewById(R.id.btn_selected);
        btnPreview = (Button)findViewById(R.id.btn_preview);

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getSelectedIamges()==null || mAdapter.getSelectedIamges().size() == 0){
                    return ;
                }

                ScrollViewGroup scrollViewGroup = new ScrollViewGroup(AlbumActivity.this);

                for(int i=0;i<mAdapter.getSelectedIamges().size();i++){
                    ImageView imageView = new ImageView(AlbumActivity.this);

                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                   mAttacher = new PhotoViewAttacher(imageView);



                    Glide.with(AlbumActivity.this).load(mAdapter.getSelectedIamges().get(i).path).into(imageView);
                    scrollViewGroup.addView(imageView);
                }
                 mPopWindow = new PopupWindow(scrollViewGroup,ViewGroup.LayoutParams.MATCH_PARENT,
                         ViewGroup.LayoutParams.MATCH_PARENT);
                mPopWindow.showAtLocation(mGridView, Gravity.CENTER, 0,0);
            }
        });
        mAdapter = new AlbumGridViewAdapter(this,mPresentor.getImages());
        btnSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });

        mAdapter.setOnSelectedChangeListener(new AlbumGridViewAdapter.OnSelectedChangeListener() {
            @Override
            public void OnSelectedChange(List<Image> selecteds) {
                btnSelected.setText("确定("+selecteds.size()+")");
            }

            @Override
            public void onSelectedOverflow(int max) {
              Toast toast =  Toast.makeText(AlbumActivity.this,"最多选取"+max+"张",Toast.LENGTH_SHORT) ;
                      toast  .setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    private void returnResult() {
        ArrayList<String> mList = new ArrayList<String>();
        List<Image> images =  mAdapter.getSelectedIamges() ;
        if(images!=null){
            for(Image image :images){
                mList.add(image.path);
            }
        }
        Intent intent = new Intent() ;
        intent.putStringArrayListExtra(IMAGES_PATH_CODE,mList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected AlbumPresentor<IAlbumView> createPresentor() {
        return new AlbumPresentor<>();
    }

    @Override
    public void showImages() {
        mAdapter.changeDataSet(mPresentor.getImages());
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void showProgressDialog() {
       mProgressDialog = ProgressDialog.show(this,"正在加载 ",null);
    }

    @Override
    public void cancelProgressDialog() {
        if(null!=mProgressDialog){
            mProgressDialog.dismiss();
            mProgressDialog =null ;
        }
    }

    @Override
    public void onBackPressed() {
        if(mPopWindow!=null && mPopWindow.isShowing()){
            mPopWindow.dismiss();return;
        }
        super.onBackPressed();
    }
}
