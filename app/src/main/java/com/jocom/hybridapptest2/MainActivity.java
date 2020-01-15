package com.jocom.hybridapptest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    WebView wv;
    TextView tv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv= findViewById(R.id.wv);
        tv= findViewById(R.id.tv);
        et= findViewById(R.id.et);

        //웹뷰설정들
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());

        WebSettings settings= wv.getSettings();
        settings.setJavaScriptEnabled(true);

        //ajax기술은 원래 서버에서만 사용가능했었음.
        //안드로이드 안에서 허용하도록 설정할 수 있음
        //즉, 경로가 http://가 아니라 file://이어도 ajax가 동작함
        settings.setAllowUniversalAccessFromFileURLs(true);

        // 웹뷰와 연결하는 연결자객체 생성 및 설정
        // 2번째 파라미터 : JS에서 이 연결자 객체를 부르는 이름(별명) 지정
        wv.addJavascriptInterface( new WebViewConnector(), "Droid");

        //웹뷰가 보여줄 웹문서 로드하기
        wv.loadUrl("file:///android_asset/index.html");

    }

    public void clickSend(View view) {
        //웹뷰에서 보여주는 index.html안에 있는
        //특정 함수(프로그래머가 만든 함수 : setReceivedMessage())를 호출해서 값을 전달
        //Native Java에서 html의 dom요소를 직접 제어할 수 없음

        //웹뷰에 보낼 글씨
        String msg= et.getText().toString();
        wv.loadUrl("javascript:setReceivedMessage('"+msg+"')");

        et.setText("");
    }


    //웹뷰의 Javascript와 통신을 담당할 중계자(위임자, 연결자) 클래스 정의
    class WebViewConnector{
        //javascript에서 호출 할 메소드
        //이 메소드를 javascript에서 호출할 수 있게 하려면
        //특별한 어노테이션을 지정해야함

        @JavascriptInterface
        public void setTextView( String msg ){
            tv.setText( msg );
        }

        @JavascriptInterface
        public void openGallery(){
            Intent intent= new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivity(intent);
        }


    }

}

