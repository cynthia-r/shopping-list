package com.example.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCatalogItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCatalogItemFragment extends DialogFragment {

    public static final String TAG = "AddCatalogItem";

    private AddCatalogItemDialogListener mAddCatalogItemDialogListener;

    public AddCatalogItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddCatalogItemFragment.
     */
    public static AddCatalogItemFragment newInstance() {
        AddCatalogItemFragment fragment = new AddCatalogItemFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.add_item);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_catalog_item, (ViewGroup) getView(), false);
        alertDialogBuilder.setView(viewInflated);

        // Locate the EditText in listview_main.xml
        final EditText editTextView = viewInflated.findViewById(R.id.new_item);

        alertDialogBuilder.setPositiveButton(R.string.ok,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // On success
                mAddCatalogItemDialogListener.onItemAdd(editTextView.getText().toString());
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return alertDialogBuilder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Show keyboard
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_catalog_item, container, false);
    }

    public void setAddCatalogItemDialogListener(AddCatalogItemDialogListener addItemDialogListener) {
        mAddCatalogItemDialogListener = addItemDialogListener;
    }

    public interface AddCatalogItemDialogListener {
        void onItemAdd(String inputText);
    }
}
