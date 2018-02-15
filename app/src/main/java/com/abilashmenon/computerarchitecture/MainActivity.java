package com.abilashmenon.computerarchitecture;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_cpu_scheduling:
                    cpuSchedulingFragmentTransaction();
                    return true;
                case R.id.navigation_main_memory:
                    memoryAllocationFragmentTransaction();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        cpuSchedulingFragmentTransaction();
    }

    private void cpuSchedulingFragmentTransaction() {
        CPUSchedulingFragment cpuFragment = new CPUSchedulingFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, cpuFragment, getString(R.string.cpuFragment));
        transaction.commit();
    }

    private void memoryAllocationFragmentTransaction() {
        MemoryAllocationFragment memoryFragment = new MemoryAllocationFragment();
        FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
        transaction2.replace(R.id.container, memoryFragment, getString(R.string.memoryFragment));
        transaction2.commit();
    }
}
