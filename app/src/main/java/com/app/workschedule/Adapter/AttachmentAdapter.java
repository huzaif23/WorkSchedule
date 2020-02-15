package com.app.workschedule.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workschedule.Fragment.AudioPlayerBottomSheetDialog;
import com.app.workschedule.Interface.DeleteItem;
import com.app.workschedule.R;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.FileUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {

    List<String> attachments;
    DeleteItem deleteItem;
    FileUtils fileUtils;
    Context context;
    Fragment fragment;

    public AttachmentAdapter(DeleteItem deleteItem, Context context, Fragment fragment) {
        attachments = new ArrayList<>();
        this.deleteItem = deleteItem;
        fileUtils = FileUtils.getInstance(context);
        this.context = context;
        this.fragment = fragment;
    }

    public void addData(Collection<String> attachments) {
        this.attachments.addAll(attachments);
    }

    public void clearData() {
        this.attachments.clear();
    }

    @NonNull
    @Override
    public AttachmentAdapter.AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttachmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_attachment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentAdapter.AttachmentViewHolder holder, int position) {
        File file = new File(attachments.get(position));
        holder.textViewTitle.setText(file.getName());
        holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = FileProvider.getUriForFile(context
                        , context.getApplicationContext().getPackageName() + ".fileprovider"
                        , file);
                if (file.getName().contains(".jpg")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);
                } else {
                    AudioPlayerBottomSheetDialog audioPlayerBottomSheetDialog = new AudioPlayerBottomSheetDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.TASK_FILE_PATH_KEY,file.getPath());
                    audioPlayerBottomSheetDialog.setArguments(bundle);
                    audioPlayerBottomSheetDialog.show(fragment.getFragmentManager(), "audioPickerDialog");

//                    Intent viewMediaIntent = new Intent();
//                    viewMediaIntent.setAction(android.content.Intent.ACTION_VIEW);
//                    viewMediaIntent.setDataAndType(uri, "audio/*");
//                    viewMediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    context.startActivity(viewMediaIntent);
                }
            }
        });


        if (deleteItem != null) {
            holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem.deleteItem(attachments.get(position));
                    attachments.remove(position);
                    notifyItemRemoved(position);
                }
            });
        } else {
            holder.imageViewDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    protected class AttachmentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageViewDelete;

        public AttachmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            imageViewDelete = itemView.findViewById(R.id.image_view_delete);
        }

    }

//    private File getFileFromUri(String path) {}
}
