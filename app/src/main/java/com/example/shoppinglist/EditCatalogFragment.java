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
 * Use the {@link EditCatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCatalogFragment extends DialogFragment {

    public static final String TAG = "EditCatalog";

    private String mItemName;
    private int mPosition;
    private EditCatalogItemDialogListener mEditCatalogItemDialogListener;

    public EditCatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemName Item name.
     * @return A new instance of fragment EditCatalogFragment.
     */
    public static EditCatalogFragment newInstance(String itemName, int position) {
        EditCatalogFragment fragment = new EditCatalogFragment();
        fragment.setItemName(itemName);
        fragment.setPosition(position);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.edit_item);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_catalog_item, (ViewGroup) getView(), false);
        alertDialogBuilder.setView(viewInflated);

        // Locate the EditText in the view
        final EditText editTextView = viewInflated.findViewById(R.id.new_item);
        editTextView.setText(mItemName);

        alertDialogBuilder.setPositiveButton(R.string.save,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // On success
                mEditCatalogItemDialogListener.onItemEdit(mPosition, editTextView.getText().toString());
            }
        });
        alertDialogBuilder.setNeutralButton(R.string.delete_item,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // On success
                mEditCatalogItemDialogListener.onItemDelete(mPosition);
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

    public void setEditCatalogItemDialogListener(EditCatalogItemDialogListener editCatalogItemDialogListener) {
        mEditCatalogItemDialogListener = editCatalogItemDialogListener;
    }

    public interface EditCatalogItemDialogListener {
        void onItemEdit(int position, String newItemName);
        void onItemDelete(int position);
    }

    private void setItemName(String itemName) {
        mItemName = itemName;
    }

    private void setPosition(int position) {
        mPosition = position;
    }
}
