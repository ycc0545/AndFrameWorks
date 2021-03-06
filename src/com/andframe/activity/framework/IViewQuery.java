package com.andframe.activity.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 安卓版 JQuery 接口
 * Created by SCWANG on 2016/8/18.
 */
@SuppressWarnings("unused")
public interface IViewQuery<T extends IViewQuery<T>> {

    /**
     * Points the current operating view to the first view found with the id under the root.
     *
     * @param id the id
     * @return self
     */
    T id(int id);

    /**
     * Points the current operating view to the specified view.
     *
     * @return self
     */
    T id(View view);


    /**
     * Set the rating of a RatingBar.
     *
     * @param rating the rating
     * @return self
     */
    T rating(float rating);


    /**
     * Set the text of a TextView.
     *
     * @param resid the resid
     * @return self
     */
    T text(int resid);


    /**
     * Set the text of a TextView with localized formatted string
     * from application's package's default string table
     *
     * @param resid the resid
     * @return self
     * @see Context#getString(int, Object...)
     */
    T text(int resid, Object... formatArgs);


    /**
     * Set the text of a TextView.
     *
     * @param text the text
     * @return self
     */
    T text(CharSequence text);


    /**
     * Set the text of a TextView. Hide the View (gone) if text is empty.
     *
     * @param text        the text
     * @param goneIfEmpty hide if text is null or length is 0
     * @return self
     */

    T text(CharSequence text, boolean goneIfEmpty);


    /**
     * Set the text of a TextView.
     *
     * @param text the text
     * @return self
     */
    T text(Spanned text);


    /**
     * Set the text color of a TextView. Note that it's not a color resource id.
     *
     * @param color color code in ARGB
     * @return self
     */
    T textColor(int color);


    /**
     * Set the text color of a TextView from  a color resource id.
     *
     * @param id color resource id
     * @return self
     */
    T textColorId(int id);


    /**
     * Set the text typeface of a TextView.
     *
     * @param typeface typeface
     * @return self
     */
    T typeface(Typeface typeface);


    /**
     * Set the text size (in sp) of a TextView.
     *
     * @param size size
     * @return self
     */
    T textSize(float size);


    /**
     * Set the adapter of an AdapterView.
     *
     * @param adapter adapter
     * @return self
     */

    T adapter(Adapter adapter);


    /**
     * Set the adapter of an ExpandableListView.
     *
     * @param adapter adapter
     * @return self
     */
    T adapter(ExpandableListAdapter adapter);


    /**
     * Set the image of an ImageView.
     *
     * @param resid the resource id
     * @return self
     */
    T image(int resid);


    /**
     * Set the image of an ImageView.
     *
     * @param drawable the drawable
     * @return self
     */
    T image(Drawable drawable);


    /**
     * Set the image of an ImageView.
     *
     * @param bm Bitmap
     * @return self
     */
    T image(Bitmap bm);


    /**
     * Set the image of an ImageView.
     *
     * @param url Image url.
     * @return self
     */

    T image(String url);


    /**
     * Set tag object of a View.
     *
     * @return self
     */
    T tag(Object tag);

    /**
     * Set tag object of a View.
     *
     * @return self
     */
    T tag(int key, Object tag);

    /**
     * Enable a View.
     *
     * @param enabled state
     * @return self
     */
    T enabled(boolean enabled);

    /**
     * Set checked state of a compound button.
     *
     * @param checked state
     * @return self
     */
    T checked(boolean checked);


    /**
     * Get checked state of a compound button.
     *
     * @return checked
     */
    boolean isChecked();

    /**
     * Set clickable for a View.
     *
     * @return self
     */
    T clickable(boolean clickable);


    /**
     * Set View visibility to View.GONE.
     *
     * @return self
     */
    T gone();

    /**
     * Set View visibility to View.INVISIBLE.
     *
     * @return self
     */
    T invisible();

    /**
     * Set View visibility to View.VISIBLE.
     *
     * @return self
     */
    T visible();

    /**
     * Set View visibility, such as View.VISIBLE.
     *
     * @return self
     */
    T visibility(int visibility);


    /**
     * Set View background.
     *
     * @param id the id
     * @return self
     */
    T background(int id);

    /**
     * Set View background color.
     *
     * @param color color code in ARGB
     * @return self
     */
    T backgroundColor(int color);

    /**
     * Set View background color.
     *
     * @param colorId color code in resource id
     * @return self
     */
    T backgroundColorId(int colorId);

    /**
     * Notify a ListView that the data of it's adapter is changed.
     *
     * @return self
     */
    T dataChanged();


    /**
     * Checks if the current View exist.
     *
     * @return true, if is exist
     */
    boolean isExist();

    /**
     * Gets the tag of the View.
     *
     * @return tag
     */
    Object getTag();

    /**
     * Gets the tag of the View.
     *
     * @param id the id
     * @return tag
     */
    Object getTag(int id);

    /**
     * Gets the current View as an View.
     *
     * @return View
     */
    View getView();

    /**
     * Gets the current View as an image View.
     *
     * @return ImageView
     */
    ImageView getImageView();

    /**
     * Gets the current View as an Gallery.
     *
     * @return Gallery
     */
    @SuppressWarnings("deprecation")
    Gallery getGallery();


    /**
     * Gets the current View as a text View.
     *
     * @return TextView
     */
    TextView getTextView();

    /**
     * Gets the current View as an edit text.
     *
     * @return EditText
     */
    EditText getEditText();

    /**
     * Gets the current View as an progress bar.
     *
     * @return ProgressBar
     */
    ProgressBar getProgressBar();

    /**
     * Gets the current View as seek bar.
     *
     * @return SeekBar
     */

    SeekBar getSeekBar();

    /**
     * Gets the current View as a button.
     *
     * @return Button
     */
    Button getButton();

    /**
     * Gets the current View as a checkbox.
     *
     * @return CheckBox
     */
    CheckBox getCheckBox();

    /**
     * Gets the current View as a listview.
     *
     * @return ListView
     */
    ListView getListView();

    /**
     * Gets the current View as a ExpandableListView.
     *
     * @return ExpandableListView
     */
    ExpandableListView getExpandableListView();

    /**
     * Gets the current View as a gridview.
     *
     * @return GridView
     */
    GridView getGridView();

    /**
     * Gets the current View as a RatingBar.
     *
     * @return RatingBar
     */
    RatingBar getRatingBar();

    /**
     * Gets the current View as a webview.
     *
     * @return WebView
     */
    WebView getWebView();

    /**
     * Gets the current View as a spinner.
     *
     * @return Spinner
     */
    Spinner getSpinner();

    /**
     * Gets the editable.
     *
     * @return the editable
     */
    Editable getEditable();

    /**
     * Gets the text of a TextView.
     *
     * @return the text
     */
    CharSequence getText();

    /**
     * Gets the selected item if current View is an adapter View.
     *
     * @return selected
     */
    Object getSelectedItem();


    /**
     * Gets the selected item position if current View is an adapter View.
     * <p>
     * Returns AdapterView.INVALID_POSITION if not valid.
     *
     * @return selected position
     */
    int getSelectedItemPosition();

    /**
     * Register a callback method for when the View is clicked.
     *
     * @param listener The callback method.
     * @return self
     */
    T clicked(View.OnClickListener listener);

    /**
     * Register a callback method for when the View is long clicked.
     *
     * @param listener The callback method.
     * @return self
     */
    T longClicked(View.OnLongClickListener listener);

    /**
     * Register a callback method for when an item is clicked in the ListView.
     *
     * @param listener The callback method.
     * @return self
     */
    T itemClicked(AdapterView.OnItemClickListener listener);


    /**
     * Register a callback method for when an item is long clicked in the ListView.
     *
     * @param listener The callback method.
     * @return self
     */
    T itemLongClicked(AdapterView.OnItemLongClickListener listener);


    /**
     * Set selected item of an AdapterView.
     *
     * @param position The position of the item to be selected.
     * @return self
     */
    T setSelection(int position);


    /**
     * Register a callback method for when a textview text is changed. Method must have signature of method(CharSequence s, int start, int before, int count)).
     *
     * @param method The method name of the callback.
     * @return self
     */
    T textChanged(TextWatcher method);


    /**
     * Set the margin of a View. Notes all parameters are in DIP, not in pixel.
     *
     * @param leftDip   the left dip
     * @param topDip    the top dip
     * @param rightDip  the right dip
     * @param bottomDip the bottom dip
     * @return self
     */
    T margin(float leftDip, float topDip, float rightDip, float bottomDip);

    /**
     * Set the width of a View in dip.
     * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
     *
     * @param dip width in dip
     * @return self
     */

    T width(int dip);

    /**
     * Set the height of a View in dip.
     * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
     *
     * @param dip height in dip
     * @return self
     */

    T height(int dip);

    /**
     * Set the width of a View in dip or pixel.
     * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
     *
     * @param width width
     * @param dip   dip or pixel
     * @return self
     */

    T width(int width, boolean dip);

    /**
     * Set the height of a View in dip or pixel.
     * Can also be ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, or ViewGroup.LayoutParams.MATCH_PARENT.
     *
     * @param height height
     * @param dip    dip or pixel
     * @return self
     */

    T height(int height, boolean dip);


    View view();
}
