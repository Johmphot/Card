package johmphot.card;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import johmphot.card.bluetooth.CreateMatchActivity;


public class FinishActivity extends ActionBarActivity {

    private Button restart, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);


        restart = (Button)findViewById(R.id.restart_button);
        restart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(FinishActivity.this, CreateMatchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        exit = (Button)findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                System.exit(0);
            }
        });
    }

}
