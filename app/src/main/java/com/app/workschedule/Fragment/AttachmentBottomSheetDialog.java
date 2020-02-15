package com.app.workschedule.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.app.workschedule.Interface.DeleteItem;
import com.app.workschedule.Interface.PopulateAttachmentList;
import com.app.workschedule.R;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.DateFormatter;
import com.app.workschedule.Utils.FileUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class AttachmentBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    TextView textViewAddPicture;
    TextView textViewAddVoice;
    TextView textViewVoice;
    Chronometer textViewRecordingTime;
    ImageView imageViewPickFromGallery;
    ImageView imageViewOpenCamera;
    ImageView imageViewVoice;
    RelativeLayout pictureContainer;
    RelativeLayout mainContainer;
    RelativeLayout voiceContainer;


    int PICK_FROM_GALLERY_REQUEST_CODE = 3;
    int OPEN_CAMERA_REQUEST_CODE = 4;
    String imagePath = "";
    PopulateAttachmentList populateAttachmentList;
    FileUtils fileUtils;
    Uri cameraPictureUri;
    boolean startRecording = true;
    Timer timer = new Timer();
    TimerTask timerTask;
    MediaRecorder recorder;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO
            , Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    String audioFileName;


    public AttachmentBottomSheetDialog(PopulateAttachmentList populateAttachmentList) {
        super();
        this.populateAttachmentList = populateAttachmentList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attachement_bottom_sheet_dialog_layout, container, false);

        textViewAddPicture = view.findViewById(R.id.text_view_picture_attach);
        textViewAddVoice = view.findViewById(R.id.text_view_voice_attach);
        textViewVoice = view.findViewById(R.id.text_view_voice);
        textViewRecordingTime = view.findViewById(R.id.text_view_recording_time);
        pictureContainer = view.findViewById(R.id.picture_container);
        mainContainer = view.findViewById(R.id.main_container);
        voiceContainer = view.findViewById(R.id.voice_container);
        imageViewOpenCamera = view.findViewById(R.id.image_view_open_camera);
        imageViewPickFromGallery = view.findViewById(R.id.image_view_pick_from_gallery);
        imageViewVoice = view.findViewById(R.id.image_view_voice);

        textViewAddVoice.setOnClickListener(this);
        textViewAddPicture.setOnClickListener(this);
        imageViewOpenCamera.setOnClickListener(this);
        imageViewPickFromGallery.setOnClickListener(this);
        imageViewVoice.setOnClickListener(this);


        fileUtils = FileUtils.getInstance(getContext());

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContext().getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    populateAttachmentList.populateRecyclerView(fileUtils.getFileNameFromUri(uri,getContext()), filePath);
                    getDialog().dismiss();
                }
            }
        } else if (requestCode == OPEN_CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (!imagePath.isEmpty()) {
                        File file = new File(imagePath);
                        if (file.exists()) {
                            if (cameraPictureUri != null) {
                                populateAttachmentList.populateRecyclerView(fileUtils.getFileNameFromUri(uri,getContext()), cameraPictureUri.getPath());
                                getDialog().dismiss();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.text_view_picture_attach:

                if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                    return;
                }
                pictureContainer.setVisibility(View.VISIBLE);
                mainContainer.setVisibility(View.GONE);
                break;
            case R.id.text_view_voice_attach:
                if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                    return;
                }
                mainContainer.setVisibility(View.GONE);
                voiceContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.image_view_open_camera:
                String root = fileUtils.createPictureDirectory();
                imagePath = root + File.separator + DateFormatter.getInstance().getTimeStampForFileName() + ".jpg";
                File file = new File(imagePath);
                Uri uri = FileProvider.getUriForFile(getContext()
                        , getContext().getApplicationContext().getPackageName() + ".fileprovider"
                        , file);
                cameraPictureUri = uri;
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, OPEN_CAMERA_REQUEST_CODE);
                break;
            case R.id.image_view_pick_from_gallery:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), PICK_FROM_GALLERY_REQUEST_CODE);
                break;
            case R.id.image_view_voice:

                onRecord(startRecording);

                if (startRecording) {
                    imageViewVoice.setImageResource(R.drawable.mic_on_ic);
                    textViewVoice.setText("Press mic to stop recording");
                    textViewRecordingTime.setVisibility(View.VISIBLE);
                    textViewRecordingTime.setBase(SystemClock.elapsedRealtime());
                    textViewRecordingTime.start();

                } else {
                    imageViewVoice.setImageResource(R.drawable.mic_off_ic);
                    textViewVoice.setText("Pgress mic to start recording");
                    textViewRecordingTime.setVisibility(View.GONE);
                    textViewRecordingTime.stop();
                }
                startRecording = !startRecording;

        }
    }


    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        audioFileName = fileUtils.createAudioDirectory() + File.separator + DateFormatter.getInstance().getTimeStampForFileName() + ".mp3";
        recorder.setOutputFile(audioFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("test", "prepare() failed");
        }
        recorder.start();

    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        if (audioFileName != null) {
            File file = new File(audioFileName);
            Uri uri = FileProvider.getUriForFile(getContext()
                    , getContext().getApplicationContext().getPackageName() + ".fileprovider"
                    , file);
            populateAttachmentList.populateRecyclerView(file.getName(), file.getPath());
        }
        audioFileName = null;
        getDialog().dismiss();
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

}
