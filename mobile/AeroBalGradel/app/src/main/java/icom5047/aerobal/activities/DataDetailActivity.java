package icom5047.aerobal.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.aerobal.data.objects.Run;
import com.aerobal.data.objects.Stats;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import icom5047.aerobal.containers.SpinnerContainer;
import icom5047.aerobal.controllers.ExperimentController;
import icom5047.aerobal.controllers.UnitController;
import icom5047.aerobal.fragments.DataRawDataFragment;
import icom5047.aerobal.fragments.DataSummaryFragment;
import icom5047.aerobal.resources.GlobalConstants;
import icom5047.aerobal.resources.Keys;
import icom5047.aerobal.resources.ViewGroupUtils;
import scala.collection.JavaConversions;

public class DataDetailActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private volatile ExperimentController experimentController;
    private volatile UnitController unitController;

    //Fragment References
    private volatile DataRawDataFragment dataRawDataFragment;
    private volatile DataSummaryFragment dataSummaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Action Bar as Back Button
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager() );

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


        //Instance Fields
        Bundle bundle = this.getIntent().getExtras();


        this.experimentController = (ExperimentController) bundle.get(Keys.BundleKeys.ExperimentController);
        this.unitController = (UnitController) bundle.get(Keys.BundleKeys.UnitController);


        dataSummaryFragment = DataSummaryFragment.getInstance(experimentController, unitController);
        dataRawDataFragment = DataRawDataFragment.getInstance(experimentController, unitController);


        //Add Spinner
        int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        View titleView = findViewById(titleId);
        //Set Up
        Spinner abSpinner = (Spinner) getLayoutInflater().inflate(R.layout.spinner_action_bar, null);
        ArrayAdapter<SpinnerContainer> adapter = new ArrayAdapter<SpinnerContainer>(this, R.layout.spinner_white_item, experimentController.getRunsListSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        abSpinner.setAdapter(adapter);
        abSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("DataAct:","OnItemSelected Called");
                SpinnerContainer spinnerContainer = (SpinnerContainer) adapterView.getItemAtPosition(i);

                experimentController.setActiveRun(spinnerContainer);
                Log.v("DataAct", "State Active Run Controller:"+experimentController.getActiveRun()+" Selected: "+spinnerContainer+"must be equals");
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //NoOp
            }
        });
        //Change in Code
        ViewGroupUtils.replaceView(titleView, abSpinner);





    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.ab_btn_graph:
                if(experimentController.getActiveRun().index != ExperimentController.ALL_RUNS){
                    showGraphDialog();
                } else{
                    Toast.makeText(this, R.string.frag_data_raw_full_exp_no_display, Toast.LENGTH_SHORT).show();
                }

                break;

        }
        return true;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    /*
        Getter Controllers
     */
    public ExperimentController getExperimentController() {
        return experimentController;
    }

    public UnitController getUnitController(){
        return unitController;
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            switch (position){
                case 0:
                    return dataSummaryFragment;
                case 1:
                    return dataRawDataFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_data_summary).toUpperCase(l);
                case 1:
                    return getString(R.string.title_fragment_data_raw).toUpperCase(l);
            }
            return null;
        }
    }

    public void refresh(){

        dataRawDataFragment.fullRefresh(experimentController.getActiveRun());
        dataSummaryFragment.fullRefresh(experimentController.getActiveRun());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.data_detail, menu);
        return true;
    }

    private void showGraphDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_dialog_gen_graph);

        View view = this.getLayoutInflater().inflate(R.layout.dialog_graph, null, false);

        final Spinner xSpinner = (Spinner) view.findViewById(R.id.dialogGraphXAxisSpinner);

        //Measurement Spinner
        xSpinner.setAdapter(new ArrayAdapter<SpinnerContainer>(this, android.R.layout.simple_dropdown_item_1line, GlobalConstants.Measurements.getGraphListSpinner()));
        xSpinner.setSelection(0);


        final Spinner ySpinner = (Spinner) view.findViewById(R.id.dialogGraphYAxisSpinner);
        //Measurement Spinner
        ySpinner.setAdapter(new ArrayAdapter<SpinnerContainer>(this, android.R.layout.simple_dropdown_item_1line, GlobalConstants.Measurements.getMeasurementListSpinner()));
        ySpinner.setSelection(1);

        builder.setView(view);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //NoOp
            }
        });

        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getBaseContext(), GraphActivity.class);
                Bundle bundle = new Bundle();
                SpinnerContainer spinnerContainerX = (SpinnerContainer) xSpinner.getSelectedItem();
                SpinnerContainer spinnerContainerY = (SpinnerContainer) ySpinner.getSelectedItem();
                bundle.putString(Keys.BundleKeys.XAxisTitle, spinnerContainerX.name);
                bundle.putString(Keys.BundleKeys.YAxisTitle, spinnerContainerY.name);
                double[] xValues;
                if(spinnerContainerX.index == GlobalConstants.Measurements.TimeKey){
                    xValues = getTimeValues(spinnerContainerY);
                }
                else{
                    xValues = getGraphValues(spinnerContainerX);
                }

                bundle.putDoubleArray(Keys.BundleKeys.XAxis, xValues);
                bundle.putDoubleArray(Keys.BundleKeys.YAxis, getGraphValues(spinnerContainerY));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        builder.create().show();

    }

    public double[] getGraphValues(SpinnerContainer container){

        //Get Runs
        List<Run> runs= JavaConversions.asJavaList(experimentController.getCloneExperiment().runs());
        Map<Integer, Stats> map = ExperimentController.getStatsForRuns( runs.get(experimentController.getActiveRun().index));
        Stats stat = map.get(container.index);

        List<Object> measurements = JavaConversions.asJavaList(stat.values());

        Log.v("Graph Values", "Runs Size: "+runs.size()+ " Stats: "+stat.toString()+"\nMeasurements "+measurements+"\n stats.value: "+stat.values());
        double[] val_measurements = new double[measurements.size()];

        for(int i=0; i<measurements.size(); i++){
            val_measurements[i] = (Double)measurements.get(i);
            Log.v("DActGraphValue:", val_measurements[i]+"");
        }
        return val_measurements;
    }


    public double[] getTimeValues(SpinnerContainer container){
        //Get Runs
        List<Run> runs= JavaConversions.asJavaList(experimentController.getCloneExperiment().runs());
        Map<Integer, Stats> map = ExperimentController.getStatsForRuns(ExperimentController.getCloneRun(runs.get(experimentController.getActiveRun().index)));
        Stats stat = map.get(container.index);


        List<Object> measurements = JavaConversions.asJavaList(stat.values());
        Log.v("Time Values", "Runs Size: "+runs.size()+ " Stats: "+stat.toString()+"\nMeasurements "+measurements+"\n stats.value: "+stat.values());
        double[] time_measurements = new double[measurements.size()];

        //Add To

        double timeCounter = 0;
        time_measurements[0] = 0;
        for(int i=1; i<measurements.size(); i++){
            timeCounter +=experimentController.getExperiment().getFrequency();
            time_measurements[i] = timeCounter;
        }


        return time_measurements;
    }



}
