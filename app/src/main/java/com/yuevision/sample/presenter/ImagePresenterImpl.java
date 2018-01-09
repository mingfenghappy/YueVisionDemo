package com.yuevision.sample.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.yuevision.sample.iview.IImgListener;
import com.yuevision.sample.modle.ImageModelImpl;
import com.yuevision.sample.utils.MLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by sjy on 2018/1/8.
 */

public class ImagePresenterImpl implements IImgListener {
    IImgListener iGetMessageView;
    Context context;
    ImageModelImpl model;

    public ImagePresenterImpl(Context context, IImgListener iGetMessageView) {
        this.iGetMessageView = iGetMessageView;
        this.context = context;
        model = new ImageModelImpl(context);
    }

    public void pGetImageResult(Bitmap bmp, int orientation) {
        try {
            Bitmap newBmp = adjustPhotoRotation(bmp, orientation);
            //方式1 图片质量大
            //        int bytes = bmp.getByteCount();
            //        ByteBuffer buf = ByteBuffer.allocate(bytes);
            //        bmp.copyPixelsToBuffer(buf);
            //        byte[] byteArray = buf.array();

            //方式2 图片质量小
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            MLog.d("上传图片大小：" + byteArray.length / 1024 + "Kb");
            model.getImageResult(byteArray, "1", "5940e7451d88e", iGetMessageView);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetSuccess(Object object) {
        iGetMessageView.onGetSuccess(object);
    }

    @Override
    public void onGetFailed(String code, String result, Exception e) {
        iGetMessageView.onGetFailed(code, result, e);
    }

    //旋转图片 根据获取的bitmap的角度修改
    private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }


}
