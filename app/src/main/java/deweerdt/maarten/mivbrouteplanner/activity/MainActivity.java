package deweerdt.maarten.mivbrouteplanner.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.fragments.StopsFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new StopsFragment()).commit();
        }

    }

}
