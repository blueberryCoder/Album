package com.bluberry.album.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.bluberry.album.R;
import com.bluberry.album.base.BaseMvpActivity;
import com.bluberry.album.entity.Image;
import com.bluberry.album.presentor.AlbumPresentor;
import com.bluberry.album.view.adapter.AlbumGridViewAdapter;

import java.util.ArrayList;
import java.util.List;


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
        mAdapter = new AlbumGridViewAdapter(this,mPresentor.getImages());
        btnSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
