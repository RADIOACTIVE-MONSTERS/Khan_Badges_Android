package com.example.tylerlacroix.khan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by tylerlacroix on 15-12-29.
 */
public class PopupFragment extends DialogFragment implements Button.OnClickListener {
    private Badge badge;
    private static final String BADGE_KEY = "badge_key";

    public static PopupFragment newInstance(Badge _badge) {
        PopupFragment fragment = new PopupFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BADGE_KEY, _badge);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        badge = (Badge) getArguments().getSerializable(BADGE_KEY);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.popup, null);
        ((TextView)view.findViewById(R.id.badge_description)).setText(badge.description_extended);
        ((TextView)view.findViewById(R.id.badge_title)).setText(badge.descript);
        TextView points = ((TextView)view.findViewById(R.id.badge_points));
        points.setText(badge.points + "");
        if (badge.points > 0) {
            points.setVisibility(View.VISIBLE);
        }
        else {
            points.setVisibility(View.INVISIBLE);
        }
        Picasso.with(getActivity()).load(badge.image).into(((ImageView) view.findViewById(R.id.badge_image)));
        ((Button)view.findViewById(R.id.close_button)).setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
