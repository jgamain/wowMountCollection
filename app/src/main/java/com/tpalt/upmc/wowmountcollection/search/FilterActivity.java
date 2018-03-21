package com.tpalt.upmc.wowmountcollection.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.search.SearchEngine.OriginListChoice;

import java.lang.reflect.Field;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        RadioButton allMounts = findViewById(R.id.allMounts);
        allMounts.setSelected(true);
        initFilters();
    }

    public void onResetFiltersClick(View view){
        SearchEngine.resetFilters();
        initFilters();
    }

    public void onSearchButtonClick(View view){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("methodName","preformSearch");
        startActivity(intent);
    }

    public void initFilters(){
        switch (SearchEngine.originList){
            case ALL_MOUNTS: ((RadioButton)findViewById(R.id.allMounts)).setChecked(true); break;
            case MY_MOUNTS: ((RadioButton)findViewById(R.id.myMounts)).setChecked(true); break;
            case MISSING_MOUNTS: ((RadioButton)findViewById(R.id.missingMounts)).setChecked(true); break;
        }
        RadioGroup radioGroup = findViewById(R.id.originListGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.allMounts: SearchEngine.originList = OriginListChoice.ALL_MOUNTS; break;
                    case R.id.myMounts: SearchEngine.originList = OriginListChoice.MY_MOUNTS; break;
                    case R.id.missingMounts: SearchEngine.originList = OriginListChoice.MISSING_MOUNTS; break;
                    default: SearchEngine.originList = OriginListChoice.ALL_MOUNTS; break;
                }
            }
        });

        CheckBox checkBox = findViewById(R.id.allianceBox);
        checkBox.setChecked(SearchEngine.alliance);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.alliance = isChecked;
            }
        });
        checkBox = findViewById(R.id.hordeBox);
        checkBox.setChecked(SearchEngine.horde);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.horde = isChecked;
            }
        });
        checkBox = findViewById(R.id.groundBox);
        checkBox.setChecked(SearchEngine.ground);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.ground = isChecked;
            }
        });
        checkBox = findViewById(R.id.flyingBox);
        checkBox.setChecked(SearchEngine.flying);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.flying = isChecked;
            }
        });
        checkBox = findViewById(R.id.oneSeatBox);
        checkBox.setChecked(SearchEngine.oneSeat);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.oneSeat = isChecked;
            }
        });
        checkBox = findViewById(R.id.twoSeatsBox);
        checkBox.setChecked(SearchEngine.twoSeats);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.twoSeats = isChecked;
            }
        });
        checkBox = findViewById(R.id.threeSeatsBox);
        checkBox.setChecked(SearchEngine.threeSeats);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.threeSeats = isChecked;
            }
        });
        checkBox = findViewById(R.id.easyBox);
        checkBox.setChecked(SearchEngine.easy);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.easy = isChecked;
            }
        });
        checkBox = findViewById(R.id.mediumBox);
        checkBox.setChecked(SearchEngine.medium);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.medium = isChecked;
            }
        });
        checkBox = findViewById(R.id.hardBox);
        checkBox.setChecked(SearchEngine.hard);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.hard = isChecked;
            }
        });
        checkBox = findViewById(R.id.removedBox);
        checkBox.setChecked(SearchEngine.removed);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.removed = isChecked;
            }
        });
        checkBox = findViewById(R.id.unavailableBox);
        checkBox.setChecked(SearchEngine.unavailable);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.unavailable = isChecked;
            }
        });
        checkBox = findViewById(R.id.vendorBox);
        checkBox.setChecked(SearchEngine.vendor);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.vendor = isChecked;
            }
        });
        checkBox = findViewById(R.id.questBox);
        checkBox.setChecked(SearchEngine.quest);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.quest = isChecked;
            }
        });
        checkBox = findViewById(R.id.professionBox);
        checkBox.setChecked(SearchEngine.profession);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.profession = isChecked;
            }
        });
        checkBox = findViewById(R.id.otherBox);
        checkBox.setChecked(SearchEngine.other);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SearchEngine.other = isChecked;
            }
        });
    }
}
