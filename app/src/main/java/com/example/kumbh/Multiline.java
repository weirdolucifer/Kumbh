package com.example.kumbh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * An EditText that lets you use actions ("Done", "Go", etc.) on multi-line edits.
 * Since a combination of those actions and multi-lining is not supported by the default EditText,
 * it was very much necessary to create our own "MultilineActionEditText" !!!1 :-)
 */
public class Multiline extends android.support.v7.widget.AppCompatEditText {

    public Multiline(Context context) {
        super(context);
    }

    public Multiline(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Multiline(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return conn;
    }
}