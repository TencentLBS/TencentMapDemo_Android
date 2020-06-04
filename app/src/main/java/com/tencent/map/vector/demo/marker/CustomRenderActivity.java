package com.tencent.map.vector.demo.marker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.CustomRender;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * OpenGL绘制
 */
public class CustomRenderActivity extends AppCompatActivity {

    private static final String TAG = CustomRenderActivity.class.getSimpleName();

    // 标识是否第一次绘制3D立方体
    private boolean mIsFirstDraw3DCube = true;

    // 立方体8个顶点坐标
    private float[] mVertices = {
            -1, -1, -0,
            1, -1, -0,
            1, 1, -0,
            -1, 1, -0,
            -1, -1, 1,
            1, -1, 1,
            1, 1, 1,
            -1, 1, 1,
    };

    // 3D立方体顶点绘制顺序列表
    private short[] mDrawIndices = {
            5, 4, 0, 1, 5, 0, 6, 5, 1, 2, 6, 1,
            7, 6, 2, 3, 7, 2, 4, 7, 3, 0, 4, 3,
            6, 7, 4, 5, 6, 4, 1, 0, 3, 2, 1, 3
    };

    // 3D立方体8个顶点颜色值
    private float[] mVertexColors = {
            1f, 1f, 0f, 1f,
            0f, 1f, 1f, 1f,
            1f, 0f, 1f, 1f,
            0f, 0f, 0f, 1f,
            1f, 1f, 1f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f
    };

    // 立方体顶点坐标Buffer
    private FloatBuffer mVertexBuffer;

    // 顶点绘制顺序Buffer
    private ShortBuffer mIndexBuffer;

    // 立方体顶点颜色Buffer
    private FloatBuffer mColorBuffer;

    // 3D立方体着色器
    private CubeShader mCubeShader;

    private MapView mapView;

    private TencentMap mMap;

    private LatLng latlng = new LatLng(40.04199816,116.27479076);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_render);
        mapView = findViewById(R.id.map_view);
        mMap = mapView.getMap();
        mMap.addOnMapLoadedCallback(new TencentMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.setCustomRender(new CubeRender());
                mMap.removeOnMapLoadedCallback(this);
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                .target(latlng).zoom(16).tilt(45).build()));


    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
    }

    private class CubeRender implements CustomRender {

        private void initCubeData(float width, float height, float depth) {
            // 对标地图坐标
            initVertices(mVertices, width, height, depth);

            // 立方体顶点数据Buffer
            mVertexBuffer = ByteBuffer.allocateDirect(mVertices.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            mVertexBuffer.put(mVertices).position(0);

            // 立方体顶点绘制顺序Buffer
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(mDrawIndices.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            mIndexBuffer = byteBuffer.asShortBuffer();
            mIndexBuffer.put(mDrawIndices);
            mIndexBuffer.position(0);

            // 立方体顶点颜色Buffer
            ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(mVertexColors.length * 4);
            byteBuffer1.order(ByteOrder.nativeOrder());
            mColorBuffer = byteBuffer1.asFloatBuffer();
            mColorBuffer.put(mVertexColors);
            mColorBuffer.position(0);
        }

        private void initCubeShader() {
            mCubeShader = new CubeShader();
            mCubeShader.init();
        }

        private void initVertices(float[] vertices, float width, float height, float depth) {
            for (int i = 0; i < vertices.length / 3; i++) {
                int m = i * 3;
                vertices[m] = vertices[m] * width;
                vertices[m + 1] = vertices[m + 1] * height;
                vertices[m + 2] = vertices[m + 2] * depth;
            }
        }

        @Override
        public void onDrawFrame() {
            drawCube();
        }

        private void drawCube() {

            if (mIsFirstDraw3DCube) {
                float cubeWidth = 0.2f * 10000 / 2;
                float cubeHeight = 0.2f * 10000 / 2;
                float cubeDepth = 0.4f * 10000 / 2;
                initCubeData(cubeWidth, cubeHeight, cubeDepth);
                initCubeShader();
                mIsFirstDraw3DCube = false;
            }

            if (null == mCubeShader) {
                return;
            }

            // 绑定地图移动
            PointF p1f = mMap.getProjection().glVertexForCoordinate(latlng);
            float[] mvpMatrix = mMap.getProjection().glModelMatrix(p1f, 1);

            // Step2 开始绘制设置
            GLES20.glUseProgram(mCubeShader.mProgram);
            checkGlError("glUseProgram");

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            checkGlError("glEnable");

            //对齐矩阵
            GLES20.glUniformMatrix4fv(mCubeShader.mMvpMatrix, 1, false, mvpMatrix, 0);
            checkGlError("glUniformMatrix4fv");

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            // 顶点指针
            GLES20.glEnableVertexAttribArray(mCubeShader.mVertex);
            checkGlError("glEnableVertexAttribArray");
            GLES20.glVertexAttribPointer(mCubeShader.mVertex, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
            checkGlError("glVertexAttribPointer");

            // 颜色指针
            GLES20.glEnableVertexAttribArray(mCubeShader.mColor);
            checkGlError("glEnableVertexAttribArray");
            GLES20.glVertexAttribPointer(mCubeShader.mColor, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
            checkGlError("glVertexAttribPointer");

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            // 开始画
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawIndices.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
            checkGlError("glDrawElements");

            GLES20.glDisableVertexAttribArray(mCubeShader.mVertex);
            checkGlError("glDisableVertexAttribArray");

            GLES20.glDisable(GLES20.GL_DEPTH_TEST);
            checkGlError("glDisable");
        }
    }

    private static class CubeShader {
        int mVertex;
        int mMvpMatrix;
        int mColor;
        int mProgram;

        String vertexShader = "precision mediump float;\n" +
                "        attribute vec3 mVertex;//顶点数组,三维坐标\n" +
                "        attribute vec4 mColor;//颜色数组,三维坐标\n" +
                "        uniform mat4 mMvpMatrix;//mvp矩阵\n" +
                "        varying vec4 color;//\n" +
                "        void main(){\n" +
                "            gl_Position = mMvpMatrix * vec4(mVertex, 1.0);\n" +
                "            color = mColor;\n" +
                "        }";

        String fragmentShader = "//有颜色 没有纹理\n" +
                "        precision mediump float;\n" +
                "        varying vec4 color;//\n" +
                "        void main(){\n" +
                "            gl_FragColor = color;\n" +
                "        }";

        public void init() {
            int vertexLocation = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            GLES20.glShaderSource(vertexLocation, vertexShader);
            checkGlError("glShaderSource");
            GLES20.glCompileShader(vertexLocation);
            checkGlError("glCompileShader");
            int fragmentLocation = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
            GLES20.glShaderSource(fragmentLocation, fragmentShader);
            checkGlError("glShaderSource");
            GLES20.glCompileShader(fragmentLocation);
            checkGlError("glCompileShader");

            mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(mProgram, vertexLocation);
            GLES20.glAttachShader(mProgram, fragmentLocation);
            GLES20.glLinkProgram(mProgram);
            int[] linked = new int[1];
            GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linked, 0);
            checkGlError("glLinkProgram");
            if (linked[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(mProgram));
                GLES20.glDeleteProgram(mProgram);
                mProgram = 0;
            }


            mVertex = GLES20.glGetAttribLocation(mProgram, "mVertex");
            mMvpMatrix = GLES20.glGetUniformLocation(mProgram, "mMvpMatrix");
            mColor = GLES20.glGetAttribLocation(mProgram, "mColor");
        }

    }

    static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
        }
    }
}
