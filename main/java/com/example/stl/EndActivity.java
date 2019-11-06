package com.example.stl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//길안내를 종료할 시 실행되는 Activity
public class EndActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        button=(Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 모든 Activity를 종료시키지 않고 버튼 클릭 시 MainActivity로 되돌아감.
                finish();

                // 아래 코드는 앱을 종료시키는 코드이다.
                // 근데 알약에서 배터리 유출이 발생한다고 하니 나중에 사용하게 된다면 고쳐서 사용하자.
                // finishAffinity();
                // System.runFinalization();
                // System.exit(0);
            }
        });

    }
}
