package com.example.android.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.MalformedURLException;

/**
 * Created by ErwinF on 11/29/2017.
 */

public class DetailStepFragment extends Fragment {

    private final String TAG = "DetailStepFragment";
    private ImageView imageView;
    private TextView mStepInstructions;
    private Button mNext;
    private Button mPrev;
    private SimpleExoPlayerView mMediaPlayer;
    private SimpleExoPlayer mExoPlayer;

    private long mPlayerPosition;
    private String PLAYER_SATE = "player_state";
    private String PLAY_WHEN_READY = "play_when_ready";
    private boolean playWhenReady = true;
    private static ViewGroup.LayoutParams mLayoutParams;
    public int mI;
    private int mIndex;

    public DetailStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.thumbnail_image);
        mStepInstructions = (TextView) rootView.findViewById(R.id.step_instructions);
        mMediaPlayer = (SimpleExoPlayerView) rootView.findViewById(R.id.media_player);
        mNext = (Button) rootView.findViewById(R.id.next_step);
        mPrev = (Button) rootView.findViewById(R.id.prev_step);

        mMediaPlayer.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        mStepInstructions.setTextColor(Color.parseColor("#FF9800"));
        rootView.setBackgroundColor(Color.parseColor("#3F51B4"));

        if (savedInstanceState != null) {

            mPlayerPosition = savedInstanceState.getLong(PLAYER_SATE);
            mI = savedInstanceState.getInt("i");
            mIndex = savedInstanceState.getInt("index");
            try {
                playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            } catch (Exception e) {
                // Not saved
            }
            setIndex(mIndex, mI);

        } else {
            try {
                mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getResources().getDimensionPixelSize(R.dimen.media_player_height));
            } catch (Exception e){
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIndexingEvent(IndexingEvent event) {
        setIndex(event.mIndex, event.mI);
    }

    public void setIndex(int index, int i){

        mI = i;
        mIndex = index;
        setUp();

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mI++;
                setUp();
            }
        });

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mI--;
                setUp();
            }
        });
    }

    private void setUp() {

        // Check position:
        int max = JsonInfoUtils.STEPS_lengths[mIndex] -1;
        if (mI < 0) {
            mI = max;
            Toast.makeText(getContext(), "This is the last step", Toast.LENGTH_SHORT).show();
        } else if (mI > max) {
            mI = 0;
            Toast.makeText(getContext(), "Back to first step", Toast.LENGTH_SHORT).show();
        }

        // Set things up:
        String instructions = "\n" + JsonInfoUtils.STEPS_DESCRIPTION[mIndex][mI];
        mStepInstructions.setText(instructions);

        try {
            if (!JsonInfoUtils.IMAGE_URL[mIndex].equals("")) {
                Picasso.with(getContext())
                        .load(JsonInfoUtils.IMAGE_URL[mIndex])
                        .into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else if (!JsonInfoUtils.STEPS_THUMBNAIL_URL[mIndex][mI].equals("")){
                Picasso.with(getContext())
                        .load(JsonInfoUtils.STEPS_THUMBNAIL_URL[mIndex][mI])
                        .into(imageView);
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }

            initializePlayer(JsonInfoUtils.STEPS_VIDEO_URL[mIndex][mI]);

        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "Failed Initializing properly the player",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void initializePlayer(String mediaUrl)
            throws MalformedURLException{

        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mMediaPlayer.setPlayer(mExoPlayer);
        }
        if (!mediaUrl.equals("")) {

            mMediaPlayer.setVisibility(View.VISIBLE);
            try {
                setFullScreen(getContext());
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            Uri Url = Uri.parse(mediaUrl);

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(Url, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.seekTo(mPlayerPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        } else {
            mMediaPlayer.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "No video", Toast.LENGTH_SHORT).show();
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void setFullScreen(Context context) {

        ViewGroup.LayoutParams params = mMediaPlayer.getLayoutParams();

        if (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE && JsonInfoUtils.mSmallScreen) {

            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;

            mStepInstructions.setVisibility(View.GONE);
            mNext.setVisibility(View.GONE);
            mPrev.setVisibility(View.GONE);

        } else {

            params.height = mLayoutParams.height;
            params.width = mLayoutParams.width;

            mStepInstructions.setVisibility(View.VISIBLE);
            mNext.setVisibility(View.VISIBLE);
            mPrev.setVisibility(View.VISIBLE);
        }

        mMediaPlayer.requestLayout();
    }

    private void saveBundleState(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            if(mPlayerPosition != C.TIME_UNSET)
                savedInstanceState.putLong(PLAYER_SATE, mPlayerPosition);

            savedInstanceState.putInt("index", mIndex);
            savedInstanceState.putInt("i", mI);
            savedInstanceState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveBundleState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
        }
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            initializePlayer(JsonInfoUtils.STEPS_VIDEO_URL[mIndex][mI]);
        } catch (Exception e) {
            Log.e(TAG, "Video url malformed");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
