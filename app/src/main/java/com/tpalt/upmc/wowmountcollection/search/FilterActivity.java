package com.tpalt.upmc.wowmountcollection.search;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.search.SearchEngine.OriginListChoice;

public class FilterActivity extends AppCompatActivity {

    private boolean allianceActive = false;
    private boolean hordeActive = false;

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
        saveFilters();
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("methodName","performSearch");
        startActivity(intent);
    }

    private void saveFilters(){
        RadioGroup radioGroup = findViewById(R.id.originListGroup);
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.allMounts: SearchEngine.originList = OriginListChoice.ALL_MOUNTS; break;
            case R.id.myMounts: SearchEngine.originList = OriginListChoice.MY_MOUNTS; break;
            case R.id.missingMounts: SearchEngine.originList = OriginListChoice.MISSING_MOUNTS; break;
            default: SearchEngine.originList = OriginListChoice.ALL_MOUNTS; break;
        }
        //faction
        SearchEngine.alliance = allianceActive;
        SearchEngine.horde = hordeActive;

        //type
        SearchEngine.ground = ((CheckBox)findViewById(R.id.groundBox)).isChecked();
        SearchEngine.flying = ((CheckBox)findViewById(R.id.flyingBox)).isChecked();
        //seats
        SearchEngine.oneSeat = ((CheckBox)findViewById(R.id.oneSeatBox)).isChecked();
        SearchEngine.twoSeats = ((CheckBox)findViewById(R.id.twoSeatsBox)).isChecked();
        SearchEngine.threeSeats = ((CheckBox)findViewById(R.id.threeSeatsBox)).isChecked();
        //difficulty
        SearchEngine.easy = ((CheckBox)findViewById(R.id.easyBox)).isChecked();
        SearchEngine.medium = ((CheckBox)findViewById(R.id.mediumBox)).isChecked();
        SearchEngine.hard = ((CheckBox)findViewById(R.id.hardBox)).isChecked();
        SearchEngine.removed = ((CheckBox)findViewById(R.id.removedBox)).isChecked();
        SearchEngine.unavailable = ((CheckBox)findViewById(R.id.unavailableBox)).isChecked();
        //source
        SearchEngine.vendor = ((CheckBox)findViewById(R.id.vendorBox)).isChecked();
        SearchEngine.loot = ((CheckBox)findViewById(R.id.lootBox)).isChecked();
        SearchEngine.quest = ((CheckBox)findViewById(R.id.questBox)).isChecked();
        SearchEngine.profession = ((CheckBox)findViewById(R.id.professionBox)).isChecked();
        SearchEngine.other = ((CheckBox)findViewById(R.id.otherBox)).isChecked();

    }

    private void initFilters(){
        switch (SearchEngine.originList){
            case ALL_MOUNTS: ((RadioButton)findViewById(R.id.allMounts)).setChecked(true); break;
            case MY_MOUNTS: ((RadioButton)findViewById(R.id.myMounts)).setChecked(true); break;
            case MISSING_MOUNTS: ((RadioButton)findViewById(R.id.missingMounts)).setChecked(true); break;
        }

        final ImageView allianceIcon = findViewById(R.id.allianceBox);
        allianceActive = SearchEngine.alliance;
        if(allianceActive) allianceIcon.setImageResource(R.drawable.ic_alliance_color);
        else allianceIcon.setImageResource(R.drawable.ic_alliance_grey);

        allianceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allianceActive = !allianceActive;
                if (allianceActive) allianceIcon.setImageResource(R.drawable.ic_alliance_color);
                else allianceIcon.setImageResource(R.drawable.ic_alliance_grey);
            }
        });

        final ImageView hordeIcon = findViewById(R.id.hordeBox);
        hordeActive = SearchEngine.horde;
        if(hordeActive) hordeIcon.setImageResource(R.drawable.ic_horde_red);
        else hordeIcon.setImageResource(R.drawable.ic_horde_grey);

        hordeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hordeActive = !hordeActive;
                if (hordeActive) hordeIcon.setImageResource(R.drawable.ic_horde_red);
                else hordeIcon.setImageResource(R.drawable.ic_horde_grey);
            }
        });

        CheckBox checkBox = findViewById(R.id.groundBox);
        checkBox.setChecked(SearchEngine.ground);

        checkBox = findViewById(R.id.flyingBox);
        checkBox.setChecked(SearchEngine.flying);

        checkBox = findViewById(R.id.oneSeatBox);
        checkBox.setChecked(SearchEngine.oneSeat);

        checkBox = findViewById(R.id.twoSeatsBox);
        checkBox.setChecked(SearchEngine.twoSeats);

        checkBox = findViewById(R.id.threeSeatsBox);
        checkBox.setChecked(SearchEngine.threeSeats);

        checkBox = findViewById(R.id.easyBox);
        checkBox.setChecked(SearchEngine.easy);

        checkBox = findViewById(R.id.mediumBox);
        checkBox.setChecked(SearchEngine.medium);

        checkBox = findViewById(R.id.hardBox);
        checkBox.setChecked(SearchEngine.hard);

        checkBox = findViewById(R.id.removedBox);
        checkBox.setChecked(SearchEngine.removed);

        checkBox = findViewById(R.id.unavailableBox);
        checkBox.setChecked(SearchEngine.unavailable);

        checkBox = findViewById(R.id.vendorBox);
        checkBox.setChecked(SearchEngine.vendor);

        checkBox = findViewById(R.id.questBox);
        checkBox.setChecked(SearchEngine.quest);

        checkBox = findViewById(R.id.professionBox);
        checkBox.setChecked(SearchEngine.profession);

        checkBox = findViewById(R.id.otherBox);
        checkBox.setChecked(SearchEngine.other);
    }
}
