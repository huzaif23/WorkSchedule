package com.app.workschedule.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.app.workschedule.R;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.MByteArrayDataSource;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.apache.commons.io.IOUtils;

import java.io.File;


public class AudioPlayerBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener, MediaPlayer.OnCompletionListener {


    TextView textViewFileName;
    ImageView imageViewPlay;
    ImageView imageViewPause;
    SeekBar seekBarAudio;

    MediaPlayer mp;


    File file;


    public AudioPlayerBottomSheetDialog() {
        super();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null)
            file = new File(args.getString(Constants.TASK_FILE_PATH_KEY));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_player_bottom_sheet_layout, container, false);

        textViewFileName = view.findViewById(R.id.text_view_audio_name);
        imageViewPlay = view.findViewById(R.id.image_view_play);

        seekBarAudio = view.findViewById(R.id.seek_bar_audio);

        imageViewPlay.setOnClickListener(this);

        mp = new MediaPlayer();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        playAudio();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.image_view_play:
                if (mp.isPlaying())
                    pauseAudio();
                else
                    playAudio();
                break;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            mp.release();
        } catch (Exception e) {

        }
    }

    private void playAudio() {
        //set up MediaPlayer
        mp = new MediaPlayer();
        try {
            mp.setDataSource(file.getPath());
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(this);

            imageViewPlay.setImageDrawable(getActivity().getDrawable(R.drawable.pause_ic));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseAudio() {
        try {
            mp.pause();
            imageViewPlay.setImageDrawable(getActivity().getDrawable(R.drawable.play_ic));
        } catch (Exception e) {
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        getDialog().dismiss();
    }
}
