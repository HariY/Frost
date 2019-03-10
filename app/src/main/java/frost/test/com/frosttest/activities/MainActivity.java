package frost.test.com.frosttest.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import frost.test.com.frosttest.R;
import frost.test.com.frosttest.adapters.CustomListAdapter;
import frost.test.com.frosttest.model.CategoryListItemElement;
import frost.test.com.frosttest.model.Children;
import frost.test.com.frosttest.service.RequestCallBack;
import frost.test.com.frosttest.service.RequestManager;
import frost.test.com.frosttest.service.ServerReqestTypes;
import frost.test.com.frosttest.service.VideoSearchRequest;

public class MainActivity extends SuperActivity
        implements NavigationView.OnNavigationItemSelectedListener, RequestCallBack {

    ListView multiLevelListView;
    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private ProgressBar progressBar;
    LayoutInflater inflator;
    ArrayList<CategoryListItemElement> parentDataListArray=new ArrayList<>();
    private static final DefaultBandwidthMeter BANDWIDTH_METER =new DefaultBandwidthMeter();
    String videoUrl;
    Toolbar toolbar;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        multiLevelListView=findViewById(R.id.left_nav_listview);
        playerView=(PlayerView) findViewById(R.id.video_view);
        progressBar=findViewById(R.id.progressbar);
        inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setSupportActionBar(toolbar);

        toolbar.setLogo(ResourcesCompat.getDrawable(getResources(),R.mipmap.ic_launcher,null));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadLeftNavList();

        getVideoContent("da8a81ee-8507-407e-818d-3cf1acc24045");
    }



    private void loadLeftNavList()
    {
        ArrayList<CategoryListItemElement> totalListArray=dummyData();
        final CustomListAdapter listAdapter=new CustomListAdapter(this,parentDataListArray,totalListArray,inflator);
        multiLevelListView.setAdapter(listAdapter);

        multiLevelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CategoryListItemElement element = (CategoryListItemElement) listAdapter.getItem(position);

                if(!element.isHasChildren()) {
                    toolbar.setTitle(element.getContentText());
                    toolbar.setLogo(listAdapter.getRespectiveIcon(element.getLevel()));
                    drawer.closeDrawers();
                }

                ArrayList<CategoryListItemElement> elements = listAdapter.getElements();
                ArrayList<CategoryListItemElement> elementsData = listAdapter.getElementsData();



                if (element.isExpanded()) {
                    element.setExpanded(false);


                    ArrayList<CategoryListItemElement> elementsToDel = new ArrayList<CategoryListItemElement>();
                    for (int i = position + 1; i < elements.size(); i++) {
                        if (element.getLevel() >= elements.get(i).getLevel())
                            break;
                        elementsToDel.add(elements.get(i));
                    }
                    elements.removeAll(elementsToDel);
                    listAdapter.notifyDataSetChanged();
                } else {
                    element.setExpanded(true);
                    int i = 1;
                    for (CategoryListItemElement e : elementsData) {
                        if (e.getParendId() == element.getId()) {
                            e.setExpanded(false);
                            elements.add(position + i, e);
                            i++;
                        }
                    }
                    multiLevelListView.setVerticalScrollbarPosition(0);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getVideoContent(String contentId)
    {
        showLoader();
        VideoSearchRequest videoSearchRequest=new VideoSearchRequest(contentId,this);
        RequestManager.getInstance().addRequest(videoSearchRequest);
    }

    private void setPlayerToPlayVideo(Children children)
    {

        videoUrl=children.getVideoList().get(0);
        Log.d("PRINT",videoUrl);

        setUpSimpleExoPlayer();

    }

    //Server response callbacks
   @Override
    public void onRequestSuccessFull(ServerReqestTypes serverReqestType, Object result) {
        if (serverReqestType == ServerReqestTypes.VIDEO_SEARCH) {
            hideLoader();
            List<Children> dataList = (List<Children>) result;
            //showDataInRecyclerView(dataList);
            setPlayerToPlayVideo(dataList.get(0));


        }
    }

    @Override
    public void onRequestFailure(ServerReqestTypes serverReqestType, String errorMsg) {
        hideLoader();
    }

    private void showLoader(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader(){
        progressBar.setVisibility(View.GONE);
    }




    private void setUpSimpleExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "Frost"), defaultBandwidthMeter);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoUrl));

        // SimpleExoPlayer
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        // Prepare the player with the source.
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

        playerView.setPlayer(exoPlayer);

        playerView.showController();

    }





    private ArrayList<CategoryListItemElement> dummyData()
    {
        ArrayList<CategoryListItemElement> dataArrayList=new ArrayList<>();
        CategoryListItemElement categoryListItemElement1=new CategoryListItemElement("Linear Data Structure",
                0,1,-1,true,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement1);

        CategoryListItemElement categoryListItemElement11=new CategoryListItemElement("Arrays",
                1,11,1,true,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement11);

        CategoryListItemElement categoryListItemElement111=new CategoryListItemElement("Introduction",
                2,111,11,false,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement111);

        CategoryListItemElement categoryListItemElement112=new CategoryListItemElement("One Dimensional Array",
                2,112,11,false,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement112);

        CategoryListItemElement categoryListItemElement113=new CategoryListItemElement("Two Dimensional Array",
                2,113,11,false,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement113);

        CategoryListItemElement categoryListItemElement114=new CategoryListItemElement("Three Dimensional Array",
                2,114,11,false,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement114);



        CategoryListItemElement categoryListItemElement12=new CategoryListItemElement("Linked List",
                1,12,1,true,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement12);

        CategoryListItemElement categoryListItemElement121=new CategoryListItemElement("Introduction",
                2,121,12,false,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement121);

        CategoryListItemElement categoryListItemElement122=new CategoryListItemElement("Single Linked List",
                2,122,12,false,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement122);

        CategoryListItemElement categoryListItemElement123=new CategoryListItemElement("Double Linked List",
                2,123,12,false,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement123);

        //------- section two starts---

        CategoryListItemElement categoryListItemElement2=new CategoryListItemElement("Non Linear Data Structure",
                0,2,-1,true,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement2);

        CategoryListItemElement categoryListItemElement21=new CategoryListItemElement("Graph",
                1,21,2,false,false,"",false,0,null);
        dataArrayList.add(categoryListItemElement21);



        parentDataListArray.add(categoryListItemElement1);
        parentDataListArray.add(categoryListItemElement2);




        return dataArrayList;


    }

    private void releasePlayer()
    {
        if(exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
        exoPlayer = null;

    }

    @Override
    protected void onPause() {
        super.onPause();
//        releasePlayer();
        if(exoPlayer!=null)
            exoPlayer.stop(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(videoUrl!=null)
            setUpSimpleExoPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.getInstance().stop();
        releasePlayer();
    }


}
