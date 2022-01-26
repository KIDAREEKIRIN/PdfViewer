package com.srctree.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_asset1;
    Button btn_asset2;
    Button btn_asset3;
    Button btn_remote1;
    Button btn_remote2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_asset1 = findViewById(R.id.btn_asset1);
        btn_asset2 = findViewById(R.id.btn_asset2);
        btn_asset3 = findViewById(R.id.btn_asset3);
        btn_remote1 = findViewById(R.id.btn_remote1);
        btn_remote2 = findViewById(R.id.btn_remote2);

        btn_asset1.setOnClickListener(this);
        btn_asset2.setOnClickListener(this);
        btn_asset3.setOnClickListener(this);
        btn_remote1.setOnClickListener(this);
        btn_remote2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btn_asset1) {
            openPdf("pdf_sample1.pdf");
        }
        else if(view == btn_asset2) {
            openPdf("pdf_sample2.pdf");
        }
        else if(view == btn_asset3) {
            openPdf("pdf_sample3.pdf");
        }
        else if(view == btn_remote1) {
            openPdf("http://3.37.249.79/dutylist/sooryun/sooryun1.pdf");
        }
        else if(view == btn_remote2) {
            openPdf("https://www.robertwalters.co.kr/content/dam/robert-walters/country/korea/files/resume/RESUME_Templete_6_KR.pdf");
        }
    }

    void openPdf(String fileName) {
        copyFileFromAssets(fileName);

        /** PDF reader code */
        File file = new File(getFilesDir() + "/" + fileName);

        Uri uri = null;
        if(!fileName.startsWith("http")) {
            uri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file);
        } else {
            uri = Uri.parse(fileName);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFileFromAssets(String fileName) {
        AssetManager assetManager = this.getAssets();

        //앱 내의 파일폴더에 저장
        File cacheFile = new File( getFilesDir() + "/" + fileName );
        InputStream in = null;
        OutputStream out = null;
        try {
            if ( cacheFile.exists() ) {
                return;
            } else {
                in = assetManager.open(fileName);
                out = new FileOutputStream(cacheFile);
                copyFile(in, out);

                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}