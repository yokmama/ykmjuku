package ykmjuku.android.sample.app006;

/***
 * Copyright (c) 2010-2012 Re:Kayo-System, Ltd. All rights reserved.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class MainActivity extends Activity {
    private Camera mCamera;
    private SurfaceView mSurface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSurface = (SurfaceView) findViewById(R.id.surfaceView1);

        SurfaceHolder holder = mSurface.getHolder();
        holder.addCallback(new MyHolder());
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private boolean isPortrait() {
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    class MyHolder implements SurfaceHolder.Callback {
        public void surfaceCreated(SurfaceHolder holder) {
            if (mCamera == null) {
                mCamera = Camera.open();
            }

            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                mCamera.release();
                mCamera = null;
            }
        }

        /***
         * CameraのPreviewはカメラからうけとった画像をアスペクト比をむししてそのまま貼りつけてしまうので、フルスクリーンのアプリに一杯の
         * 状態にしないと縦横の比率がおかしくなってしまうので、ここではプレビューサイズからサーフェスの大きさを再設定しています。
         */
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                int height) {
            mCamera.stopPreview();
            Parameters mParam = mCamera.getParameters();

            // カメラの向きをレイアウトにあわせて変更する
            boolean portrait = isPortrait();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                // 2.1以前では
                mParam.set("orientation", portrait ? "portrait" : "landscape");
            } else {
                mCamera.setDisplayOrientation(portrait ? 90 : 0);
            }

            // 縦レイアウトと横レイアウトで縦と横を入れ替える
            int previewWidth = portrait ? height : width;
            int previewHeight = portrait ? width : height;

            // サポートされているプレビューの中から画面にあった最大のサイズを取得する
            List<Size> sizes = mParam.getSupportedPreviewSizes();
            int tmpHeight = 0;
            int tmpWidth = 0;
            for (Size size : sizes) {
                if ((size.width > previewWidth)
                        || (size.height > previewHeight)) {
                    continue;
                }
                if (tmpHeight < size.height) {
                    tmpWidth = size.width;
                    tmpHeight = size.height;
                }
            }
            previewWidth = tmpWidth;
            previewHeight = tmpHeight;

            // プレビューのサイズを設定する
            mParam.setPreviewSize(previewWidth, previewHeight);

            // レイアウトのサイズを向いてる方向にあわせて入りかえておく
            float layoutHeight = portrait ? previewWidth : previewHeight;
            float layoutWidth = portrait ? previewHeight : previewWidth;

            // サーフェスのサイズをプレビューの比率にあわせて再設定する
            float fact = Math.min(height / layoutHeight, width / layoutWidth);
            ViewGroup.LayoutParams layoutParams = mSurface.getLayoutParams();
            layoutParams.height = (int) (layoutHeight * fact);
            layoutParams.width = (int) (layoutWidth * fact);
            mSurface.setLayoutParams(layoutParams);

            mCamera.setParameters(mParam);
            mCamera.startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    };
}